package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/12/17.
 */

public class DoctorsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<DoctorOutObj> items = new ArrayList<DoctorOutObj>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private String specialtyname;

    public interface OnItemClickListener {
        void onItemClick(View view, DoctorOutObj obj, int position);
        void onItemLongClick(View view, DoctorOutObj obj, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DoctorsListAdapter(Context context, List<DoctorOutObj> items, String specialtyname) {
        this.items = items;
        ctx = context;
        this.specialtyname = specialtyname;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView from, email, phonenumber, relationship, image_letter;
        public ImageView image;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public OriginalViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.from);
            email = (TextView) view.findViewById(R.id.email);
            phonenumber = (TextView) view.findViewById(R.id.phonenumber);
            relationship = (TextView) view.findViewById(R.id.relationship);
            image_letter = (TextView) view.findViewById(R.id.image_letter);
            image = (ImageView) view.findViewById(R.id.image);
            lyt_checked = (RelativeLayout) view.findViewById(R.id.lyt_checked);
            lyt_image = (RelativeLayout) view.findViewById(R.id.lyt_image);
            lyt_parent = (View) view.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_person, parent, false);
        return new OriginalViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view, final int position) {

        if (view instanceof OriginalViewHolder)
        {
            OriginalViewHolder holder = (OriginalViewHolder) view;

            final DoctorOutObj doctor = items.get(position);

            // displaying text view data

            holder.from.setText(StringUtils.join(" ",doctor.getDoctor().getFirstname(), doctor.getDoctor().getOthername(), doctor.getDoctor().getLastname()) );
            holder.email.setText(doctor.getDoctor().getGendergroup().getName());
            holder.phonenumber.setText(specialtyname);


            holder.relationship.setText("");
            if (holder.from.getText() != null && holder.from.getText().length() > 1) {
                holder.image_letter.setText(holder.from.getText().toString().substring(0, 1));
            }
         //   holder.lyt_parent.setActivated(selected_items.get(position, false));


            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mOnItemClickListener == null) return false;
                    mOnItemClickListener.onItemLongClick(view, items.get(position), position);
                    return true;
                }
            });


            displayImage(holder, doctor);
        }
    }

    private void displayImage(OriginalViewHolder holder, DoctorOutObj doctor) {

        holder.lyt_checked.setVisibility(View.GONE);
        holder.lyt_checked.setVisibility(View.GONE);
        if (doctor.getPicture() != null) {
            Tools.displayImageRound(ctx, holder.image, doctor.getPicture().getPhoto());
            holder.image.setColorFilter(null);
            holder.image_letter.setVisibility(View.GONE);
        } else {
            holder.image.setImageResource(R.drawable.shape_circle);
            holder.image.setColorFilter(AppConstants.LIST_ROUND_COLOR);
            holder.image_letter.setVisibility(View.VISIBLE);
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
}
