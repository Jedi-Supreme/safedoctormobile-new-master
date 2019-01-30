package com.safedoctor.safedoctor.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.widget.LineItemDecoration;
import com.safedoctor.safedoctor.Adapter.ProfileStatsSummaryAdapter;
import com.safedoctor.safedoctor.Adapter.ReviewDetailsAdapter;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.Review;
import com.safedoctor.safedoctor.Model.responses.ReviewBlock;
import com.safedoctor.safedoctor.Model.responses.ReviewStatistics;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stevkky on 10/15/17.
 */

public class ActivityProfileStatsDetails extends AppCompatActivity
{
    private SafeDoctorService mSafeDoctorService;
    private ProgressDialog progress;

    private RecyclerView recyclerstats;
    private RecyclerView recyclerView;

    private int questiontypeid, reviewtypeid;
    private  String question;
    private String doctoruserid;

    private ImageView image;
    private TextView image_letter,totalnumber,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_stats_details);

        this.questiontypeid =  getIntent().getIntExtra("questiontypeid", 0);
        this.reviewtypeid =  getIntent().getIntExtra("reviewtypeid", 2);
        this.question =  getIntent().getStringExtra("question");
        this.doctoruserid = getIntent().getStringExtra("doctoruserid");

        iniToolbar();
        initComponent();

        fetchDetails();
    }

    private void iniToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(question);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        mSafeDoctorService = ApiUtils.getAPIService();

        progress = new ProgressDialog(this);
        progress.setMessage("Working... Please wait");

        image = (ImageView) findViewById(R.id.image);
        image_letter = (TextView) findViewById(R.id.image_letter);
        totalnumber = (TextView) findViewById(R.id.totalnumber);
        title = (TextView) findViewById(R.id.title);


        //Review Statistics Recycler
        recyclerstats = (RecyclerView) findViewById(R.id.recyclerstats);
        recyclerstats.setLayoutManager(new LinearLayoutManager(ActivityProfileStatsDetails.this,LinearLayoutManager.VERTICAL, false));
        //recyclerstats.addItemDecoration(new LineItemDecoration(ActivityProfileStatsDetails.this, LinearLayout.VERTICAL));
        recyclerstats.setHasFixedSize(true);


        //Recycler for the other stuffs
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityProfileStatsDetails.this,LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LineItemDecoration(ActivityProfileStatsDetails.this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchDetails()
    {
        Map<String, String> query =  new HashMap<String, String>();
        query.put("reviewtypeid",String.valueOf(reviewtypeid));
        query.put("questiontypeid",String.valueOf(questiontypeid));

        progress.show();
        Call<SwagArrayResponseModel<List<ReviewStatistics>>> call = mSafeDoctorService.getReviewStatsDetails(TokenString.tokenString, doctoruserid,query);
        Log.i("Safe", "profile URL is " + mSafeDoctorService.getReviewStatsDetails(TokenString.tokenString, doctoruserid,query).request().url().toString());
        call.enqueue(new Callback<SwagArrayResponseModel<List<ReviewStatistics>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ReviewStatistics>>> call, Response<SwagArrayResponseModel<List<ReviewStatistics>>> response) {
                Log.i("Safe", "Load stats details Called");
                progress.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please re-login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                } else if (response.isSuccessful()) {

                    SwagArrayResponseModel<List<ReviewStatistics>> data = response.body();
                    if(data.getData() != null && data.getData().size() > 0) {
                        setItems(data.getData().get(0));
                    }

                }
                else {
                    Log.i("Safe", "Fetching fee error code " + response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ReviewStatistics>>> call, Throwable t) {
                Log.i("SafeRes","Fetching error"+  t.getMessage());
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });


    }

    private void setItems(ReviewStatistics stats)
    {

        if(stats.getSummary().getTotalnumber() > 0)
        {

            image.setImageResource(R.drawable.shape_circle);
            image.setColorFilter(AuxUtils.getRatingColor(stats.getSummary().getPercentage()));
            title.setText(stats.getReviewtype());
            image_letter.setText(String.format("%.2f", stats.getSummary().getPercentage()) + "%");
            totalnumber.setText(stats.getSummary().getTotalnumber() +  AuxUtils.getPlural(stats.getSummary().getTotalnumber()," Review"));

        }

        ProfileStatsSummaryAdapter statsAdapter = new ProfileStatsSummaryAdapter(ActivityProfileStatsDetails.this,stats.getDetails());
        recyclerstats.setAdapter(statsAdapter);

        ReviewDetailsAdapter madapter = new ReviewDetailsAdapter(ActivityProfileStatsDetails.this,combineDetails(stats));
        recyclerView.setAdapter(madapter);
    }

    private List<Review> combineDetails(ReviewStatistics stats)
    {
        List<Review> lsts = new ArrayList<Review>();

        for(ReviewBlock block : stats.getDetails())
        {
            lsts.addAll(block.getDetails());
        }

        Collections.sort(lsts, new Comparator<Review>() {
            @Override
            public int compare(Review left, Review right) {
                if(right.getReviewdate()!=null){
                    return right.getReviewdate().compareToIgnoreCase(left.getReviewdate()); // descending order
                }
                return 0;

            }
        });

        return lsts;
    }
}
