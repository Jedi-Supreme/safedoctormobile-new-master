package com.safedoctor.safedoctor.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.safedoctor.safedoctor.Adapter.MedicalHistoryTypeAdapter;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Model.responses.MedicalhistorytypeQuestion;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.MedicalHistoryHandler;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.widget.LineItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by Stevkkys on 9/19/2017.
 */

public class MedicalHistories extends Fragment
{
    private RecyclerView recyclerView;
    private MedicalHistoryTypeAdapter mAdapter;
    private View parent_view;
    private View root;
    private Common common;
    private SwipeRefreshLayout mSwipeRefresh;

    private AppCompatActivity parentactivity;

    private List<MedicalhistorytypeQuestion> items = new ArrayList<MedicalhistorytypeQuestion>();

    public static MedicalHistories newInstance()
    {
        MedicalHistories medicalhistories = new MedicalHistories();
        return  medicalhistories;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_medicalhistory, container, false);

        parent_view = root.findViewById(android.R.id.content);

        parentactivity = (AppCompatActivity)getActivity();


        initComponent();

        return  root;

    }


    private void initComponent()
    {

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);
        common=new Common(context);
        mSwipeRefresh=(SwipeRefreshLayout)root.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMedHistory();
            }
        });






    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                getMedHistory();
            }
        });
    }

    void getMedHistory(){
        mSwipeRefresh.setRefreshing(true);
        items = AppConstants.CACHE_FORMDATA.getMedicalhistorytypequestion();
        if(items.size()==0){
            Log.e("MedHistory","Loading for the second time");
            common.getPatientFormData();
            common.setOnTaskCompletedListener(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object result) {
                    if((boolean)result){
                        items = AppConstants.CACHE_FORMDATA.getMedicalhistorytypequestion();
                        initAdapter();
                    }else{
                        Toast.makeText(context,"Med history format",Toast.LENGTH_LONG).show();
                    }
                    mSwipeRefresh.setRefreshing(false);

                }
            });
        }else{
            initAdapter();
            mSwipeRefresh.setRefreshing(false);
        }

    }

    void initAdapter(){
        //set data and list adapter
        mAdapter = new MedicalHistoryTypeAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MedicalHistoryTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MedicalhistorytypeQuestion obj, int position) {
                Intent intent = new Intent(getContext(), MedicalHistoryHandler.class);
                Bundle b = new Bundle();
                intent.putExtra("historyid", obj.getId());
                intent.putExtra("title",obj.getHistory());
                startActivity(intent);

                //Toast.makeText(getContext(),"Will not load the activity for "+ obj.getHistory(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
