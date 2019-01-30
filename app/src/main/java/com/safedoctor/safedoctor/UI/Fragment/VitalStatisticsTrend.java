package com.safedoctor.safedoctor.UI.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.safedoctor.safedoctor.Adapter.ContactPersonsAdapter;
import com.safedoctor.safedoctor.Adapter.FirstAidAdapter;
import com.safedoctor.safedoctor.Adapter.VitalSignsCapturedAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.Model.CapturedSignsList;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.Peripheral;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.Vitalsignscapture;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityFullScreenVitalStatistics;
import com.safedoctor.safedoctor.UI.Activities.ActivityVitalStatistics;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.RecyclerTouchListener;
import com.safedoctor.safedoctor.bluetooth.DeviceType;
import com.safedoctor.safedoctor.bluetooth.HealthDevice;
import com.safedoctor.safedoctor.widget.LineItemDecoration;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 07/08/2018.
 */

public class VitalStatisticsTrend extends Fragment implements View.OnClickListener,OnTaskCompleted
{

    private HealthDevice device;

    private View root;
    private Common common;
    private SafeDoctorService mSafeDoctorService;
    private RecyclerView recyclerView;
    private GraphView graph;

    private VitalSignsCapturedAdapter mAdapter;

    private ImageView imgFullscreen;

    private HealthDevice hdevice;

    private List<Vitalsignscapture> list = new ArrayList<>();

    private ProgressDialog progress;

    private double max = 0;
    private double min =0;

    public static VitalStatisticsTrend newInstance(HealthDevice device)
    {
        VitalStatisticsTrend vitalStatisticsTrend = new VitalStatisticsTrend();

        Bundle b = new Bundle();
        b.putSerializable("device",device);
        vitalStatisticsTrend.setArguments(b);

        return vitalStatisticsTrend;
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        hdevice =  (HealthDevice) getArguments().getSerializable("device");

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Working. Please wait ...");

        root = inflater.inflate(R.layout.fragment_vitalstatisticstrend, container, false);
        common = new Common(getActivity().getApplicationContext(),this);
        common = new Common(getActivity().getApplicationContext(),this);
        mSafeDoctorService = ApiUtils.getAPIService();

        imgFullscreen = (ImageView) root.findViewById(R.id.imgFullscreen);

        recyclerView = (RecyclerView)  root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        imgFullscreen.setOnClickListener(this);

         graph = (GraphView) root.findViewById(R.id.graph);

        fetchResults();


        return root;
    }

