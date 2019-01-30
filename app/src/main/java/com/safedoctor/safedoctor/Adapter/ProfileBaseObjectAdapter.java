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
import com.safedoctor.safedoctor.Model.responses.ProfileBasicObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/13/17.
 */

public class ProfileBaseObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<ProfileBasicObject> items = new ArrayList<ProfileBasicObject>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ProfileBasicObject obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public ProfileBaseObjectAdapter(Context context, List<ProfileBasicObject> items) {
        this.items = items;
        ctx = context;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
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
            //lyt_checked = (RelativeLayout) view.findViewById(R.id.lyt_checked);
           // lyt_image = (RelativeLayout) view.findViewById(R.id.lyt_image);
            lyt_parent = (View) view.findViewById(R.id.lyt_parent);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profilestuff_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            ProfileBasicObject p = items.get(position);
            view.from.setText(p.getName());
            view.email.setText(p.getOthername());
            view.relationship.setText(p.getOtherid());

            if(p.getName() != null && p.getName().length() > 1) {
                view.image_letter.setText(p.getName().substring(0, 1));
            }

            if(p.image !=null && !p.image.isEmpty()) {
                Tools.displayImageRound(ctx, view.image, p.image);
                view.image_letter.setVisibility(View.GONE);
            }
            else
            {
                view.image.setImageResource(R.drawable.shape_circle);
                view.image.setColorFilter(p.color);
                view.image_letter.setVisibility(View.VISIBLE);
            }
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            if(view.relationship.getText() == null || view.relationship.getText().toString().isEmpty())
            {
                view.relationship.setVisibility(View.GONE);
            }

            if(view.email.getText() == null || view.email.getText().toString().isEmpty())
            {
                view.email.setVisibility(View.GONE);
            }

            if(view.phonenumber.getText() == null || view.phonenumber.getText().toString().isEmpty())
            {
                view.phonenumber.setVisibility(View.GONE);
            }


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
