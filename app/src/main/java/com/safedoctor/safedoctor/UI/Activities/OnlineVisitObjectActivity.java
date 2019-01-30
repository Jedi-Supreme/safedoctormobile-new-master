package com.safedoctor.safedoctor.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Adapter.BasicStringAdapter;
import com.safedoctor.safedoctor.Adapter.ReferralsAdapter;
import com.safedoctor.safedoctor.Model.Referral;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.R;

import java.util.List;

public class OnlineVisitObjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BasicStringAdapter basicStringAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_visit_object);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        textView=(TextView)findViewById(R.id.title) ;

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       int referralposition= getIntent().getIntExtra("position",0);
       if(referralposition!=0){
           List<Referral> referralList=(List<Referral>)getIntent().getSerializableExtra("object");
           textView.setText((String)getIntent().getSerializableExtra("title"));
           ReferralsAdapter referralsAdapter=new ReferralsAdapter(referralList,getApplicationContext());
           recyclerView.setAdapter(referralsAdapter);


       }else{
           List<BasicObject> baseObjectsList=(List<BasicObject>)getIntent().getSerializableExtra("object");
           textView.setText((String)getIntent().getSerializableExtra("title"));
           if(baseObjectsList!=null){
               basicStringAdapter=new BasicStringAdapter(getApplicationContext(),baseObjectsList);
               recyclerView.setAdapter(basicStringAdapter);
           }
       }





    }
}
