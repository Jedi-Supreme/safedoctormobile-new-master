package com.safedoctor.safedoctor.Backgroundservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.safedoctor.safedoctor.Model.AppointmentModel;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.DetailModel;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.Notifications.broadcastreceivers.NotificationEventReceiver;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityBookingNotificationShow;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.localpersistance.Appointmenttbl;
import com.safedoctor.safedoctor.localpersistance.Detailtbl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.safedoctor.safedoctor.Utils.App.context;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getNow;
import static com.safedoctor.safedoctor.localpersistance.Appointmenttbl.getSavedAppointment;
import static com.safedoctor.safedoctor.localpersistance.Detailtbl.getSavedDetail;

/**
 * Created by stevkky on 10/16/17.
 */

public class MainService extends Service
{
    private final IBinder mBinder = new MyBinder();
    private static int NOTIFICATION_ID = 1;

    private  static int motif_max = 10;
    private static  int count =0;
    private String TAG="MainService";
    private List<ConfirmedAppointmentContentModel> apptList=new ArrayList<>();

    private Handler handler = new Handler();// my timer

    private ConfirmedAppointmentContentModel appt;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public MainService()
    {
        super();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        /*
        if(intent != null) {
            appt = (ConfirmedAppointmentContentModel) intent.getSerializableExtra("data");
            if (appt != null) {
                handler.postDelayed(runnable, 6000);
                Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
            }
        }*/

        handler.postDelayed(runnable, 600000);//1min

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class MyBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    private void processStartNotification(int val) {
        count++;

        if(apptList.size()==0){
            apptList=getLocalConfirmedAppointmentContentModel();
        }

        if(count>=apptList.size()){
            count=0;
            val=count;
        }

        if(apptList.size()>0) {


            ConfirmedAppointmentContentModel appt = apptList.get(val);

            try{
                String bookingdate= appt.getAppointment().getBookingdate();
                String date=bookingdate.split("T")[0];
                String today=getNow();



                Date date1 = format.parse(date);
                Date date2 = format.parse(today);

                if (date2.compareTo(date1) == 1) {
                    System.out.println("earlier");
                    return;
                }

                if(appt.getAppointment().getRemind()==0){
                    return;//remind is false
                }




            }catch (Exception e){
                e.printStackTrace();
            }


            //for (ConfirmedAppointmentContentModel appt:apptList) {
            int statusid = appt.getAppointment().getStatusid();
            //4:cancelled; 5:missed;3:complete
            if (statusid != 4 || statusid != 5 || statusid != 3) {


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle("Booked Appointment")
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setContentText("Safe Doctor Appointment on " + AuxUtils.Date.formateDate(appt.getAppointment().getBookingdate(), "E, d MMMM, yyyy"))
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setSmallIcon(R.drawable.ic_notification);
                // .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.safe_doctor_icon));

                if (appt.getAppointment().getDoctorphoto() != null && !appt.getAppointment().getDoctorphoto().isEmpty()) {
                    builder = builder.setLargeIcon(Tools.convertToBitmap(appt.getAppointment().getDoctorphoto()));
                }

                TimeSlot t = new TimeSlot();
                t.setDate(appt.getAppointment().getBookingdate());
                t.setStarttime(appt.getDetails().get(0).getStarttime());
                t.setEndtime(appt.getDetails().get(0).getEndtime());
                t.setDoctorid(appt.getAppointment().getDoctoruserid());
                t.setDoctorname(appt.getAppointment().getDoctorname());
                t.setSlotid(appt.getDetails().get(0).getSlotid());
                t.setRoasterid(appt.getDetails().get(0).getSlotid());
                t.setServiceid(appt.getDetails().get(0).getServiceid());
                t.setStatusid(appt.getAppointment().getStatusid());
                t.bookingnumber = appt.getAppointment().getBookingnumber();
                t.specialtytext = AuxUtils.getSpecialtyByID(appt.getDetails().get(0).getServiceplaceid());

                Intent mainIntent = new Intent(getBaseContext(), ActivityBookingNotificationShow.class);
                //mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra("timeslot", t);

                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

                final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                manager.cancel(NOTIFICATION_ID);
                NOTIFICATION_ID++;
                Notification notification = builder.build();
                ShortcutBadger.applyNotification(getBaseContext(), notification, NOTIFICATION_ID);
                manager.notify(NOTIFICATION_ID, notification);
            }
            //}
        }else {
            count=0;
        }





        //playNotificationSound();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            Log.i("Safedoctor service", "in handler");
           // if(count <= motif_max) {
                if(new SessionManagement(context).getNotificationStatus()){
                    processStartNotification(count);
                    Log.e("MAinservice","notification number : "+count);
                    handler.postDelayed(this, 600000);//10mins:600000
                //}

            }
        }
    };

