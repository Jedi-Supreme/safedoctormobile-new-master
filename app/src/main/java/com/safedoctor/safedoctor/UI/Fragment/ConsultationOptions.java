package com.safedoctor.safedoctor.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.CallActivity;
import com.safedoctor.safedoctor.UI.Activities.Consultation;
import com.safedoctor.safedoctor.UI.Activities.VideoChat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultationOptions extends Fragment {

    private TextView btnChat;
    private TextView btnCall;
    private TextView btnVideoChat;



    public ConsultationOptions() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        ConsultationOptions fragment = new ConsultationOptions();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_consultation_options, container, false);

        btnChat = (TextView) RootView.findViewById(R.id.btn_chat);
        btnCall = (TextView) RootView.findViewById(R.id.btn_call);
        btnVideoChat = (TextView) RootView.findViewById(R.id.btn_video_chat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCall();
            }
        });

        btnVideoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideoChat();
            }
        });


        return RootView;
    }

    private void openChat()
    {
        Intent openChats = new Intent(getActivity(), Consultation.class);
        getActivity().startActivity(openChats);

    }

    private void openVideoChat()
    {
        Intent openVideoChats = new Intent(getActivity(), VideoChat.class);
        getActivity().startActivity(openVideoChats);

    }

    private void openCall()
    {
        Intent openChats = new Intent(getActivity(), CallActivity.class);
        getActivity().startActivity(openChats);

    }

}
