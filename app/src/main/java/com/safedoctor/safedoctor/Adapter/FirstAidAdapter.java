package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.HealthPost;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.R;

import java.util.ArrayList;
import java.util.List;



public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.MyViewHolder> implements Filterable{
    private List<Aid.Content> mAidList=new ArrayList<>();
    private List<Aid.Content> mAidListfiltered=new ArrayList<>();
    private Context mContext;

    public FirstAidAdapter(Context context, List<Aid.Content> aidList) {
        mContext = context;
        mAidList = aidList;
        mAidListfiltered=aidList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView firstAidTitle;
        private ImageView firstAidImageUrl;

        private MyViewHolder(View view) {
            super(view);
            firstAidTitle = (TextView) view.findViewById(R.id.firstAidTitleLabel);
            firstAidImageUrl = (ImageView) view.findViewById(R.id.firstAidImage);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.first_aid_row_item, parent, false);
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Aid.Content aid = mAidListfiltered.get(position);
        holder.firstAidTitle.setText(aid.getTitle());

        if (aid.getImage() != null) {
            if(aid.getImage().length()>0){
                Tools.displayImageRound(mContext, holder.firstAidImageUrl, aid.getImage());
            }

        }
    }

    @Override
    public int getItemCount() {
        return mAidListfiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mAidListfiltered = mAidList;
                } else {
                    List<Aid.Content> filteredList = new ArrayList<>();
                    for (Aid.Content row : mAidList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getContent().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mAidListfiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mAidListfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mAidListfiltered = (ArrayList<Aid.Content>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}








