package com.safedoctor.safedoctor.UI.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ServiceContentModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.stevkky.customcalendarview.CalendarView;
import com.stevkky.customcalendarview.EventDay;
import com.stevkky.customcalendarview.listeners.OnDayClickListener;
import com.stevkky.customcalendarview.listeners.OnMonthChangeListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Adapter.SlotAdapter.getSpecialtyName;
import static com.safedoctor.safedoctor.Api.Common.tokenerror;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Stevkkys on 9/24/2017.
 */


public class NewAppointment extends AppCompatActivity implements OnTaskCompleted {

    private SafeDoctorService mSafeDoctorService;
    private SwagResponseModel<List<ServiceContentModel>> swagServiceResponse;
    private CalendarView mCalendarView;
    //private List<EventDay> mEventDays = new ArrayList<>();
    private Common common;
    private AppCompatEditText spnspecialty,spndoctors;
    private TextView seeyourdoctors;
    private ProgressBar progress;
    private  HashMap<Integer, List<TimeSlot>> calendarslots = new HashMap<Integer, List<TimeSlot>>();

    private static final int GET_SELECTED_DOCTOR_FROM_PROFILE = 1;
    private boolean isnow=false;
    private int emergencyslottypeid=2;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newappointment);

        iniToolbar();
        initComponent();
        isnow=getIntent().getBooleanExtra("now",false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching slots, please wait...");
        mProgressDialog.setCancelable(true);

        if(isnow){
            mProgressDialog.show();
        }


        setupCalendar();



    }

    private void iniToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Create new appointment");
        setSupportActionBar(toolbar);
    }

    private void initComponent()
    {
        common = new Common(getApplicationContext(),this);
        mSafeDoctorService = ApiUtils.getAPIService();

        progress = (ProgressBar) findViewById(R.id.progress);
        spndoctors = (AppCompatEditText) findViewById(R.id.spndoctors);

        final List<AppCompatEditText> dependants = new ArrayList<AppCompatEditText>();

        dependants.add(spndoctors);

        spnspecialty = (AppCompatEditText) findViewById(R.id.spnspecialty);

        /*
        if( null != AppConstants.CACHE_SPECIALTIES && AppConstants.CACHE_SPECIALTIES.size() > 0) {
            AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_SPECIALTIES,
                    AppConstants.CACHE_SPECIALTIES.get(0).getId()), spnspecialty);

        }*/


        if( null != AppConstants.CACHE_SPECIALTIES && AppConstants.CACHE_SPECIALTIES.size() > 0) {
            AuxUtils.setDropdownValue(new BasicObject(0,"All"), spnspecialty);

        }


        spnspecialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common.displayDropDownList(NewAppointment.this, AppConstants.CACHE_SPECIALTIES,spnspecialty,null,dependants);
            }
        });

        spndoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppConstants.CACHE_DOCTORS.size()==0){
                    Toast.makeText(getApplicationContext(),"Fetching data...try again soon",Toast.LENGTH_LONG).show();
                    return;
                }
                common.displayDoctorsList(NewAppointment.this, AppConstants.CACHE_DOCTORS,spndoctors,null,null);
            }
        });

        spnspecialty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Log.i("Safe",  " beforeTextChanged: ID:" +AuxUtils.getTagValue(spnspecialty.getTag()) + " Text:" + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.i("Safe",  " onTextChanged: ID:" +AuxUtils.getTagValue(spnspecialty.getTag()) + " Text:" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.i("Safe",  "afterTextChanged: ID:" +AuxUtils.getTagValue(spnspecialty.getTag()) + " Text:" + s.toString());

                common.getSpecialtyDoctors(AuxUtils.getTagValue(spnspecialty.getTag()));
                getSpecialtySlots();
            }
        });


        spndoctors.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getSpecialtySlots();
            }
        });


        seeyourdoctors = (TextView) findViewById(R.id.seeyourdoctors);

        seeyourdoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spnspecialty.getText() != null)
                {
                    Intent intent = new Intent(getApplicationContext(), ActivityDoctorsList.class);
                    Bundle b = new Bundle();
                    intent.putExtra("specialtyid", AuxUtils.getTagValue(spnspecialty.getTag()));
                    intent.putExtra("Specialityname",spnspecialty.getText().toString());
                    startActivityForResult(intent, GET_SELECTED_DOCTOR_FROM_PROFILE);
                }

            }
        });

    }


    private void setupCalendar()
    {
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        if(isnow){
            mProgressDialog.show();
            getSpecialtySlots();
        }

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay)
            {

                if(eventDay.getEventcount() > 0)
                {
                    common.displaySlotList(NewAppointment.this,calendarslots.get(eventDay.getEventid().intValue()),"Choose preferred time",spnspecialty.getText().toString());
                }

            }
        });

        mCalendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void OnMonthChange(int state) {

                if(state == 0) //the calendar page is completely changed
                {
                    getSpecialtySlots();
                }
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCalendarAppointments(AppConstants.CACHE_SLOTS);

            }
        }, 10000);//10 secs later to prevents black screen due to blocking on uithread






    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case GET_SELECTED_DOCTOR_FROM_PROFILE:
                if( (resultCode == RESULT_OK) && (data.getStringExtra("selecteddoctoruserid") != null && !data.getStringExtra("selecteddoctoruserid").isEmpty()))
                {
                    spndoctors.setTag(data.getStringExtra("selecteddoctoruserid"));
                    spndoctors.setText(data.getStringExtra("selecteddoctorname"));
                }
                break;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        if(result instanceof TimeSlot)
        {
            TimeSlot t = (TimeSlot) result;
           // t.specialtytext = spnspecialty.getText().toString();
            t.specialtytext=getSpecialtyName(t.getSpecialityid());

            if(t.getServiceid() > 0)
            {
                AppConstants.serviceID = t.getServiceid();
            }
            else
            {
                AppConstants.serviceID = Integer.parseInt(spnspecialty.getTag().toString());
                t.setServiceid(AppConstants.serviceID);
            }
            Intent intent = new Intent(getApplicationContext(), ActivityAppBooking.class);
            intent.putExtra("timeslot", t);
            intent.putExtra("now",isnow);
            startActivity(intent);
        }

    }

    private void showProgress(final boolean status)
    {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(status)
                {
                    progress.setVisibility(View.VISIBLE);
                }
                else
                {
                    progress.setVisibility(View.GONE);
                }
            }
        });


    }


    public void getSpecialtySlots()
    {
        mProgressDialog.show();
        mCalendarView.clearEvents();
        calendarslots.clear();

        int specialtyid = AuxUtils.getTagValue(spnspecialty.getTag());


        if(specialtyid == 0)//to get all slots
        {
            //Toast.makeText(context,"Select Specialty",Toast.LENGTH_LONG).show();
            //return;
        }

        HashMap<String, String> queryparams = new HashMap<String, String>();

        if(specialtyid!=0){//when patientid is passed with specialty 0 , no data is returned
            queryparams.put("patientid", String.valueOf(AppConstants.patientID));
        }


        String doctoruserid = AuxUtils.getCharTagValue(spndoctors.getTag(), true);
        if(doctoruserid != null)
        {
            queryparams.put("doctoruserid", doctoruserid);
        }

        String[] monthlimits = AuxUtils.Date.getMonthBeginEndDates(mCalendarView.getCurrentPageDate(), false);
        if(monthlimits == null)
        {
            return;
        }



        if(isnow){
            queryparams.put("slottypeid",emergencyslottypeid+"" );

            getSpecialtyAvailableServices(specialtyid,queryparams);
        }else{
            queryparams.put("from", monthlimits[0]);
            queryparams.put("to", monthlimits[1]);
            getSpecialtyAvailableServices(specialtyid,queryparams);
        }




    }

    private void setCalendarAppointments(final List<TimeSlot> slotslist)
    {

      this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              mCalendarView.clearEvents();
              calendarslots.clear();


              try{
                  mProgressDialog.show();
              }catch (Exception e){

              }

          }
      });



        if(slotslist == null || slotslist.size() == 0 )
        {
            return;
        }


        String[] monthlimits = AuxUtils.Date.getMonthBeginEndDates(mCalendarView.getCurrentPageDate(), false);


        if(monthlimits == null)
        {
            return;
        }

        showProgress(true);

       final  List<String> datelist = AuxUtils.Date.generateDateList(monthlimits[0],monthlimits[1]);

        final List<EventDay> mEventDays = new ArrayList<>();



        int index = 0;
        for(String date : datelist)
        {
            EventDay eday = new EventDay(AuxUtils.Date.getCalendarDate(date));
            List<TimeSlot> tempslots = new ArrayList<TimeSlot>();

            for (TimeSlot slot : slotslist)
            {


                if(date.equalsIgnoreCase((AuxUtils.Date.formateDate(slot.getDate()))))
                {
                    if(isDateToday(date)){
                        if(slotTimeNotPassed(slot)==true){
                            eday.setEventcount(eday.getEventcount() + 1);
                            tempslots.add(slot);
                        }

                    }else {
                        eday.setEventcount(eday.getEventcount() + 1);
                        tempslots.add(slot);
                    }

                }


            }

            if(eday.getEventcount() > 0)
            {
                index++;
                eday.setEventid(index);
                mEventDays.add(eday);

                calendarslots.put(index,tempslots);
            }
        }







        mCalendarView.setEvents(mEventDays);
        doctorSlotCount(slotslist);


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                mProgressDialog.dismiss();
            }
        });





    }


    void doctorSlotCount(List<TimeSlot> slotslist){
        int i=0;
        for (DoctorOutObj doc:AppConstants.CACHE_DOCTORS) {
            i=0;
            for (TimeSlot slot:slotslist) {
                if(isDateToday(slot.getDate())) {
                    if (doc.getDoctor().getId().equals(slot.getDoctorid()) && slotTimeNotPassed(slot)) {
                        i++;
                    }
                }else{
                    if (doc.getDoctor().getId().equals(slot.getDoctorid()) ) {
                        i++;
                    }
                }
            }

            doc.setSlotcount(i);
        }
    }


    private boolean slotTimeNotPassed(TimeSlot slot){
        SimpleDateFormat otimeFormatter = new SimpleDateFormat("HH:mm:s");

        Date tday=new Date(System.currentTimeMillis());
        String currtime=otimeFormatter.format(tday);


        try {
            Date slotendtime=otimeFormatter.parse(slot.getEndtime());
            Date now=otimeFormatter.parse(currtime);
            if(!slotendtime.before(now)){
                //newlist.add(slot);
                return true;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

    private boolean isDateToday(String date){
        Date cdate=new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String curdate=dateFormatter.format(cdate);

        try {
            Date currdate = dateFormatter.parse(curdate);
            Date slotdate=dateFormatter.parse(date);
            if(currdate.equals(slotdate)){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }


    void getSpecialtyAvailableServices(int specialtyid,HashMap<String, String> queryparams){
        Call<SwagArrayResponseModel<List<TimeSlot>>> call = mSafeDoctorService.getSpecialtyAvailableServices(TokenString.tokenString,specialtyid,queryparams);
        showProgress(true);
        mProgressDialog.show();
        call.enqueue(new Callback<SwagArrayResponseModel<List<TimeSlot>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<TimeSlot>>> call, final Response<SwagArrayResponseModel<List<TimeSlot>>> response) {
                //  Log.i("Safe", "loadServices Called");
                mProgressDialog.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Your session has timed out. Please login again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext().getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                    finish();

                }
                else if (response.isSuccessful())
                {
                    List<TimeSlot> timeSlots=response.body().getData();
                    if(timeSlots.size()==0){
                        if(isnow){

                            builder.setTitle("No slots");

                            // set dialog message
                            builder
                                    .setMessage("No emergency slots available at the moment")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                        }
                                    })
                                    .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = builder.create();

                            // show it
                            alertDialog.show();
                            Toast.makeText(context,"No emergency slots available",Toast.LENGTH_LONG).show();
                            isnow=false;

                        }else{
                            Toast.makeText(context,"No slots available",Toast.LENGTH_LONG).show();
                        }

                    }else{


                        setCalendarAppointments(response.body().getData());


                    }


                    showProgress(false);

                }else{
                    try {
                        String error = response.errorBody().string();
                        tokenerror(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<TimeSlot>>> call, Throwable t) {
                mProgressDialog.dismiss();
                showProgress(false);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

