package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.FirstAidAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.AidDetail;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.ClickListener;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.RecyclerTouchListener;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;


public class FirstAid extends Fragment implements ClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<Aid.Content> mAidList=new ArrayList<>();
    private RecyclerView mAidRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private SafeDoctorService mSafeDoctorService;
    private static FirstAidAdapter firstAidAdapter;

    public static Fragment newInstance() {
        FirstAid fragment = new FirstAid();
        return  fragment;
    }

    public FirstAid() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSafeDoctorService = ApiUtils.getAPIService();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_aid, container, false);
        mAidRecyclerView = (RecyclerView)  rootView.findViewById(R.id.firstAidRecyclerView);
        mAidRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAidRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, mAidRecyclerView, this));
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeRefresh.setOnRefreshListener(this);






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

                if (!AppConstants.isNetworkAvailable()){
                    Toast.makeText(context,context.getString(R.string.no_internet_connection_txt),Toast.LENGTH_LONG).show();
                    //AppConstants.displayNoNetworkAlert(context, getActivity().findViewById(R.id.main_layout));
                    mSwipeRefresh.setRefreshing(false);
                }
                else if(AppConstants.CACHE_AIDS.size()==0){
                    GetFirstAids();
                }else if(AppConstants.CACHE_AIDS.size()>0){

                    FirstAidAdapter firstAidAdapter = new FirstAidAdapter(context, AppConstants.CACHE_AIDS);
                    mAidList=AppConstants.CACHE_AIDS;
                    mAidRecyclerView.setAdapter(firstAidAdapter);
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void GetFirstAids() {
        //mSwipeRefresh.setRefreshing(true);
        mSafeDoctorService.getFirstAids()
                .enqueue(new Callback<Aid>() {
                    @Override
                    public void onResponse(Call<Aid> call, Response<Aid> response) {

                        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN){
                            startActivity(new Intent(context, FormLogin.class));
                            getActivity().finish();
                        }
                        else if (response.isSuccessful()){
                            mAidList = response.body().getData().getContent();
                            AppConstants.CACHE_AIDS=mAidList;
                            firstAidAdapter = new FirstAidAdapter(context, mAidList);
                            mAidRecyclerView.setAdapter(firstAidAdapter);
                        }
                        mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<Aid> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {
        Aid.Content selectedFirstAid = mAidList.get(position);
        Intent aidIntent = new Intent(context, AidDetail.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("firstaid",selectedFirstAid);
        aidIntent.putExtras(bundle);

       // aidIntent.putExtra(AppConstants.AID_TITLE, selectedFirstAid.getTitle());
/*
        if(position==2){
            selectedFirstAid.setImage("");
            aidIntent.putExtra("firstaid",selectedFirstAid);
        }else{
            aidIntent.putExtra("firstaid",selectedFirstAid);
        }*/


        try{
            startActivity(aidIntent);
        }catch(Exception e){
            Log.e("",e.getMessage());
        }

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    public static void searchitem(String newText){
        if(firstAidAdapter!=null)
            firstAidAdapter.getFilter().filter(newText);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        GetFirstAids();
    }
}