    private void fetchResults()
    {
        progress.show();

        mSafeDoctorService.getAllMyCaptureForDevice(TokenString.tokenString,AppConstants.patientID,hdevice.getPrefix())
                .enqueue(new Callback<SwagArrayResponseModel<List<Vitalsignscapture>>>() {
                    @Override
                    public void onResponse(Call<SwagArrayResponseModel<List<Vitalsignscapture>>> call, Response<SwagArrayResponseModel<List<Vitalsignscapture>>> response) {

                        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN){
                            startActivity(new Intent(context, FormLogin.class));
                            getActivity().finish();
                        }
                        else if (response.isSuccessful()){

                            list = response.body().getData();
                           mAdapter = new VitalSignsCapturedAdapter(getActivity(), list);
                           recyclerView.setAdapter(mAdapter);
                           initGraph();

                        }

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SwagArrayResponseModel<List<Vitalsignscapture>>> call, Throwable throwable) {
                        throwable.printStackTrace();
                        progress.dismiss();
                    }
                });


    }


    private void sortByDateList()
    {
        Comparator<Vitalsignscapture> comparator = new Comparator<Vitalsignscapture>() {
            @Override
            public int compare(Vitalsignscapture left, Vitalsignscapture right) {
                return left.getCapturedate().compareToIgnoreCase(right.getCapturedate()); // ascending order

            }
        };

        Collections.sort(list,comparator);


    }
    private void setItemBounds(DeviceType type)
    {

        min = 0;
        max = 0;
        int size = list.size();

        if(size ==0)
        {
            return;
        }

        sortByDateList();


        for(int i=0;i<size;i++)
        {
           switch (type)
           {
               case BLOODPRESSURE:

                   if(i == 0)
                   {
                       min =  Double.parseDouble (list.get(i).getResult().split("\\/")[0]);
                       max = Double.parseDouble (list.get(i).getResult().split("\\/")[0]);
                   }

                   if( Double.parseDouble (list.get(i).getResult().split("\\/")[0]) > max)
                   {
                       max = Double.parseDouble (list.get(i).getResult().split("\\/")[0]);
                   }

                   if( min > Double.parseDouble (list.get(i).getResult().split("\\/")[0]))
                   {
                       min = Double.parseDouble (list.get(i).getResult().split("\\/")[0]);
                   }
                   break;

               case TEMPERATURE:

                   if(i == 0)
                   {
                       min =  Double.parseDouble (list.get(i).getResult());
                       max = Double.parseDouble (list.get(i).getResult());
                   }

                   if( Double.parseDouble (list.get(i).getResult()) > max)
                   {
                       max = Double.parseDouble (list.get(i).getResult());
                   }

                   if( min > Double.parseDouble (list.get(i).getResult()))
                   {
                       min = Double.parseDouble (list.get(i).getResult());
                   }

                   break;


           }
        }


    }

    private void initGraph() {

        if(list.size() == 0)
        {
            return;
        }

        progress.show();
        setItemBounds(DeviceType.fromValue(hdevice.getPrefix()));

        DataPoint[] points = new DataPoint[list.size()];
        DataPoint[] points2 = new DataPoint[list.size()];

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{});

        switch (DeviceType.fromValue(hdevice.getPrefix()))
        {
            case TEMPERATURE:
                for (int i = 0; i < points.length; i++)
                {
                    points[i] = new DataPoint(AuxUtils.Date.getCalendarDate(list.get(i).getCapturedate()).getTime(), Double.parseDouble(list.get(i).getResult()));
                }

                series = new LineGraphSeries<>(points);
                graph.addSeries(series);
                series.setTitle( hdevice.getVitalname()+ " Graph");

                break;
            case BLOODPRESSURE:

                for (int i = 0; i < points.length; i++)
                {
                    String parts[] = list.get(i).getResult().split("\\/");
                    points[i] = new DataPoint(AuxUtils.Date.getCalendarDate(list.get(i).getCapturedate()).getTime(),Double.parseDouble(parts[0]));

                    points2[i] = new DataPoint(AuxUtils.Date.getCalendarDate(list.get(i).getCapturedate()).getTime(),Double.parseDouble(parts[1]));
                }


                series = new LineGraphSeries<>(points);
                series2 = new LineGraphSeries<>(points2);

                graph.addSeries(series2);
                graph.getSecondScale().addSeries(series);

                series2.setColor(Color.RED);
                graph.getSecondScale().setMinY(min);
                graph.getSecondScale().setMaxY(max);
                graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

                series.setTitle("Systolic Graph");
                series2.setTitle("Diastolic Graph");


                break;
        }


        // enable scaling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);



        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(list.size());

        graph.getViewport().setMinX(AuxUtils.Date.getCalendarDate(list.get(0).getCapturedate()).getTime().getTime());
        graph.getViewport().setMaxX(AuxUtils.Date.getCalendarDate(list.get(list.size() -1 ).getCapturedate()).getTime().getTime());
        graph.getViewport().setXAxisBoundsManual(true);


        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


        progress.dismiss();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imgFullscreen:

                Intent intent = new Intent(getActivity(), ActivityFullScreenVitalStatistics.class);
                intent.putExtra("device",hdevice);
                CapturedSignsList l = new CapturedSignsList();
                l.setList(list);
                intent.putExtra("listdata",l);
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onTaskCompleted(Object result) {

    }
}
