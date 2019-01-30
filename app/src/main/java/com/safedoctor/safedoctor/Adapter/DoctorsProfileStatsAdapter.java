package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Model.responses.ReviewBlock;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/13/17.
 */

public class DoctorsProfileStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<ReviewBlock> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ReviewBlock obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DoctorsProfileStatsAdapter(Context context, List<ReviewBlock> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title,image_letter,totalnumber;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            image_letter = (TextView) v.findViewById(R.id.image_letter);
            totalnumber = (TextView) v.findViewById(R.id.totalnumber);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctors_profile_stats, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder)
        {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            ReviewBlock p = items.get(position);
            view.title.setText(p.getName());
            view.image_letter.setText(String.format("%.2f", p.getPercentage()) + "%");
            view.totalnumber.setText(p.getTotalnumber() + AuxUtils.getPlural(p.getTotalnumber() ," Review"));
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            displayImage(view, p);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void displayImage(OriginalViewHolder holder, ReviewBlock block)
    {
        holder.image.setImageResource(R.drawable.shape_circle);
        holder.image.setColorFilter(AuxUtils.getRatingColor(block.getPercentage()));
    }
}
