package com.safedoctor.safedoctor.UI.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Api.TinyDB;
import com.safedoctor.safedoctor.Backgroundservices.MainService;
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
import com.safedoctor.safedoctor.Utils.StringUtils;
import com.stevkky.supertooltips.ToolTipView;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormLogin extends AppCompatActivity  implements ServiceConnection, OnTaskCompleted{



    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SessionManagement sessionManagement;

    private SafeDoctorService mSafeDoctorService;

    private SwagArrayResponseModel<List<LoginDataModel>> swagLoginResponse;
    private List<LoginDataModel> loginData;
    private PatientModel patient;
    private TokenModel token;
    private ProgressDialog loginProgressDialog;
    private TinyDB tinyDB;
    private  boolean isResetmode = true;

    private List<StartRegistrationDataModel> startRegistrationData;
    private StartRegistrationModel startRegistrationModel;
    private SwagArrayResponseModel<List<StartRegistrationDataModel>> swagStartRegistrationResponse;

    private SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>> swagResponse;
    private List<ValidatePhoneCodeDataModel> validateList;
    private AlertDialog alertDialog;
    private TextView reset_password_lnk;
    private TextView register,policytxt;

    private Common common;

    private Activity activity;

    private MainService s;

    private ToolTipView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_form_login);
        setContentView(R.layout.activity_design_login);

        initComponent();
        sessionManagement=new SessionManagement(getApplicationContext());

        activity=this;



        //startService();


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToolTip();
            }
        }, 500);
*/

    }

   /*private void addToolTip()
    {
        ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipRelativeLayout);

        ToolTip toolTip = new ToolTip()
                .withText("Click here to register")
                .withColor(R.color.colorPrimary)
                .withTextColor(Color.WHITE)
                .withShadow()
                .withAnimationType(ToolTip.AnimationType.FROM_TOP);
        test = toolTipRelativeLayout.showToolTipForView(toolTip, findViewById(R.id.navigateToSignUpBtn));
        test.setOnToolTipViewClickedListener(new ToolTipView.OnToolTipViewClickedListener() {
            @Override
            public void onToolTipViewClicked(ToolTipView toolTipView) {
                if( test == null)
                {
                    addToolTip();
                }
                else
                {
                    test.remove();
                    test = null;
                }
            }
        });
    }
    */
    private void startService()
    {
        Intent service = new Intent(getApplicationContext(), MainService.class);
        getApplicationContext().startService(service);
    }

    private void initComponent() {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


        reset_password_lnk = (TextView) findViewById(R.id.reset_password_lnk) ;

        register = (TextView) findViewById(R.id.register) ;
        policytxt=(TextView)findViewById(R.id.policy);
        policytxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new FinestWebView.Builder(activity).show(getResources().getString(R.string.policy_url));
            }
        });

        mSafeDoctorService = ApiUtils.getAPIService();

        loginProgressDialog =  new ProgressDialog(this);
        loginProgressDialog.setMessage("Logging In... Please Wait");
        tinyDB = new TinyDB(this);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        reset_password_lnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                handlePasswordReset();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       // Button btnsignup = (Button) findViewById(R.id.navigateToSignUpBtn);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() ,Enter_Phonenumber.class);
                startActivity(intent);
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        common = new Common(getApplicationContext(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Invalid password.Min of 4 chars required");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError("Please enter your Phone number");
            focusView = mEmailView;
            cancel = true;
        }

        if(password.isEmpty())
        {
            mPasswordView.setError("Please enter your password");
            mPasswordView.requestFocus();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            showProgress(true);
            LoginModel loginModel = new LoginModel(email, password);
            login(loginModel);

        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    private void resendCode(String phonenumber)
    {
        ResendCodeModel resendCodeModel= new ResendCodeModel(phonenumber);
        loginProgressDialog.show();
        Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call= mSafeDoctorService.resendResetcode(resendCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> response) {
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
                Log.i("Safe","Fetching error"+ t.getMessage());
                loginProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(final LoginModel loginModel){
       // loginProgressDialog.show();
        Call<SwagArrayResponseModel<List<LoginDataModel>>> call = mSafeDoctorService.patientLogin(loginModel);
        call.enqueue(new Callback<SwagArrayResponseModel<List<LoginDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Response<SwagArrayResponseModel<List<LoginDataModel>>> response) {
                Log.i("Safe", "we're in");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {

                    try {
                        String error=response.errorBody().string();
                        Log.e("login",""+error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Wrong Phone number or Password", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    mEmailView.setError("Invalid Phone number or Password");
                    mPasswordView.setError("Invalid Phone number or Password");
                    mPasswordView.requestFocus();
                }
                else if (response.isSuccessful() && response.code()==200){
                    Log.i("Safe", "successful");
                    swagLoginResponse = response.body();
                    loginData = swagLoginResponse.getData();

                    for (LoginDataModel l: loginData)
                    {
                        patient = l.getPatient();
                        token = l.getTokenModel();

                    }
                    sessionManagement.createLoginSession(token);
                    sessionManagement.createPatientDetail(patient);
                    AppConstants.PatientObj = patient;
                    TokenString.tokenString = token.getTokenType() + " " + token.getToken();
                    AppConstants.patientID = patient.getPatientid();
                    AppConstants.patientPhoneNumber = patient.getPhonenumber();
                    AppConstants.patientName = StringUtils.join(" ",patient.getFirstname(),patient.getMiddlename(),patient.getLastname());


                    //Log.i("Safe", "Patient number is " + AppConstants.patientPhoneNumber);

                    common.fetchProfilePicture();
                    AppConstants.IS_LOGIN = true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Phone number or Password", Toast.LENGTH_LONG).show();
                    showProgress(false);
                    try {
                        String error=response.errorBody().string();
                        Log.e("login",""+error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                showProgress(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(getApplicationContext(), ActivityWelcome.class);
        //startActivity(intent);
        //finish();
        loginProgressDialog.dismiss();
        showProgress(false);
    }

    @Override
    public void onTaskCompleted(Object result) {

        if(result.getClass()  ==  Boolean.class ||  result.getClass()  ==  boolean.class)
        {            //TODO There is not picture so fix the default avator

            AppConstants.profilePictureString = "";
            sessionManagement.setProfileImage(String.valueOf(""));
        }
        else
        {
            if(result.getClass() == String.class &&  null != result) {
                AppConstants.profilePictureString = String.valueOf(result);
                sessionManagement.setProfileImage(String.valueOf(result));

            }
        }

        Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        MainService.MyBinder b = (MainService.MyBinder) iBinder;
        b.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        s = null;
    }
}

