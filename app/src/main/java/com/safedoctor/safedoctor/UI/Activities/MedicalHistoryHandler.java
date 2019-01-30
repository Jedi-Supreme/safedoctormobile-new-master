package com.safedoctor.safedoctor.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.widget.SpacingItemDecoration;
import com.safedoctor.safedoctor.Adapter.MedicalHistoryHandlerAdapter;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.requests.ClinicalHistoryLineIn;
import com.safedoctor.safedoctor.Model.requests.PatientMedicalHistoryIn;
import com.safedoctor.safedoctor.Model.responses.Medicalhistoryquestion;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedicalhistory;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class MedicalHistoryHandler  extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private MedicalHistoryHandlerAdapter mAdapter;

    private List<Medicalhistoryquestion> items=new ArrayList<>(),genderbaseditems=new ArrayList<>();
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<Patientprofilemedicalhistory>> dataresponse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_medical_history_handler);    }

        items = AppConstants.CACHE_FORMDATA.getMedicalhistorytypequestion(getIntent().getIntExtra("historyid",0));

        Integer usergendergroup=AppConstants.PatientObj.getGendergroupid();
        if(usergendergroup!=null){
            for (Medicalhistoryquestion medicalhistoryquestion:items) {
                if(medicalhistoryquestion.getGendergroupid()==usergendergroup || medicalhistoryquestion.getGendergroupid()==3){
                    genderbaseditems.add(medicalhistoryquestion);
                }

            }
        }else{
            genderbaseditems=items;
        }
        initToolbar();
        initComponent();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);
    }

    private void initComponent()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(this, 4), true));
        recyclerView.setHasFixedSize(true);

        mSafeDoctorService = ApiUtils.getAPIService();

        //set data and list adapter
        mAdapter = new MedicalHistoryHandlerAdapter(this, genderbaseditems);

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new MedicalHistoryHandlerAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, MedicalHistoryHandlerAdapter.ViewHolder holder, Medicalhistoryquestion question, Integer answerid, int pos)
            {
                saveItem(holder,answerid,question);

               //Toast.makeText(getApplicationContext(),holder.txtremark.getText().toString() + " "+
              //         holder.answer.getText().toString(),Toast.LENGTH_SHORT).show();
            }


        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private  void saveItem(MedicalHistoryHandlerAdapter.ViewHolder holder, Integer answerid, final Medicalhistoryquestion question)
    {

        PatientMedicalHistoryIn history = new PatientMedicalHistoryIn();

        history.setId(answerid); // Can be null in case its a fresh insert
        history.setPatientid(AppConstants.patientID);
        history.setQuestionid(question.getId());
        history.setRemarks(AuxUtils.getTextValue(holder.txtremark.getText(),true));
        history.setHistorytypeid(getIntent().getIntExtra("historyid",1));
        history.setAnswer(holder.answer.isChecked());

        List<ClinicalHistoryLineIn> historylines = new ArrayList<ClinicalHistoryLineIn>();

        int finput = holder.finite_input.getCheckedRadioButtonId();
        if(finput != -1)
        {
            RadioButton selectedinput = (RadioButton) findViewById(finput);
            historylines.add(new ClinicalHistoryLineIn(null, AuxUtils.getTagValue(selectedinput.getTag(),true) ));
        }

        if( AuxUtils.getTextValue(holder.infinite_input.getText(), true) != null)
        {
            String[] infinitelst = AuxUtils.getTextValue(holder.infinite_input.getText(), true).split(",");
            for(String  lst : infinitelst)
            {
                historylines.add(new ClinicalHistoryLineIn(lst.trim(),null ));
            }
        }

        history.setHistorylines(historylines);
        List<PatientMedicalHistoryIn> historylst = new ArrayList<PatientMedicalHistoryIn>();
        historylst.add(history);

    // Data is now ready to be saved. Get on and push to endpoint for persistence

        Call<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> call = mSafeDoctorService.saveMedicalHistory(TokenString.tokenString,AppConstants.patientID, historylst);

        call.enqueue(new Callback<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> call, Response<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> response) {

                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getApplicationContext(), "Your session has expired. Please Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                }
                else if (response.isSuccessful())
                {
                    dataresponse = response.body();

                    if(dataresponse.getData() != null && dataresponse.getData().size() > 0)
                    {
                        Patientprofilemedicalhistory saved = dataresponse.getData().get(0);

                        AppConstants.CACHE_FORMDATA.setPatientprofilemedicalhistory(question.getId(), saved);

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> call, Throwable throwable) {

                try {
                    Toast.makeText(getApplicationContext(), "Network Error, please try again", Toast.LENGTH_LONG).show();
                    throwable.printStackTrace();
                }
                catch (Exception e)
                {

                }
            }
        });



    }

}
