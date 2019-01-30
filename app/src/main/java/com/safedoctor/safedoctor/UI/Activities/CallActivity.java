package com.safedoctor.safedoctor.UI.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ChatSessionDataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.UI.Activities.Consultation.getConsultationTime;



public class CallActivity extends AppCompatActivity  implements  Session.SessionListener, PublisherKit.PublisherListener  {

    private static final String LOG_TAG = CallActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private ImageView buttonEndCall;
    private Session mSession;
    private Publisher mPublisher;
//    private FrameLayout mPublisherViewContainer;
//    private FrameLayout mSubscriberViewContainer;
    private Subscriber mSubscriber;
    private TextView callingTv;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<ChatSessionDataModel>> chatSessionResponse;
    private List<ChatSessionDataModel> chatSession;
    private Handler handler=new Handler(),handler2 = new Handler();
    private Runnable runnable1,runnable2;
    private TextView countDownTv;
    private String CountDownTime;
    private int minute = 5;

    private long constime;
    private CountDownTimer countDownTimer;

    String doctorid=null;
    Common common;






    //  private ProgressBar videoLoadingProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_call);

        buttonEndCall = (ImageView) findViewById(R.id.button_end_call);
        callingTv = (TextView) findViewById(R.id.calling_tv);
        countDownTv = (TextView) findViewById(R.id.count_down_tv);
        //countDownTv.setVisibility(View.VISIBLE);


        mSafeDoctorService = ApiUtils.getAPIService();

        runnable1 = new Runnable() {
            @Override
            public void run() {
                //hide calling text after every 1 sec
                callingTv.setVisibility(View.INVISIBLE);
                //show calling text after every 0.5 sec
                handler2.postDelayed(runnable2, 500);
                handler.postDelayed(this, 1000);
            }
        };

        runnable2 = new Runnable() {
            @Override
            public void run() {
                callingTv.setVisibility(View.VISIBLE);
            }
        };

        //hide calling text every 1 sec
        handler.postDelayed(runnable1, 1000);

        constime=getConsultationTime(getIntent());
        common=new Common(getApplicationContext());




//        handler2.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                //call function
//                callingTv.setVisibility(View.VISIBLE);
//                handler2.postDelayed(this, 1200);
//                //  callingTv.setVisibility(View.VISIBLE);
//            }
//        }, 1200);

        buttonEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endCall();
            }
        });

        requestPermissions();
    }



    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        //mPublisher = new Publisher.Builder(this).build();
          mPublisher = new Publisher.Builder(this).videoTrack(false).build();
        mPublisher.setPublisherListener(this);

        Toast.makeText(getApplicationContext(),"Session connected and ready",Toast.LENGTH_LONG).show();


     //   mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
//        AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);

        //    AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
        session.disconnect();
        finish();
        Toast.makeText(getApplicationContext(),"Session disconnected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");
//        videoLoadingProgressBar.setVisibility(View.GONE);
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriber.setSubscribeToVideo(false);
            callingTv.setText("Connected");
            handler.removeCallbacks(runnable1);
            handler2.removeCallbacks(runnable2);
            callingTv.setVisibility(View.VISIBLE);
            countDownTv.setVisibility(View.VISIBLE);

            countDownTimer=common.setCountDownTimer(this,constime,countDownTimer,countDownTv);

            //   mSubscriberViewContainer.addView(mSubscriber.getView());
        }
      //  AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
           // mSubscriberViewContainer.removeAllViews();
        }
        session.disconnect();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
           // mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
          //  mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);

            fetchChatSession();
            // initialize and connect to the session


        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    private void fetchChatSession()
    {
        chatSession = new ArrayList<>();
        Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call = mSafeDoctorService.getChatSession(TokenString.tokenString,AppConstants.bookingid);
        call.enqueue(new Callback<SwagArrayResponseModel<List<ChatSessionDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call, Response<SwagArrayResponseModel<List<ChatSessionDataModel>>> response) {
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    Toast.makeText(getApplicationContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                } else if (response.isSuccessful()) {

                    chatSessionResponse = response.body();
                    chatSession = chatSessionResponse.getData();

                    for (ChatSessionDataModel c : chatSession)
                    {
                        Log.d("chat", "Session: "+c.getSessionid()+"  Token: "+c.getToken());
                        AppConstants.SESSION_ID = c.getSessionid();
                        AppConstants.TOKEN = c.getToken();

                        mSession = new Session.Builder(CallActivity.this, AppConstants.API_KEY, c.getSessionid()).build();
                        mSession.setSessionListener(CallActivity.this);
                        mSession.setSignalListener(new Session.SignalListener() {
                            @Override
                            public void onSignalReceived(Session session, String type, String data, Connection connection) {
                                countDownTimer=common.systemMsg(CallActivity.this,type,data,countDownTimer,constime,countDownTv,null);

                            }
                        });
                        mSession.connect(c.getToken());
                    }


                }

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void endCall()
    {
        try {
            mSession.disconnect();
            mPublisher.destroy();

            if(mSubscriber != null) {
                mSubscriber.destroy();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        mSubscriber.destroy();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            handler.removeCallbacks(runnable1);
            handler2.removeCallbacks(runnable2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to leave the consultation?");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Might want to do something here later
            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }
}
