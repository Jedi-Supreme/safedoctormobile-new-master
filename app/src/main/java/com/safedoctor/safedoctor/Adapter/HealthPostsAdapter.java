package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.safedoctor.safedoctor.Model.HealthPost;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.ArrayList;
import java.util.List;


public class HealthPostsAdapter extends RecyclerView.Adapter<HealthPostsAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private List<HealthPost.Content> mHealthPostList;
    private List<HealthPost.Content> mHealthPostListfiltered;


    public HealthPostsAdapter(Context context, List<HealthPost.Content> healthPostList) {
        this.context = context;
        this.mHealthPostListfiltered = healthPostList;
        this.mHealthPostList=healthPostList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_post_row, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HealthPost.Content healthPost = mHealthPostListfiltered.get(position);
        holder.title.setText(healthPost.getTitle());
        holder.createdAt.setText("Added "+ AuxUtils.Date.formateDate(healthPost.getCreatetime(),"E, d MMMM, yyyy"));
        holder.tipsThumbnailImageView.setImageDrawable(TextDrawable.builder().buildRound(healthPost.getTitle().charAt(0)+"", ContextCompat.getColor(context,R.color.blue_500)));
        //Tools.getURLThumbnail(context, holder.tipsThumbnailImageView,healthPost.getLinks());
    }

    @Override
    public int getItemCount() {
        return mHealthPostListfiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, createdAt;
        public ImageView tipsThumbnailImageView;

        private MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tipsTitleLabel);
            createdAt = (TextView) view.findViewById(R.id.tipsCreatedAtLabel);
            tipsThumbnailImageView = (ImageView) view.findViewById(R.id.tipsThumbnailImageView);
        }
    }





    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mHealthPostListfiltered = mHealthPostList;
                } else {
                    List<HealthPost.Content> filteredList = new ArrayList<>();
                    for (HealthPost.Content row : mHealthPostList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getLinks().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mHealthPostListfiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mHealthPostListfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mHealthPostListfiltered = (ArrayList<HealthPost.Content>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}








