package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityAppBooking;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.AppointmentActivity;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.UI.Activities.ManageProfile;
import com.safedoctor.safedoctor.UI.Activities.PostDetail;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.thefinestartist.finestwebview.FinestWebView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Backgroundservices.MainService.getLocalConfirmedAppointmentContentModel;
import static com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage.updateCounter;
import static com.safedoctor.safedoctor.UI.Fragment.Appointments.bookedstatusid;
import static com.safedoctor.safedoctor.UI.Fragment.Appointments.savetoLocal;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 11/13/17.
 */

public class LandingPage extends Fragment {
    private View rootView;

    private BottomNavigationView bottomNavigationView;
    private Common common;

    private View btn_healthtips, btn_firstaid, btn_reminders, btn_appointments, btn_onlinehistory, btn_clinichistory;
    private View nav_home, nav_profile, nav_chatroom, nav_socialmedia, nav_partners;
    private TextView badge_reminder,badge_appointment,badge_online_visit,badge_clinic_visit;
    private String TAG="LandingPage";
    private SessionManagement sessionManagement;



    public static Fragment newInstance() {
        LandingPage fragment = new LandingPage();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_landingpage, container, false);

        common=new Common(context);
        sessionManagement=new SessionManagement(context);


        init_home_bottom();
        badge_reminder=(TextView)rootView.findViewById(R.id.badge_reminder);
        badge_appointment=(TextView)rootView.findViewById(R.id.badge_appointment);
        badge_appointment.setVisibility(View.GONE);
        badge_reminder.setVisibility(View.GONE);

        badge_online_visit=(TextView)rootView.findViewById(R.id.badge_online_visit);
        if(AppConstants.onlinerecords!=0){
            badge_online_visit.setText(""+AppConstants.onlinerecords);
            badge_online_visit.setVisibility(View.VISIBLE);
        }else{
            badge_online_visit.setVisibility(View.GONE);
        }

        badge_clinic_visit=(TextView)rootView.findViewById(R.id.badge_clinic_visit);
        badge_clinic_visit.setVisibility(View.GONE);

        if(AppConstants.IS_LOGIN){
            preloadAppt();
            preloadOnlineVisit();

        }

        btn_healthtips = rootView.findViewById(R.id.btn_healthtips);
        btn_healthtips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = HealthPosts.newInstance();
                setToolbarTitle(getString(R.string.health_posts_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btn_firstaid = rootView.findViewById(R.id.btn_firstaid);
        btn_firstaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = FirstAid.newInstance();
                setToolbarTitle(getString(R.string.first_aid_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btn_reminders = rootView.findViewById(R.id.btn_reminders);
        btn_reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = Notifications.newInstance();
                setToolbarTitle(getString(R.string.reminder_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btn_appointments = rootView.findViewById(R.id.btn_appointments);
        btn_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, AppointmentActivity.class));

                /*
                Fragment selectedFragment = Appointments.newInstance();
                setToolbarTitle(getString(R.string.appointments_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();*/
            }
        });

        btn_onlinehistory = rootView.findViewById(R.id.btn_onlinehistory);
        btn_onlinehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = OnlineMedicalRecords.newInstance();
                setToolbarTitle(getString(R.string.online_medical_records_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        btn_clinichistory = rootView.findViewById(R.id.btn_clinichistory);
        btn_clinichistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = ClinicVisitRecords.newInstance();
                setToolbarTitle(getString(R.string.clinic_visit_medical_records_txt));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });


        final Activity activity=getActivity();
        try{
            if(activity!=null){
                ((ActivityLandingPage)activity).setOnBackPressedListener(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadConfirmedAppointments();
    }

    private void setToolbarTitle(String title) {

        ((ActivityLandingPage)getActivity()).getSupportActionBar().setTitle(title);
        //getActivity().setTitle(title);
    }

    private void loadBadges(){
        if(AppConstants.maxbooked>0){
            badge_reminder.setText(""+AppConstants.maxbooked);
            badge_appointment.setText(""+AppConstants.maxbooked);
            badge_appointment.setVisibility(View.VISIBLE);
            badge_reminder.setVisibility(View.VISIBLE);
        }

        updateCounter(null);

    }



    private void init_home_bottom() {

        //init fab views
        View fab_twitter = rootView.findViewById(R.id.fab_twitter);
        fab_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FinestWebView.Builder(getActivity()).show(getResources().getString(R.string.twitter));
            }
        });
        View fab_facebook = rootView.findViewById(R.id.fab_facebook);
        fab_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FinestWebView.Builder(getActivity()).show(getResources().getString(R.string.facebook));
            }
        });

        View fab_instagram=rootView.findViewById(R.id.fab_instagram);
        fab_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FinestWebView.Builder(getActivity()).show(getResources().getString(R.string.instagram));
            }
        });

        nav_home = rootView.findViewById(R.id.nav_home);
        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, LandingPage.newInstance());
                transaction.commit();
            }
        });

        nav_profile = rootView.findViewById(R.id.nav_profile);
        if (!(new SessionManagement(getContext()).isLoggedIn()))
            nav_profile.setVisibility(View.GONE);
        else
            nav_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, ManageProfile.class));

                }
            });

        nav_chatroom = rootView.findViewById(R.id.nav_chatroom);
        if (!(new SessionManagement(getContext()).isLoggedIn()))
            nav_chatroom.setVisibility(View.GONE);
        else
            nav_chatroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, ChatRoom.newInstance());
                    transaction.commit();
                }
            });

        nav_socialmedia = rootView.findViewById(R.id.nav_socialmedia);
