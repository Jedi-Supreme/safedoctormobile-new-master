package com.safedoctor.safedoctor.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerSpecialists extends Fragment {

    private List<Facilityinfo> mHospitalsList;
//    private RecyclerView mAidRecyclerView;
//    private SwipeRefreshLayout mSwipeRefresh;
//    private SafeDoctorService mSafeDoctorService;

    private SafeDoctorService mSafeDoctorService;

    public static Fragment newInstance() {
        PartnerHospitals fragment = new PartnerHospitals();
        return  fragment;
    }


    public PartnerSpecialists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Common common = new Common(getContext());
//        common.getFacilities(hospitalRecyclerView);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partner_specialists, container, false);
    }

}
