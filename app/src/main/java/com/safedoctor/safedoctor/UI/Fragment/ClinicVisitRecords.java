package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class ClinicVisitRecords extends Fragment {
    private View rootView;
    public static Fragment newInstance() {
        ClinicVisitRecords fragment = new ClinicVisitRecords();
        return  fragment;
    }

    public ClinicVisitRecords() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clinic_visits_medical_records, container, false);

        Activity activity=getActivity();
        new Common(context).fragmentInitOnbackpressed(activity);
        return rootView;
    }
}
