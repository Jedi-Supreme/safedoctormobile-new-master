package com.safedoctor.safedoctor.UI.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Api.TinyDB;
import com.safedoctor.safedoctor.Model.LoginDataModel;
import com.safedoctor.safedoctor.Model.LoginModel;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.ResendCodeModel;
import com.safedoctor.safedoctor.Model.ResendPhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.Device;
import com.safedoctor.safedoctor.Utils.SessionManagement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/passwordEditText.
 */
public class Login extends AppCompatActivity {

    // UI references.
    private EditText mEmailView, mPasswordView;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<LoginDataModel>> swagLoginResponse;
    private List<LoginDataModel> loginData;
    private PatientModel patient;
    private TokenModel token;
    private ProgressDialog loginProgressDialog;
    private TinyDB tinyDB;
    private  boolean isResetmode = true;

    private SessionManagement sessionManagement;

    private List<StartRegistrationDataModel> startRegistrationData;
    private StartRegistrationModel startRegistrationModel;
    private SwagArrayResponseModel<List<StartRegistrationDataModel>> swagStartRegistrationResponse;

    private SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>> swagResponse;
    private List<ValidatePhoneCodeDataModel> validateList;
    private AlertDialog alertDialog;
    private TextView reset_password_lnk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mEmailView = (EditText) findViewById(R.id.signinEmail);
        mPasswordView = (EditText) findViewById(R.id.signinPassword);
        reset_password_lnk = (TextView) findViewById(R.id.reset_password_lnk) ;

        mSafeDoctorService = ApiUtils.getAPIService();

        loginProgressDialog =  new ProgressDialog(this);
        loginProgressDialog.setMessage("Logging In... Please Wait");
        tinyDB = new TinyDB(this);

        sessionManagement=new SessionManagement(getApplicationContext());
       /* if (!tinyDB.getString("number").matches("") && !tinyDB.getBoolean("code"))
        {
            startActivity(new Intent(this,Enter_Verification_Code.class));
            finish();
        }*/

