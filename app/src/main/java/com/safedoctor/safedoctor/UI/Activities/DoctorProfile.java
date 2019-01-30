package com.safedoctor.safedoctor.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.CircleTransform;


public class DoctorProfile extends AppCompatActivity {
    private TextView doctorName, doctorJobTitle;
    private ImageView doctorImageView;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doctorImageView = findViewById(R.id.profile_doctor_image_thumbnail);
        doctorName = findViewById(R.id.profile_doctor_fullname);
        doctorJobTitle = findViewById(R.id.profile_doctor_job_title);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.added_favorites_txt, Snackbar.LENGTH_LONG).show();
            }
        });
        Bundle data = getIntent().getExtras();
        if (data != null){

            Glide.with(DoctorProfile.this)
                    .load(data.getString(AppConstants.DOCTOR_IMG_URL))
                    .transform(new CircleTransform(DoctorProfile.this))
                    .crossFade()
                    .into(doctorImageView);
/*
            Glide.with(DoctorProfile.this)
                    .load(data.getString(AppConstants.DOCTOR_IMG_URL))
                    .apply(new RequestOptions()
                            .transform(new CircleTransform(DoctorProfile.this)))
                    .transition(new DrawableTransitionOptions()
                            .crossFade())
                    .into(doctorImageView);*/
            doctorName.setText(data.getString(AppConstants.DOCTOR_FULL_NAME));
            doctorJobTitle.setText(data.getString(AppConstants.DOCTOR_JOB_TITLE));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
