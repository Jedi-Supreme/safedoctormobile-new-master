package com.safedoctor.safedoctor.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.PatientPhotoModel;
import com.safedoctor.safedoctor.Model.Picture;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.SessionManagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

public class Profile2 extends AppCompatActivity {
    private static final int RC_VIDEO_APP_PERM = 3 ;
    ImageView profile_picture;
    public final int GALLERY_REQUEST=3;
    public final int CAMERA_REQUEST=2;
    Uri ImageUri = null;

    private SafeDoctorService mSafeDoctorService;

    private String imageString;

    private SwagArrayResponseModel<List<PatientPhotoModel>> photoResponse;
    private List<PatientPhotoModel> photoList;
    private PatientPhotoModel photo;

    private ProgressDialog pictureDialog;
    private SessionManagement sessionManagement=new SessionManagement(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        profile_picture = (ImageView) findViewById(R.id.my_profile_img);

        pictureDialog = new ProgressDialog(this);

        mSafeDoctorService = ApiUtils.getAPIService();

        getProfilePicture();
        requestPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };

                if (EasyPermissions.hasPermissions(Profile2.this,perms))
                {
                    popUpConfirmation();
                }
                else {
                    EasyPermissions.requestPermissions(Profile2.this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);

                }


            }
        });

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {



        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
           // requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public void popUpConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                if( ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
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
        pictureDialog.setMessage("Working... Please wait");
        pictureDialog.show();

        Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call = mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID);

        Log.i("Safe", mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagArrayResponseModel<List<PatientPhotoModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Response<SwagArrayResponseModel<List<PatientPhotoModel>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    pictureDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful()){
                    pictureDialog.dismiss();

                    photoResponse = response.body();
                    photoList = photoResponse.getData();

                    try {
                        photo = photoList.get(0);
                        imageString = photo.getPhoto();
                        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        profile_picture.setImageBitmap(decodedImage);
                        Toast.makeText(getApplicationContext(), "Picture fetched successfully", Toast.LENGTH_SHORT).show();


                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"No profile picture uploaded",Toast.LENGTH_LONG).show();
                    }



                }
                else {
                    pictureDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                    try {
                        Log.i("Safe", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Throwable throwable) {

                try {
                    pictureDialog.dismiss();
                    Log.i("TheSafe", throwable.getMessage());
                    Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setProfilePicture(){
        pictureDialog.setMessage("Uploading Picture");
        pictureDialog.show();

        Picture picture = new Picture(imageString);

        Call<SwagArrayResponseModel> call = mSafeDoctorService.updateProfilePicture(TokenString.tokenString, AppConstants.patientID, picture);
        Log.i("Safe", mSafeDoctorService.updateProfilePicture(TokenString.tokenString, AppConstants.patientID, picture).request().url().toString());


        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                Log.i("Safe", "we're in");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN)
                {
                    pictureDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful()){
                    pictureDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Picture successfully uploaded", Toast.LENGTH_SHORT).show();
                    AppConstants.profilePictureString = imageString;
                    sessionManagement.setProfileImage(String.valueOf(imageString));
                }
                else {
                    pictureDialog.dismiss();
                    try {
                        Log.i("Safe", response.errorBody().string());
                        Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable throwable) {
                pictureDialog.dismiss();
                try {
                    Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                }
                catch (Exception e)
                {

                }
            }
        });
    }
}
