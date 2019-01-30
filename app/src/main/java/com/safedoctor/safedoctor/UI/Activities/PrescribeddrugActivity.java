package com.safedoctor.safedoctor.UI.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.ConsultationPayment;
import com.safedoctor.safedoctor.Model.Prescriptiondrug;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.HTTP;

import static com.safedoctor.safedoctor.Utils.App.context;

public class PrescribeddrugActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private View paybtn,cancelbtn;
    private TextView drugtxt,druginfotxt,doctortxt,feetxt,
            quantitytxt,amounttxt,datetxt,facilitytxt;

    private ConsultationPayment consultationPayment;
    private String setVodafonetoken;
    private Prescriptiondrug prescriptiondrug;
    private TextView serviceFeeTextView;

    private final int Servicetypeid_DRUG=1;
    private EditText alternateNumberEditText;
    private String TAG="PrescribeddrugActivity";
    private  Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribeddrug);


        paybtn=(Button)findViewById(R.id.paybtn);
        paybtn.setOnClickListener(this);
        cancelbtn=findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(this);

        drugtxt=(TextView)findViewById(R.id.drugtxt);

        datetxt=(TextView)findViewById(R.id.datetxt);

        doctortxt=(TextView)findViewById(R.id.doctortxt);
        feetxt=(TextView)findViewById(R.id.feetxt);
        quantitytxt=(TextView)findViewById(R.id.quantitytxt);
        amounttxt=(TextView)findViewById(R.id.amounttxt);
        facilitytxt=(TextView)findViewById(R.id.facilitytxt);


        Bundle bundle=getIntent().getExtras();
        prescriptiondrug=(Prescriptiondrug) bundle.getSerializable("drug");
        String doctor=bundle.getString("doctor");
        prescriptiondrug.setDoctor(doctor);

        if(prescriptiondrug.getPaid()==null)prescriptiondrug.setPaid(false);
        if(prescriptiondrug.getProviderid()==null || prescriptiondrug.getPaid()==true){
            paybtn.setVisibility(View.GONE);
        }

        showView(prescriptiondrug);


    }


    void showView(Prescriptiondrug prescriptiondrug){
        drugtxt.setText(prescriptiondrug.getDrug().getName());

        datetxt.setText(AuxUtils.Date.formateDate(prescriptiondrug.getPrescriptiontime(),"MMM dd yyyy HH:mm"));
        //AuxUtils.Date.formateDate(prescriptiondrug.getPrescriptiontime(),"MMM dd yyyy HH:mm");
        doctortxt.setText(prescriptiondrug.getDoctor());
        feetxt.setText(prescriptiondrug.getUnitfee());
        quantitytxt.setText(prescriptiondrug.getQtygiven());
        amounttxt.setText(String.format( "%.2f", Double.valueOf(prescriptiondrug.getAmount())));

        if(prescriptiondrug.getProvider()!=null){
            facilitytxt.setText(prescriptiondrug.getProvider().getName());
        }else{
            facilitytxt.setText("-");
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelbtn:
                finish();break;
            case R.id.paybtn:
                payDrug();break;

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()){

            case R.id.mobileMoneyTypeRadioGroup:
                mobileMoneyTypeSetter(checkedId);
                break;
        }

    }


    void payDrug(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.form_drug_payment);

        TextView lbltitle=(TextView)dialog.findViewById(R.id.lbltitle);
        TextView lblDate=(TextView)dialog.findViewById(R.id.lblDate);
        RadioGroup mobileMoneyTypeRadioGroup=(RadioGroup)dialog.findViewById(R.id.mobileMoneyTypeRadioGroup);
        alternateNumberEditText=(EditText)dialog.findViewById(R.id.alternateNumberEditText);
        serviceFeeTextView=(TextView)dialog.findViewById(R.id.serviceFeeTextView);

        Button bt_hide_info=(Button)dialog.findViewById(R.id.bt_hide_info);
        bt_hide_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button paydrugbtn=(Button)dialog.findViewById(R.id.book_doctor_btn);

        mobileMoneyTypeRadioGroup.setOnCheckedChangeListener(this);

        int checkedID = mobileMoneyTypeRadioGroup.getCheckedRadioButtonId();
        mobileMoneyTypeSetter(checkedID);

        lbltitle.setText(prescriptiondrug.getDrug().getName());
        lblDate.setText(AuxUtils.Date.formateDate(prescriptiondrug.getPrescriptiontime(),"MMM dd yyyy HH:mm"));

        serviceFeeTextView.setText("GHS "+String.format( "%.2f", Double.valueOf(prescriptiondrug.getAmount())));


        paydrugbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrugPayment();
            }
        });





        dialog.show();
    }


    void DrugPayment(){

        if(alternateNumberEditText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Enter wallet number",Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(),"Making payment....",Toast.LENGTH_LONG).show();
        dialog.dismiss();

        ConsultationPayment consultationPayment=new ConsultationPayment
                (Double.valueOf(String.format( "%.2f", Double.valueOf(prescriptiondrug.getAmount()))),
                        prescriptiondrug.getConsultationid(),
                        AppConstants.mobilemoneynetworkid,prescriptiondrug.getDrug().getDrugid(),
                        prescriptiondrug.getProviderid(),
                        Servicetypeid_DRUG,setVodafonetoken,alternateNumberEditText.getText().toString());


        ApiUtils.getAPIService().payDrug(TokenString.tokenString,consultationPayment).enqueue(new Callback<SwagArrayResponseModel<List<String>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<String>>> call, Response<SwagArrayResponseModel<List<String>>> response) {
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(context, "Your session has expired please login again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, FormLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
                else if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.body().getData().get(0),Toast.LENGTH_LONG).show();

                }else{
                    try {
                        String error = response.errorBody().string();
                        if(error!=null){
                            Log.e(TAG,error);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<String>>> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_LONG).show();
            }
        });






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
                //bookNPay.setVodafonetoken(null);
                break;
            case R.id.airtelRadioButton:
                AppConstants.mobilemoneynetworkid = "AIRTEL";
                //bookNPay.setVodafonetoken(null);
                break;
            case R.id.tigoRadioButton:
                AppConstants.mobilemoneynetworkid = "TIGO";
                //bookNPay.setVodafonetoken(null);
                break;
        }

    }


    public void popUpVodafoneToken()
    {

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
                    setVodafonetoken=vodaTokenEt.getText().toString();
                    alertDialog.dismiss();
                }
            }
        });

    }
}
