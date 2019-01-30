package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.Model.responses.Userphoto;
import com.safedoctor.safedoctor.UI.Activities.ActivityDoctorsProfile;
import com.safedoctor.safedoctor.UI.Activities.CallActivity;
import com.safedoctor.safedoctor.UI.Activities.Consultation;
import com.safedoctor.safedoctor.UI.Activities.VideoChat;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AppointmentsViewHolder;
import com.safedoctor.safedoctor.Utils.AuxUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getNow;

/**
 * Created by Alvin on 22/08/2017.
 */

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsViewHolder> {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Context context;
    List<ConfirmedAppointmentContentModel> appointmentModels;
    int tab;

    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AppointmentsAdapter(Context context, List<ConfirmedAppointmentContentModel> appointmentModels,int tab) {
        this.context = context;
        this.appointmentModels = appointmentModels;
        this.tab=tab;
    }

    @Override
    public AppointmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.appointment_list_item, parent, false);

        return new AppointmentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( AppointmentsViewHolder holder, final int position)
    {
      final ConfirmedAppointmentContentModel  booking = appointmentModels.get(position);

        if(booking.getAppointment().getDoctorphoto() != null && !booking.getAppointment().getDoctorphoto().isEmpty())
        {
            Tools.displayImageRound(context, holder.doctor_img, booking.getAppointment().getDoctorphoto() );
        }

        holder.appointmentDate.setText(AuxUtils.Date.formateDate(booking.getAppointment().getBookingdate(),"E, d MMMM, yyyy"));
        holder.appointmentDoctorName.setText(booking.getAppointment().getDoctorname());
        holder.apptStartTimeTv.setText("Start time: " + AuxUtils.Date.formateTime(booking.getDetails().get(0).getStarttime()));
        holder.apptEndTimeTv.setText("End time: " + AuxUtils.Date.formateTime(booking.getDetails().get(0).getEndtime()));
        holder.bookingno.setText("Booking No: " + booking.getAppointment().getBookingnumber());


        holder.appointmentStatus.setTextColor(context.getResources().getColor(R.color.appointment_confirmed));
        holder.appointmentStatus.setText(AppConstants.getStatuses().get(booking.getAppointment().getStatusid()));


        if (booking.getAppointment().getDoctoruserid() == null) {
            holder.appointmentDoctorName.setText(R.string.no_doc_available);
        }

        switch (booking.getAppointment().getConsultationchattypeid())
        {
            case 1:
                holder.chatTypeImg.setImageResource(R.drawable.chat_64px);
                break;
            case 2:
                holder.chatTypeImg.setImageResource(R.drawable.phonecall2_64px);
                break;
            case 3:
                holder.chatTypeImg.setImageResource(R.drawable.vid_64px);
                break;
        }


        holder.statuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
                {
                    return;
                }else {

                    try{

                        String bookingdate= booking.getAppointment().getBookingdate();
                        String date=bookingdate.split("T")[0];
                        String today=getNow();



                        Date date1 = format.parse(date);
                        Date date2 = format.parse(today);

                        int comp=date2.compareTo(date1);


                        if (date2.compareTo(date1) != 0) {
                            System.out.println("not same date");
                            Toast.makeText(context,"Sorry consultation is not due",Toast.LENGTH_LONG).show();
                            return;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
                AppConstants.bookingid = appointmentModels.get(position).getAppointment().getBookingid();
                switch (booking.getAppointment().getConsultationchattypeid())
                {
                    case 1:
                        context.startActivity(new Intent(context,Consultation.class)
                                .putExtra("doctorid",booking.getAppointment().getDoctoruserid())
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                    case 2:
                        context.startActivity(new Intent(context,CallActivity.class)
                                .putExtra("doctorid",booking.getAppointment().getDoctoruserid())
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                    case 3:
                        context.startActivity(new Intent(context,VideoChat.class)
                                .putExtra("doctorid",booking.getAppointment().getDoctoruserid())
                                .putExtra("starttime",booking.getDetails().get(0).getStarttime())
                                .putExtra("endtime",booking.getDetails().get(0).getEndtime()));
                        break;
                }


            }
        });

        if(tab==2){
            holder.more.setVisibility(View.GONE);
            holder.statuslayout.setClickable(false);
        }else{
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, booking);
                }
            });
        }



        holder.doctor_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
                {
                    return;
                }

                Intent intent = new Intent(context, ActivityDoctorsProfile.class);

                DoctorOutObj obj = new DoctorOutObj();

                UserAccount acc = new UserAccount();
                acc.setFirstname(booking.getAppointment().getDoctorname());
                acc.setId(booking.getAppointment().getDoctoruserid());
                obj.setDoctor(acc);

                if(booking.getAppointment().getDoctorphoto() != null && !booking.getAppointment().getDoctorphoto().isEmpty())
                {
                    Userphoto photo = new Userphoto();
                    photo.setPhoto(booking.getAppointment().getDoctorphoto());

                    obj.setPicture(photo);
                }

                intent.putExtra("doctor",obj);
                intent.putExtra("viewonlymode",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(booking.getAppointment().getDoctoruserid() == null || booking.getAppointment().getDoctoruserid().isEmpty())
        {
            holder.statuslayout.setVisibility(View.GONE);
        }


    }

    private void onMoreButtonClick(final View view, final ConfirmedAppointmentContentModel appt) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, appt, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_appt_actions);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return appointmentModels.size();
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ConfirmedAppointmentContentModel obj, MenuItem item);
    }

}
