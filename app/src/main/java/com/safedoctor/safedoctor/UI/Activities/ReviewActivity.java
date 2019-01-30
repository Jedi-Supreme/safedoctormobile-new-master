package com.safedoctor.safedoctor.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.ReviewAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.Review;
import com.safedoctor.safedoctor.Model.responses.ReviewQuestionsOut;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Reviewquestion;
import com.safedoctor.safedoctor.Model.responses.Reviewtype;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.StringUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

public class ReviewActivity extends AppCompatActivity {

    private SafeDoctorService mSafeDoctorService;
    private LinearLayout reviewlayout;
    private RecyclerView recyclerView;
    private Common common;
    private List<Reviewtype> reviewtypeList=new ArrayList<>();
    private List<Reviewquestion> reviewquestionList=new ArrayList<>();
    private List<Reviewanswer> reviewanswerList=new ArrayList<>();
    private List<String> reviewanswerStringList=new ArrayList<>();
    private ArrayList<String> typelist=new ArrayList<>();
    private ProgressDialog progress;
    private SessionManagement sessionManagement=new SessionManagement(context);
    private AppCompatEditText reviewtypetxt;
    private AlertDialog dialog;
    private int reviewtypeid;
    private ReviewAdapter reviewAdapter;
    private Button retrybtn;
    private boolean isAnswersFetched=false;
    private String TAG="ReviewActivity";
    private AVLoadingIndicatorView avi;
    private AppCompatEditText reviewtxt;
    private Button submitbtn;
    private int reviewTypeid=0;
    private UserAccount doctor=null;
    private int savedReviewsCount=0;
    private List<Review> finalReviewList=new ArrayList<>();
    private OnReviewListSaved onReviewListSaved;


    public int getReviewtypeid() {
        return reviewtypeid;
    }

    public void setReviewtypeid(int reviewtypeid) {
        this.reviewtypeid = reviewtypeid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_sheet);

        if(getIntent().getExtras()!=null)
        {
            doctor = (UserAccount) getIntent().getExtras().getSerializable("Doctor");
            String reviewtype=getIntent().getExtras().getString("Reviewtype",null);
            if(reviewtype!=null){
                reviewTypeid=2;
            }
        }

