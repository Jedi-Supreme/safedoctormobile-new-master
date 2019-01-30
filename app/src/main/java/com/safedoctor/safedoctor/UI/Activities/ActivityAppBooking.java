package com.safedoctor.safedoctor.UI.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AuxUtils.Date;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Utils.ViewAnimation;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.AppointmentModel;
import com.safedoctor.safedoctor.Model.Booking;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.ServiceFeeDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.AppConstants.NEW_APPT;

/**
 * Created by Stevkkys on 9/27/2017.
 */

public class ActivityAppBooking extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_info;
    private View lyt_expand_info;
    private int chatId, serviceFee;
    private AppCompatEditText alternativeNumberEditText;
    private CountDownTimer timer;
    private TextView serviceFeeTextView;
    private RadioGroup chatTypeRadioGroup;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<ServiceFeeDataModel>> serviceFeeResponse;
    private List<ServiceFeeDataModel> serviceFees;
    private SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>> bookNPayResponse;
    private List<ConfirmedAppointmentContentModel> confirmedAppointmentList;
    private ProgressDialog serviceProgressDialog;
    private boolean status = false;
    private Booking bookNPay;
    private TimeSlot timeslot;
    private boolean isnow=false;
    private TextView tv_booking_code,lbltitle,lblDate,lblstarttime,lblstartdate,lblendtime,lblenddate,lblpatientname,lbldoctorname,lblduration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbooking);

        timeslot = getIntent().getParcelableExtra("timeslot");
        isnow=getIntent().getBooleanExtra("now",false);

        initToolbar();
        initComponent();
        initControls();
        initData();
    }

    private void initControls()
    {
        Button bookDoctorBtn = findViewById(R.id.book_doctor_btn);

        serviceFeeTextView = findViewById(R.id.serviceFeeTextView);
        chatTypeRadioGroup = findViewById(R.id.chatTypeRadioGroup);
        RadioGroup mobileMoneyTypeRadioGroup = findViewById(R.id.mobileMoneyTypeRadioGroup);

        alternativeNumberEditText = findViewById(R.id.alternateNumberEditText);

        alternativeNumberEditText.setText(AppConstants.patientPhoneNumber);

        serviceProgressDialog = new ProgressDialog(this);
        serviceProgressDialog.setMessage("Working... Please wait");

        bookNPay = new Booking();

        //   viewProfileBtn.setOnClickListener(this);
        bookDoctorBtn.setOnClickListener(this);
        chatTypeRadioGroup.setOnCheckedChangeListener(this);
        mobileMoneyTypeRadioGroup.setOnCheckedChangeListener(this);

        lbltitle = findViewById(R.id.lbltitle);
        lblDate = findViewById(R.id.lblDate);
        lblstarttime = findViewById(R.id.lblstarttime);
        lblstartdate = findViewById(R.id.lblstartdate);
        lblendtime = findViewById(R.id.lblendtime);
        lblenddate = findViewById(R.id.lblenddate);
        lblpatientname = findViewById(R.id.lblpatientname);
        lbldoctorname = findViewById(R.id.lbldoctorname);
        lblduration = findViewById(R.id.lblduration);


        int checkedID = mobileMoneyTypeRadioGroup.getCheckedRadioButtonId();
        mobileMoneyTypeSetter(checkedID);

        mSafeDoctorService = ApiUtils.getAPIService();
    }

    private void initData()
    {

        lbltitle.setText(timeslot.specialtytext);
        lblDate.setText(Date.formateDate(timeslot.getDate(),"E, d MMMM, yyyy"));
        lblstarttime.setText(Date.formateTime(timeslot.getStarttime()));
        lblstartdate.setText(Date.formateDate(timeslot.getDate(),"d MMM"));
        lblendtime.setText(Date.formateTime(timeslot.getEndtime()));
        lblenddate.setText(Date.formateDate(timeslot.getDate(),"d MMM"));
        lblpatientname.setText(AppConstants.patientName);
        if(timeslot.getDoctorname() != null && !timeslot.getDoctorname().isEmpty() )
        {
            lbldoctorname.setText(timeslot.getDoctorname());
        }

        lblduration.setText("Duration: "+ Date.getTimeDifference(timeslot.getStarttime(), timeslot.getEndtime()));

        loadServiceFee(timeslot.getServiceid());
    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Appointment Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        // info item_section
        bt_toggle_info = findViewById(R.id.bt_toggle_info);
        Button bt_hide_info = findViewById(R.id.bt_hide_info);
        lyt_expand_info = findViewById(R.id.lyt_expand_info);
        bt_toggle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info);
            }
        });

        bt_hide_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info);
            }
        });



        // copy to clipboard
        tv_booking_code = findViewById(R.id.tv_booking_code);
        ImageButton bt_copy_code = findViewById(R.id.bt_copy_code);
        bt_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.copyToClipboard(getApplicationContext(), tv_booking_code.getText().toString());
            }
        });

        // nested scrollview
        nested_scroll_view = findViewById(R.id.nested_scroll_view);


    }

    private void toggleSectionInfo(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    ////Starting dump from old///

    private void loadServiceFee(int serviceid) {

        HashMap<String, Integer> query = new HashMap<>();

        if(chatId == 0)
        {
            chatId =1;
        }
        query.put("consultationchattypeid", chatId);
        query.put("patientid", AppConstants.patientID);

        serviceProgressDialog.setMessage("Working... Please wait");
        serviceProgressDialog.show();
        Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call = mSafeDoctorService.getServiceFee(TokenString.tokenString, serviceid, query);
        Log.i("Safe", "Fee URL is " + mSafeDoctorService.getServiceFee(TokenString.tokenString, serviceid, query).request().url().toString());
        call.enqueue(new Callback<SwagArrayResponseModel<List<ServiceFeeDataModel>>>() {
            @Override
            public void onResponse(@NonNull Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call,
                                   @NonNull Response<SwagArrayResponseModel<List<ServiceFeeDataModel>>> response) {
                Log.i("Safe", "LoadServiceFee Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()) {
                    Log.i("Safe", "Fetching Cash");
                    serviceFeeResponse = response.body();

                    if (serviceFeeResponse != null) {
                        serviceFees = serviceFeeResponse.getData();
                    }

                    for (ServiceFeeDataModel s : serviceFees) {
                        serviceFee = s.getFee();
                        Log.i("Safe", serviceFee + "");

                    }

                    serviceFeeTextView.setText("GHS" + serviceFee);

                    serviceProgressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Fetch Successful", Toast.LENGTH_SHORT).show();
                } else {
                    serviceProgressDialog.dismiss();
                    serviceFeeTextView.setText("GHS0.0");
                    //Toast.makeText(getApplicationContext(), "Unable To Fetch. Please Contact HelpLine", Toast.LENGTH_LONG).show();
                    Log.i("Safe", "Fetching fee error code " + response.code() + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call,
                                  @NonNull Throwable t) {

                //Log.i("SafeRes", t.getMessage());
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void loadBookAndPay()
    {
        serviceProgressDialog.setMessage("Initiating appointment booking...");
        serviceProgressDialog.show();

        bookNPay.setConsultationchattypeid(chatId);
        bookNPay.setDoctorid(timeslot.getDoctorid());
        bookNPay.setMobilemoneynetworkid(AppConstants.mobilemoneynetworkid);
        bookNPay.setPatientid(AppConstants.patientID);
        bookNPay.setReasonid(AppConstants.reasonid);
        bookNPay.setServiceid(timeslot.getServiceid());
        bookNPay.setSlotid(timeslot.getSlotid());

        bookNPay.setConsultationtypeid(getConsultationtypeid(isnow));


        bookNPay.setWalletnumber(alternativeNumberEditText.getText().toString());

        Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call = mSafeDoctorService.bookAndPay(TokenString.tokenString, bookNPay);
        Log.i("Safe", "After loadBookNPay, wallet is " + bookNPay.getWalletnumber());
        Log.i("Safe", "After loadBookNPay, chat is " + bookNPay.getConsultationchattypeid() + "");
        Log.i("Safe", "After loadBookNPay, patientid is " + bookNPay.getPatientid() + "");
        Log.i("Safe", "After loadBookNPay, network is " + bookNPay.getMobilemoneynetworkid() + "");
        Log.i("Safe", "After loadBookNPay, service is " + bookNPay.getServiceid() + "");
        Log.i("Safe", "After loadBookNPay, bookNPay is " + bookNPay.getSlotid() + "");
        Log.i("Safe", "After loadBookNPay, bookNPay is " + bookNPay.getReasonid() + "");
        Log.i("Safe", "After loadBookNPay, bookNPay is " + bookNPay.getDoctorid() + "");
        Log.i("Safe", "After loadBookNPay, bookNPay is " + bookNPay.getVodafonetoken() + "");

        call.enqueue(new Callback<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>>() {
            @Override
            public void onResponse(@NonNull Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call,
                                   @NonNull Response<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> response) {
                Log.i("Safe", "loadBookNPay Called");

                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN ) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()){
                    serviceProgressDialog.dismiss();
                    Log.i("Safe", "Fetching BookNPay Response");
                    bookNPayResponse = response.body();
                    if (bookNPayResponse != null) {
                        confirmedAppointmentList = bookNPayResponse.getData();
                    }
                    AppointmentModel appointment = confirmedAppointmentList.get(0).getAppointment();
                    Log.i("Safe", appointment.getBookingid() + "");

                    AppConstants.bookingid = appointment.getBookingid();

                    if(serviceFee==0){
                        Toast.makeText(getApplicationContext(), "Booking Successful.", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(NEW_APPT);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        Intent newintent=new Intent(new Intent(ActivityAppBooking.this,AppointmentActivity.class));
                        newintent.putExtra("refresh",true);
                        startActivity(newintent);

                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), "Booking Successful. Awaiting Payment", Toast.LENGTH_LONG).show();
                        waitUntilPayment();
                    }



                } else {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable To Make Booking. Please Contact HelpLine", Toast.LENGTH_LONG).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        if (response.errorBody() != null) {
                            Log.i("Safe", "Response is " + response.errorBody().string());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("SafeTggggg", "Out kwraa");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call,
                                  @NonNull Throwable t) {
                try {
                    Log.i("SafeRes", "Fetching error "+t.getMessage());
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadBookingStatus(int bookingid) {
        serviceProgressDialog.setMessage("Verifying Payment. \nPlease ensure payment has been made");
        serviceProgressDialog.show();

        Log.i("Safe", "loadBooking About To be called");
        Call<SwagArrayResponseModel> call = mSafeDoctorService.getBookingStatus(TokenString.tokenString, bookingid);
        Log.i("Safe", "Fee URL is " + mSafeDoctorService.getBookingStatus(TokenString.tokenString, bookingid).request().url().toString());
        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SwagArrayResponseModel> call, @NonNull Response<SwagArrayResponseModel> response) {
                Log.i("Safe", "loadBookingStatus Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.code() == 200) {
                    Log.i("Safe", "Fetching Booking Status");
                    serviceProgressDialog.dismiss();
                    status = true;
                    Toast.makeText(getApplicationContext(), "Payment Successful and your booking has been confirmed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 500)
                {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Payment Pending. Please Try Again", Toast.LENGTH_SHORT).show();
                }
                else {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable To Fetch Booking Status. Please Contact HelpLine", Toast.LENGTH_SHORT).show();
                    Log.i("Safe", "Fetching fee error code " + response.code() + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwagArrayResponseModel> call,
                                  @NonNull Throwable t) {
                Log.i("SafeRes","Fetching error" +t.getMessage());
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(!serviceProgressDialog.isShowing())
        {
            super.onBackPressed();
        }
        //serviceProgressDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.book_doctor_btn:
                //   if (mo)
                if(alternativeNumberEditText.getText().toString().isEmpty())
                {
                    alternativeNumberEditText.setError("Wallet number is required");
                    alternativeNumberEditText.requestFocus();
                }else if(chatTypeRadioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(),"Select a Consultation Type",Toast.LENGTH_LONG).show();
                }
                else {
                    popUpConfirmation();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()){
            case R.id.chatTypeRadioGroup:
                Log.i("Safe", "RadioCheck called");
                chatIdSetter(checkedId);
                loadServiceFee(timeslot.getServiceid());

                break;
            case R.id.mobileMoneyTypeRadioGroup:
                mobileMoneyTypeSetter(checkedId);
                break;
        }

    }

    int getConsultationtypeid(boolean isnow){
        if(isnow){
            return 3;//id for emergency
        }else{
            return 1;//id for schedule
        }
    }

    void chatIdSetter(int checkID) {
        switch (checkID) {
            case R.id.textChatRadioButton:
                chatId = 1;
                AppConstants.consultationchattypeid = chatId;
                break;
            case R.id.audioChatRadioButton:
                chatId = 2;
                AppConstants.consultationchattypeid = chatId;
                break;
            case R.id.videoChatRadioButton:
                chatId = 3;
                AppConstants.consultationchattypeid = chatId;
                break;
        }
    }

    public void mobileMoneyTypeSetter(int checkID){
        switch (checkID)
        {
            case R.id.vodafoneRadioButton:
                AppConstants.mobilemoneynetworkid = "VODAFONE";
                popUpVodafoneToken();
                break;
            case R.id.mtnRadioButton:
                AppConstants.mobilemoneynetworkid = "MTN";
                bookNPay.setVodafonetoken(null);
                break;
            case R.id.airtelRadioButton:
                AppConstants.mobilemoneynetworkid = "AIRTEL";
                bookNPay.setVodafonetoken(null);
                break;
            case R.id.tigoRadioButton:
                AppConstants.mobilemoneynetworkid = "TIGO";
                bookNPay.setVodafonetoken(null);
                break;
        }

    }

    public void popUpConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Booking and Payment!");
        builder.setMessage("You are about to book and make payment. Kindly Confirm ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadBookAndPay();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Booking Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void popUpMakePaymentTransaction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment");
        builder.setMessage("After making payment via USSD.\nPress Confirm or Cancel to Not Make Payment");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadBookingStatus(AppConstants.bookingid);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status)
                {
                    Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

    public void waitUntilPayment()
    {
        serviceProgressDialog.setCancelable(false);
        serviceProgressDialog.setTitle("Payment Initiated.");
        serviceProgressDialog.setMessage("Please proceed to make payment via USSD on "+ AppConstants.mobilemoneynetworkid);
        serviceProgressDialog.show();

        final Common c = new Common(getApplicationContext());

        c.setOnTaskCompletedListener(new OnTaskCompleted()
        {
            @Override
            public void onTaskCompleted(Object result) {
                boolean response = (boolean) result;

                status = response;
                if(response)
                {
                    if(timer != null)
                    {
                        timer.cancel();
                    }
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Payment Successful and your booking has been confirmed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


       timer = new CountDownTimer(120000,20000) {

            @Override
            public void onTick(long millisUntilFinished)
            {
                if(!status) {
                    c.checkBookingStatus(AppConstants.bookingid);
                }
                else
                {
                    if(timer != null)
                    {
                        timer.cancel();
                    }
                }
            }

            @Override
            public void onFinish()
            {
                serviceProgressDialog.dismiss();
                if(!status)
                {
                    Toast.makeText(getApplicationContext(), "Your booking could not be confirmed. Please try again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Payment Successful and your booking has been confirmed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.start();


    }

    public void popUpVodafoneToken()
    {
        //   = new EditText(this);
        // edtText.setBackground(R.drawable.edit_text_bg);
//        vodaTokenEt = new EditText(this);
//        vodaTokenEt.setBackgroundResource(R.drawable.edit_text_bg_grey);
//        vodaTokenEt.setPadding(26,26,26,26);

        final  EditText vodaTokenEt;
        final  TextView btnCancel;
        final TextView btnConfirm;




        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //   builder.setTitle("Vodafone Cash");
        // builder.setMessage("Please Enter Vodafone Cash Token");
        LayoutInflater inflater = this.getLayoutInflater();
        View  RootView = inflater.inflate(R.layout.vodafone_token_alert,null);

        vodaTokenEt = RootView.findViewById(R.id.vodaTokenEt);
        btnCancel = RootView.findViewById(R.id.btn_cancel);
        btnConfirm = RootView.findViewById(R.id.btn_confirm);

        builder.setCancelable(false);
        builder.setView(RootView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Token Not Set", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vodaTokenEt.getText().toString().trim().matches(""))
                {
                    vodaTokenEt.setError("please enter token");
                    vodaTokenEt.requestFocus();
                }
                else
                {
                    bookNPay.setVodafonetoken(vodaTokenEt.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });

    }
}
