package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Model.responses.ReviewBlock;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/15/17.
 */

public class ProfileStatsSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<ReviewBlock> items = new ArrayList<>();

    private Context ctx;

    public ProfileStatsSummaryAdapter(Context context, List<ReviewBlock> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress;
        public TextView answertype,totalreviews,percentage;


        public OriginalViewHolder(View v) {
            super(v);
            progress = (ProgressBar) v.findViewById(R.id.progress);
            answertype = (TextView) v.findViewById(R.id.answertype);
            totalreviews = (TextView) v.findViewById(R.id.totalreviews);
            percentage = (TextView) v.findViewById(R.id.percentage);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_stats_detail, parent, false);
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
            view.answertype.setText(p.getName());
            view.percentage.setText(String.format("%.2f", p.getPercentage()) + "%");
            view.totalreviews.setText(p.getTotalnumber() + AuxUtils.getPlural(p.getTotalnumber()," Review"));

            view.progress.setProgress((int)Math.round(p.getPercentage()));
            setProgressColor(view,p.getTypeid());

        }
    }

    private void setProgressColor(OriginalViewHolder view , int questiontypeid)
    {
        int color = AuxUtils.getAnswerTypeColor(questiontypeid);

        LayerDrawable progressBarDrawable = new LayerDrawable(
                new Drawable[]{
                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[]{Color.parseColor("#fff7f7f7"),Color.parseColor("#fff7f7f7")}),

                        new ClipDrawable(
                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("#fff7f7f7"),Color.parseColor("#fff7f7f7")}),
                                Gravity.START,
                                ClipDrawable.HORIZONTAL),

                        new ClipDrawable(
                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{color,color}),
                                Gravity.START,
                                ClipDrawable.HORIZONTAL)
                });

        progressBarDrawable.setId(0,android.R.id.background);
        progressBarDrawable.setId(1,android.R.id.secondaryProgress);
        progressBarDrawable.setId(2,android.R.id.progress);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25);

      view.progress.setProgressDrawable(progressBarDrawable);
        view.progress.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
