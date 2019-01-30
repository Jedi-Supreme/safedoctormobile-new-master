package com.safedoctor.safedoctor.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.ServiceContentModel;
import com.safedoctor.safedoctor.R;

import java.util.ArrayList;

/**
 * Created by kwabena on 8/17/17.
 */

public class ServiceAdapter extends ArrayAdapter {
    ServiceContentModel service;
    private int groupid;
    private ArrayList<ServiceContentModel> services;
    private LayoutInflater inflater;
    private Context context;

    public ServiceAdapter(Activity context, int groupid,int id, ArrayList<ServiceContentModel> services) {
        super(context,groupid, services);
        this.services = services;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
        this.context = context;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        service = services.get(position);
        TextView textView = (TextView) itemView.findViewById(R.id.service_name);
        textView.setText(service.getName());

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        service = services.get(position);
        TextView textView = (TextView) itemView.findViewById(R.id.service_name);
        textView.setText(service.getName());

        return itemView;
    }
}
