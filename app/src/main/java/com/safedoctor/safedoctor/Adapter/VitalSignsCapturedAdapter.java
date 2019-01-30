package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.Vitalsignscapture;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.List;

/**
 * Created by stevkky on 08/12/2018.
 */

public class VitalSignsCapturedAdapter extends RecyclerView.Adapter<VitalSignsCapturedAdapter.ViewHolder>
{
    private Context ctx;
    private List<Vitalsignscapture> items;
    private VitalSignsCapturedAdapter.OnClickListener onClickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;


    public void setOnClickListener(VitalSignsCapturedAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VitalSignsCapturedAdapter(Context mContext, List<Vitalsignscapture> items) {
        this.ctx = mContext;
        this.items = items;
        selected_items = new SparseBooleanArray();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView result, remark, image_letter,createdtime;
        public ImageView image;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            result = (TextView) view.findViewById(R.id.drugname);
            remark = (TextView) view.findViewById(R.id.remark);
            image_letter = (TextView) view.findViewById(R.id.image_letter);
            image = (ImageView) view.findViewById(R.id.image);
            lyt_checked = (RelativeLayout) view.findViewById(R.id.lyt_checked);
            lyt_image = (RelativeLayout) view.findViewById(R.id.lyt_image);
            lyt_parent = (View) view.findViewById(R.id.lyt_parent);
            createdtime = (TextView) view.findViewById(R.id.createdtime);
        }
    }

    @Override
    public VitalSignsCapturedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new VitalSignsCapturedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VitalSignsCapturedAdapter.ViewHolder holder, final int position) {
        final Vitalsignscapture capture = items.get(position);

        // displaying text view data

        holder.result.setText(capture.getResult());
        holder.remark.setText(capture.getActualperipheral());
        holder.image_letter.setText(capture.getActualperipheral().substring(0, 1));
        holder.createdtime.setText(AuxUtils.Date.formateDate(capture.getCapturedate(),"yyyy-MM-dd"));

        holder.lyt_parent.setActivated(selected_items.get(position, false));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, capture, position);
            }
        });

        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, capture, position);
                return true;
            }
        });

        //toggleCheckedIcon(holder, position);
        displayImage(holder, capture);

    }

    private void displayImage(VitalSignsCapturedAdapter.ViewHolder holder, Vitalsignscapture capture) {
        holder.image.setImageResource(R.drawable.shape_circle);
        holder.image.setColorFilter(AppConstants.LIST_ROUND_COLOR);
        holder.image_letter.setVisibility(View.VISIBLE);

    }

    public Vitalsignscapture getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnClickListener
    {
        void onItemClick(View view, Vitalsignscapture obj, int pos);

        void onItemLongClick(View view, Vitalsignscapture obj, int pos);
    }

}
