
package com.safedoctor.safedoctor.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.safedoctor.safedoctor.Adapter.SignalMessageAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Chat;
import com.safedoctor.safedoctor.Model.ChatSessionDataModel;
import com.safedoctor.safedoctor.Model.SignalMessage;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.WebServiceCoordinator;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Api.Common.CurrentConsultationID;
import static com.safedoctor.safedoctor.UI.Activities.Consultation.getConsultationTime;
import static com.safedoctor.safedoctor.Utils.App.context;


public class VideoChat extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks,
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener,Session.SignalListener {

    private static final String LOG_TAG = VideoChat.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private Publisher mPublisher;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Subscriber mSubscriber;
    private ProgressBar videoLoadingProgressBar;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<ChatSessionDataModel>> chatSessionResponse;
    private List<ChatSessionDataModel> chatSession;
    private TextView vidChatCountDown;
    private BottomSheetLayout bottomSheetLayout;
    private View chatview;
    private Button showchatbtn;

    private String token,sessionid=null;
    private long constime;
    private CountDownTimer countDownTimer;




    //text chat
    private EditText mMessageEditTextView;
    private ListView mMessageHistoryListView;
    private SignalMessageAdapter mMessageHistory;
    private ImageView sendMessageImgBtn;
    public static final String SIGNAL_TYPE = "msg";
    private WebServiceCoordinator mWebServiceCoordinator;

    private String sendermessage="";
    private Common common;
    String doctorid;
    private Common.onConsultationStart onConsultationStartListener;
    Long currconsultationid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_video_chat);
        videoLoadingProgressBar = (ProgressBar) findViewById(R.id.video_loading_progress_bar);
        mMessageEditTextView = (EditText)findViewById(R.id.message_edit_text);
       // mMessageHistoryListView = (ListView)findViewById(R.id.message_history_list_view);
        sendMessageImgBtn = (ImageView) findViewById(R.id.send_message_img_btn);
        vidChatCountDown = (TextView) findViewById(R.id.vid_chat_count_down);

        bottomSheetLayout=(BottomSheetLayout)findViewById(R.id.chatsheetlayout);
        chatview=LayoutInflater.from(context).inflate(R.layout.videochatsheet, bottomSheetLayout, false);
        mMessageHistoryListView = (ListView)chatview.findViewById(R.id.message_history_list_view);

        mSafeDoctorService = ApiUtils.getAPIService();
        requestPermissions();

        mMessageHistory = new SignalMessageAdapter(this);
        mMessageHistoryListView.setAdapter(mMessageHistory);
        //hide  dividers between each message
        mMessageHistoryListView.setDivider(null);
        mMessageHistoryListView.setDividerHeight(0);

        showchatbtn=(Button)findViewById(R.id.showchatbtn);
        showchatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bottomSheetLayout.isSheetShowing()){
                    bottomSheetLayout.showWithSheetView(chatview);
                }
            }
        });

        showchatbtn.setVisibility(View.GONE);
        mMessageEditTextView.setVisibility(View.GONE);
        sendMessageImgBtn.setVisibility(View.GONE);

        constime=getConsultationTime(getIntent());
        doctorid=getIntent().getStringExtra("doctorid");
        common=new Common(context);

        if(CurrentConsultationID!=null){
            currconsultationid=CurrentConsultationID;
            common.loadPreviousData(common,mMessageHistory,currconsultationid);
        }
        onConsultationStartListener=new Common.onConsultationStart() {
            @Override
            public void onStart(Long consultationid) {
                if(consultationid!=null){
                    currconsultationid=consultationid;
                    common.loadPreviousData(common,mMessageHistory,currconsultationid);
                }
            }
        };
    }

    public static void hidekeyboard(Activity activity){
        try{
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        }catch(Exception e){
            Log.e("","trying to hide keyboard exception "+e.getMessage());
        }
    }



    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        //  mPublisher = new Publisher.Builder(this).videoTrack(false).build();
        mPublisher.setPublisherListener(this);


        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);

        sendMessageImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (mMessageEditTextView.getText().toString().trim().matches(""))
                {
                    mMessageEditTextView.setError(getString(R.string.chat_empty_msg_error));
                    mMessageEditTextView.requestFocus();
                }
                else {
                    sendMessage();
                    hidekeyboard(VideoChat.this);
                }
            }
        });

        showchatbtn.setVisibility(View.VISIBLE);
        mMessageEditTextView.setVisibility(View.VISIBLE);
        sendMessageImgBtn.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(),"Session connected and ready",Toast.LENGTH_LONG).show();

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
        videoLoadingProgressBar.setVisibility(View.GONE);
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());

        }
         vidChatCountDown.setVisibility(View.VISIBLE);
//        if (!vidChatCountDown.getText().toString().contains("min"))
//        {
        //countDownTimer=setCountDownTimer(this,constime,countDownTimer,vidChatCountDown);

        //  }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
        session.disconnect();
        stream.getSession().disconnect();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
        Toast.makeText(getApplicationContext(),"Error connecting",Toast.LENGTH_LONG).show();

        logOpenTokError(opentokError);

        if(sessionid!=null && token!=null){
            Log.e(LOG_TAG,"sessionid and token not null");
            initializeSession(AppConstants.API_KEY,sessionid, token);
        }else{
            fetchChatSession();
        }

        mMessageEditTextView.setText(sendermessage);

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mSession.disconnect();
            mPublisher.destroy();
            mSubscriber.destroy();
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

                        sessionid=c.getSessionid();
                        token=c.getToken();



                        mSession = new Session.Builder(VideoChat.this, AppConstants.API_KEY, c.getSessionid()).build();
                        mSession.setSessionListener(VideoChat.this);
                        mSession.setSignalListener(VideoChat.this);
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



    private void sendMessage() {

        Log.d(LOG_TAG, "Send Message");
        sendermessage=mMessageEditTextView.getText().toString();
        SignalMessage signal = new SignalMessage(mMessageEditTextView.getText().toString());
        mSession.sendSignal(SIGNAL_TYPE, signal.getMessageText());

       // mMessageEditTextView.setText("");
        Toast.makeText(getApplicationContext(),"Sending message...",Toast.LENGTH_LONG).show();


    }

    private void showMessage(String messageData, boolean remote) {

        Log.d(LOG_TAG, "Show Message");
        Log.d(LOG_TAG, messageData);

        SignalMessage message = new SignalMessage(messageData, remote);
        mMessageHistory.add(message);

    }

    private void logOpenTokError(OpentokError opentokError) {

        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

     /* Activity lifecycle methods */

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onSignalReceived(Session session, String type, String data, Connection connection) {
        boolean remote = !connection.equals(mSession.getConnection());
        if (SIGNAL_TYPE != null && type.equals(SIGNAL_TYPE)) {
            mMessageEditTextView.setText("");
            showMessage(data, remote);
            if(!bottomSheetLayout.isSheetShowing()){
                bottomSheetLayout.showWithSheetView(chatview);
            }
        }else {
            countDownTimer=common.systemMsg(this,type,data,countDownTimer,constime,vidChatCountDown,onConsultationStartListener);
        }

        if(!remote){
            Chat chat=new Chat(sendermessage,String.valueOf(AppConstants.patientID),doctorid,currconsultationid);
            common.saveMessage(chat);
        }

    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "Initializing Session");

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.setSignalListener(this);
        mSession.connect(token);
    }

    /* Web Service Coordinator delegate methods */
    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
    //    initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        showConfigError("Web Service error", error.getMessage());
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


    /* alert dialogue for errors */

    private void showConfigError(String alertTitle, final String errorMessage) {

        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VideoChat.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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