package com.safedoctor.safedoctor.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.widget.LineItemDecoration;
import com.safedoctor.safedoctor.Adapter.DoctorsListAdapter;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevkky on 10/12/17.
 */

public class ActivityDoctorsList extends AppCompatActivity
{

    private RecyclerView recyclerView;

    private List<DoctorOutObj> items = new ArrayList<DoctorOutObj>();
    private DoctorsListAdapter mAdapter;

    private static final int GET_SELECTED_DOCTOR_FROM_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        iniToolbar();
        initComponent();
    }

    private void iniToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Doctors List");
        setSupportActionBar(toolbar);
    }
    private void initComponent()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityDoctorsList.this));
        recyclerView.addItemDecoration(new LineItemDecoration(ActivityDoctorsList.this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        items = AppConstants.CACHE_DOCTORS;

        mAdapter = new DoctorsListAdapter(ActivityDoctorsList.this, items, getIntent().getStringExtra("Specialityname"));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DoctorsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DoctorOutObj obj, int position) {

                Intent intent = new Intent(getApplicationContext(), ActivityDoctorsProfile.class);
                intent.putExtra("doctor",obj);
                startActivityForResult(intent, GET_SELECTED_DOCTOR_FROM_PROFILE);

            }

            @Override
            public void onItemLongClick(View view, DoctorOutObj obj, int position) {

                Intent intent  = getIntent();
                intent.putExtra("selecteddoctoruserid", obj.getDoctor().getId());
                intent.putExtra("selecteddoctorname", StringUtils.join(" ",obj.getDoctor().getFirstname(), obj.getDoctor().getOthername(), obj.getDoctor().getLastname()));
                setResult(RESULT_OK, intent);
                finish();

            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        else
        {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED, new Intent());
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case GET_SELECTED_DOCTOR_FROM_PROFILE:
                if( (resultCode == RESULT_OK) && (data.getStringExtra("selecteddoctoruserid") != null && !data.getStringExtra("selecteddoctoruserid").isEmpty()))
                {
                    Intent intent  = getIntent();
                    intent.putExtra("selecteddoctoruserid", data.getStringExtra("selecteddoctoruserid"));
                    intent.putExtra("selecteddoctorname", data.getStringExtra("selecteddoctorname"));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }

    }
}
