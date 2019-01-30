package com.safedoctor.safedoctor.UI.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Api.TinyDB;
import com.safedoctor.safedoctor.Model.ResendCodeModel;
import com.safedoctor.safedoctor.Model.ResendPhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Notifications.broadcastreceivers.SafeDoctorSMSReceiver;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.hbb20.CountryCodePicker;
import com.safedoctor.safedoctor.Utils.Device;


public class Enter_Phonenumber extends AppCompatActivity {
    private SafeDoctorService mSafeDoctorService;
    List<StartRegistrationDataModel> startRegistrationData;
    private StartRegistrationModel startRegistrationModel;
    private SwagArrayResponseModel<List<StartRegistrationDataModel>> swagStartRegistrationResponse;
    private String user_phonenumber;
    private EditText phonenumber;
    Button fab;
    private ProgressDialog enterPhoneDialog;
    private TinyDB tinyDB;

    private String phonecode;
    private SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>> swagResponse;
    private List<ValidatePhoneCodeDataModel> validateList;
    private  boolean valid = false;
    private com.hbb20.CountryCodePicker select_country;
    private Device d;
    private CountDownTimer timer;
    private TextView resend_code;

    private static final int PERMISSIONS_REQUEST_ACCESS_READ_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();

        setContentView(R.layout.activity_enter__phonenumber);
        phonenumber = (EditText) findViewById(R.id.phoneNumberEditText);
        fab = (Button) findViewById(R.id.next);
        mSafeDoctorService = ApiUtils.getAPIService();
        select_country = (CountryCodePicker) findViewById(R.id.select_country);
        tinyDB = new TinyDB(this);


        enterPhoneDialog = new ProgressDialog(this);
        enterPhoneDialog.setMessage(getResources().getString(R.string.alert_working));
        d = new Device(this.getApplicationContext());
        phonenumber.setText(d.getMyPhoneNumber());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_phonenumber = phonenumber.getText().toString();

