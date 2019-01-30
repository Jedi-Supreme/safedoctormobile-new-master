package com.safedoctor.safedoctor.Adapter;

import android.bluetooth.BluetoothDevice;
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
 * Created by stevkky on 05/25/2018.
 */

public class PeripheralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BluetoothDevice> items = new ArrayList<BluetoothDevice>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public PeripheralAdapter(Context context, List<BluetoothDevice> items)
    {
        this.items = items;
        ctx = context;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
        public ImageView image;
        public View layout;
        public HealthDevice hd;

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
            final OriginalViewHolder holder = (OriginalViewHolder) view;

            final BluetoothDevice device = items.get(position);

            // displaying text view data
            holder.hd = BluetoothConfigs.getDevice(device.getName());

            if(holder.hd == null)
            {
                return;
            }

            holder.name.setText(holder.hd.getDisplayname() + " " + device.getName().substring(6));




            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //holder.layout.setBackgroundColor(ctx.getResources().getColor(R.color.gray));

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mOnItemClickListener == null) return false;
                    mOnItemClickListener.onItemLongClick(view, items.get(position), position);
                    return true;
                }
            });


            displayImage(holder, device);
        }

    }

    private void displayImage(OriginalViewHolder holder, BluetoothDevice device) {

         holder.image.setImageResource(holder.hd.getImageresource());
        //holder.image.setImageResource(R.drawable.shape_circle);
        //holder.image.setColorFilter(AppConstants.LIST_ROUND_COLOR);


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void clear()
    {
        this.items.clear();
        notifyDataSetChanged();
    }

    public boolean removeDevice(BluetoothDevice device)
    {
       boolean flag =  this.items.remove(device);

       if(flag) {
           notifyDataSetChanged();
       }

       return  flag;
    }

    public boolean canAdd(BluetoothDevice device)
    {
        if(alreadyAdded(device))
        {
            return false;
        }

        if(isMedicalDevice(device))
        {
            return true;
        }

        return false;
    }

    private boolean alreadyAdded(BluetoothDevice device)
    {
       return items.contains(device);
    }

    private boolean isMedicalDevice(BluetoothDevice device)
    {
        if(BluetoothConfigs.getDevice(device.getName()) != null)
        {
            return true;
        }

        return false;

    }



    public interface OnItemClickListener {
        void onItemClick(View view, BluetoothDevice device, int position);
        void onItemLongClick(View view, BluetoothDevice device, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
}
