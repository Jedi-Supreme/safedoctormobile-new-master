package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Model.responses.OtherBasicObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/22/2017.
 */

public class OtherBasicObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<OtherBasicObject> items = new ArrayList<OtherBasicObject>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, OtherBasicObject obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public OtherBasicObjectAdapter(Context context, List<OtherBasicObject> items) {
        this.items = items;
        ctx = context;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,image_letter,info;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            image_letter = (TextView) v.findViewById(R.id.image_letter);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            info=(TextView)v.findViewById(R.id.info);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basicobject, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            OtherBasicObject p = items.get(position);
            view.name.setText(p.getName());
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

            if (p.slotcount != null) {

                if(p.slotcount!=0){
                    view.info.setText("Available slots : "+p.slotcount);
                    view.info.setVisibility(View.VISIBLE);
                }else{
                    view.info.setVisibility(View.GONE);
                }

            }else{
                view.info.setVisibility(View.GONE);
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