                if (!user_phonenumber.isEmpty())
                {

                    AppConstants.patientPhoneNumber = user_phonenumber;

                    tinyDB.putString("number",user_phonenumber);
                    tinyDB.putBoolean("code", false);
                    startRegistrationModel = new StartRegistrationModel(user_phonenumber);
                    startRegistrations(startRegistrationModel);

                }
                else
                {
                    phonenumber.setError("Please enter the phone number");
                    phonenumber.requestFocus();
                }
            }
        });

        select_country.resetToDefaultCountry();

        SafeDoctorSMSReceiver.bindListener(new SafeDoctorSMSReceiver.SafeDoctorSMS() {
            @Override
            public void OnSMSReceived(String from, String SMS, String time) {

                SMS = SMS.toUpperCase();
                if(SMS.startsWith("WELCOME"))
                {
                    String[] msgparts = SMS.split("\\:");
                    if(msgparts.length == 2)
                    {
                        user_phonenumber = phonenumber.getText().toString();
                        ValidatePhoneCodeModel validator = new ValidatePhoneCodeModel(msgparts[1].trim(),user_phonenumber);
                        AppConstants.patientPhoneNumber = validator.getMobilephone();

                        fab.setText("SMS Received. Validating...");
                        fab.setTextSize(12);

                        startValidation(validator);

                        if(timer != null)
                        {
                            timer.cancel();
                        }

                    }
                }

            }
        });

        resend_code = (TextView) findViewById(R.id.resend_code);
        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timer != null)
                {
                    timer.cancel();
                }

                fab.setText("OK");
                fab.setTextSize(20);
                fab.setEnabled(true);

                user_phonenumber = phonenumber.getText().toString();
                ResendCodeModel resendCodeModel= new ResendCodeModel(user_phonenumber);
                ResendValidation(resendCodeModel);
            }
        });

        phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(timer != null)
                {
                    timer.cancel();
                }

                fab.setText("OK");
                fab.setTextSize(20);
                fab.setEnabled(true);
            }
        });


    }


    private void ResendValidation(ResendCodeModel resendCodeModel) {

        enterPhoneDialog.setMessage("Working. Please wait...");
        enterPhoneDialog.show();
        Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call= mSafeDoctorService.resendCode(resendCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>response) {
                Log.i("Safe", "startValidation Called");
                enterPhoneDialog.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {

                    Toast.makeText(getApplicationContext(), "Authentication Issue. Please try again", Toast.LENGTH_SHORT).show();

                }
                else if (response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();

                    fab.setText("Waiting to receive SMS for auto validation");
                    fab.setTextSize(12);
                    waitUntilSMSReceived();
                }
                else {

                    Toast.makeText(getApplicationContext(), "Unable to Verify.  Number Exists Already. Please contact Helpline", Toast.LENGTH_SHORT).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        Log.i("Safe", "Response is " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call, Throwable t) {
                enterPhoneDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }


        });
    }

    public void waitUntilSMSReceived()
    {

        timer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long l) {

                if(TimeUnit.MILLISECONDS.toMinutes( l) > 0)
                {
                    fab.setText("Waiting for SMS auto validation:" + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes( l), TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                }
                else
                {
                    fab.setText("Waiting for SMS auto validation:" + String.format("%d sec",  TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                }


            }

            @Override
            public void onFinish()
            {
                fab.setText("SMS Verification failed");
                Snackbar.make(getWindow().getDecorView().getRootView(), "Did not receive SMS after waiting for 2mins. Please resend", Snackbar.LENGTH_LONG).show();

                //Intent intent = new Intent(getApplicationContext(), Enter_Verification_Code.class);
                //startActivity(intent);
            }
        }.start();


    }

    private void startRegProcess(StartRegistrationModel startRegistrationModel)
    {
        Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call = mSafeDoctorService.startRegistration(startRegistrationModel);
        call.enqueue(new Callback<SwagArrayResponseModel<List<StartRegistrationDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call, Response<SwagArrayResponseModel<List<StartRegistrationDataModel>>> response) {
                Log.i(this.toString(), "Code");
                Log.i(this.toString(), response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED)
                {
                    Log.i("Safe", "successful");
                    swagStartRegistrationResponse = response.body();
                    startRegistrationData = swagStartRegistrationResponse.getData();
                    enterPhoneDialog.dismiss();

                    fab.setText("Waiting to receive SMS for auto validation");
                    fab.setTextSize(12);
                    fab.setEnabled(false);
                    waitUntilSMSReceived();

                    resend_code.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_actionnotcompleted), Toast.LENGTH_SHORT).show();
                    enterPhoneDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_networkissues), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                enterPhoneDialog.dismiss();
            }
        });

    }

    private void startRegistrations(final StartRegistrationModel startRegistrationModel)
    {
        enterPhoneDialog.setMessage("Working. Please wait...");
        enterPhoneDialog.show();
        Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> firstrequest = mSafeDoctorService.checkValidatePhoneCode(startRegistrationModel.getPhonenumber());
        firstrequest.enqueue(new Callback<SwagArrayResponseModel<List<StartRegistrationDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call, Response<SwagArrayResponseModel<List<StartRegistrationDataModel>>> response) {
                Log.i("Safe", "Code");
                Log.i("Safe", response.code() + "");
                enterPhoneDialog.dismiss();
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED)
                {
                    Log.i("Safe", "successful");
                    swagStartRegistrationResponse = response.body();
                    startRegistrationData = swagStartRegistrationResponse.getData();

                    if(startRegistrationData != null && startRegistrationData .size() > 0)
                    {
                        if(startRegistrationData.get(0).getIsvalidated())
                        {

                           // enterPhoneDialog.dismiss();
                            if(startRegistrationData.get(0).getPatientid() > 0) // Has already registered
                            {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_already_registered), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                                startActivity(intent);
                                finish();
                            }
                            else  //already validated. Take him to registration page
                            {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_already_validated), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            fab.setText("Code Already sent via SMS");
                            fab.setTextSize(12);
                            fab.setEnabled(false);
                            //waitUntilSMSReceived();
                            resend_code.setVisibility(View.VISIBLE);
                            //phonenumber.setEnabled(false);

                        }

                        return;

                    }

                }

               startRegProcess(startRegistrationModel);

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_networkissues), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                enterPhoneDialog.dismiss();
            }
        });


    }

    public void onClickValidateNow(View view)
    {
       validateNow();
    }

    private void validateNow()
    {
        user_phonenumber = phonenumber.getText().toString();
        if( user_phonenumber.isEmpty())
        {
            phonenumber.setError("Please enter phone number first");
            phonenumber.requestFocus();
            return;
        }

        final  EditText txtphonecode;
        final TextView btnCancel;
        final TextView btnConfirm;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View  RootView = inflater.inflate(R.layout.popup_enter_validationcode,null);

        txtphonecode = (EditText) RootView.findViewById(R.id.txtphonecode);
        btnCancel = (TextView) RootView.findViewById(R.id.btncancelvalidate);
        btnConfirm = (TextView) RootView.findViewById(R.id.btnvalidate);

        builder.setCancelable(false);
        builder.setView(RootView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtphonecode.getText().toString().trim().matches(""))
                {
                    txtphonecode.setError("Please enter phone code");
                    txtphonecode.requestFocus();
                }
                else
                {

                    ValidatePhoneCodeModel validator = new ValidatePhoneCodeModel(txtphonecode.getText().toString().trim(),user_phonenumber);
                    AppConstants.patientPhoneNumber = validator.getMobilephone();
                    startValidation(validator);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void startValidation(ValidatePhoneCodeModel validatePhoneCodeModel) {

        enterPhoneDialog.setMessage("Validating phone code. Please wait...");
        enterPhoneDialog.show();
        Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call= mSafeDoctorService.validatePhoneCode(validatePhoneCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>response) {
                Log.i("Safe", "startValidation Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    enterPhoneDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Authentication Issue. Please try again", Toast.LENGTH_SHORT).show();
                }
                else if (response.isSuccessful())
                {
                    Log.i("Safe", "Fetching validation");
                    swagResponse = response.body();
                    validateList = swagResponse.getData();

                    for (ValidatePhoneCodeDataModel s : validateList) {
                        valid = s.getValid();
                    }

                    if (valid)
                    {
                        enterPhoneDialog.dismiss();
                        tinyDB.putBoolean("code", valid);
                        Toast.makeText(getApplicationContext(), "Phone Code Verified.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                        startActivity(intent);
                    }

                }
                else
                    {
                    enterPhoneDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Phone number do not match with code. Please check and trya again", Toast.LENGTH_LONG).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        Log.i("Safe", "Response is " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Throwable t) {
                //Log.i("Safe", ""+t.getMessage());
                enterPhoneDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        enterPhoneDialog.dismiss();
    }

    /**
     * Just to make sure that I have permission
     */

    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_READ_SMS)
    private void requestPermissions() {
        String[] perms = {  Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS };
        if (EasyPermissions.hasPermissions(this, perms))
        {

        } else {
            //if permission is denied
            EasyPermissions.requestPermissions(this, "This app needs access to read SMS", PERMISSIONS_REQUEST_ACCESS_READ_SMS, perms);
        }
    }

}
