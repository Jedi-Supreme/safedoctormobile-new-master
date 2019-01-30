package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.bluetooth.BluetoothConfigs;
import com.safedoctor.safedoctor.bluetooth.HealthDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 07/08/2018.
 */

public class VitalStatsDevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<HealthDevice> items = new ArrayList<HealthDevice>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public VitalStatsDevicesAdapter(Context context, List<HealthDevice> items)
    {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
        public ImageView image;
        public View layout;

        public OriginalViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.image);
            layout = view.findViewById(R.id.layout);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.peripheral_item, parent, false);
        return new OriginalViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view, final int position) {

        if (view instanceof OriginalViewHolder)
        {
            OriginalViewHolder holder = (OriginalViewHolder) view;

            final HealthDevice device = items.get(position);

            holder.name.setText(device.getVitalname());




            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            displayImage(holder, device);
        }

    }

    private void displayImage(OriginalViewHolder holder, HealthDevice device) {

        holder.image.setImageResource(device.getImageresource());
        //holder.image.setImageResource(R.drawable.shape_circle);
        //holder.image.setColorFilter(AppConstants.LIST_ROUND_COLOR);


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, HealthDevice device, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
}
