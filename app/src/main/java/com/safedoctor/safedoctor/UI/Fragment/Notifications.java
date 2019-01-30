package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.Adapter.ReminderAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.Utils.App;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.Backgroundservices.MainService.getLocalConfirmedAppointmentContentModel;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class Notifications extends Fragment {
    private View rootView;
    private RecyclerView patientAppointmentsRecyclerView;
    private ReminderAdapter reminderAdapter;
    private String TAG="Notifications";

    public static Fragment newInstance() {
        Notifications fragment = new Notifications();
        return  fragment;
    }

    public Notifications() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        patientAppointmentsRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        patientAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ConfirmedAppointmentContentModel> confirmedAppointmentContentModelList=new ArrayList<>();
        confirmedAppointmentContentModelList=getLocalConfirmedAppointmentContentModel();

        if(confirmedAppointmentContentModelList.size()>0){
            Log.e(TAG," fetch appointment from local. Size : "+confirmedAppointmentContentModelList);
            reminderAdapter=new ReminderAdapter(App.context,confirmedAppointmentContentModelList);
            patientAppointmentsRecyclerView.setAdapter(reminderAdapter);
            reminderAdapter.notifyDataSetChanged();
        }

        Activity activity=getActivity();
        new Common(context).fragmentInitOnbackpressed(activity);

        return rootView;
    }
}
