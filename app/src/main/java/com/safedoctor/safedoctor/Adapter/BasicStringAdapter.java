package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/20/2017.
 * This is my main adapter i use for the dropdowns which are not based on spinner but appcompat text editor
 */


public class BasicStringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<BasicObject> items = new ArrayList<BasicObject>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    private Integer info;
    private Infobtnclick infobtnclicklistener;

    public interface OnItemClickListener {
        void onItemClick(View view, BasicObject obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public BasicStringAdapter(Context context, List<BasicObject> items) {
        this.items = items;
        ctx = context;
    }

    public BasicStringAdapter(Context context, List<BasicObject> items,Integer info) {
        this.items = items;
        ctx = context;
        this.info=info;
    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,infobtn;
        public TextView name,image_letter,infotxt1,infotxt2,otherinfo;
        public View lyt_parent,infobox;

        public OriginalViewHolder(View v) {
            super(v);
            infobtn = (ImageView) v.findViewById(R.id.infobtn);
            name = (TextView) v.findViewById(R.id.infotxt);
            infotxt1 = (TextView) v.findViewById(R.id.infotxt1);
            infotxt2 = (TextView) v.findViewById(R.id.infotxt2);
            infobox=v.findViewById(R.id.infobox);
            otherinfo=v.findViewById(R.id.otherinfo);


            //image_letter = (TextView) v.findViewById(R.id.image_letter);
            //lyt_parent = (View) v.findViewById(R.id.lyt_parent);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_online_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    public void setOnInfoClickListener(Infobtnclick infobtnclicklistener){
        this.infobtnclicklistener=infobtnclicklistener;
    }

    public interface Infobtnclick{
        void onclick(int position);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            BasicObject p = items.get(position);
            if(p.getName()!=null){
                view.name.setText(formatString(p.getName()));
            }else{
                view.name.setVisibility(View.GONE);
            }

            if(p.getInfo()!=null){
                view.infotxt1.setText(formatString(p.getInfo()));
            }else{
                view.infotxt1.setVisibility(View.GONE);
            }
            if(p.getInfo1()!=null){
                view.infotxt2.setText(formatString(p.getInfo1()));
            }else{
                view.infotxt2.setVisibility(View.GONE);
            }

            if(info!=null){
                view.infobox.setVisibility(View.VISIBLE);
                view.otherinfo.setVisibility(View.GONE);
                view.infobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(infobtnclicklistener!=null){
                            infobtnclicklistener.onclick( position);
                        }

                    }
                });


                if(p.getInfo1()!=null){
                    view.infotxt2.setText("GHS "+String.format( "%.2f", Double.valueOf(p.getInfo1() )) );
                }else{
                    view.infotxt2.setVisibility(View.GONE);
                }

                //view.otherinfo.setText(p.in);
            }

            /*
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
            });*/



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

    private String formatString(String input){
        String result=input.toLowerCase();
        //Log.e("formatString",result);

        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

}
