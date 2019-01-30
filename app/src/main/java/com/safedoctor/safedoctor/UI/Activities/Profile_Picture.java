package com.safedoctor.safedoctor.UI.Activities;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import java.net.HttpURLConnection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.R.id.profile_picture;
import static com.safedoctor.safedoctor.Utils.App.context;

public class Profile_Picture extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_VIDEO_APP_PERM = 4;
    de.hdodenhof.circleimageview.CircleImageView image;
    FloatingActionButton fabnext;
    FloatingActionButton selectProfilePicture;
    public final int GALLERY_REQUEST=3;
    public final int CAMERA_REQUEST=2;
    SafeDoctorService mSafeDoctorService;

    Uri picUri;
    String imageString;
    private ProgressBar progress;
    private SessionManagement sessionManagement=new SessionManagement(context);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile__picture);
       image = (CircleImageView) findViewById(profile_picture);
        selectProfilePicture= (FloatingActionButton) findViewById(R.id.selectProfilePicture);
        fabnext = (FloatingActionButton) findViewById(R.id.next);

        progress = (ProgressBar) findViewById(R.id.progress);

        mSafeDoctorService = ApiUtils.getAPIService();

        selectProfilePicture.setOnClickListener(this);
        fabnext.setOnClickListener(this);
        requestPermissions();
        getProfilePicture();
    }

    private void showProgress(final boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectProfilePicture:
                String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };

                if (EasyPermissions.hasPermissions(Profile_Picture.this,perms))
                {
                    popUpConfirmation();
                }
                else {
                    EasyPermissions.requestPermissions(Profile_Picture.this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);

                }
                break;

            case R.id.next:
                //TODO will work on this app into thing later when things are set


                Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public void popUpConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Source of Picture");
        builder.setMessage("Kindly choose where you want to select your picture");
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

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // do something after permission granted


        } else {
            //if permission is denied
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case GALLERY_REQUEST:
                    picUri = data.getData();

                    Glide.with(getApplicationContext()).load(picUri).asBitmap().into(new BitmapImageViewTarget(image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            image.setImageBitmap(resource);
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                Bitmap bitmap =  resource;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                                byte[] imageBytes = baos.toByteArray();
                                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                Log.i("Thetring", imageString);
                                setProfilePicture();

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                    break;



                    //encode image to base64 string



                                                                              //image.setImageURI(picUri);
                    /*
                    image.buildDrawingCache();

                    //encode image to base64 string
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap =  image.getDrawingCache();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Log.i("Thetring", imageString);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }*/


                case CAMERA_REQUEST:
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    image.setImageBitmap(bitmap);
                    setProfilePicture();
                    break;

            }
//            imageText.setText(imageString);
        }

    }
    @Override
    public void onBackPressed() {
        showProgress(false);
        super.onBackPressed();

    }

    public void setProfilePicture()
    {
        showProgress(true);
        //String base = getString(R.string.swag_user) + ":" + getString(R.string.swag_pass);
        //String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        //Log.i("SafeRes", "Auth " + authHeader);

        Picture picture = new Picture(imageString);

        Call<SwagArrayResponseModel> call = mSafeDoctorService.uploadProfilePicture(TokenString.tokenString, AppConstants.patientID, picture);

        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response)
            {
                Log.i("Safe", "picture uploaded successfully");
                showProgress(false);
                if (response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Picture successfully uploaded", Toast.LENGTH_SHORT).show();
                    AppConstants.profilePictureString = imageString;
                    sessionManagement.setProfileImage(String.valueOf(imageString));

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable throwable) {
                showProgress(false);
                Log.i("TheSafe", throwable.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_networkissues), Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
        });
    }

    public void getProfilePicture()
    {
        if(getIntent().getBooleanExtra("issignup", false))
        {
            return;
        }

        showProgress(true);
        if(AppConstants.profilePictureString != null && !AppConstants.profilePictureString.isEmpty())
        {
            byte[] imageBytes = Base64.decode(AppConstants.profilePictureString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(decodedImage);
        }

        Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call = mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID);

        Log.i("Safe", mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagArrayResponseModel<List<PatientPhotoModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Response<SwagArrayResponseModel<List<PatientPhotoModel>>> response) {
                Log.i("TheSafe", response.code() + "");
                showProgress(false);
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {

                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful())
                {
                    SwagArrayResponseModel<List<PatientPhotoModel>> photoResponse = response.body();
                    List<PatientPhotoModel> photoList = photoResponse.getData();
                    if(photoList != null && photoList.size() > 0)
                    {
                        PatientPhotoModel photo = photoList.get(0);
                        imageString = photo.getPhoto();
                        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        image.setImageBitmap(decodedImage);
                    }

                }

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Throwable throwable) {

                try {
                    showProgress(false);
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

}
