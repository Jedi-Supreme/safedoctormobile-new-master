package com.safedoctor.safedoctor.UI.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.BasicStringAdapter;
import com.safedoctor.safedoctor.Adapter.ReferralsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Model.ClinicalNote;
import com.safedoctor.safedoctor.Model.ConsComplain;
import com.safedoctor.safedoctor.Model.Consultation;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.Diagnoses;
import com.safedoctor.safedoctor.Model.Examfinding;
import com.safedoctor.safedoctor.Model.Prescriptiondrug;
import com.safedoctor.safedoctor.Model.Servicerequest;
import com.safedoctor.safedoctor.Model.Systemreviews;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.PrescribeddrugActivity;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage.updateCounter;
import static com.safedoctor.safedoctor.Utils.App.context;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.formateDate;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class OnlineMedicalRecords extends Fragment implements View.OnClickListener {

    private View rootView;
    private List<ConsultationProfile> visitList=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private Common common;
    private BasicStringAdapter basicStringAdapter;
    private TextView doctor,date,countdown,clinicalnote,sysreviews,complaints,diagnoses,drugs,requests,referrals,examfindings,searchvisit;
    private ImageView chattype,rightarrow,leftarrow;
    private LinearLayout tablayout;
            private View doctorview;
    private int currentPosition;
    private String[] list;
    private int tabcount;private ArrayList<View> tabs= new ArrayList<>();
    private String TAG="OnlineMedicalRecords";
    private Dialog dialog;
    private static int type;
    private static EditText from,to;
    private boolean initload=false;

    public ConsultationProfile consultationProfile=null;




    public static Fragment newInstance() {
        OnlineMedicalRecords fragment = new OnlineMedicalRecords();
        return  fragment;
    }

    public OnlineMedicalRecords() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.onlinevisit, container, false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
        mSwipeRefresh=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        common=new Common(context);
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOnlineVisits(null,null);
            }
        });

        /*
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ConsultationProfile consultationProfile = visitList.get(position);
                Intent intent = new Intent(context, OnlineVisitInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("profile",consultationProfile);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));*/
        countdown=(TextView)rootView.findViewById(R.id.countdown);
        doctor=(TextView)rootView.findViewById(R.id.doctor);
        date=(TextView)rootView.findViewById(R.id.date);
        chattype=(ImageView)rootView.findViewById(R.id.chat_type_img);

        tablayout=(LinearLayout)rootView.findViewById(R.id.tab_layout);
        tabcount=tablayout.getChildCount();
        doctorview=(View)rootView.findViewById(R.id.doctorview);

        diagnoses=(TextView)rootView.findViewById(R.id.diagnoses);
        diagnoses.setOnClickListener(this);
        diagnoses.setContentDescription("tab");
        complaints=(TextView)rootView.findViewById(R.id.complaints);
        complaints.setOnClickListener(this);
        complaints.setContentDescription("tab");
        sysreviews=(TextView)rootView.findViewById(R.id.sysreviews);
        sysreviews.setOnClickListener(this);
        sysreviews.setContentDescription("tab");
        clinicalnote=(TextView)rootView.findViewById(R.id.clinicalnote);
        clinicalnote.setOnClickListener(this);
        clinicalnote.setContentDescription("tab");

        examfindings=(TextView)rootView.findViewById(R.id.examfindings);
        examfindings.setOnClickListener(this);
        examfindings.setContentDescription("tab");
        referrals=(TextView)rootView.findViewById(R.id.referrals);
        referrals.setOnClickListener(this);
        referrals.setContentDescription("tab");
        requests=(TextView)rootView.findViewById(R.id.requests);
        requests.setOnClickListener(this);
        requests.setContentDescription("tab");
        drugs=(TextView)rootView.findViewById(R.id.drugs);
        drugs.setOnClickListener(this);
        drugs.setContentDescription("tab");

        list=context.getResources().getStringArray(R.array.online_profile);

        rightarrow=(ImageView)rootView.findViewById(R.id.rightarrow);
        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ++currentPosition;
                loadConsultation();
            }
        });
        leftarrow=(ImageView)rootView.findViewById(R.id.leftarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --currentPosition;
                loadConsultation();
            }
        });
        searchvisit=(TextView)rootView.findViewById(R.id.searchvisit);
        searchvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.getvisit);
                dialog.show();

                 from=(EditText)dialog.findViewById(R.id.from);
                 to=(EditText)dialog.findViewById(R.id.to);
                 TextView searchbtn=(TextView)dialog.findViewById(R.id.search);

                from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type=0;
                        showDatePickerDialog(view);
                    }
                });
                to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type=1;
                        showDatePickerDialog(view);
                    }
                });
                searchbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSwipeRefresh.setRefreshing(true);
                        dialog.dismiss();
                        getOnlineVisits(formateDate(from.getText().toString()+" 00:00:00"),formateDate(to.getText().toString()+" 00:00:00"));

                    }
                });

            }
        });

        hideView();

        Activity activity=getActivity();
        common.fragmentInitOnbackpressed(activity);


        return rootView;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //Do something with the date chosen by the user
            if(type==0){
                from.setText(year + "-" + (month + 1) + "-" + day);
                //from.setText(day + "/" + (month + 1) + "/" + year);
            }else if(type==1){
                to.setText(year + "-" + (month + 1) + "-" + day);
                //to.setText(day + "/" + (month + 1) + "/" + year);
            }

        }
    }



    void loadConsultation(){
        recyclerView.setVisibility(View.GONE);
        Log.e(TAG,"current position= "+currentPosition);

        if(currentPosition>=0 && currentPosition<visitList.size()){
            countdown.setText(""+(currentPosition+1)+" out of "+visitList.size());
            ConsultationProfile first=visitList.get(currentPosition);
            Consultation consultation=first.getConsultation();

            doctor.setText(consultation.getDoctor().getFullname());
            date.setText(AuxUtils.Date.formateDate(consultation.getConendtime(),"E, d MMMM, yyyy"));


                switch (consultation.getConsultationtypeid())
                {
                    case 1:
                        chattype.setImageResource(R.drawable.chat_64px);
                        break;
                    case 2:
                        chattype.setImageResource(R.drawable.phonecall2_64px);
                        break;
                    case 3:
                        chattype.setImageResource(R.drawable.vid_64px);
                        break;

                }


            showView();
            initload=true;
            clinicalnote.performClick();

        }else if(currentPosition<0 ){
            Toast.makeText(context,"End of all visits",Toast.LENGTH_LONG).show();
            currentPosition=0;
        }else if(currentPosition>=visitList.size()){
            Toast.makeText(context,"End of all visits",Toast.LENGTH_LONG).show();
            currentPosition=visitList.size()-1;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(AppConstants.CACHE_ONLINE_VISIT.size()>0){
            visitList=AppConstants.CACHE_ONLINE_VISIT;
            loadConsultation();

            Toast.makeText(context,"Swipe down to refresh",Toast.LENGTH_LONG).show();
        }else{
            mSwipeRefresh.setRefreshing(true);
            mSwipeRefresh.post(new Runnable() {
                @Override
                public void run() {

                    getOnlineVisits(null,null);
                }
            });
        }

    }

    void getOnlineVisits(final String from, String to){
        common.getOnlineVisits(AppConstants.patientID,mSwipeRefresh,from,to);
        common.setOnTaskCompletedListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object result) {
                visitList=(List<ConsultationProfile>) result;
                if(visitList.size()>0){
                    AppConstants.onlinerecords=visitList.size();updateCounter(null);
                    currentPosition=0;

                    loadConsultation();



                    //onlineVisitAdapter=new OnlineVisitAdapter(visitList,context);
                   // recyclerView.setAdapter(onlineVisitAdapter);
                }else{
                    if(from!=null){
                        Toast.makeText(context,"No online visits",Toast.LENGTH_LONG).show();
                    }

                }
                mSwipeRefresh.setRefreshing(false);


            }
        });



    }


    @Override
    public void onClick(View view) {
         consultationProfile=visitList.get(currentPosition);

            try{
                tablayout.findViewsWithText(tabs,"tab",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if(tabs.size()!=0){
                    for (View tab:tabs) {
                        ((TextView)tab).setTextColor(ContextCompat.getColor(context,R.color.grey_20));
                    }
                }
            }catch (Exception e){
                Log.e(TAG,""+e);
            }



        ((TextView)view).setTextColor(ContextCompat.getColor(context,R.color.white));

        switch (view.getId()){
            case R.id.clinicalnote:
                List<ClinicalNote> clinicalNotes= consultationProfile.getClinicalnotes();
                if(clinicalNotes.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (ClinicalNote note:clinicalNotes) {
                        BasicObject basicObject=new BasicObject(0,note.getClinicalnotes());
                        baseObjectsList.add(basicObject);
                    }
                    showAdapter(baseObjectsList,null);
                }else{
                    if(initload){

                    }else{
                        noDataMessage(0);
                    }
                    initload=false;

                }
                break;
            case R.id.sysreviews:
                List<Systemreviews> systemreviewsList=consultationProfile.getSystemreviews();
                if(systemreviewsList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (Systemreviews systemreview:systemreviewsList) {
                        BasicObject basicObject=new BasicObject(null,systemreview.getSymptom().getName(),systemreview.getRemarks());
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,null);
                }else{
                    noDataMessage(1);
                }
                break;
            case R.id.complaints:
                List<ConsComplain> complainList=consultationProfile.getComplains();
                if(complainList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (ConsComplain complain:complainList) {
                        BasicObject basicObject=new BasicObject(0,complain.getComplaint());
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,null);


                }else{
                    noDataMessage(2);
                }
                break;
            case R.id.diagnoses:
                List<Diagnoses> diagnosesList=consultationProfile.getDiagnoses();
                if(diagnosesList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (Diagnoses diagnoses:diagnosesList) {
                        BasicObject basicObject=new BasicObject(diagnoses.getDisease().getName(),diagnoses.getDiagtype().getName(),null);
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,null);


                }else{
                    noDataMessage(3);
                }
                break;
            case R.id.examfindings:
                List<Examfinding> examfindingList=consultationProfile.getExamsandfindings();
                if(examfindingList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (Examfinding examfinding:examfindingList) {
                        BasicObject basicObject=new BasicObject(examfinding.getExamfindings(),examfinding.getSystem().getName(),examfinding.getRemarks());
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,null);


                }else{
                    noDataMessage(4);
                }
                break;
            case R.id.referrals:
                List<com.safedoctor.safedoctor.Model.Referral> referralList=consultationProfile.getReferrals();
                if(referralList.size()>0){

                    ReferralsAdapter referralsAdapter=new ReferralsAdapter(referralList,context);
                    recyclerView.setAdapter(referralsAdapter);

                }else{
                    noDataMessage(7);
                }

                break;
            case R.id.requests:
                List<Servicerequest> servicerequestList=consultationProfile.getServicerequests();
                if(servicerequestList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (Servicerequest servicerequest:servicerequestList) {
                        BasicObject basicObject=new BasicObject(servicerequest.getService().getName(),servicerequest.getServiceprovider()!=null?servicerequest.getServiceprovider().getName():null,servicerequest.getRemark());
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,null);
                }else{
                    noDataMessage(6);
                }
                break;
            case R.id.drugs:
                List<Prescriptiondrug> prescriptiondrugList=consultationProfile.getPrescriptions();
                if(prescriptiondrugList.size()>0){
                    List<BasicObject> baseObjectsList=new ArrayList<>();
                    for (Prescriptiondrug prescriptiondrug:prescriptiondrugList) {
                        BasicObject basicObject=new BasicObject(prescriptiondrug.getDrug().getName(),prescriptiondrug.getDosage()+" "+prescriptiondrug.getFrequency().getName()+"   "+prescriptiondrug.getDurationvalue()+" "+prescriptiondrug.getDurationunit().getUnit(),prescriptiondrug.getAmount());
                        baseObjectsList.add(basicObject);

                    }
                    showAdapter(baseObjectsList,5);
                }else{
                    noDataMessage(5)   ;
                }

                break;
        }

    }

    void showAdapter(List<BasicObject> baseObjectsList,Integer info){



        if(info!=null){
            basicStringAdapter=new BasicStringAdapter(context,baseObjectsList,info);
            if(info==5){
                basicStringAdapter.setOnInfoClickListener(new BasicStringAdapter.Infobtnclick() {
                    @Override
                    public void onclick(int position) {
                        Prescriptiondrug drug=consultationProfile.getPrescriptions().get(position);
                        Intent intent=new Intent(context, PrescribeddrugActivity.class);
                        intent.putExtra("drug",drug);
                        intent.putExtra("doctor",consultationProfile.getConsultation().getDoctor().getFullname());

                        startActivity(intent);

                    }
                });
            }
        }else{
            basicStringAdapter=new BasicStringAdapter(context,baseObjectsList);
        }

        recyclerView.setAdapter(basicStringAdapter);
        recyclerView.setVisibility(View.VISIBLE);

    }

    void noDataMessage(int position){
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(context,"No "+list[position],Toast.LENGTH_LONG).show();
    }


    void hideView(){
        countdown.setVisibility(View.GONE);
        doctorview.setVisibility(View.GONE);
        tablayout.setVisibility(View.GONE);
        searchvisit.setVisibility(View.GONE);

    }
    void showView(){
        countdown.setVisibility(View.VISIBLE);
        doctorview.setVisibility(View.VISIBLE);
        tablayout.setVisibility(View.VISIBLE);
        searchvisit.setVisibility(View.VISIBLE);

    }
}
