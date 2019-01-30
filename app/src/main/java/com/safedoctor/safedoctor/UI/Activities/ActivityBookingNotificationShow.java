package com.safedoctor.safedoctor.UI.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Booking;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.ServiceFeeDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.List;

/**
 * Created by stevkky on 10/16/17.
 */

public class ActivityBookingNotificationShow extends AppCompatActivity implements View.OnClickListener
{
    private ImageButton bt_toggle_info;
    private Button bt_hide_info;
    private View lyt_expand_info;
    private int chatId, serviceFee;
    private AppCompatEditText alternativeNumberEditText;
    private CountDownTimer timer;
    private TextView serviceFeeTextView;
    private RadioGroup chatTypeRadioGroup;
    private RadioGroup mobileMoneyTypeRadioGroup;
    private Button  bookDoctorBtn;
    private SwagArrayResponseModel<List<ServiceFeeDataModel>> serviceFeeResponse;
    private List<ServiceFeeDataModel> serviceFees;
    private SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>> bookNPayResponse;
    private List<ConfirmedAppointmentContentModel> confirmedAppointmentList;
    private ProgressDialog serviceProgressDialog;
    private boolean status = false;
    private Booking bookNPay;
    private TimeSlot timeslot;
    private TextView bookingnumber,tv_booking_code,lbltitle,lblDate,lblstarttime,lblstartdate,lblendtime,lblenddate,lblpatientname,lbldoctorname,lblduration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingnotif_details);

        timeslot = getIntent().getParcelableExtra("timeslot");

        View parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
        initControls();
        initData();
    }

    private void initControls()
    {
        lbltitle = findViewById(R.id.lbltitle);
        lblDate = findViewById(R.id.lblDate);
        lblstarttime = findViewById(R.id.lblstarttime);
        lblstartdate = findViewById(R.id.lblstartdate);
        lblendtime = findViewById(R.id.lblendtime);
        lblenddate = findViewById(R.id.lblenddate);
        lblpatientname = findViewById(R.id.lblpatientname);
        lbldoctorname = findViewById(R.id.lbldoctorname);
        lblduration = findViewById(R.id.lblduration);
        bookingnumber  = findViewById(R.id.bookingnumber);

        SafeDoctorService mSafeDoctorService = ApiUtils.getAPIService();
    }

    private void initData()
    {

        lbltitle.setText(timeslot.specialtytext);
        lblDate.setText(AuxUtils.Date.formateDate(timeslot.getDate(),"E, d MMMM, yyyy"));
        lblstarttime.setText(AuxUtils.Date.formateTime(timeslot.getStarttime()));
        lblstartdate.setText(AuxUtils.Date.formateDate(timeslot.getDate(),"d MMM"));
        lblendtime.setText(AuxUtils.Date.formateTime(timeslot.getEndtime()));
        lblenddate.setText(AuxUtils.Date.formateDate(timeslot.getDate(),"d MMM"));
        lblpatientname.setText(AppConstants.patientName);
        if(timeslot.getDoctorname() != null && !timeslot.getDoctorname().isEmpty() )
        {
            lbldoctorname.setText(timeslot.getDoctorname());
        }

        lblduration.setText("Duration: "+AuxUtils.Date.getTimeDifference(timeslot.getStarttime(), timeslot.getEndtime()));
        if(timeslot.bookingnumber != null && !timeslot.bookingnumber.isEmpty())
        {
            tv_booking_code.setText(timeslot.bookingnumber);
            bookingnumber.setText("Booking number");
        }



    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

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
        NestedScrollView nested_scroll_view = findViewById(R.id.nested_scroll_view);


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        else
        {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
