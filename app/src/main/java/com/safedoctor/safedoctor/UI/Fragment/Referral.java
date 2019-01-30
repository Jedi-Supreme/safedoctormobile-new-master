package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.Adapter.ReferralsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage.updateCounter;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class Referral extends Fragment {
    private View rootView;
    private List<com.safedoctor.safedoctor.Model.Referral> referralList=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private Common common;
    private ReferralsAdapter referralsAdapter;



    public static Fragment newInstance() {
        Referral fragment = new Referral();
        return  fragment;
    }

    public Referral() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_referral, container, false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        common=new Common(context);
        mSwipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReferrals();
            }
        });

        Activity activity=getActivity();

        new Common(context).fragmentInitOnbackpressed(activity);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                getReferrals();
            }
        });
    }

    void getReferrals(){
        common.getConsultationReferral(AppConstants.patientID);
        common.setOnTaskCompletedListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object result) {
                referralList=(List<com.safedoctor.safedoctor.Model.Referral>) result;
                if(referralList.size()>0){
                    AppConstants.referrals=referralList.size(); updateCounter(null);
                    referralsAdapter=new ReferralsAdapter(referralList,context);
                    recyclerView.setAdapter(referralsAdapter);
                }else{
                    //Toast.makeText(context,"No referrals",Toast.LENGTH_LONG).show();
                }
                mSwipeRefresh.setRefreshing(false);


            }
        });



    }


}
