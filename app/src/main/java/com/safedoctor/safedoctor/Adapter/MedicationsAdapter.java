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

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedication;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class MedicationsAdapter extends RecyclerView.Adapter<MedicationsAdapter.ViewHolder>
{
    private Context ctx;
    private List<Patientprofilemedication> items;
    private MedicationsAdapter.OnClickListener onClickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;


    public void setOnClickListener(MedicationsAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MedicationsAdapter(Context mContext, List<Patientprofilemedication> items) {
        this.ctx = mContext;
        this.items = items;
        selected_items = new SparseBooleanArray();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView drugname, remark, image_letter,createdtime;
        public ImageView image;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            drugname = (TextView) view.findViewById(R.id.drugname);
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
    public MedicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new MedicationsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MedicationsAdapter.ViewHolder holder, final int position) {
        final Patientprofilemedication medication = items.get(position);

        // displaying text view data

        holder.drugname.setText(medication.getDrug().getName());
        holder.remark.setText(medication.getRemarks());
        holder.image_letter.setText(medication.getDrug().getName().substring(0, 1));
        holder.createdtime.setText(AuxUtils.Date.formateDate(medication.getCreatedtime(),"yyyy-MM-dd"));

        holder.lyt_parent.setActivated(selected_items.get(position, false));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, medication, position);
            }
        });

        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, medication, position);
                return true;
            }
        });

        toggleCheckedIcon(holder, position);
        displayImage(holder, medication);

    }

    private void displayImage(MedicationsAdapter.ViewHolder holder, Patientprofilemedication medication) {
        if (medication.getDrug().getPicture() != null && !medication.getDrug().getPicture().isEmpty()) {
            Tools.displayImageRound(ctx, holder.image, medication.getDrug().getPicture());
            holder.image.setColorFilter(null);
            holder.image_letter.setVisibility(View.GONE);
        }
        else
            {
            holder.image.setImageResource(R.drawable.shape_circle);
            holder.image.setColorFilter(medication.color);
            holder.image_letter.setVisibility(View.VISIBLE);
        }
    }

    private void toggleCheckedIcon(MedicationsAdapter.ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public Patientprofilemedication getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        items.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    public interface OnClickListener
    {
        void onItemClick(View view, Patientprofilemedication obj, int pos);

        void onItemLongClick(View view, Patientprofilemedication obj, int pos);
    }
}
