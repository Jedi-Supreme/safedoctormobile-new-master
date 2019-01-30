package com.safedoctor.safedoctor.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.AdapterItemsLstCategory;
import com.safedoctor.safedoctor.Model.ClinicalNote;
import com.safedoctor.safedoctor.Model.ConsComplain;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.Diagnoses;
import com.safedoctor.safedoctor.Model.Examfinding;
import com.safedoctor.safedoctor.Model.ListItemCategory;
import com.safedoctor.safedoctor.Model.Referral;
import com.safedoctor.safedoctor.Model.Servicerequest;
import com.safedoctor.safedoctor.Model.Systemreviews;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.ClickListener;
import com.safedoctor.safedoctor.Utils.RecyclerTouchListener;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.widget.SpacingItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.Utils.App.context;

public class OnlineVisitInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterItemsLstCategory adapterItemsLstCategory;
    private ConsultationProfile consultationProfile;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_visit_info);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        list=context.getResources().getStringArray(R.array.online_profile);
        List<ListItemCategory> onlinelist=new ArrayList<>();
        for (String s:list) {
            ListItemCategory listItemCategory=new ListItemCategory();
            listItemCategory.title=s;
            onlinelist.add(listItemCategory);

        }

        adapterItemsLstCategory=new AdapterItemsLstCategory(getApplicationContext(),onlinelist);
        recyclerView.setAdapter(adapterItemsLstCategory);
        consultationProfile=(ConsultationProfile)getIntent().getSerializableExtra("profile");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                switch (position){
                    case 0:
                        List<ClinicalNote> clinicalNotes= consultationProfile.getClinicalnotes();
                        if(clinicalNotes.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (ClinicalNote note:clinicalNotes) {
                                BasicObject basicObject=new BasicObject(0,note.getClinicalnotes());
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);
                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }

                        break;
                    case 1:
                        List<Systemreviews> systemreviewsList=consultationProfile.getSystemreviews();
                        if(systemreviewsList.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (Systemreviews systemreview:systemreviewsList) {
                                BasicObject basicObject=new BasicObject(systemreview.getClinicalsystem().getName(),systemreview.getSymptom().getName(),systemreview.getRemarks());
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);
                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 2:
                        List<ConsComplain> complainList=consultationProfile.getComplains();
                        if(complainList.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (ConsComplain complain:complainList) {
                                BasicObject basicObject=new BasicObject(0,complain.getComplaint());
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);


                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3:
                        List<Diagnoses> diagnosesList=consultationProfile.getDiagnoses();
                        if(diagnosesList.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (Diagnoses diagnoses:diagnosesList) {
                                BasicObject basicObject=new BasicObject(diagnoses.getDisease().getName(),diagnoses.getDiagtype().getName(),null);
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);


                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;


                    case 4:
                        List<Examfinding> examfindingList=consultationProfile.getExamsandfindings();
                        if(examfindingList.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (Examfinding examfinding:examfindingList) {
                                BasicObject basicObject=new BasicObject(examfinding.getExamfindings(),examfinding.getSystem().getName(),examfinding.getRemarks());
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);


                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5://Prescription drugs
                        break;

                    case 6:
                        List<Servicerequest> servicerequestList=consultationProfile.getServicerequests();
                        if(servicerequestList.size()>0){
                            List<BasicObject> baseObjectsList=new ArrayList<>();
                            for (Servicerequest servicerequest:servicerequestList) {
                                BasicObject basicObject=new BasicObject(servicerequest.getService().getName(),servicerequest.getServiceprovider()!=null?servicerequest.getServiceprovider().getName():null,servicerequest.getRemark());
                                baseObjectsList.add(basicObject);

                            }
                            showIntent(baseObjectsList,position);


                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 7:
                        List<Referral> referralList=consultationProfile.getReferrals();
                        if(referralList.size()>0){
                            Intent intent=new Intent(getApplicationContext(),OnlineVisitObjectActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("object", (Serializable) referralList);
                            bundle.putInt("position",position);
                            bundle.putString("title",list[position]);
                            intent.putExtras(bundle);
                            startActivity(intent);


                        }else{
                            Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
                        }
                        break;

                }

            }
            @Override
            public void onLongClick(View view, int position) {

            }

        }));




    }

    private void showIntent(List<BasicObject> baseObjectsList,int position){
        Intent intent=new Intent(getApplicationContext(),OnlineVisitObjectActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("object", (Serializable) baseObjectsList);
        bundle.putString("title",list[position]);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
