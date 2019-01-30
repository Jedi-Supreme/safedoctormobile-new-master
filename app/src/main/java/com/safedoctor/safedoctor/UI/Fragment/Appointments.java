package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.safedoctor.safedoctor.Adapter.AppointmentsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.AppointmentModel;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.DetailModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityBookingNotificationShow;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.UI.Activities.NewAppointment;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.localpersistance.Appointmenttbl;
import com.safedoctor.safedoctor.localpersistance.Detailtbl;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Api.Common.tokenerror;
import static com.safedoctor.safedoctor.Backgroundservices.MainService.getLocalConfirmedAppointmentContentModel;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class Appointments extends Fragment {

    private  RecyclerView patientAppointmentsRecyclerView;

    private  SwagResponseModel<List<ConfirmedAppointmentContentModel>> confirmedAppointmentResponse;
    private  SafeDoctorService mSafeDoctorService;


    private  DataModel<List<ConfirmedAppointmentContentModel>> appointmentData;

    private  TextView noAppointmentsTv;

    private static String TAG="Appointments";
    private  SwipeRefreshLayout appointmentsRefresh;


    private  List<ConfirmedAppointmentContentModel> confirmedAppointmentsList;
    private  boolean islocal=false;

    private int currenttab=0;

    public static int bookedstatusid=8;
    private LinearLayout appttab;
    private ArrayList<View> tabs= new ArrayList<>();

    //private OnTaskCompleted onTaskCompleted;

    private boolean autorefresh=false;



    public static Fragment newInstance() {
        return new Appointments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);

        patientAppointmentsRecyclerView = rootView.findViewById(R.id.patient_appointments_recyclerView);
        patientAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noAppointmentsTv = rootView.findViewById(R.id.no_appointments_tv);
        appointmentsRefresh = rootView.findViewById(R.id.appointments_refresh);
        appointmentsRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));


        ProgressDialog appointmentFetchProgressDialog = new ProgressDialog(getActivity());
        appointmentFetchProgressDialog.setMessage(getResources().getString(R.string.alert_working));

        FloatingActionButton fab = rootView.findViewById(R.id.createAppointmentFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                chooseAppttpe();
                //startActivity(new Intent(getActivity(), NewAppointment.class));
            }
        });
        appttab= rootView.findViewById(R.id.appttab);
        mSafeDoctorService = ApiUtils.getAPIService();
        TextView bookedtab = rootView.findViewById(R.id.booked);
        bookedtab.setContentDescription("tab");
        bookedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetabcolor(view);
                currenttab=0;
                if(getLocalConfirmedAppointmentContentModel().size()>0){
                    Log.e(TAG," fetch appointment from local. Size : "+getLocalConfirmedAppointmentContentModel().size());
                    islocal=true;
                    showAppointments(getLocalConfirmedAppointmentContentModel(),bookedstatusid);
                    Toast.makeText(context,"Swipe down to refresh",Toast.LENGTH_LONG).show();
                }else{
                    islocal=false;
                    loadConfirmedAppointments(getStatusId(0));
                }
            }
        });
        TextView inprogresstab = rootView.findViewById(R.id.inprogress);
        inprogresstab.setContentDescription("tab");
        inprogresstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetabcolor(view);
                currenttab=1;
                loadConfirmedAppointments(getStatusId(1));
            }
        });
        TextView completedtab = rootView.findViewById(R.id.completed);
        completedtab.setContentDescription("tab");
        completedtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetabcolor(view);
                currenttab=2;
                loadConfirmedAppointments(getStatusId(2));
            }
        });
        bookedtab.performClick();

        appointmentsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh() {
                if(currenttab==0){
                    appointmentsRefresh.setRefreshing(true);
                    islocal=false;
                    loadConfirmedAppointments(getStatusId(currenttab));
                }else if(currenttab==1){
                    loadConfirmedAppointments(getStatusId(currenttab));
                }else if(currenttab==2){
                    loadConfirmedAppointments(getStatusId(currenttab));
                }

            }
        });


        Activity activity=getActivity();

        new Common(context).fragmentInitOnbackpressed(activity);

        if(autorefresh){
            Toast.makeText(context,"Refreshing Booked Appointment",Toast.LENGTH_LONG).show();
            currenttab=0;
            islocal=false;
            loadConfirmedAppointments(bookedstatusid);
        }

        return rootView;
    }

    void changetabcolor(View view){
        try{
            appttab.findViewsWithText(tabs,"tab",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            if(tabs.size()!=0){
                for (View tab:tabs) {
                    ((TextView)tab).setTextColor(ContextCompat.getColor(context,R.color.grey_20));
                }
            }
        }catch (Exception e){
            Log.e(TAG,""+e);
        }



        ((TextView)view).setTextColor(ContextCompat.getColor(context,R.color.white));
    }

    int getStatusId(int tab){
        int status=0;
        switch (tab){
            case 0:status=bookedstatusid;
                break;
            case 1:status=7;
                break;
            case 2:status=3;
                break;
        }
        return status;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void chooseAppttpe(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Objects.requireNonNull(getActivity()));

        // set title
        alertDialogBuilder.setTitle("Booking Appointment");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to see a doctor now?")
                .setCancelable(false)
                .setPositiveButton("Yes, Emergency",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        startActivity(new Intent(getActivity(), NewAppointment.class).putExtra("now",true));

                    }
                })
                .setNegativeButton("No, Schedule",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getActivity(), NewAppointment.class).putExtra("now",false));

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void loadConfirmedAppointments(final int statusid)
    {
        appointmentsRefresh.setRefreshing(true);
        Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call = mSafeDoctorService.getConfirmedAppointments(TokenString.tokenString, AppConstants.patientID,statusid);
        call.enqueue(new Callback<SwagResponseModel<List<ConfirmedAppointmentContentModel>>>() {
            @Override
            public void onResponse(@NonNull Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call,
                                   @NonNull Response<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> response) {

                if (appointmentsRefresh.isRefreshing())
                {
                    appointmentsRefresh.setRefreshing(false);
                }

                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(context, "Your session has expired please login again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, FormLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
                else if (response.isSuccessful())
                {
                    Log.i("Safe", "loadConfirmedAppoints successfully called");
                    confirmedAppointmentResponse = response.body();

                    if (confirmedAppointmentResponse != null) {
                        if (confirmedAppointmentResponse.getData() == null) {
                            List<String> noServiceAvailable = new ArrayList<>();
                            noServiceAvailable.add("No Appointment Available");
                            noAppointmentsTv.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            appointmentData = confirmedAppointmentResponse.getData();
                            confirmedAppointmentsList = appointmentData.getContent();
                            showAppointments(confirmedAppointmentsList,statusid);
                        }
                    }
                }
                else
                    {
                    //appointmentFetchProgressDialog.dismiss();

                    try {
                        //Toast.makeText(getContext(), "Unable to Fetch Appointment. Please contact Helpline", Toast.LENGTH_SHORT).show();
                        String error;
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                            tokenerror(error);
                        }

                        Log.i("Safe", "Fetching error code " + response.code() + "");
                        //Log.i("Safe", "Response is " + response.errorBody().string());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call,
                                  @NonNull Throwable t) {

                if (appointmentsRefresh.isRefreshing())
                {
                    appointmentsRefresh.setRefreshing(false);
                }

                Toast.makeText(context, "Network Error, please try again", Toast.LENGTH_SHORT).show();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAttach(Context context) {
        try{


            if(Objects.requireNonNull(getActivity()).getIntent().getExtras()!=null){
                boolean refreashappt= Objects.requireNonNull(getActivity().getIntent().getExtras()).getBoolean("refresh");
                if(refreashappt){
                    autorefresh=true;
                    //loadConfirmedAppointments(bookedstatusid);

                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }


        super.onAttach(context);


    }

    private void showAppointments(List<ConfirmedAppointmentContentModel> confirmedAppointmentsList, int statusid){


        //needed to sort List based on status id
        Comparator<ConfirmedAppointmentContentModel> comparator = new Comparator<ConfirmedAppointmentContentModel>() {
            @Override
            public int compare(ConfirmedAppointmentContentModel left, ConfirmedAppointmentContentModel right) {
                return left.getAppointment().getBookingdate().compareToIgnoreCase(right.getAppointment().getBookingdate()); // ascending order
                // return right.getAppointment().getBookingdate().compareToIgnoreCase(left.getAppointment().getBookingdate()); // descending order
            }
        };

        //sorts List
        Collections.sort(confirmedAppointmentsList, comparator);

        AppointmentsAdapter mAdapter = new AppointmentsAdapter(getActivity(), confirmedAppointmentsList, currenttab);
        mAdapter.notifyDataSetChanged();
        patientAppointmentsRecyclerView.setAdapter(mAdapter);
        // appointmentFetchProgressDialog.dismiss();

        if(statusid==bookedstatusid){
            AppConstants.maxbooked = confirmedAppointmentsList.size();
        }

        //updateCounter(null);

        mAdapter.setOnMoreButtonClickListener(new AppointmentsAdapter.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, ConfirmedAppointmentContentModel obj, MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.mnu_showappt:

                        TimeSlot t = new TimeSlot();
                        t.setDate(obj.getAppointment().getBookingdate());
                        t.setStarttime(obj.getDetails().get(0).getStarttime());
                        t.setEndtime(obj.getDetails().get(0).getEndtime());
                        t.setDoctorid(obj.getAppointment().getDoctoruserid());
                        t.setDoctorname(obj.getAppointment().getDoctorname());
                        t.setSlotid(obj.getDetails().get(0).getSlotid());
                        t.setRoasterid(obj.getDetails().get(0).getSlotid());
                        t.setServiceid(obj.getDetails().get(0).getServiceid());
                        t.setStatusid(obj.getAppointment().getStatusid());
                        t.bookingnumber = obj.getAppointment().getBookingnumber();
                        t.specialtytext = AuxUtils.getSpecialtyByID(obj.getDetails().get(0).getServiceplaceid());
                        Intent intent = new Intent(context, ActivityBookingNotificationShow.class);
                        intent.putExtra("timeslot",t);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case R.id.mnu_apptreschedule:
                        Toast.makeText(context, obj.getAppointment().getBookingnumber() + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mnu_cancelappt:
                        Toast.makeText(context, obj.getAppointment().getBookingnumber() + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        //Save to local
        if(statusid==bookedstatusid) {


            if (!islocal) {
                savetoLocal(confirmedAppointmentsList);
            }
        }




    }

    static void savetoLocal(List<ConfirmedAppointmentContentModel> confirmedAppointmentsList){
        //if(islocal==false){
            cleardb();
            for (ConfirmedAppointmentContentModel confirmed:confirmedAppointmentsList) {
                AppointmentModel m= confirmed.getAppointment();
                Appointmenttbl appointmenttbl=new Appointmenttbl(m.getBookingdate(), m.getBookingid(), m.getBookingnumber(), m.getDoctorname(), m.getConsultationchattypeid(),
                        m.getCreatetime(), m.getCreateuserid(), m.getDoctoruserid(), m.getPatientid(), m.getServertime(),
                        m.getSourceid(),m.getStatusdate(),m.getStatusid(),m.getUpdatetime(),m.getUpdateuserid(),m.getDoctorphoto(),m.getRemind());

                appointmenttbl.save();



                DetailModel d=confirmed.getDetails().get(0);
                Detailtbl detailtbl=new Detailtbl(d.getBookingid(),d.getCreatetime(),d.getCreateuserid(),d.getEndtime(),d.getId(),
                        d.getNotes(),d.getReasonid(),d.getServertime(),d.getServicefee(),d.getServiceid(),
                        d.getServiceplaceid(),d.getSlotid(),d.getStarttime(),d.getStatusdate(),d.getStatusid(),
                        d.getUpdatetime(),d.getUpdateuserid());

                detailtbl.save();

                Log.e(TAG,"Appointment booking id "+m.getBookingid()+" Detail booking id "+d.getBookingid());



            }

            Log.e(TAG," saved appointments . Size : "+getLocalConfirmedAppointmentContentModel().size());
        //}
    }

    public static void cleardb(){
        try{
            new Delete().from(Appointmenttbl.class).execute();
            ActiveAndroid.getDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME='Appointment'");
            new Delete().from(Detailtbl.class).execute();
            ActiveAndroid.getDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME='Detail'");
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        //loadConfirmedAppointments();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
