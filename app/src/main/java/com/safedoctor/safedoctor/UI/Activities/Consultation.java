package com.safedoctor.safedoctor.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
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
import com.safedoctor.safedoctor.Utils.OpenTokConfig;
import com.safedoctor.safedoctor.Utils.WebServiceCoordinator;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Api.Common.CurrentConsultationID;


public class Consultation extends AppCompatActivity  implements  WebServiceCoordinator.Listener, Session.SessionListener, Session.SignalListener {

    public static final String SIGNAL_TYPE = "msg";
    private static final String LOG_TAG = Consultation.class.getSimpleName();
    private WebServiceCoordinator mWebServiceCoordinator;

    private Session mSession;
    private SignalMessageAdapter mMessageHistory;
    private ListView mMessageHistoryListView;
    private ImageView sendMessageImgBtn;


    private EditText mMessageEditTextView;

    //private ImageView recordMessageImgBtn;
    private ProgressDialog mProgressDialog;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<ChatSessionDataModel>> chatSessionResponse;
    private List<ChatSessionDataModel> chatSession;
    private Subscriber mSubscriber;
    private TextView textChatCountDown;

    private TextView retryconnection;

    private long constime;
    private CountDownTimer countDownTimer;
    private  long remainingsec;

    private String sendermessage="";
    private Common common;

    private  Long currconsultationid;

    String doctorid;

    Common.onConsultationStart onConsultationStartListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        setTitle(getString(R.string.consultation));

        //getSupportActionBar().hide();

        // inflate views
        mMessageEditTextView = (EditText)findViewById(R.id.message_edit_text);
        mMessageHistoryListView = (ListView)findViewById(R.id.message_history_list_view);
        textChatCountDown = (TextView) findViewById(R.id.text_chat_count_down);
        textChatCountDown.setVisibility(View.VISIBLE);

