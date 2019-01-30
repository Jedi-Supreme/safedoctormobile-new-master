package com.safedoctor.safedoctor.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.SessionManagement;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static com.safedoctor.safedoctor.Utils.App.context;


public class SettingsFragment extends Fragment {


   private ToggleSwitch notiswitch;
   private View rootView;
   private String TAG="SettingsFragment";
   private SessionManagement sessionManagement;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_settings, container, false);
        sessionManagement=new SessionManagement(context);

        notiswitch=(ToggleSwitch)rootView.findViewById(R.id.notitoggle);
        notiswitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Log.e(TAG,"position "+position+" isChecked "+isChecked);

                if(isChecked){
                    if(position==0){
                        Toast.makeText(context,"Notifications ON",Toast.LENGTH_LONG).show();
                        sessionManagement.setNotificationStatus(true);
                    }else if(position==1){
                        Toast.makeText(context,"Notifications OFF",Toast.LENGTH_LONG).show();
                        sessionManagement.setNotificationStatus(false);
                    }
                }

            }
        });

        return rootView;
    }

}
