package com.safedoctor.safedoctor.UI.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.AvailableServiceAdapter;
import com.safedoctor.safedoctor.Adapter.ServiceAdapter;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.AppointmentModel;
import com.safedoctor.safedoctor.Model.Booking;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.Doctor;
import com.safedoctor.safedoctor.Model.ServiceAvailabilityContentModel;
import com.safedoctor.safedoctor.Model.ServiceContentModel;
import com.safedoctor.safedoctor.Model.ServiceFeeDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.ClickListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAppointment extends AppCompatActivity implements View.OnClickListener, ClickListener,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener,android.app.DatePickerDialog.OnDateSetListener {

    ArrayList<ServiceContentModel> servicesList;
    ArrayList<ServiceAvailabilityContentModel> serviceAvailabilityList;
    ServiceAdapter serviceAdapter;
    AvailableServiceAdapter availableServiceAdapter;
    int chatId, serviceFee;

    private EditText alternativeNumberEditText;

    private TextView assignedDoctorTextView;
    private ImageView assignedDoctorImageView;

    private EditText fromDateEditText;
    private EditText toDateEditText;
    private android.app.DatePickerDialog fromDatePickerDialog;
    private android.app.DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private TextView serviceFeeTextView;
    private RadioGroup chatTypeRadioGroup;
    private RadioGroup mobileMoneyTypeRadioGroup;
    private Spinner serviceSpinner, availableServiceSpinner;
    private Button  bookDoctorBtn;

    private SafeDoctorService mSafeDoctorService;
    private SwagResponseModel<List<ServiceContentModel>> swagServiceResponse;
    private SwagResponseModel<List<ServiceAvailabilityContentModel>> availabilityResponseModel;
    private DataModel<List<ServiceContentModel>> data;
    private DataModel<List<ServiceAvailabilityContentModel>> serviceAvailabilityDataModel;
    private ArrayList<Doctor> mDoctorList;
    private List<ServiceContentModel> services;
    private List<ServiceAvailabilityContentModel> availableServiceList;

    private SwagArrayResponseModel<List<ServiceFeeDataModel>> serviceFeeResponse;
    private List<ServiceFeeDataModel> serviceFees;
    private SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>> bookNPayResponse;

    private List<ConfirmedAppointmentContentModel> confirmedAppointmentList;

    private SwagArrayResponseModel bookingStatusResponse;

    private ProgressDialog serviceProgressDialog;

    private boolean status = false;

    Booking bookNPay;


    //// TODO: 8/20/17 Fix ProgressDialog issues when you go back and come back to appointment 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // viewProfileBtn = (Button) findViewById(R.id.view_doctor_profile_btn);
        bookDoctorBtn = (Button) findViewById(R.id.book_doctor_btn);

        serviceSpinner = (Spinner) findViewById(R.id.serviceSpinner);


        availableServiceSpinner = (Spinner) findViewById(R.id.availableServiceSpinner);
        serviceFeeTextView = (TextView) findViewById(R.id.serviceFeeTextView);
        chatTypeRadioGroup = (RadioGroup) findViewById(R.id.chatTypeRadioGroup);
        mobileMoneyTypeRadioGroup = (RadioGroup) findViewById(R.id.mobileMoneyTypeRadioGroup);

        alternativeNumberEditText = (EditText) findViewById(R.id.alternateNumberEditText);

        assignedDoctorImageView = (ImageView) findViewById(R.id.assignedDoctorImageView);
        assignedDoctorTextView = (TextView) findViewById(R.id.assignedDoctorTextView);

        fromDateEditText = (EditText) findViewById(R.id.fromDateEditText);
        fromDateEditText.setInputType(InputType.TYPE_NULL);

        toDateEditText = (EditText) findViewById(R.id.toDateEditText);
        toDateEditText.setInputType(InputType.TYPE_NULL);

        serviceProgressDialog = new ProgressDialog(this);
        serviceProgressDialog.setMessage("Working... Please wait");

        fromDateEditText.setOnClickListener(this);
        toDateEditText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new android.app.DatePickerDialog(this,this,newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog = new android.app.DatePickerDialog(this,this,newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
        fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() +100000000);

        //Toast.makeText(getApplicationContext(),System.currentTimeMillis()+"",Toast.LENGTH_LONG).show();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        bookNPay = new Booking();
        Calendar todayDate = Calendar.getInstance();
        fromDateEditText.setText(dateFormatter.format(todayDate.getTime()));
        toDateEditText.setText(dateFormatter.format(todayDate.getTime()));

     //   viewProfileBtn.setOnClickListener(this);
        bookDoctorBtn.setOnClickListener(this);
        serviceSpinner.setOnItemSelectedListener(this);
        availableServiceSpinner.setOnItemSelectedListener(this);
        chatTypeRadioGroup.setOnCheckedChangeListener(this);
        mobileMoneyTypeRadioGroup.setOnCheckedChangeListener(this);

        int checkedID = mobileMoneyTypeRadioGroup.getCheckedRadioButtonId();
        mobileMoneyTypeSetter(checkedID);

        mSafeDoctorService = ApiUtils.getAPIService();
        loadServices();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadServices() {
        serviceProgressDialog.show();

        servicesList = new ArrayList<>();

        Call<SwagResponseModel<List<ServiceContentModel>>> call = mSafeDoctorService.getServiceResponse(TokenString.tokenString, AppConstants.patientID);
        call.enqueue(new Callback<SwagResponseModel<List<ServiceContentModel>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<ServiceContentModel>>> call, Response<SwagResponseModel<List<ServiceContentModel>>> response) {
                Log.i("Safe", "loadServices Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()) {
                    Log.i("Safe", "Fetching Services");
                    swagServiceResponse = response.body();
                    data = swagServiceResponse.getData();
                    services = data.getContent();

                    for (ServiceContentModel serviceContentModel : services) {
                        servicesList.add(serviceContentModel);
                    }

                    serviceAdapter = new ServiceAdapter(CreateAppointment.this, R.layout.services_spinner_layout, R.id.service_name, servicesList);
                    serviceSpinner.setAdapter(serviceAdapter);

                    serviceProgressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Fetch Successful", Toast.LENGTH_SHORT).show();
                } else {
                    serviceProgressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Unable To Fetch. Please Contact HelpLine", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<ServiceContentModel>>> call, Throwable t) {
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void loadAvailableServices(int serviceid, HashMap <String, String> query) {
        serviceProgressDialog.setMessage("Fetch Available Slots For Selected Service");
        serviceProgressDialog.show();
        serviceAvailabilityList = new ArrayList<>();

        Call<SwagResponseModel<List<ServiceAvailabilityContentModel>>> call = mSafeDoctorService.getAvailableServices(TokenString.tokenString, serviceid, query);
        call.enqueue(new Callback<SwagResponseModel<List<ServiceAvailabilityContentModel>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<ServiceAvailabilityContentModel>>> call, Response<SwagResponseModel<List<ServiceAvailabilityContentModel>>> response) {
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()) {
                    Log.i("Safe", "loacAvailableServices successfully called");
                    availabilityResponseModel = response.body();

                    if (availabilityResponseModel.getData() == null) {
                        List<String> noServiceAvailable = new ArrayList<String>();
                        noServiceAvailable.add("No Service Available");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateAppointment.this, android.R.layout.simple_spinner_item, noServiceAvailable);
                        availableServiceSpinner.setAdapter(dataAdapter);
                        availableServiceList = null;

                        serviceProgressDialog.dismiss();
                       // Toast.makeText(getApplicationContext(), "No Service Available", Toast.LENGTH_SHORT).show();
                    } else {
                        serviceAvailabilityDataModel = availabilityResponseModel.getData();
                        availableServiceList = serviceAvailabilityDataModel.getContent();

                        for (ServiceAvailabilityContentModel serviceAvailability : availableServiceList) {
                            serviceAvailabilityList.add(serviceAvailability);
                            Log.i("Safe", "Slot id is " + serviceAvailability.getSlotid());
                        }

                        availableServiceAdapter = new AvailableServiceAdapter(CreateAppointment.this, R.layout.service_availabilty_spinner_layout, R.id.doc_name_tv, serviceAvailabilityList);
                        availableServiceSpinner.setAdapter(availableServiceAdapter);

                        serviceProgressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "Fetch Successful", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    serviceProgressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Unable To Fetch. Please Contact HelpLine", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<ServiceAvailabilityContentModel>>> call, Throwable t) {
                Log.i("SafeRes","Fetching error"+  t.getMessage());
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void loadServiceFee(int serviceid, HashMap<String, Integer> query) {
        serviceProgressDialog.setMessage("Fetching Fee For Selected Slot");
        serviceProgressDialog.show();
        Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call = mSafeDoctorService.getServiceFee(TokenString.tokenString, serviceid, query);
        Log.i("Safe", "Fee URL is " + mSafeDoctorService.getServiceFee(TokenString.tokenString, serviceid, query).request().url().toString());
        call.enqueue(new Callback<SwagArrayResponseModel<List<ServiceFeeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call, Response<SwagArrayResponseModel<List<ServiceFeeDataModel>>> response) {
                Log.i("Safe", "LoadServiceFee Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()) {
                    Log.i("Safe", "Fetching Cash");
                    serviceFeeResponse = response.body();

                    serviceFees = serviceFeeResponse.getData();

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
            public void onFailure(Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> call, Throwable t) {
                Log.i("SafeRes", "Fetching error"+ t.getMessage());
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }



    public void loadBookAndPay()
    {
        serviceProgressDialog.setMessage("Creating Booking and Making Payment");
        serviceProgressDialog.show();

        String wallet = AppConstants.patientPhoneNumber + "555";
        bookNPay.setConsultationchattypeid(AppConstants.consultationchattypeid);
        bookNPay.setDoctorid(AppConstants.doctorid);
        bookNPay.setMobilemoneynetworkid(AppConstants.mobilemoneynetworkid);
        bookNPay.setPatientid(AppConstants.patientID);
        bookNPay.setReasonid(AppConstants.reasonid);
        bookNPay.setServiceid(AppConstants.serviceID);
        bookNPay.setSlotid(AppConstants.slotid);

        if(!alternativeNumberEditText.getText().toString().isEmpty())
        {
            bookNPay.setWalletnumber(alternativeNumberEditText.getText().toString());
        }
        else {
            bookNPay.setWalletnumber(AppConstants.patientPhoneNumber);
        }



        Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call = mSafeDoctorService.bookAndPay(TokenString.tokenString, bookNPay);
        Log.i("Safe", "After loadBookNPay, wallet is " + bookNPay.getWalletnumber().toString() + " pNo is " + wallet);
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
            public void onResponse(Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call, Response<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> response) {
                Log.i("Safe", "loadBookNPay Called");

                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()){
                    serviceProgressDialog.dismiss();
                    Log.i("Safe", "Fetching BookNPay Response");
                    bookNPayResponse = response.body();
                    confirmedAppointmentList = bookNPayResponse.getData();
                    AppointmentModel appointment = confirmedAppointmentList.get(0).getAppointment();
                    Log.i("Safe", appointment.getBookingid() + "");

                    AppConstants.bookingid = appointment.getBookingid();
                    Toast.makeText(getApplicationContext(), "Booking Successful. Awaiting Payment", Toast.LENGTH_LONG).show();
                    popUpMakePaymentTransaction();
                } else {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable To Make Booking. Please Contact HelpLine", Toast.LENGTH_LONG).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        Log.i("Safe", "Response is " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("SafeTggggg", "Out kwraa");
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> call, Throwable t) {
                try {
                    Log.i("SafeRes","Fetching error"+  t.getMessage());
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
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                Log.i("Safe", "loadBookingStatus Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    serviceProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.code() == 200) {
                    Log.i("Safe", "Fetching Booking Status");
                    bookingStatusResponse = response.body();
                    serviceProgressDialog.dismiss();
                    status = true;
                    Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable t) {
                Log.i("SafeRes", "Fetching error"+ t.getMessage());
                serviceProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        serviceProgressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          //  case R.id.view_doctor_profile_btn:
//                selectedDoctor = mDoctorList.get(availableDocsSpinner.getSelectedItemPosition());
//                Intent intent = new Intent(CreateAppointment.this, DoctorProfile.class);
//                intent.putExtra(AppConstants.DOCTOR_IMG_URL, selectedDoctor.getImageUrl());
//                intent.putExtra(AppConstants.DOCTOR_FULL_NAME, selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName());
//                intent.putExtra(AppConstants.DOCTOR_JOB_TITLE, selectedDoctor.getJobTitle());
//                startActivity(intent);
             //   break;
            case R.id.fromDateEditText:
                fromDatePickerDialog.show();
                break;
            case R.id.toDateEditText:
                toDatePickerDialog.show();
                break;
            case R.id.book_doctor_btn:
             //   if (mo)
                popUpConfirmation();
                break;
        }
    }

    @Override
    public void onClick(View view, int position) {
        switch (view.getId()) {
//            case R.id.availableDocsSpinner:
//                Doctor doctor = mDoctorList.get(position);
//                showPopupMenu(view, doctor);
//                break;
            case R.id.serviceSpinner:
                break;
        }

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    private void showPopupMenu(View view, Doctor doctor) {
        // inflate menu
        PopupMenu popup = new PopupMenu(CreateAppointment.this, view);
        popup.getMenuInflater().inflate(R.menu.menu_available_doctor, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(doctor));
        popup.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.serviceSpinner:
                Log.i("Safe", "Service Spinner Called");

                ServiceContentModel service = (ServiceContentModel) parent.getItemAtPosition(position);
                AppConstants.serviceID = service.getId();

                int checkedID = chatTypeRadioGroup.getCheckedRadioButtonId();

                chatIdSetter(checkedID);

            //    setDateFields();

                HashMap<String, Integer> serviceFeequery = new HashMap<String, Integer>();
                serviceFeequery.put("consultationchattypeid", chatId);
                serviceFeequery.put("patientid", AppConstants.patientID);

                HashMap<String, String> availableServiceQuery = new HashMap<String, String>();
                availableServiceQuery.put("from", fromDateEditText.getText().toString());
                availableServiceQuery.put("to", toDateEditText.getText().toString());

                AppConstants.serviceFeeQuery = serviceFeequery;

                loadAvailableServices(AppConstants.serviceID, availableServiceQuery);
                loadServiceFee(AppConstants.serviceID, AppConstants.serviceFeeQuery);

                AppConstants.walletnumber = AppConstants.patientPhoneNumber;
                break;

            case R.id.availableServiceSpinner:
                mDoctorList = new ArrayList<>();
                String imageUrl = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAg_AAAAJDFhNDU5Mzk4LTE4MzgtNDJlNy1hYmZiLTU2MTFlY2MwNzBmYQ.jpg";

                if (availableServiceList != null) {
                    Log.i("Safe", "Available Service Spinner Selected");
                    ServiceAvailabilityContentModel serviceAvailability = (ServiceAvailabilityContentModel) parent.getItemAtPosition(position);
                    for (ServiceAvailabilityContentModel s : availableServiceList) {
                        if (s.getDoctorname().equalsIgnoreCase(serviceAvailability.getDoctorname())) {
                            mDoctorList.add(new Doctor(serviceAvailability.getDoctorname(), imageUrl));
                        }
                    }

                    AppConstants.slotid = serviceAvailability.getSlotid();
                    Log.i("Safe", "Book.slot id is " + AppConstants.slotid);
                    AppConstants.doctorid = serviceAvailability.getDoctorid();

                    assignedDoctorTextView.setText(serviceAvailability.getDoctorname());

                    //Toast.makeText(this, "List being empty is " + mDoctorList.isEmpty(), Toast.LENGTH_LONG).show();
                }
                else {
                    assignedDoctorTextView.setText("No Doctor Assigned");
                }

//                loadDoctors(mDoctorList);
                break;

//            case R.id.availableDocsSpinner:
//            if (availableServiceList != null) {
//                Doctor aDoctor = (Doctor) parent.getItemAtPosition(position);
//                Log.i("Safe", "Doctor Spinner Selected");
//            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //// TODO: 8/20/17 Pull from api/setup/consultationchattypes and passing it to constants

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()){
            case R.id.chatTypeRadioGroup:
                Log.i("Safe", "RadioCheck called");
                chatIdSetter(checkedId);

                HashMap<String, String> availableServiceQuery = new HashMap<String, String>();
                availableServiceQuery.put("from", fromDateEditText.getText().toString());
                availableServiceQuery.put("to", toDateEditText.getText().toString());

                loadServiceFee(AppConstants.serviceID, AppConstants.serviceFeeQuery);

                break;
            case R.id.mobileMoneyTypeRadioGroup:
                mobileMoneyTypeSetter(checkedId);
                break;
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

//    void setDateFields()
//    {
//        Calendar newCalendar = Calendar.getInstance();
//    //   efldk
//        fromDatePickerDialog = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//
//                fromDateEditText.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        toDatePickerDialog = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//
//                toDateEditText.setText(dateFormatter.format(newDate.getTime()));
//
//                HashMap<String, String> availableServiceQuery = new HashMap<String, String>();
//                availableServiceQuery.put("from", fromDateEditText.getText().toString());
//                availableServiceQuery.put("to", toDateEditText.getText().toString());
//
//                loadAvailableServices(AppConstants.serviceID, availableServiceQuery);
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if (datePicker == fromDatePickerDialog.getDatePicker())
        {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            fromDateEditText.setText(dateFormatter.format(newDate.getTime()));
        }
        else {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            toDateEditText.setText(dateFormatter.format(newDate.getTime()));

            HashMap<String, String> availableServiceQuery = new HashMap<String, String>();
            availableServiceQuery.put("from", fromDateEditText.getText().toString());
            availableServiceQuery.put("to", toDateEditText.getText().toString());

            loadAvailableServices(AppConstants.serviceID, availableServiceQuery);
        }
    }

    //// TODO: 8/20/17 make selection of doctor similar to other spinners 
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private Doctor mDoctor;

        private MyMenuItemClickListener(Doctor doctor) {
            mDoctor = doctor;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view_profile:
                    Intent intent = new Intent(CreateAppointment.this, DoctorProfile.class);
                    intent.putExtra(AppConstants.DOCTOR_FULL_NAME, mDoctor.getFullName());
                    intent.putExtra(AppConstants.DOCTOR_IMG_URL, mDoctor.getImageUrl());
                    intent.putExtra(AppConstants.DOCTOR_JOB_TITLE, mDoctor.getJobTitle());
                    startActivity(intent);
                    return true;
                case R.id.action_book:
                    Toast.makeText(CreateAppointment.this, "Book", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
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
                if (status == true)
                {
                    Toast.makeText(getApplicationContext(), "Nice Try, Payment Successful", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
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

        vodaTokenEt = (EditText) RootView.findViewById(R.id.vodaTokenEt);
        btnCancel = (TextView) RootView.findViewById(R.id.btn_cancel);
        btnConfirm = (TextView) RootView.findViewById(R.id.btn_confirm);

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

//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),vodaTokenEt.getText().toString().length(),Toast.LENGTH_LONG).show();
//                if (vodaTokenEt.getText().toString().trim().matches(""))
//                {
//                    vodaTokenEt.setError("please enter token");
//                    vodaTokenEt.requestFocus();
//                }
//                else
//                {
//                    bookNPay.setVodafonetoken(vodaTokenEt.getText().toString());
//                }
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                Toast.makeText(getApplicationContext(), "Token Not Set", Toast.LENGTH_SHORT).show();
//            }
//        });

     //   builder.show();
    }
}
