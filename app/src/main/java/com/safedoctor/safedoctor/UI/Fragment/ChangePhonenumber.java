package com.safedoctor.safedoctor.UI.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ResendCodeModel;
import com.safedoctor.safedoctor.Model.ResendPhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePhonenumber extends Fragment {

    private SafeDoctorService mSafeDoctorService;
    private ProgressDialog mainProgressDialog;
    private String TAG="ChangePhonenumber";
    private boolean isreset=false;
    private  View resetlayout;
    private TextView btnconfirm;

    public static Fragment newInstance(){
        ChangePhonenumber fragment=new ChangePhonenumber();
        return fragment;
    }


    public ChangePhonenumber() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.popup_change_phonenumber, container, false);
        final EditText txtphonenumber=(EditText)rootView.findViewById(R.id.txtphonenumber);
        final EditText txtresetcode=(EditText)rootView.findViewById(R.id.txtresetcode);
        TextView btncancelvalidate=(TextView)rootView.findViewById(R.id.btncancelvalidate);
        btncancelvalidate.setVisibility(View.GONE);
        btncancelvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // alertDialog.dismiss();
            }
        });
        btnconfirm=(TextView)rootView.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isreset){

                    if(!TextUtils.isEmpty(txtphonenumber.getText().toString())&& !TextUtils.isEmpty(txtresetcode.getText().toString())){
                        //changePhoneNumber(txtphonenumber.getText().toString());
                        ValidatePhoneCodeModel validatePhoneCodeModel=new ValidatePhoneCodeModel(txtresetcode.getText().toString(),txtphonenumber.getText().toString());
                        startValidation(validatePhoneCodeModel);
                    }else{
                        Toast.makeText(context,"Phone number or reset code cannot be empty",Toast.LENGTH_LONG).show();
                    }


                }else{
                    if(!TextUtils.isEmpty(txtphonenumber.getText().toString())){
                        changePhoneNumber(txtphonenumber.getText().toString());
                    }else{
                        Toast.makeText(context,"Phone number cannot be empty",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        resetlayout=(View)rootView.findViewById(R.id.resetlayout);
        resetlayout.setVisibility(View.GONE);

        TextView btnhavecode=(TextView)rootView.findViewById(R.id.btnhavecode);
        btnhavecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareReset();
            }
        });

        TextView btnresendcode=(TextView)rootView.findViewById(R.id.btnresendcode);
        btnresendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(txtphonenumber.getText().toString())){
                    ResendCodeModel resendCodeModel=new ResendCodeModel(txtphonenumber.getText().toString());
                    ResendValidation(resendCodeModel);
                }else{
                    Toast.makeText(context,"Phone number cannot be empty",Toast.LENGTH_LONG).show();
                }


            }
        });
        mSafeDoctorService = ApiUtils.getAPIService();

        mainProgressDialog = new ProgressDialog(getActivity());
        return rootView;

    }


    private void changePhoneNumber(String phonenumber){
        mainProgressDialog.setMessage(getResources().getString(R.string.alert_working));
        mainProgressDialog.setCancelable(false);
        mainProgressDialog.show();
        ResendCodeModel resendCodeModel=new ResendCodeModel(phonenumber);
        Call<SwagArrayResponseModel<ResendPhoneCodeDataModel>> call = mSafeDoctorService.updatePhoneNumber(TokenString.tokenString, AppConstants.patientID,resendCodeModel);
        call.enqueue(new Callback<SwagArrayResponseModel<ResendPhoneCodeDataModel>>()
        {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<ResendPhoneCodeDataModel>> call, Response<SwagArrayResponseModel<ResendPhoneCodeDataModel>> response)
            {

                if(response.isSuccessful()){

                    //Log.e(TAG,""+response.body());
                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                   prepareReset();

                    //startActivity(new Intent(context, ActivityLandingPage.class));

                }else if(response.code()==500){

                    try {
                        String error=response.errorBody().string();

                            JSONObject jsonObject=new JSONObject(error);
                            String message=(String)jsonObject.get("message");

                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else if(response.code()== HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED){


                    Toast.makeText(context,"You have to sign in",Toast.LENGTH_LONG).show();

                }
                mainProgressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<ResendPhoneCodeDataModel>> call, Throwable t) {
                mainProgressDialog.dismiss();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");

            }
        });


    }

    private void startValidation(ValidatePhoneCodeModel validatePhoneCodeModel) {
        mainProgressDialog.show();
        Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call= mSafeDoctorService.validatePhoneCode(validatePhoneCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>>response) {
                Log.i("Safe", "startValidation Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    mainProgressDialog.dismiss();
                    Toast.makeText(context,"You have to sign in",Toast.LENGTH_LONG).show();
                } else if (response.isSuccessful()) {
                    boolean valid=false;
                    Log.i("Safe", "Fetching validation");
                    List<ValidatePhoneCodeDataModel> validateList = response.body().getData();


                    for (ValidatePhoneCodeDataModel s : validateList) {
                        valid = s.getValid();
                    }

                    if (valid){
                        Toast.makeText(context, "Code Verified. Thank you", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(context, "Unable to Verify.  Number Exists Already. Please contact Helpline", Toast.LENGTH_SHORT).show();
                    Log.i("Safe", "Fetching error code " + response.code() + "");
                    try {
                        Log.i("Safe", "Response is " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mainProgressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> call, Throwable t) {
                Log.i("Safe", "Fetching error"+ t.getMessage());
                mainProgressDialog.dismiss();
                Toast.makeText(context, "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ResendValidation(ResendCodeModel resendCodeModel) {


        mainProgressDialog.show();
        Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call= mSafeDoctorService.resendCode(resendCodeModel);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>> call, Response<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>response) {
                Log.i("Safe", "startValidation Called");
                mainProgressDialog.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {

                    Toast.makeText(context, "Authentication Issue. Please try again", Toast.LENGTH_SHORT).show();

                }
                else if (response.isSuccessful())
                {
                    Toast.makeText(context, "Code Sent", Toast.LENGTH_SHORT).show();


                }
                else {

                    Toast.makeText(context, "Unable to Verify.  Number Exists Already. Please contact Helpline", Toast.LENGTH_SHORT).show();
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
                mainProgressDialog.dismiss();
                Toast.makeText(context, "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }


        });
    }


    private void prepareReset(){
        isreset=true;
        resetlayout.setVisibility(View.VISIBLE);
        btnconfirm.setText("RESET");
    }

}
