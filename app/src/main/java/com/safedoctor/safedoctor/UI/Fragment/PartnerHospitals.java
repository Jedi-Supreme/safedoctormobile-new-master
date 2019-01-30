package com.safedoctor.safedoctor.UI.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.PartnerDoctorsAdapter;
import com.safedoctor.safedoctor.Adapter.PartnerHospitalsAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.Utils.App.context;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerHospitals extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Facilityinfo> mHospitalsList;
    private RecyclerView hospitalRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    //private ProgressDialog mProgressDialog;
    private String facilitytype=null;
    private String TAG="PartnerHospitals";
//    private SafeDoctorService mSafeDoctorService;

    public static Fragment newInstance() {
        PartnerHospitals fragment = new PartnerHospitals();
        return fragment;
    }

    public PartnerHospitals() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mSafeDoctorService = ApiUtils.getAPIService();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_partner_hospitals, container, false);
        hospitalRecyclerView = (RecyclerView)  rootView.findViewById(R.id.partner_hospital_layout);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeRefresh.setOnRefreshListener(this);

        /*
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Connecting, please wait...");
        mProgressDialog.setCancelable(false);*/

        Bundle bundle=getArguments();
        if(bundle!=null){
            facilitytype=bundle.getString("type",null);
        }
        Activity activity=getActivity();
        new Common(context).fragmentInitOnbackpressed(activity);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Common common = new Common(getContext());

        /*
        if(new SessionManagement(context).isLoggedIn()==false){
            Toast.makeText(context,"Login",Toast.LENGTH_LONG).show();
            return;

        }*/
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {

                if(facilitytype.equals("s")){
                    common.getSystemUsers(hospitalRecyclerView, facilitytype, getActivity(), mSwipeRefresh);
                    common.setOnTaskCompletedListener(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object result) {
                            List<UserAccount> userAccountList=(List<UserAccount>)result;
                            List<UserAccount> doctorList=new ArrayList<>();

                            for (UserAccount user:userAccountList) {
                                if(user.getUserprofileid()==2 && !user.isLocked()){
                                    doctorList.add(user);
                                }

                            }

                            PartnerDoctorsAdapter partnerdocAdapter = new PartnerDoctorsAdapter(doctorList, getActivity());
                            hospitalRecyclerView.setAdapter(partnerdocAdapter);
                            //Toast.makeText(context,"user accounts "+userAccountList.size(),Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }


                if(AppConstants.CACHE_FACILITY_INFO.size()==0){
                    if (!AppConstants.isNetworkAvailable()){
                        AppConstants.displayNoNetworkAlert(getActivity(), getActivity().findViewById(R.id.main_layout));
                        mSwipeRefresh.setRefreshing(false);
                    }
                    else{


                        Log.e(TAG,"facilitytype "+facilitytype);
                        if(facilitytype!=null){
                            common.getFacilities(hospitalRecyclerView, facilitytype, getActivity(), mSwipeRefresh);
                        }
                    }
                }else{
                    //Toast.makeText(context,"loading from cache",Toast.LENGTH_LONG).show();
                    List<Facilityinfo> hospitaltypelist=new ArrayList<>();
                    hospitaltypelist=sortList(facilitytype,AppConstants.CACHE_FACILITY_INFO,hospitaltypelist);

                    if(hospitaltypelist.size()!=0){
                        Log.e("getFacilities","# list size "+hospitaltypelist.size());
                        PartnerHospitalsAdapter partnerHospitalsAdapter = new PartnerHospitalsAdapter(hospitaltypelist, getActivity());
                        hospitalRecyclerView.setAdapter(partnerHospitalsAdapter);
                    }else{
                        Toast.makeText(context,"No service providers available yet",Toast.LENGTH_LONG).show();
                    }

                    mSwipeRefresh.setRefreshing(false);
                }

            }
        });
    }

    public static List<Facilityinfo> sortList(String facilitytype,List<Facilityinfo> allFacilityList,List<Facilityinfo> hospitaltypelist){

        if (facilitytype.equals("h")) {//hospital

            for (Facilityinfo f:allFacilityList) {
                if(f.getFacilitytypeid()==1){
                    hospitaltypelist.add(f);
                }
            }
        }else if(facilitytype.equals("p")){ //pharmacy
            for (Facilityinfo f:allFacilityList) {
                if(f.getFacilitytypeid()==2){
                    hospitaltypelist.add(f);
                }
            }

        }else if(facilitytype.equals("d")){
            for (Facilityinfo f:allFacilityList) {//diagnotics centres and labs
                if(f.getFacilitytypeid()==4 ||f.getFacilitytypeid()==3  ){
                    f.getLogo();
                    hospitaltypelist.add(f);
                }
            }
        }

        return hospitaltypelist;

    }

    @Override
    public void onRefresh() {

    }
}
