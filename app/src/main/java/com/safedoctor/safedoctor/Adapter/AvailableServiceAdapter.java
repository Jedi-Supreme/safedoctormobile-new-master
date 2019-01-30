package com.safedoctor.safedoctor.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.ServiceAvailabilityContentModel;
import com.safedoctor.safedoctor.R;

import java.util.ArrayList;

/**
 * Created by Alvin on 18/08/2017.
 */

public class AvailableServiceAdapter extends ArrayAdapter {

    ServiceAvailabilityContentModel serviceAvailabilityContentModel;
    private int groupid;
    private ArrayList<ServiceAvailabilityContentModel> servicesAvailable;
    private LayoutInflater inflater;
    private Context context;


    public AvailableServiceAdapter(Activity context, int groupid, int id, ArrayList<ServiceAvailabilityContentModel> servicesAvailable) {
        super(context,id,groupid, servicesAvailable);
        this.groupid = groupid;
        this.servicesAvailable = servicesAvailable;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("SafeAdp", "Construct");

    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        serviceAvailabilityContentModel  = servicesAvailable.get(position);

        TextView docNameTv = (TextView) itemView.findViewById(R.id.doc_name_tv);
        docNameTv.setText(serviceAvailabilityContentModel.getDoctorname());

        TextView dateTv = (TextView) itemView.findViewById(R.id.date_tv);
        dateTv.setText(serviceAvailabilityContentModel.getDate());

        TextView startTimeTv = (TextView) itemView.findViewById(R.id.start_time_tv);
        startTimeTv.setText("Start time: "+serviceAvailabilityContentModel.getStarttime());

        TextView endTimeTv = (TextView) itemView.findViewById(R.id.end_time_tv);
        endTimeTv.setText("End time: "+serviceAvailabilityContentModel.getEndtime());

        Log.i("SafeAdp", "FirstCall");

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getView(position, convertView, parent);
    }
}
