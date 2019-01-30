package com.safedoctor.safedoctor.UI.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.RegistrationDataModel;
import com.safedoctor.safedoctor.Model.RegistrationModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener ,OnTaskCompleted{
    private Calendar mCalendar;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup genderRadioGroup;
    private EditText dateOfBirthEditText;
    private EditText userNameEditText;
    private EditText confirmPasswordEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private Button signUpButton;
    private String firstName, lastName, password, confirmPassword, userName, dateOfBirth;
    private SimpleDateFormat dateFormatter;
    private Integer genderId;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<RegistrationDataModel>> swagRegisterResponse;
    private List<RegistrationDataModel> registerData;
    private PatientModel patient;
    private DatePickerDialog dateOfBirthDatePickerDialog;
    private ProgressDialog signUpProgressDialog;
    private RegistrationDataModel registeredPatient;
    private  Common c ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = (EditText) findViewById(R.id.signupFirstname);
        lastNameEditText = (EditText) findViewById(R.id.signupLastname);
        userNameEditText = (EditText) findViewById(R.id.signupUsername);
        passwordEditText = (EditText) findViewById(R.id.signupPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.signupConfirmPassword);
        dateOfBirthEditText = (EditText) findViewById(R.id.signupDob);
        genderRadioGroup = (RadioGroup) findViewById(R.id.signupGenderGroup);
        signUpButton = (Button) findViewById(R.id.signupBtn);
        phoneNumberEditText = (EditText) findViewById(R.id.signupphonenumber);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        c = new Common(getApplicationContext(),this);

        dateOfBirthEditText.setOnClickListener(this);
        genderRadioGroup.setOnCheckedChangeListener(this);
        signUpButton.setOnClickListener(this);

        mSafeDoctorService = ApiUtils.getAPIService();

        phoneNumberEditText.setText(AppConstants.patientPhoneNumber);


        int checkedID = genderRadioGroup.getCheckedRadioButtonId();
        genderIdSetter(checkedID);

        dateOfBirthDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateOfBirthEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        signUpProgressDialog = new ProgressDialog(this);
        signUpProgressDialog.setMessage( getResources().getString(R.string.alert_working));

    }

    private void StartSignUp(RegistrationModel registrationModel)
    {
        signUpProgressDialog.show();
        Call<SwagArrayResponseModel<List<RegistrationDataModel>>> call = mSafeDoctorService.patientRegister(registrationModel);
        call.enqueue(new Callback<SwagArrayResponseModel<List<RegistrationDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<RegistrationDataModel>>> call, Response<SwagArrayResponseModel<List<RegistrationDataModel>>> response) {
                Log.i("Safe", "we're in");
                Log.i("Safe", "Fetching error code " + response.code() + "");
                signUpProgressDialog.dismiss();
                if (response.isSuccessful())
                {
                    Log.i("Safe", "successful");
                    swagRegisterResponse = response.body();
                    registerData = swagRegisterResponse.getData();
                    registeredPatient = registerData.get(0);
                    AppConstants.patientID = registeredPatient.getPatientid();

                    c.login(AppConstants.patientPhoneNumber,password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed. Please Try again", Toast.LENGTH_LONG).show();
                    signUpProgressDialog.dismiss();
                }
            }


            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<RegistrationDataModel>>> call, Throwable t) {
                signUpProgressDialog.dismiss();
                t.printStackTrace();
                Log.i("message", t.toString());
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.alert_networkissues), Toast.LENGTH_LONG).show();

            }
        });

    }
    void genderIdSetter(int checkID) {
        switch (checkID) {
            case R.id.male_radiobutton:
                genderId = 1;
                AppConstants.genderId = genderId;
                break;
            case R.id.female_radiobutton:
                genderId = 2;
                AppConstants.genderId = genderId;
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupDob:
                dateOfBirthDatePickerDialog.show();
                break;
            case R.id.signupBtn:
                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();
                userName = userNameEditText.getText().toString().trim();
                dateOfBirth = dateOfBirthEditText.getText().toString();

                if(firstNameEditText.getText().toString().isEmpty())
                {
                    firstNameEditText.setError("Please enter your first name");
                    firstNameEditText.requestFocus();
                    return;
                }

                if(lastNameEditText.getText().toString().isEmpty())
                {
                    lastNameEditText.setError("Please enter the last name");
                    lastNameEditText.requestFocus();
                    return;
                }

                if(passwordEditText.getText().toString().isEmpty())
                {
                    passwordEditText.setError("Please enter your password");
                    passwordEditText.requestFocus();
                    return;
                }

                if(passwordEditText.getText().toString().length() < 4)
                {
                    passwordEditText.setError("Min Charactors 4");
                    passwordEditText.requestFocus();
                    return;
                }

               /* if(userNameEditText.getText().toString().isEmpty())
                {
                    userNameEditText.setError("Please enter your username");
                    userNameEditText.requestFocus();
                    return;
                }*/

                if( dateOfBirthEditText.getText().toString().isEmpty())
                {
                    dateOfBirthEditText.setError("Please enter your date of birth");
                    dateOfBirthEditText.requestFocus();

                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please set your Date of Birth", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if(!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString()))
                {
                    passwordEditText.setError("Invalid Password confirmation");
                    passwordEditText.requestFocus();
                    return;
                }

                RegistrationModel registrationModel =
                        new RegistrationModel(null, password, firstName,
                                lastName, dateOfBirth,
                                AppConstants.patientPhoneNumber, genderId);

                StartSignUp(registrationModel);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()) {
            case R.id.signupGenderGroup:
                genderIdSetter(checkedId);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signUpProgressDialog.dismiss();
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        boolean flag = (boolean)result;

        if(flag)
        {
           // Intent intent = new Intent(getApplicationContext(), Profile_Picture.class);
            //intent.putExtra("issignup", true);
            Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
            startActivity(intent);
            Snackbar.make(getWindow().getDecorView().getRootView(), "Registration successful. Welcome to Safe Dokor", Snackbar.LENGTH_LONG).show();

        }
    }
}
