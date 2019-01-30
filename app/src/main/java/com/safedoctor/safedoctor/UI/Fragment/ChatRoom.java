package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.safedoctor.safedoctor.Adapter.SignalMessageAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Chat;
import com.safedoctor.safedoctor.Model.ChatMessage;
import com.safedoctor.safedoctor.Model.SignalMessage;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.ChatRoomToken;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.Consultation;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.OpenTokConfig;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.WebServiceCoordinator;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getReadabledate;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class ChatRoom extends Fragment  {

    public static final String SIGNAL_TYPE = "text-signal";
    private static final String LOG_TAG = Consultation.class.getSimpleName();
    private View rootView;
    private WebServiceCoordinator mWebServiceCoordinator;

    private Session mSession;
    private Gson gson = new Gson();


    private SignalMessageAdapter signalMessageAdapter;
    private ListView mMessageHistoryListView;
    private ImageView sendMessageImgBtn;

    private Subscriber mSubscriber;

    private SafeDoctorService mSafeDoctorService;
    private SessionManagement sessionManagement;
    private String TAG="ChatRoom";
    private ProgressDialog mProgressDialog;
    private EditText mMessageEditTextView;

    private String sendermessage="";
    private Common common;

    List<Chat> chatList=new ArrayList<>();

    public ChatRoom() {
    }

    public static Fragment newInstance() {
        ChatRoom fragment = new ChatRoom();
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_consultation, container, false);
        sessionManagement=new SessionManagement(context);
        mSafeDoctorService = ApiUtils.getAPIService();
        sendMessageImgBtn = (ImageView) rootView.findViewById(R.id.send_message_img_btn);
        common=new Common(context);
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
        mMessageEditTextView = (EditText)rootView.findViewById(R.id.message_edit_text);
        mMessageHistoryListView = (ListView)rootView.findViewById(R.id.message_history_list_view);

        // Attach data source to message history
        signalMessageAdapter = new SignalMessageAdapter(context);
        mMessageHistoryListView.setAdapter(signalMessageAdapter);
        //hide  dividers between each message
        mMessageHistoryListView.setDivider(null);
        mMessageHistoryListView.setDividerHeight(0);
        mMessageHistoryListView.setStackFromBottom(true);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Connecting, please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        fetchChatSession();
        common.loadPreviousData(common,signalMessageAdapter,(long)0);



        Activity activity=getActivity();
        common.fragmentInitOnbackpressed(activity);


        return rootView;
    }

    private void sendMessage() {

        Log.d(LOG_TAG, "Send Message");

        ChatMessage message=new ChatMessage(mMessageEditTextView.getText().toString(), AppConstants.patientName,getReadabledate(),AppConstants.profilePictureString,"mobile");
        //Log.e(TAG,"Chat message "+gson.toJson(message).toString());
        sendermessage=mMessageEditTextView.getText().toString();
        SignalMessage signal = new SignalMessage(gson.toJson(message).toString());
        mSession.sendSignal(SIGNAL_TYPE, signal.getMessageText());
        mMessageEditTextView.setText("");
    }

    private void fetchChatSession()
    {
        Call<SwagArrayResponseModel<List<ChatRoomToken>>> call = mSafeDoctorService.generateChatSession(TokenString.tokenString);
        call.enqueue(new Callback<SwagArrayResponseModel<List<ChatRoomToken>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ChatRoomToken>>> call, Response<SwagArrayResponseModel<List<ChatRoomToken>>> response) {
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(context, "Session expired. Login again", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    sessionManagement.logoutSessionExpired();
                    if(getActivity()!=null){
                        getActivity().finish();
                    }
                } else if (response.isSuccessful() && response.code()==200) {
                    mProgressDialog.dismiss();
                    Log.e(TAG,"onResponse "+response.body());

                    SwagArrayResponseModel<List<ChatRoomToken>> session=response.body();
                    List<ChatRoomToken> token=session.getData();
                    ChatRoomToken chatRoomToken=token.get(0);

                    InitiateChatSession(chatRoomToken.getSessionid(),chatRoomToken.getToken());

                }

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ChatRoomToken>>> call, Throwable t) {
                mProgressDialog.dismiss();
                Log.e(TAG,"fetchChatSession onFailure"+t.getMessage());
                Toast.makeText(context, "Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void InitiateChatSession(String sessionid, String token)
    {
        Log.d("chat", "Starting session with params: Session ID: "+sessionid+"  Token: "+token);
        initializeSession(OpenTokConfig.API_KEY, sessionid, token);
    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "Initializing Session");

        mSession = new Session.Builder(context, apiKey, sessionId).build();
        mSession.connect(token);
        mSession.setSessionListener(new Session.SessionListener() {
            @Override
            public void onConnected(Session session) {
                mProgressDialog.dismiss();
                Log.e(TAG, "onConnected Session Connected");
            }

            @Override
            public void onDisconnected(Session session) {
                Log.e(TAG, "Session Disconnected");
            }

            @Override
            public void onStreamReceived(Session session, Stream stream) {
                Log.e(TAG, "Stream Received");
            }

            @Override
            public void onStreamDropped(Session session, Stream stream) {
                Log.e(TAG, "Stream Dropped");
            }

            @Override
            public void onError(Session session, OpentokError opentokError) {
                Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
                Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
                mProgressDialog.dismiss();
                mMessageEditTextView.setText(sendermessage);
                Toast.makeText(context,"Connection error.Try again",Toast.LENGTH_LONG).show();
            }
        });
        mSession.setSignalListener(new Session.SignalListener() {
            @Override
            public void onSignalReceived(Session session, String type, String data, Connection connection) {
                boolean remote = !connection.equals(mSession.getConnection());
                if(type!=null){
                    if (  SIGNAL_TYPE != null && type.equals(SIGNAL_TYPE)) {
                        showMessage(data, remote);
                    }
                }
                else if(type==null){
                    showMessage(data,remote);
                }

                if(!remote){
                    Chat chat=new Chat(sendermessage,String.valueOf(AppConstants.patientID),null);
                    common.saveMessage(chat);
                }


                /*
                if (!textChatCountDown.getText().toString().contains("min"))
                {
                    new CountDownTimer(1800000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            textChatCountDown.setText(""+String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            //  CountDownTime =
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            //    countDownTv.setText("done!");
                            finish();
                        }

                    }.start();
                }*/

                Log.d("msg_data",data);
            }


        });


    }




    private void showMessage(String messageData, boolean remote) {

        Log.d(LOG_TAG, "Showing Message");

        SignalMessage message = new SignalMessage(messageData, remote);
        signalMessageAdapter.add(message);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mSession.disconnect();
            mSubscriber.destroy();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }
}