        retryconnection=(TextView)findViewById(R.id.retryconnection);
        retryconnection.setVisibility(View.GONE);
        retryconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Retrying to connect",Toast.LENGTH_LONG).show();
                mProgressDialog.show();
                if(chatSession.size()==0){
                    fetchChatSession(AppConstants.bookingid);
                }else{
                    prepareChat();
                }
            }
        });


        sendMessageImgBtn = (ImageView) findViewById(R.id.send_message_img_btn);
      //  recordMessageImgBtn = (ImageView) findViewById(R.id.record_message_img_btn);

        // Attach data source to message history
        mMessageHistory = new SignalMessageAdapter(this);
        mMessageHistoryListView.setAdapter(mMessageHistory);
        //hide  dividers between each message
        mMessageHistoryListView.setDivider(null);
        mMessageHistoryListView.setDividerHeight(0);

        mProgressDialog = new ProgressDialog(Consultation.this);
        mProgressDialog.setMessage("Connecting, please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mSafeDoctorService = ApiUtils.getAPIService();

        constime=getConsultationTime(getIntent());

        doctorid=getIntent().getStringExtra("doctorid");

        fetchChatSession(AppConstants.bookingid);
        common=new Common(getApplicationContext());

        if(CurrentConsultationID!=null){
            currconsultationid=CurrentConsultationID;
            common.loadPreviousData(common,mMessageHistory,currconsultationid);
        }

        onConsultationStartListener=new Common.onConsultationStart() {
            @Override
            public void onStart(Long consultationid) {
                if(consultationid!=null){
                    currconsultationid=consultationid;
                    common.loadPreviousData(common,mMessageHistory,consultationid);
                }
            }
        };
    }

    public static long getConsultationTime(Intent intent){
        long timediff=0;
        String starttime=intent.getStringExtra("starttime");
        String endtime=intent.getStringExtra("endtime");

        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm");
        try {
            Date starttimeformat=timeFormatter.parse(starttime);
            Date endtimeformat=timeFormatter.parse(endtime);
            timediff=endtimeformat.getTime()-starttimeformat.getTime();
            return timediff;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timediff;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mSession.disconnect();
            mSubscriber.destroy();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchChatSession(int bookingid)
    {
        //Log.e("fetchChatSession ","Booking ID : "+bookingid);
        chatSession = new ArrayList<>();
        Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call = mSafeDoctorService.getChatSession(TokenString.tokenString,bookingid);
        call.enqueue(new Callback<SwagArrayResponseModel<List<ChatSessionDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call, Response<SwagArrayResponseModel<List<ChatSessionDataModel>>> response) {
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                } else if (response.isSuccessful()) {

                    chatSessionResponse = response.body();
                    chatSession = chatSessionResponse.getData();
                    prepareChat();


                }

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                mProgressDialog.dismiss();
                retryconnection.setVisibility(View.VISIBLE);
            }
        });
    }


    void prepareChat(){
        for (ChatSessionDataModel c : chatSession)
        {
            //Log.e("chat", "Session: "+c.getSessionid()+"  Token: "+c.getToken());
            AppConstants.SESSION_ID = c.getSessionid();
            AppConstants.TOKEN = c.getToken();

            InitiateChatSession(c.getSessionid(),c.getToken());
        }
    }


    private void InitiateChatSession(String sessionid, String token)
    {
        Log.e("chat", "Starting session with params: Session ID: "+sessionid+"  Token: "+token);
        initializeSession(OpenTokConfig.API_KEY, sessionid, token);
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

    private void initializeSession(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "Initializing Session");

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.setSignalListener(this);
        mSession.connect(token);
    }

    private void sendMessage() {

        Log.d(LOG_TAG, "Send Message");
        sendermessage=mMessageEditTextView.getText().toString();
        SignalMessage signal = new SignalMessage(mMessageEditTextView.getText().toString());
        mSession.sendSignal(SIGNAL_TYPE, signal.getMessageText());

        //mMessageEditTextView.setText("");
//        Toast.makeText(getApplicationContext(),mMessageHistory.getCount()+"",Toast.LENGTH_LONG).show();


    }

    private void showMessage(String messageData, boolean remote) {

        Log.d(LOG_TAG, "Show Message");

        SignalMessage message = new SignalMessage(messageData, remote);
        mMessageHistory.add(message);
    }

    private void logOpenTokError(OpentokError opentokError) {

        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        //mMessageEditTextView.setEnabled(true);
        mProgressDialog.dismiss();
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
                }
            }
        });
        retryconnection.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Session connected and ready",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
        Toast.makeText(getApplicationContext(), "Session Disconnected", Toast.LENGTH_SHORT).show();
        retryconnection.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        mMessageEditTextView.setText(sendermessage);
        logOpenTokError(opentokError);
    }

    /* Signal Listener methods */

    @Override
    public void onSignalReceived(Session session, String type, String data, Connection connection) {

        boolean remote = !connection.equals(mSession.getConnection());
        if(SIGNAL_TYPE != null && type.equals(SIGNAL_TYPE)) {

            showMessage(data, remote);
            Log.d("msg_data",data);
        }else{
            countDownTimer=common.systemMsg(this,type,data,countDownTimer,constime,textChatCountDown,onConsultationStartListener);
            return;
        }

        if(!remote && currconsultationid!=null){
            mMessageEditTextView.setText("");
            Chat chat=new Chat(sendermessage,String.valueOf(AppConstants.patientID), doctorid,currconsultationid);
            common.saveMessage(chat);
        }else if(!remote){
            mMessageEditTextView.setText("");
        }




    }






    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "ApiKey: "+apiKey + " SessionId: "+ sessionId + " Token: "+token);

        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        showConfigError("Web Service error", error.getMessage());
    }

    /* alert dialogue for errors */

    private void showConfigError(String alertTitle, final String errorMessage) {

        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Consultation.this.finish();
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

                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }



}
