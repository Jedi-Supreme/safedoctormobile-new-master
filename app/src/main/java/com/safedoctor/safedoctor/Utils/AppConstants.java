package com.safedoctor.safedoctor.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.View;

import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.HealthPost;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.Peripheral;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.Model.responses.PatientProfileFormDataOut;
import com.safedoctor.safedoctor.Model.responses.Patientcontactperson;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedication;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Reviewquestion;
import com.safedoctor.safedoctor.Model.responses.Reviewtype;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.safedoctor.safedoctor.Backgroundservices.MainService.getLocalConfirmedAppointmentContentModel;
import static com.safedoctor.safedoctor.Utils.App.context;


public class AppConstants {

    public static  final int LIST_ROUND_COLOR = 0xFF1976D2;

    public static String SAFE_DOCTOR_SENDER_ID  = "SAFE DOKTOR";

    public static final String NEW_APPT="NewAppointment";



    //social media urls
    public static final String SOCIALMEDIA_TWITTER = "https://twitter.com/theviewtv?lang=en";
    public static final String SOCIALMEDIA_FACEBOOK = "https://www.facebook.com/purplegirltalk/";

    //Posts
    public static final String POST_TITLE = "POST_TITLE";
    public static final String POST_IMG_URL = "POST_IMG_URL";
    public static final String POST_LINK = "POST_LINK";
    public static final String POST_CREATED_AT = "POST_CREATED_AT";
    //First Aids
    public static final String AID_TITLE = "AID_TITLE";
    public static final String AID_IMG_URL = "AID_IMG_URL";
    public static final String AID_ACTIONS_TO_TAKE = "AID_ACTIONS_TO_TAKE";
    public static final String DOCTOR_FULL_NAME = "DOCTOR_FULL_NAME";
    public static final String DOCTOR_IMG_URL = "DOCTOR_IMG_URL";
    public static final String DOCTOR_JOB_TITLE = "DOCTOR_JOB_TITLE";
    public static final String DATE_PICKER_TAG = "Datepickerdialog";

    public static SessionManagement sessionManagement=new SessionManagement(context);
    public static  boolean IS_LOGIN = sessionManagement.isLoggedIn();
    //PatientInfo
    public static PatientModel PatientObj=sessionManagement.getPatientDetail();
    public static String patientPhoneNumber=sessionManagement.getPatientDetail().getPhonenumber();
    public static int patientID=sessionManagement.getPatientDetail().getPatientid();
    public static String profilePictureString=sessionManagement.getProfileImage();
    public static String patientName=sessionManagement.getPatientDetail().getFirstname()+" "+sessionManagement.getPatientDetail().getMiddlename()+" "+sessionManagement.getPatientDetail().getLastname();


    //ServiceInfo
    public static int serviceID;
    public static HashMap<String, Integer> serviceFeeQuery;
    public static HashMap<String, String> availableServiceQuery;

    //BookNPay
    public static Integer consultationchattypeid;
    public static String doctorid;
    public static String mobilemoneynetworkid;
    public static Integer reasonid = 1;
    public static Integer genderId;
    public static Integer slotid;
    public static String walletnumber;
    public static int  bookingid;

    //Opentok API
    public static String API_KEY = "45938812";
    public static String API_SECRET = "c1cc0538d527a3da58fa5421b51dff58358f8ef3";
    public static String SESSION_ID = "";
    public static String TOKEN = "";

    //Cached Items
    public static PatientProfileFormDataOut CACHE_FORMDATA = new PatientProfileFormDataOut();
    public static List<Patientcontactperson> CACHE_CONTACT_PERSONS = new ArrayList<Patientcontactperson>();
    public static List<Patientcontactperson> CACHE_NEXT_OFKINS = new ArrayList<Patientcontactperson>();
    public static List<Patientprofilemedication> CACHE_MEDICATIONS = new ArrayList<Patientprofilemedication>();
    public static List<BasicObject> CACHE_SPECIALTIES = new ArrayList<BasicObject>();
    public static List<TimeSlot> CACHE_SLOTS = new ArrayList<TimeSlot>();
    public static List<DoctorOutObj> CACHE_DOCTORS = new ArrayList<DoctorOutObj>();
    public static List<Reviewanswer> CACHE_REVIEW_ANSWERS = new ArrayList<Reviewanswer>();
    public static List<Reviewquestion> CACHE_REVIEW_QUESTION=new ArrayList<Reviewquestion>();
    public static List<Reviewtype> CACHE_REVIEW_TYPE=new ArrayList<Reviewtype>();
    public static List<Facilityinfo> CACHE_FACILITY_INFO=new ArrayList<>();
    public static List<Aid.Content> CACHE_AIDS=new ArrayList<>();
    public static List<ConsultationProfile> CACHE_ONLINE_VISIT=new ArrayList<>();
    public static List<HealthPost.Content> CACHE_HEALTH_POST=new ArrayList<>();

    public static List<BasicObject> allTitles=new ArrayList<>();

    public static List<Peripheral> CACHE_PERIPHERALS = new ArrayList<>();




    //Local SQLite
    public static final String DBNAME = "safedoctordb";

    //AppointmentStatuses
    public static Map<Integer,String> getStatuses ()
    {
        Map<Integer,String> Statuses = new HashMap<>();
        Statuses.put(1,"Free");
        Statuses.put(2,"Pending");
        Statuses.put(3,"Honoured");
        Statuses.put(4,"Cancelled");
        Statuses.put(5,"Missed");
        Statuses.put(6,"Incomplete");
        Statuses.put(7,"In progress");
        Statuses.put(8,"Booked");
        Statuses.put(9,"Unpaid");

        return  Statuses;
    }

    public static Double  REVIEW_ANSWER_MAX = null;

    //Navigation drawer values
    public static int newreminders = 0;
    public static int clinicrecords = 0;
    public static int onlinerecords = 0;
    public static int referrals =0;
    public static int healthlib=0;
    public static int maxbooked = getLocalConfirmedAppointmentContentModel().size();

    //ChatTypID
  //  public  static String

    public static void displayNoNetworkAlert(final Context context, View view){
        final Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet_connection_txt), Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.settings_txt), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                });

        snackbar.show();
    }
    public static void displayErrorAlert(View view, String message){
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Checking if user is connected to the Internet
    public static boolean isNetworkAvailable() {
        int timeOut = 5000;
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");

    }

    public static String getRelativeTime(Long date){
        Date createdAt = new Date(date);
        Long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS)
                .toString();

        return convertedDate;
    }

}
