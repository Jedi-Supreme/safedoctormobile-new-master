package com.safedoctor.safedoctor.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.AdapterItemsLstCategory;
import com.safedoctor.safedoctor.Adapter.DoctorsProfileStatsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.ListItemCategory;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.Doctoraccomplishment;
import com.safedoctor.safedoctor.Model.responses.Doctoraward;
import com.safedoctor.safedoctor.Model.responses.Doctorcertificate;
import com.safedoctor.safedoctor.Model.responses.Doctorfacility;
import com.safedoctor.safedoctor.Model.responses.Doctorlanguage;
import com.safedoctor.safedoctor.Model.responses.Doctorskill;
import com.safedoctor.safedoctor.Model.responses.Doctorspecialty;
import com.safedoctor.safedoctor.Model.responses.ProfileBasicObject;
import com.safedoctor.safedoctor.Model.responses.ReviewBlock;
import com.safedoctor.safedoctor.Model.responses.UserProfile;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.DataGenerator;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.StringUtils;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.widget.SpacingItemDecoration;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 10/12/17.
 */

public class ActivityDoctorsProfile extends AppCompatActivity implements OnTaskCompleted
{
    private Common common;
    private SafeDoctorService mSafeDoctorService;
    private ImageView profile_picture;
    private ProgressDialog progress;
    private ImageView avgstats;
    private TextView image_letter,totalnumber;
    private SessionManagement sessionManagement=new SessionManagement(context);

    private DoctorOutObj doctor;

    private RecyclerView recyclerstats;
    private RecyclerView recyclerView;

    private AdapterItemsLstCategory mAdapter;