    public void playNotificationSound()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    public static List<ConfirmedAppointmentContentModel> getLocalConfirmedAppointmentContentModel(){
        List<ConfirmedAppointmentContentModel> confirmedAppointmentContentModelList=new ArrayList<>();
        List<Appointmenttbl> appointmenttblList=getSavedAppointment();
        List<Detailtbl> detailtblList=getSavedDetail();
        List<AppointmentModel> appointmentModelList=converttoAppointmentModelList(appointmenttblList);
        List<DetailModel> detailModelList=converttoDetailModelList(detailtblList);

        for (int i = 0; i <appointmentModelList.size(); i++) {
            ConfirmedAppointmentContentModel cm=new ConfirmedAppointmentContentModel();
            cm.setAppointment(appointmentModelList.get(i));

            List<DetailModel> dl=new ArrayList<>();
            dl.add(detailModelList.get(i));
            cm.setDetails(dl);

            confirmedAppointmentContentModelList.add(cm);

        }



        return confirmedAppointmentContentModelList;
    }

    private static List<AppointmentModel> converttoAppointmentModelList(List<Appointmenttbl> appointmenttblList ){
        List<AppointmentModel>  appointmentModelList=new ArrayList<>();
        for (Appointmenttbl a:appointmenttblList) {
            AppointmentModel am=new AppointmentModel(a.getBookingdate(),a.getBookingid(),a.getBookingnumber(),a.getDoctorname(),a.getConsultationchattypeid(),a.getCreatetime(),a.getCreateuserid(),a.getDoctoruserid(),a.getPatientid(),a.getServertime(),a.getSourceid(),a.getStatusdate(),a.getStatusid(),a.getUpdatetime(),a.getUpdateuserid(),a.getDoctorphoto(),a.isRemind());
            appointmentModelList.add(am);
        }

        return appointmentModelList;
    }

    private AppointmentModel converttoApptModel(Appointmenttbl a){
        AppointmentModel am=new AppointmentModel(a.getBookingdate(),a.getBookingid(),a.getBookingnumber(),a.getDoctorname(),a.getConsultationchattypeid(),a.getCreatetime(),a.getCreateuserid(),a.getDoctoruserid(),a.getPatientid(),a.getServertime(),a.getSourceid(),a.getStatusdate(),a.getStatusid(),a.getUpdatetime(),a.getUpdateuserid(),a.getDoctorphoto(),a.isRemind());
        return am;
    }

    private DetailModel converttoDetailModel(Detailtbl d){
        DetailModel dm=new DetailModel(d.getBookingid(),d.getCreatetime(),d.getCreateuserid(),d.getEndtime(),d.getDetailIdId(),d.getNotes(),d.getReasonid(),d.getServertime(),d.getServicefee(),d.getServiceid(),d.getServiceplaceid(),d.getSlotid(),d.getStarttime(),d.getStatusdate(),d.getStatusid(),d.getUpdatetime(),d.getUpdateuserid());
        return dm;
    }



    private static List<DetailModel> converttoDetailModelList(List<Detailtbl> detailtblList){
        List<DetailModel> detailModelList=new ArrayList<>() ;
        for (Detailtbl d:detailtblList) {
            DetailModel dm=new DetailModel(d.getBookingid(),d.getCreatetime(),d.getCreateuserid(),d.getEndtime(),d.getDetailIdId(),d.getNotes(),d.getReasonid(),d.getServertime(),d.getServicefee(),d.getServiceid(),d.getServiceplaceid(),d.getSlotid(),d.getStarttime(),d.getStatusdate(),d.getStatusid(),d.getUpdatetime(),d.getUpdateuserid());
            detailModelList.add(dm);
        }
        return detailModelList;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"service is destroyed");
    }
}

