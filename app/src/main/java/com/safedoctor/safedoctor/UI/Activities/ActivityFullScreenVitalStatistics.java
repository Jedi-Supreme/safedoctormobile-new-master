package com.safedoctor.safedoctor.UI.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.safedoctor.safedoctor.Model.CapturedSignsList;
import com.safedoctor.safedoctor.Model.Vitalsignscapture;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.bluetooth.DeviceType;
import com.safedoctor.safedoctor.bluetooth.HealthDevice;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by stevkky on 07/10/2018.
 */

public class ActivityFullScreenVitalStatistics  extends AppCompatActivity
{
    private GraphView graph;
    private HealthDevice hdevice;
    private CapturedSignsList listdata;

    private double max = 0;
    private double min =0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreenvitalstatistics);

        hdevice = (HealthDevice) getIntent().getSerializableExtra("device");
        listdata = (CapturedSignsList) getIntent().getSerializableExtra("listdata");

        graph = findViewById(R.id.graph);


        initGraph();

    }


    private void sortByDateList()
    {
        Comparator<Vitalsignscapture> comparator = new Comparator<Vitalsignscapture>() {
            @Override
            public int compare(Vitalsignscapture left, Vitalsignscapture right) {
                return left.getCapturedate().compareToIgnoreCase(right.getCapturedate()); // ascending order

            }
        };

        Collections.sort(listdata.getList(),comparator);


    }
    private void setItemBounds(DeviceType type)
    {

        min = 0;
        max = 0;
        int size = listdata.getList().size();

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
                        min =  Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]);
                        max = Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]);
                    }

                    if( Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]) > max)
                    {
                        max = Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]);
                    }

                    if( min > Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]))
                    {
                        min = Double.parseDouble (listdata.getList().get(i).getResult().split("\\/")[0]);
                    }
                    break;

                case TEMPERATURE:

                    if(i == 0)
                    {
                        min =  Double.parseDouble (listdata.getList().get(i).getResult());
                        max = Double.parseDouble (listdata.getList().get(i).getResult());
                    }

                    if( Double.parseDouble (listdata.getList().get(i).getResult()) > max)
                    {
                        max = Double.parseDouble (listdata.getList().get(i).getResult());
                    }

                    if( min > Double.parseDouble (listdata.getList().get(i).getResult()))
                    {
                        min = Double.parseDouble (listdata.getList().get(i).getResult());
                    }

                    break;


            }
        }


    }

    private void initGraph() {
        if(listdata.getList().size() == 0)
        {
            return;
        }


        setItemBounds(DeviceType.fromValue(hdevice.getPrefix()));

        DataPoint[] points = new DataPoint[listdata.getList().size()];
        DataPoint[] points2 = new DataPoint[listdata.getList().size()];

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{});

        switch (DeviceType.fromValue(hdevice.getPrefix()))
        {
            case TEMPERATURE:
                for (int i = 0; i < points.length; i++)
                {
                    points[i] = new DataPoint(AuxUtils.Date.getCalendarDate(listdata.getList().get(i).getCapturedate()).getTime(), Double.parseDouble(listdata.getList().get(i).getResult()));
                }

                series = new LineGraphSeries<>(points);
                graph.addSeries(series);
                series.setTitle(hdevice.getVitalname()+ " Graph");


                break;
            case BLOODPRESSURE:

                for (int i = 0; i < points.length; i++)
                {
                    String parts[] = listdata.getList().get(i).getResult().split("\\/");
                    points[i] = new DataPoint(AuxUtils.Date.getCalendarDate(listdata.getList().get(i).getCapturedate()).getTime(),Double.parseDouble(parts[0]));
                    points2[i] = new DataPoint(AuxUtils.Date.getCalendarDate(listdata.getList().get(i).getCapturedate()).getTime(),Double.parseDouble(parts[1]));
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



        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(listdata.getList().size());


       graph.getViewport().setMinX(AuxUtils.Date.getCalendarDate(listdata.getList().get(0).getCapturedate()).getTime().getTime());
       graph.getViewport().setMaxX(AuxUtils.Date.getCalendarDate(listdata.getList().get(listdata.getList().size() -1 ).getCapturedate()).getTime().getTime());
       graph.getViewport().setXAxisBoundsManual(true);


        //graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}
