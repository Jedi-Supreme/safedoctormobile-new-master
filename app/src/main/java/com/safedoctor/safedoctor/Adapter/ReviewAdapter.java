package com.safedoctor.safedoctor.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Model.responses.Review;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Reviewquestion;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getNow;

/**
 * Created by beulahana on 10/01/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<Reviewquestion> reviewquestionList;
    private List<Reviewanswer> reviewanswerList;
    private List<String> reviewanswerStringList;
    private Activity activity;
    private List<Review> reviewList=new ArrayList<>();
    private String TAG=ReviewAdapter.class.getSimpleName();
    private Integer reviewtypeid=0;private String revieweeid;
    private String reviewdate = getNow();

    public ReviewAdapter(Context context, List<Reviewquestion> reviewquestionList,List<String> reviewanswerStringList,List<Reviewanswer> reviewanswerList, Activity activity,Integer reviewtypeid,String revieweeid) {
        this.context = context;
        this.reviewquestionList = reviewquestionList;
        this.reviewanswerList=reviewanswerList;
        this.reviewanswerStringList=reviewanswerStringList;
        this.activity=activity;
        this.reviewtypeid=reviewtypeid;
        this.revieweeid=revieweeid;
        this.reviewList=new ArrayList<>();
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_question_item, parent, false);

        return new ReviewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Reviewquestion reviewquestion=reviewquestionList.get(position);
        holder.questiontxt.setText(reviewquestion.getQuestion());
        holder.answertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"question position "+position);
                showAnswers((AppCompatEditText)view,reviewquestion.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviewquestionList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView questiontxt;
        private AppCompatEditText answertxt;

        public ViewHolder(View itemView) {
            super(itemView);
            questiontxt=(TextView)itemView.findViewById(R.id.question);
            answertxt=(AppCompatEditText)itemView.findViewById(R.id.reviewanswers);

        }
    }

    private void showAnswers(final AppCompatEditText answertxt, final int questionId){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select answer ");

        String[] answerArray = new String[reviewanswerStringList.size()];
        answerArray = reviewanswerStringList.toArray(answerArray);

        int checkedItem = 1;
        builder.setSingleChoiceItems(answerArray, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Reviewanswer reviewanswer=reviewanswerList.get(position);

                answertxt.setText(reviewanswer.getAnswer());
                Toast.makeText(context,"You selected "+reviewanswer.getAnswer(),Toast.LENGTH_LONG).show();
                Review review=new Review(reviewanswer.getId(),(Integer)questionId,(Integer)reviewtypeid,
                        Long.valueOf(AppConstants.patientID),
                        null,reviewdate,revieweeid);
                addReview(review);
                //setReviewtypeid(reviewtype.getId());

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //getReviewQuestions(getReviewtypeid());
            }
        });
        builder.setNegativeButton("Cancel", null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void addReview(Review reviewnew){
        boolean isExist=false;
        int oldIndex=0;
        if(reviewList.size()==0){
            reviewList.add(reviewnew);
        }else{
            for (Review reviewold:reviewList) {
                if(reviewold.getQuestionid()==reviewnew.getQuestionid()){
                    oldIndex=reviewList.indexOf(reviewold);
                    isExist=true;
                    break;

                }else{
                    isExist=false;
                }

            }
            if(isExist){
                reviewList.set(oldIndex,reviewnew);
            }else{
                reviewList.add(reviewnew);
            }


        }

    }

    public List<Review> getReview(){
        return reviewList;
    }

}
