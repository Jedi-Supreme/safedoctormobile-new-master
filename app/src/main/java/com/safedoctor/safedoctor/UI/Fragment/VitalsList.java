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
import com.safedoctor.safedoctor.Adapter.VitalStatsDevicesAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.ActivityVitalStatistics;
import com.safedoctor.safedoctor.UI.Activities.AidDetail;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.ClickListener;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.RecyclerTouchListener;
import com.safedoctor.safedoctor.bluetooth.BluetoothConfigs;
import com.safedoctor.safedoctor.bluetooth.HealthDevice;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 07/08/2018.
 */

public class VitalsList extends Fragment implements  SwipeRefreshLayout.OnRefreshListener
{

    private RecyclerView mvitalRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private SafeDoctorService mSafeDoctorService;
    private  VitalStatsDevicesAdapter madapter;

    public static Fragment newInstance() {
        VitalsList fragment = new VitalsList();
        return  fragment;
    }

    public VitalsList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSafeDoctorService = ApiUtils.getAPIService();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vitalstatistics, container, false);
        mvitalRecyclerView = (RecyclerView)  rootView.findViewById(R.id.vitalstatsRecyclerView);
        mvitalRecyclerView.setLayoutManager(new LinearLayoutManager(context));

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

               fillList();

            }
        });
    }


    private VitalStatsDevicesAdapter.OnItemClickListener clickListener = new VitalStatsDevicesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, HealthDevice device, int position) {


            Intent intent = new Intent(getActivity(), ActivityVitalStatistics.class);
            intent.putExtra("device",device);
            startActivity(intent);

            //Toast.makeText(getActivity(),device.getVitalname() + " clicked",Toast.LENGTH_LONG).show();
        }

    };

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);

        fillList();

    }

    private void fillList()
    {
        madapter = new VitalStatsDevicesAdapter(context, BluetoothConfigs.getSupportedDevicesList());

        mvitalRecyclerView.setAdapter(madapter);
        madapter.setOnItemClickListener(clickListener);
        mSwipeRefresh.setRefreshing(false);
    }

}