//        if (!(new SessionManagement(getContext()).isLoggedIn()))
//            nav_socialmedia.setVisibility(View.GONE);
//        else
        nav_socialmedia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                final View v = rootView.findViewById(R.id.social_media_popup);
                if(v.getVisibility() == View.GONE){
                    v.setVisibility(View.VISIBLE);
                }else v.setVisibility(View.GONE);
            }
        });

        nav_partners = rootView.findViewById(R.id.nav_partners);
//        if (!(new SessionManagement(getContext()).isLoggedIn()))
//        nav_partners.setVisibility(View.GONE);
//        else
        nav_partners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public void preloadAppt(){

        if(getLocalConfirmedAppointmentContentModel().size()>0){
            AppConstants.maxbooked = getLocalConfirmedAppointmentContentModel().size();
            loadBadges();
            Log.e(TAG,"pre loading appt from local");
        }else {

            Log.e(TAG,"pre loading appt from web");
            SafeDoctorService mSafeDoctorService = ApiUtils.getAPIService();

            Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call = mSafeDoctorService.getConfirmedAppointments(TokenString.tokenString, AppConstants.patientID,bookedstatusid);
            call.enqueue(new Callback<SwagResponseModel<List<ConfirmedAppointmentContentModel>>>() {
                @Override
                public void onResponse(Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call, Response<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> response) {


                    if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //appointmentFetchProgressDialog.dismiss();

                        sessionManagement.logoutSessionExpired();

                    } else if (response.isSuccessful()) {
                        Log.i("Safe", "loadConfirmedAppoints successfully called");
                        SwagResponseModel<List<ConfirmedAppointmentContentModel>> confirmedAppointmentResponse = response.body();


                        if (confirmedAppointmentResponse.getData() == null) {
                            List<String> noServiceAvailable = new ArrayList<String>();
                            noServiceAvailable.add("No Appointment Available");
                            //noAppointmentsTv.setVisibility(View.VISIBLE);
                            //appointmentFetchProgressDialog.dismiss();
                            //Toast.makeText(getContext(), "No Appointments Available", Toast.LENGTH_SHORT).show();
                        } else {
                            DataModel<List<ConfirmedAppointmentContentModel>> appointmentData = confirmedAppointmentResponse.getData();
                            List<ConfirmedAppointmentContentModel> confirmedAppointmentsList = appointmentData.getContent();
                            AppConstants.maxbooked = confirmedAppointmentsList.size();

                            // islocal = false;
                            savetoLocal(confirmedAppointmentsList);
                            loadBadges();


                            //showAppointments(confirmedAppointmentsList);


                        }
                    } else {
                        //appointmentFetchProgressDialog.dismiss();

                        try {
                            //Toast.makeText(getContext(), "Unable to Fetch Appointment. Please contact Helpline", Toast.LENGTH_SHORT).show();

                            Log.i("Safe", "Fetching error code " + response.code() + "");
                            Log.i("Safe", "Response is " + response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

                @Override
                public void onFailure(Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> call, Throwable t) {

                    Toast.makeText(context, "Network Error, please try again", Toast.LENGTH_SHORT).show();
                    Log.i("Safe", "Fetching error: " + t.getMessage() + "");
                }
            });
        }



    }

    public void preloadOnlineVisit(){
        common.getOnlineVisits(AppConstants.patientID,null,null,null);
        common.setOnTaskCompletedListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object result) {
               List<ConsultationProfile> visitList=(List<ConsultationProfile>) result;
                if(visitList.size()>0){
                    AppConstants.onlinerecords=visitList.size();

                    badge_online_visit.setText(""+visitList.size());
                    badge_online_visit.setVisibility(View.VISIBLE);

                    updateCounter(null);




                    //onlineVisitAdapter=new OnlineVisitAdapter(visitList,context);
                    // recyclerView.setAdapter(onlineVisitAdapter);
                }


            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Fragment currFragment= getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if(currFragment instanceof HealthPosts || currFragment instanceof FirstAid){
            menu.findItem(R.id.action_search).setVisible(true);
        }else{
            menu.findItem(R.id.action_search).setVisible(false);
        }

    }
}
