package com.safedoctor.safedoctor.UI.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.safedoctor.safedoctor.Model.PatientChangeCredentials;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {

    private SafeDoctorService mSafeDoctorService;
    private ProgressDialog mainProgressDialog;
    private String TAG="ChangePassword";

    public static Fragment newInstance() {
        ChangePassword fragment = new ChangePassword();
        return  fragment;
    }

    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.popup_change_password, container, false);

        final EditText oldpassword=(EditText)rootView.findViewById(R.id.oldpassword);
        final EditText newpassword=(EditText)rootView.findViewById(R.id.newpassword);
        final EditText confirmpassword=(EditText)rootView.findViewById(R.id.confirmpassword);

        TextView btncancelvalidate=(TextView)rootView.findViewById(R.id.btncancelvalidate);

        btncancelvalidate.setVisibility(View.GONE);
        btncancelvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TextView btnconfirm=(TextView)rootView.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(oldpassword.getText().toString())&& !TextUtils.isEmpty(newpassword.getText().toString())&&!TextUtils.isEmpty(confirmpassword.getText().toString())){
                    if(newpassword.getText().toString().equals(confirmpassword.getText().toString())==true){
                        changePassword(new PatientChangeCredentials(newpassword.getText().toString(),oldpassword.getText().toString()));
                    }else{
                        Toast.makeText(context,"New password and confirm password do not match",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"required fields * cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        mSafeDoctorService = ApiUtils.getAPIService();
        mainProgressDialog = new ProgressDialog(getActivity());

        return rootView;
    }

    private void changePassword(PatientChangeCredentials patientChangeCredentials){
        mainProgressDialog.setMessage(getResources().getString(R.string.alert_working));
        mainProgressDialog.show();

        Call<SwagArrayResponseModel> call = mSafeDoctorService.updatePassword(TokenString.tokenString, AppConstants.patientID,patientChangeCredentials);
        call.enqueue(new Callback<SwagArrayResponseModel>()
        {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response)
            {
                if(response.isSuccessful() && response.code()==200){
                    mainProgressDialog.dismiss();

                    Log.e(TAG,""+response.body());
                    Toast.makeText(context,"Changes Saved",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, ActivityLandingPage.class));

                }else if(response.code()== HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED){

                    mainProgressDialog.dismiss();
                    Toast.makeText(context,"You have to sign in",Toast.LENGTH_LONG).show();

                }else{
                    mainProgressDialog.dismiss();
                    Toast.makeText(context,"Sorry an error occured",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable t) {
                mainProgressDialog.dismiss();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");
            }
        });

    }

}
