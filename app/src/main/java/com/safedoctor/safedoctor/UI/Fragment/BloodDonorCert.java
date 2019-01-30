package com.safedoctor.safedoctor.UI.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.PatientblooddonordetailModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class BloodDonorCert extends Fragment
{
    private static final int RC_VIDEO_APP_PERM = 3 ;
    ImageView profile_picture;
    public final int GALLERY_REQUEST=3;
    public final int CAMERA_REQUEST=2;
    Uri ImageUri = null;

    private SafeDoctorService mSafeDoctorService;

    private String imageString;

    private SwagResponseModel<List<PatientblooddonordetailModel>> photoResponse;
    private DataModel<List<PatientblooddonordetailModel>> photoList;
    private List<PatientblooddonordetailModel> photo;

    // private ProgressDialog pictureDialog;
    private View progressview;

    private EditText txtdonornumber;

    private  int currentid =0;

    public static BloodDonorCert newInstance()
    {
        BloodDonorCert  blooddonorcert = new BloodDonorCert();
        return  blooddonorcert;
    }

    private void showProgress(final boolean show) {
        progressview.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blood_donor, container, false);

        profile_picture = (ImageView) root.findViewById(R.id.my_profile_img);

        progressview  = root.findViewById(R.id.progress);

        txtdonornumber = (EditText)root.findViewById(R.id.txtdonornumber);

        mSafeDoctorService = ApiUtils.getAPIService();

        //TODO setCurrentPic();
        getProfilePicture();
        requestPermissions();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtdonornumber.getText().toString().trim().isEmpty())
                {
                    txtdonornumber.setError("Please enter your donor number");
                    txtdonornumber.requestFocus();
                    return;
                }

                String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };

                if (EasyPermissions.hasPermissions(getActivity(),perms))
                {
                    popUpConfirmation();
                }
                else {
                    EasyPermissions.requestPermissions(getActivity(), "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);

                }


            }
        });

        return root;
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // initialize view objects from your layout
            // mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            //mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);

            // fetchChatSession();

            // initialize and connect to the session


        } else {
            EasyPermissions.requestPermissions(getActivity(), "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
            // requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }


    public void popUpConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Source of Picture");
        builder.setMessage("Kindly select where you want to select your picture");
        builder.setCancelable(false);
        builder.setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent selectImage = new Intent();
                selectImage.setAction(Intent.ACTION_GET_CONTENT);
                selectImage.setType("image/*");
                startActivityForResult(selectImage,GALLERY_REQUEST);
            }
        });

        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                5);
                    }
                }
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERA_REQUEST);
            }
        });

        builder.show();
    }
    private void setCurrentPic()
    {


        if(AppConstants.profilePictureString != null && !AppConstants.profilePictureString.isEmpty())
        {
            byte[] imageBytes = Base64.decode(AppConstants.profilePictureString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profile_picture.setImageBitmap(decodedImage);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)

        {
            switch (requestCode)
            {
                case GALLERY_REQUEST:
                    ImageUri = data.getData();
                    profile_picture.setImageURI(ImageUri);

                    profile_picture.buildDrawingCache();

                    //encode image to base64 string
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap =  profile_picture.getDrawingCache();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Log.i("Thetring", imageString);
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    setProfilePicture();
                    break;

                case CAMERA_REQUEST:
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    profile_picture.setImageBitmap(bitmap);
                    setProfilePicture();
                    break;

            }
//            imageText.setText(imageString);
        }

    }

    public void getProfilePicture() {
        showProgress(true);
        Call<SwagResponseModel<List<PatientblooddonordetailModel>>> call = mSafeDoctorService.getBloodonordetails(TokenString.tokenString, AppConstants.patientID);

        Log.i("Safe", mSafeDoctorService.getBloodonordetails(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagResponseModel<List<PatientblooddonordetailModel>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<PatientblooddonordetailModel>>> call, Response<SwagResponseModel<List<PatientblooddonordetailModel>>> response) {
                Log.i("TheSafe", response.code() + "");
                showProgress(false);
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {

                    Toast.makeText(getActivity().getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful())
                {
                    photoResponse = response.body();
                    photoList = photoResponse.getData();
                    if(null != photoList) {
                        photo = photoList.getContent();
                        if (photo != null && photo.size() > 0) {

                            imageString = photo.get(0).getCertificateimage();
                            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            profile_picture.setImageBitmap(decodedImage);
                            currentid = photo.get(0).getId();
                            txtdonornumber.setText(photo.get(0).getDonornumber());
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<PatientblooddonordetailModel>>> call, Throwable throwable) {

                try {
                    showProgress(false);
                    Log.i("TheSafe", throwable.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setProfilePicture(){
        showProgress(true);

        List<PatientblooddonordetailModel> picture = new ArrayList<PatientblooddonordetailModel>();
        picture.add(new PatientblooddonordetailModel(imageString, txtdonornumber.getText().toString().trim(),currentid));

        Call<SwagArrayResponseModel> call = mSafeDoctorService.uploadBloodonordetails(TokenString.tokenString, AppConstants.patientID, picture);
        //Log.i("Safe", mSafeDoctorService.updateProfilePicture(TokenString.tokenString, AppConstants.patientID, picture).request().url().toString());


        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                Log.i("Safe", "we're in");
                showProgress(false);
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful())
                {
                    //Snackbar.make(getFragmentManager().mapFragment.getView(), "Click the pin for more options", Snackbar.LENGTH_LONG).show();
                   //Toast.makeText(getActivity().getApplicationContext(), "Picture successfully uploaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable throwable) {
                showProgress(false);
                try {
                    Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_LONG).show();
                    throwable.printStackTrace();
                }
                catch (Exception e)
                {

                }
            }
        });
    }
}
