package com.safedoctor.safedoctor.UI.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Fragment.Appointments;
import com.safedoctor.safedoctor.UI.Fragment.FirstAid;
import com.safedoctor.safedoctor.UI.Fragment.Notifications;
import com.safedoctor.safedoctor.UI.Fragment.OnlineMedicalRecords;
import com.safedoctor.safedoctor.UI.Fragment.Referral;

import java.util.ArrayList;
import java.util.List;

import static com.safedoctor.safedoctor.UI.Fragment.Appointments.bookedstatusid;
import static com.safedoctor.safedoctor.Utils.AppConstants.NEW_APPT;

public class AppointmentActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver notifyReceiver;

    public OnTaskCompleted taskCompleted;

    private String TAG="AppointmentActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public  TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_appointment,
            R.drawable.ic_appointment,
            R.drawable.ic_appointment,
            R.drawable.ic_appointment,
            R.drawable.ic_appointment,
            R.drawable.ic_appointment,

    };

    private boolean autoRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.container);
        ListSectionsPager(mViewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //setupTabIcons();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        intentFilter=new IntentFilter(NEW_APPT);
        notifyReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Fragment fragment=mSectionsPagerAdapter.getItem(0);
                if(fragment!=null){
                    ((Appointments)fragment).loadConfirmedAppointments(bookedstatusid);
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                notifyReceiver, intentFilter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void ListSectionsPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Appointments(), "Appointment");
        adapter.addFragment(new OnlineMedicalRecords(), "Online Visits");
        adapter.addFragment(new Referral(),"Referrals");
        adapter.addFragment(new FirstAid(), "First Aid");
        //adapter.addFragment(new ChatRoom(), "Chat Room");

        adapter.addFragment(new Notifications(), "Reminders");
        viewPager.setAdapter(adapter);
        mSectionsPagerAdapter=adapter;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    notifyReceiver);
        }catch (Exception e){
            Log.e(TAG,"Unregister notifyReceiver failed" );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ActivityLandingPage.class));
    }
}