        init();
    }

    private void init(){
        mSafeDoctorService= ApiUtils.getAPIService();
        reviewlayout=(LinearLayout)findViewById(R.id.reviewlayout);
        reviewlayout.setVisibility(View.GONE);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        progress = new ProgressDialog(this);
        progress.setMessage("Working... Please wait");
        progress.setCancelable(false);
        reviewtypetxt=(AppCompatEditText)findViewById(R.id.reviewtype);
        reviewtypetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typelist.size()>0){
                    showReviewType();
                }else {
                    getReviewTypes();
                }
            }
        });
        retrybtn=(Button)findViewById(R.id.retryquestion);
        retrybtn.setVisibility(View.GONE);
        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrybtn.setVisibility(View.GONE);
                avi.show();
                getReviewQuestions(getReviewtypeid());
            }
        });
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.show();
        reviewtxt=(AppCompatEditText)findViewById(R.id.reviewtxt);
        submitbtn=(Button)findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savingReviews();
            }
        });
        setReviewtypeid(reviewTypeid);

        getReviewAnswers();
        if(doctor!=null){
            getReviewQuestions(getReviewtypeid());
            reviewtypetxt.setText(StringUtils.join(" ",doctor.getFirstname(),doctor.getLastname(),doctor.getLastname()));

        }else{
                getReviewTypes();
        }
        reviewtxt=(AppCompatEditText)findViewById(R.id.reviewtxt);


    }

    public void savingReviews(){
        finalReviewList=reviewAdapter.getReview();

        progress.show();

        if(finalReviewList.size()>0){
            for (Review review:finalReviewList) {
                if(!TextUtils.isEmpty(reviewtxt.getText().toString())){
                    review.setRemarks(reviewtxt.getText().toString());
                    submitReview(review);
                }else{
                    submitReview(review);
                }

            }
        }


        //progress.dismiss();

       Toast.makeText(getApplicationContext(),"Saving all reviews...",Toast.LENGTH_LONG).show();
       //startActivity(new Intent(getApplicationContext(),ActivityDoctorsProfile.class));
        //moveTaskToBack(true);
        //finish();


    }

    public void submitReview(Review review){

        Call<SwagArrayResponseModel<List<Review>>> call = mSafeDoctorService.saveReview(review,TokenString.tokenString);
        call.enqueue(new Callback<SwagArrayResponseModel<List<Review>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<Review>>> call, Response<SwagArrayResponseModel<List<Review>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    sessionManagement.logoutSessionExpired();
                    finish();

                }
                else if (response.isSuccessful() && response.code()==200)
                {
                    progress.dismiss();
                    ++savedReviewsCount;
                    Log.e("savedReviewsCount",""+savedReviewsCount);
                    List<Review> responsedata;
                    responsedata = response.body().getData();
                    Log.e(TAG,"submitReview saved");

                    if(savedReviewsCount>=1){
                        //onReviewListSaved.onSaved(true);

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",true);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }


                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<Review>>> call, Throwable throwable)
            {
                //listener.onTaskCompleted(false);
                progress.dismiss();
                Log.e(TAG," "+throwable.getMessage());
                //Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });

    }



    public void getReviewTypes(){

        progress.show();
        Call<SwagResponseModel<List<Reviewtype>>> call = mSafeDoctorService.getReviewTypes(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<Reviewtype>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Reviewtype>>> call, Response<SwagResponseModel<List<Reviewtype>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    sessionManagement.logoutSessionExpired();
                    finish();

                }
                else if (response.isSuccessful() && response.code()==200)
                {
                    progress.dismiss();
                    DataModel<List<Reviewtype>> responsedata;
                    responsedata = response.body().getData();
                    if(responsedata != null )
                    {
                        typelist=new ArrayList<>();
                        reviewtypeList=responsedata.getContent();

                        for (Reviewtype type:reviewtypeList) {
                            typelist.add(type.getName());
                            if(type.getId()==reviewTypeid){
                                reviewtypetxt.setText(type.getName());
                            }

                        }

                        showReviewType();

                    }
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Reviewtype>>> call, Throwable throwable)
            {
                //listener.onTaskCompleted(false);
                Log.e(TAG," "+throwable.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showReviewType(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
        builder.setTitle("What would you like to review?");

        String[] typeArray = new String[typelist.size()];
        typeArray = typelist.toArray(typeArray);

        int checkedItem = 1;
        builder.setSingleChoiceItems(typeArray, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                Reviewtype reviewtype=reviewtypeList.get(position);
                reviewtypetxt.setText(reviewtype.getName());
                //Toast.makeText(context,"You selected "+reviewtype.getName(),Toast.LENGTH_LONG).show();
                setReviewtypeid(reviewtype.getId());
                Log.e(TAG,"showReviewType typeid selected "+getReviewtypeid());

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG,"showReviewType getting questions for typeid "+getReviewtypeid());
                getReviewQuestions(getReviewtypeid());
            }
        });
        builder.setNegativeButton("Cancel", null);
         dialog = builder.create();
         dialog.setCancelable(false);
         dialog.show();
    }

    public void getReviewQuestions(final int reviewtypeid){

        Call<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>> call = mSafeDoctorService.getReviewQuestions(reviewtypeid,TokenString.tokenString);
        call.enqueue(new Callback<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>> call, Response<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    sessionManagement.logoutSessionExpired();
                    finish();
                }
                else if (response.isSuccessful() && response.code()==200)
                {
                    SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>> responsedata=response.body();

                    List<Reviewquestion> reviewquestionList=new ArrayList<>();
                    if(responsedata != null )
                    {
                        List<ReviewQuestionsOut<List<Reviewquestion>>> reviewQuestionsOutList=responsedata.getData();
                        ReviewQuestionsOut<List<Reviewquestion>> out=reviewQuestionsOutList.get(0);
                        //List<Reviewquestion> reviewquestions=out.getQuestions().get(0);
                        for (Reviewquestion reviewquestion:out.getQuestions()) {
                            reviewquestionList.add(reviewquestion);


                        }

                        if(reviewquestionList!=null || reviewquestionList.size()!=0){
                            if(isAnswersFetched){
                                Log.e(TAG,"getReviewQuestions isSuccessful "+reviewquestionList.size()+"id : "+reviewtypeid);
                                reviewAdapter=new ReviewAdapter(getApplicationContext(),reviewquestionList,reviewanswerStringList,reviewanswerList,ReviewActivity.this,reviewTypeid,doctor.getId());
                                recyclerView.setAdapter(reviewAdapter);
                                reviewAdapter.notifyDataSetChanged();
                                avi.hide();
                                reviewlayout.setVisibility(View.VISIBLE);


                            }else{
                                getReviewAnswers();
                                Log.e(TAG,"getReviewQuestions isSuccessful "+reviewquestionList.size()+" now fetching answers");
                            }
                        }else{
                            reviewlayout.setVisibility(View.GONE);
                            Toast.makeText(context,"Questions not set yet.",Toast.LENGTH_LONG).show();
                        }

                        //Log.e(TAG," "+reviewQuestionsOut);
                    }
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>> call, Throwable throwable)
            {
                Log.e(TAG,"getReviewQuestions onFailure"+throwable.getMessage());
                retrybtn.setVisibility(View.VISIBLE);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void getReviewAnswers(){
        Call<SwagResponseModel<List<Reviewanswer>>> call = mSafeDoctorService.getReviewAnswers(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<Reviewanswer>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Reviewanswer>>> call, Response<SwagResponseModel<List<Reviewanswer>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    sessionManagement.logoutSessionExpired();
                    finish();
                }
                else if (response.isSuccessful() && response.code()==200)
                {
                    DataModel<List<Reviewanswer>> responsedata;
                    responsedata = response.body().getData();
                    if(responsedata != null )
                    {
                        if(reviewquestionList.size()==0){
                            reviewanswerStringList=new ArrayList<>();
                            isAnswersFetched=true;
                            reviewanswerList = responsedata.getContent();
                            for (Reviewanswer answer:reviewanswerList) {
                                reviewanswerStringList.add(answer.getAnswer());

                            }
                           // Toast.makeText(getApplicationContext(),"Answers fetched",Toast.LENGTH_LONG).show();
                            Log.e(TAG,"getReviewAnswers isSuccessful "+"Answers fetched. Waiting for questions");
                        }else{
                            reviewAdapter=new ReviewAdapter(getApplicationContext(),reviewquestionList,reviewanswerStringList,reviewanswerList,ReviewActivity.this,reviewTypeid,doctor.getId());
                            recyclerView.setAdapter(reviewAdapter);
                            reviewAdapter.notifyDataSetChanged();
                            avi.hide();
                            reviewlayout.setVisibility(View.VISIBLE);
                            Log.e(TAG,"getReviewAnswers isSuccessful "+"Answers refetched.Binding to view");
                        }

                    }

                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Reviewanswer>>> call, Throwable throwable)
            {
                Log.e(TAG,"getReviewAnswers onFailure "+throwable.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public interface OnReviewListSaved{
        public void onSaved(boolean issaved);
    }

    public void setOnReviewListSavedListener(OnReviewListSaved onReviewListSaved){
        this.onReviewListSaved=onReviewListSaved;
    }






}
