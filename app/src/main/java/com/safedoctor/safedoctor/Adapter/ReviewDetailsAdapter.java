package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Model.responses.Review;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/15/17.
 */

public class ReviewDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<Review> items = new ArrayList<Review>();

    private Context ctx;

    public ReviewDetailsAdapter(Context context, List<Review> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView question,answer,date,patient,location,comments;


        public OriginalViewHolder(View v) {
            super(v);
            question = (TextView) v.findViewById(R.id.question);
            answer = (TextView) v.findViewById(R.id.answer);
            date = (TextView) v.findViewById(R.id.date);
            patient = (TextView) v.findViewById(R.id.patient);
            location = (TextView) v.findViewById(R.id.location);
            comments = (TextView) v.findViewById(R.id.comments);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_details, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder)
        {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Review p = items.get(position);
            view.question.setText(p.getQuestion().getQuestion());
            view.answer.setText(p.getReviewanswer().getAnswer());
            view.date.setText(AuxUtils.Date.formateDate(p.getReviewdate(),"d MMMM, yyyy"));
            view.patient.setText(StringUtils.join(" ",p.getPatient().getFirstname(), p.getPatient().getMiddlename(), p.getPatient().getLastname()) );
            view.location.setText("");
            view.comments.setText(p.getRemarks());

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