        reset_password_lnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                handlePasswordReset();
            }
        });
    }

    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.signinBtn:
                String username = mEmailView.getText().toString().trim();
                String password = mPasswordView.getText().toString().trim();

                if(username.isEmpty())
                {
                    mEmailView.setError("Please enter your username");
                    mEmailView.requestFocus();
                    return;
                }

                if(password.isEmpty())
                {
                    mPasswordView.setError("Please enter your password");
                    mPasswordView.requestFocus();
                    return;
                }

                LoginModel loginModel = new LoginModel(username, password);
                login(loginModel);

                break;

            case R.id.navigateToSignUpBtn:
                Intent intent = new Intent(this,Enter_Phonenumber.class);
                startActivity(intent);
                break;

        }
    }

    private void resendCode(String phonenumber)
    {
        ResendCodeModel resendCodeModel= new ResendCodeModel(phonenumber);
        loginProgressDialog.show();
        Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call= mSafeDoctorService.resendResetcode(resendCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>response) {
                Log.i("Safe", "startValidation Called");
                loginProgressDialog.dismiss();
                if (response.isSuccessful())
                {
                    Log.i("Safe", "Fetching validation");
                    Toast.makeText(getApplicationContext(), "Password reset Code Sent", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Could not send SMS. Please try again later", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }


        });

    }

    private void handlePasswordReset()
    {
        isResetmode = true;
        final  EditText txtphonenumber;
        final TextView btnCancel;
        final TextView btnConfirm;
        final EditText txtresetcode;
        final TextView lblinfo;
        final Device d = new Device(getApplicationContext());
        final TextView btnresendcode;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View  RootView = inflater.inflate(R.layout.popup_resetpassword_start,null);

        txtphonenumber = (EditText) RootView.findViewById(R.id.txtphonenumber);
        txtresetcode = (EditText) RootView.findViewById(R.id.txtresetcode);
        btnCancel = (TextView) RootView.findViewById(R.id.btncancelvalidate);
        btnConfirm = (TextView) RootView.findViewById(R.id.btnvalidate);
        lblinfo = (TextView) RootView.findViewById(R.id.lblinfo);
        btnresendcode = (TextView) RootView.findViewById(R.id.btnresendcode) ;


        txtphonenumber.setText(d.getMyPhoneNumber());
        builder.setCancelable(false);
        builder.setView(RootView);
        alertDialog = builder.create();
        alertDialog.show();

        btnresendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCode(txtphonenumber.getText().toString().trim());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isResetmode)
                {
                    if (txtphonenumber.getText().toString().trim().matches(""))
                    {
                        txtphonenumber.setError("Please enter phone number");
                        txtphonenumber.requestFocus();
                        return;
                    }

                        loginProgressDialog.setMessage(getResources().getString(R.string.alert_working));
                        loginProgressDialog.show();
                        StartRegistrationModel startRegistrationModel = new StartRegistrationModel(txtphonenumber.getText().toString().trim());

                        Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call = mSafeDoctorService.resetPassword(startRegistrationModel);
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
                                    loginProgressDialog.dismiss();
                                    txtresetcode.setVisibility(View.VISIBLE);
                                    txtphonenumber.setEnabled(false);
                                    lblinfo.setText("Enter Reset Code to verify");
                                    Toast.makeText(getApplicationContext(), "Reset code sent to your phone:"+
                                            txtphonenumber.getText().toString().trim(), Toast.LENGTH_LONG).show();
                                    isResetmode = false;
                                    btnresendcode.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Phone number is not registered", Toast.LENGTH_SHORT).show();
                                    loginProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_networkissues), Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                                loginProgressDialog.dismiss();
                            }
                        });

                }
                else
                {
                    if (txtresetcode.getText().toString().trim().matches(""))
                    {
                        txtresetcode.setError("Please enter reset code");
                        txtresetcode.requestFocus();
                        return;
                    }

                    ValidatePhoneCodeModel validator = new ValidatePhoneCodeModel(txtresetcode.getText().toString().trim(),txtphonenumber.getText().toString().trim());
                    startValidation(validator);
                }

            }
        });
    }

    private void startValidation(ValidatePhoneCodeModel validatePhoneCodeModel) {

        loginProgressDialog.setMessage("Working. Please wait...");
        loginProgressDialog.show();
        Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call= mSafeDoctorService.validatResetcode(validatePhoneCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>response)
            {
                Log.i("Safe", "startValidation Called");
                loginProgressDialog.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Authentication Issue. Please try again", Toast.LENGTH_SHORT).show();
                }
                else if (response.isSuccessful())
                {
                    Log.i("Safe", "Fetching validation");
                    swagResponse = response.body();
                    validateList = swagResponse.getData();

                    if(null != validateList && validateList.size() > 0)
                    {
                        if(validateList.get(0).getValid())
                        {
                            Toast.makeText(getApplicationContext(), "Reset Code Verified. New password sent to you", Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();

                        }
                   }


                }
                else
                {
                    loginProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Phone number do not match with code. Please check and try again", Toast.LENGTH_LONG).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        Log.i("Safe", "Response is " + response.errorBody().string());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Throwable t) {
                Log.i("Safe", "Fetching error"+ t.getMessage());
                loginProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(final LoginModel loginModel){
        loginProgressDialog.show();
        Call<SwagArrayResponseModel<List<LoginDataModel>>> call = mSafeDoctorService.patientLogin(loginModel);
        call.enqueue(new Callback<SwagArrayResponseModel<List<LoginDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Response<SwagArrayResponseModel<List<LoginDataModel>>> response) {
                Log.i("Safe", "we're in");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_LONG).show();
                    loginProgressDialog.dismiss();
                    mEmailView.setError("Invalid Username or Password");
                    mPasswordView.setError("Invalid Username or Password");
                    mPasswordView.requestFocus();
                }
                else if (response.isSuccessful()){
                    Log.i("Safe", "successful");
                    swagLoginResponse = response.body();
                    loginData = swagLoginResponse.getData();

                    for (LoginDataModel l: loginData)
                    {
                        patient = l.getPatient();
                        token = l.getTokenModel();
                    }
                    sessionManagement.createLoginSession(token);
                    TokenString.tokenString = token.getTokenType() + " " + token.getToken();
                    AppConstants.patientID = patient.getPatientid();
                    AppConstants.patientPhoneNumber = patient.getPhonenumber();
                    AppConstants.patientName = patient.getFirstname() + " " + patient.getMiddlename() + " " + patient.getLastname();
                    Log.i("Safe", "Patient number is " + AppConstants.patientPhoneNumber);

                    //Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    loginProgressDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
                    startActivity(intent);
                   // tinyDB.putBoolean("registered",true);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
                    loginProgressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                loginProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loginProgressDialog.dismiss();
    }
}