    private boolean viewonlymode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_doctors_profile);

        this.doctor = (DoctorOutObj) getIntent().getSerializableExtra("doctor");
        this.viewonlymode = getIntent().getBooleanExtra("viewonlymode", false);

        iniToolbar();
        initComponent();

        fetchDoctorProfile();
    }

    private void iniToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if(viewonlymode) {
            toolbar.setTitle(doctor.getDoctor().getFirstname());
        }
        else
        {
            toolbar.setTitle(StringUtils.join(" ", doctor.getDoctor().getTitle().getName(), doctor.getDoctor().getFirstname(), doctor.getDoctor().getOthername(), doctor.getDoctor().getLastname()));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        mSafeDoctorService = ApiUtils.getAPIService();

        common = new Common(getApplicationContext(),this);
        progress = new ProgressDialog(this);
        progress.setMessage("Working... Please wait");

        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        if(doctor.getPicture() != null && !doctor.getPicture().getPhoto().isEmpty())
        {
            byte[] imageBytes = Base64.decode(doctor.getPicture().getPhoto(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profile_picture.setImageBitmap(decodedImage);
        }

        avgstats = (ImageView) findViewById(R.id.avgstats);
        image_letter = (TextView) findViewById(R.id.image_letter);
        totalnumber = (TextView) findViewById(R.id.totalnumber);

        final CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                avgstats.setScaleX(scale >= 0 ? scale : 0);
                avgstats.setScaleY(scale >= 0 ? scale : 0);

                image_letter.setScaleX(scale >= 0 ? scale : 0);
                image_letter.setScaleY(scale >= 0 ? scale : 0);

                totalnumber.setScaleX(scale >= 0 ? scale : 0);
                totalnumber.setScaleY(scale >= 0 ? scale : 0);

            }
        });


        //Review Statistics Recycler
        recyclerstats = (RecyclerView) findViewById(R.id.recyclerstats);
        recyclerstats.setLayoutManager(new LinearLayoutManager(ActivityDoctorsProfile.this,LinearLayoutManager.HORIZONTAL, false));
        //recyclerstats.addItemDecoration(new LineItemDecoration(ActivityDoctorsProfile.this, LinearLayout.HORIZONTAL));
        recyclerstats.setHasFixedSize(true);
        recyclerstats.setNestedScrollingEnabled(false);

        //Recycler for the other stuffs
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        else if(item.getItemId() == R.id.mnuselectdoctor)
        {
            Intent intent  = getIntent();
            intent.putExtra("selecteddoctoruserid", doctor.getDoctor().getId());
            intent.putExtra("selecteddoctorname", StringUtils.join(" ",doctor.getDoctor().getFirstname(), doctor.getDoctor().getOthername(), doctor.getDoctor().getLastname()));
            setResult(RESULT_OK, intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!viewonlymode) {
            getMenuInflater().inflate(R.menu.menu_select_doctor, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED, new Intent());
        super.onBackPressed();
        finish();
    }

    private void fetchDoctorProfile()
    {
        progress.show();
        Call<SwagArrayResponseModel<List<UserProfile>>> call = mSafeDoctorService.getDoctorProfile(TokenString.tokenString, doctor.getDoctor().getId());
        Log.i("Safe", "profile URL is " + mSafeDoctorService.getDoctorProfile(TokenString.tokenString, doctor.getDoctor().getId()).request().url().toString());
        call.enqueue(new Callback<SwagArrayResponseModel<List<UserProfile>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<UserProfile>>> call, Response<SwagArrayResponseModel<List<UserProfile>>> response) {
                Log.i("Safe", "Load profile Called");
                progress.dismiss();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please relogin", Toast.LENGTH_SHORT).show();
                    sessionManagement.logoutSessionExpired();
                    finish();
                } else if (response.isSuccessful()) {
                    Log.i("Safe", "Fetching profile");

                    SwagArrayResponseModel<List<UserProfile>> data = response.body();
                    if(data.getData() != null && data.getData().size() > 0) {
                        setItems(data.getData().get(0));
                    }

                }
                else {
                    Log.e("Safe", "Fetching doctor profile... " + response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<UserProfile>>> call, Throwable t) {
                Log.i("SafeRes"," "+ t.getMessage());
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });


    }

    private void setItems(final UserProfile profile)
    {
        if(profile == null)
        {
            return;
        }

        try{
            progress.show();
        }catch (Exception e){
            e.printStackTrace();
        }


        if(profile.getReviewstatistics().getSummary().getTotalnumber() > 0)
        {
            avgstats.setImageResource(R.drawable.shape_circle);
            avgstats.setColorFilter(AuxUtils.getRatingColor(profile.getReviewstatistics().getSummary().getPercentage()));

            image_letter.setText(String.format("%.2f", profile.getReviewstatistics().getSummary().getPercentage()) + "%");
            totalnumber.setText(profile.getReviewstatistics().getSummary().getTotalnumber() + AuxUtils.getPlural(profile.getReviewstatistics().getSummary().getTotalnumber()," Review"));

        }

        DoctorsProfileStatsAdapter statsAdapter = new DoctorsProfileStatsAdapter(ActivityDoctorsProfile.this,profile.getReviewstatistics().getDetails());
        recyclerstats.setAdapter(statsAdapter);
        statsAdapter.setOnItemClickListener(new DoctorsProfileStatsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ReviewBlock obj, int position) {

                Intent intent  = new Intent(getApplicationContext(),ActivityProfileStatsDetails.class);
                intent.putExtra("doctoruserid", doctor.getDoctor().getId());
                intent.putExtra("question", obj.getName());
                intent.putExtra("reviewtypeid", 2);
                intent.putExtra("questiontypeid", obj.getTypeid());

                startActivity(intent);
            }
        });




        List<ListItemCategory> items = DataGenerator.getProfileItems(this);

        //set data and list adapter
        mAdapter = new AdapterItemsLstCategory(this, items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterItemsLstCategory.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ListItemCategory obj, int position)
            {
                List<ProfileBasicObject> profobj = new ArrayList<ProfileBasicObject>();
                switch (obj.itemid)
                {
                    case 0:
                            if(profile.getAwards().size() == 0)
                            {
                                Toast.makeText(ActivityDoctorsProfile.this,"There are no Awards for this doctor",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for(Doctoraward a : profile.getAwards())
                            {
                                ProfileBasicObject o = new ProfileBasicObject();
                                o.setId(a.getId());
                                o.setName(a.getAward());
                                o.setOthername("From: "+a.getOrganisation());
                                o.setOtherid("Year: " +a.getYear());

                                profobj.add(o);
                            }

                        break;
                    case 1:
                        if(profile.getCertificates().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Certificates for this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctorcertificate a : profile.getCertificates())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            o.setId(a.getId());
                            o.setName(a.getCertificate());
                            o.setOthername(a.getSchool());
                            o.setOtherid("" +a.getYear());

                            profobj.add(o);
                        }
                        break;
                    case 2:
                        if(profile.getSkills().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Skills for this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctorskill a : profile.getSkills())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            o.setId(a.getId());
                            o.setName(a.getSkill().getName());
                           // o.setOthername("From: "+a.get());
                           o.setOtherid("Rating: " +a.getRating());

                            profobj.add(o);
                        }
                        break;
                    case 3:
                        if(profile.getAccomplishments().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Accomplishments for this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctoraccomplishment a : profile.getAccomplishments())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            o.setId(a.getId());
                            o.setName(a.getAccomplishment());
                            o.setOthername("Year: "+a.getYear());
                            //o.setOtherid("Year: " +a.getYear());

                            profobj.add(o);
                        }
                        break;
                    case 4:
                        if(profile.getFacilities().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Service Provider Associated with this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctorfacility a : profile.getFacilities())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            //o.setDetailid();
                            o.setName(a.getFacility().getName());
                            o.image = a.getFacility().getLogo();
                            //o.setOtherid("Year: " +a.getYear());
                            profobj.add(o);
                        }
                        break;
                    case 5:
                        if(profile.getSpecialties().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Specialty has been set for this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctorspecialty a : profile.getSpecialties())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            o.setId(a.getSpecialty().getId());
                            o.setName(a.getSpecialty().getName());
                            //o.setOthername("From: "+a.getOrganisation());
                            //o.setOtherid("Year: " +a.getYear());

                            profobj.add(o);
                        }
                        break;
                    case 6:
                        if(profile.getLanguages().size() == 0)
                        {
                            Toast.makeText(ActivityDoctorsProfile.this,"There are no Langauge has been set for this doctor",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(Doctorlanguage a : profile.getLanguages())
                        {
                            ProfileBasicObject o = new ProfileBasicObject();
                            o.setId(a.getLanguage().getId());
                            o.setName(a.getLanguage().getName());
                            //o.setOthername("From: "+a.getOrganisation());
                            //o.setOtherid("Year: " +a.getYear());

                            profobj.add(o);
                        }
                        break;
                    case 7:
                        //Doctor Review
                        //((Activity)ReviewActivity).setFinishOnTouchOutside();
                        Intent intent=new Intent(ActivityDoctorsProfile.this,ReviewActivity.class);
                        intent.putExtra("Doctor",doctor.getDoctor());
                        intent.putExtra("Reviewtype","user");
                        startActivityForResult(intent,1);
                        //Toast.makeText(ActivityDoctorsProfile.this,"Reviews writing coming soon",Toast.LENGTH_SHORT).show();
                        //return;
                        break;

                }

                common.displayPopUpList(ActivityDoctorsProfile.this,profobj,obj.title, obj.imageDrw);

            }
        });

        progress.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Log.e("Doctor Profile","onActivityResult called");
            progress.setCancelable(false);
            progress.show();
            fetchDoctorProfile();
        }
    }

    @Override
    public void onTaskCompleted(Object result) {

    }
}
