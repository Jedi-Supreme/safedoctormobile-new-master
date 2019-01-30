package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/27/2017.
 */

public class SlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<TimeSlot> items = new ArrayList<TimeSlot>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private String title;

    public interface OnItemClickListener {
        void onItemClick(View view, TimeSlot obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public SlotAdapter(Context context, List<TimeSlot> items, String title) {
        this.items = items;
        ctx = context;
        this.title = title;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView specialtyname,date_tv,start_time_tv,end_time_tv;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            specialtyname = (TextView) v.findViewById(R.id.specialtyname);
            date_tv = (TextView) v.findViewById(R.id.date_tv);
            start_time_tv = (TextView) v.findViewById(R.id.start_time_tv);
            end_time_tv = (TextView) v.findViewById(R.id.end_time_tv);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            TimeSlot p = items.get(position);
            String specialtyname=getSpecialtyName(p.getSpecialityid());
            view.specialtyname.setText(specialtyname.isEmpty()?title:specialtyname);
            view.date_tv.setText("Date: "+AuxUtils.Date.formateDate(p.getDate(),"E, d MMMM, yyyy"));
            view.start_time_tv.setText("Start time: "+AuxUtils.Date.formateTime(p.getStarttime()));
            view.end_time_tv.setText("End time: " + AuxUtils.Date.formateTime(p.getEndtime()));

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    public static String getSpecialtyName(int id){

        for (BasicObject obj:AppConstants.CACHE_SPECIALTIES) {
            if(id==obj.getId()){
                return obj.getName();
            }
        }
        return "";
    }

}
