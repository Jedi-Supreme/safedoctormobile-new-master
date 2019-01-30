package com.safedoctor.safedoctor.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.UI.Fragment.Biography;
import com.safedoctor.safedoctor.UI.Fragment.BloodDonorCert;
import com.safedoctor.safedoctor.UI.Fragment.ContactPersons;
import com.safedoctor.safedoctor.UI.Fragment.MedicalHistories;
import com.safedoctor.safedoctor.UI.Fragment.Medications;
import com.safedoctor.safedoctor.UI.Fragment.NextofKins;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/12/2017.
 */

public class ManageProfile extends AppCompatActivity
{

    private ViewPager view_pager;
    private TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mange_profile);

        initToolbar();
        initComponent();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Management");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ManageProfile.SectionsPagerAdapter adapter = new ManageProfile.SectionsPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(ProfilePhoto.newInstance(), "PHOTO");
        adapter.addFragment(Biography.newInstance(), "BIOGRAPHY");
        adapter.addFragment(ContactPersons.newInstance(), "CONTACT PERSONS");
        adapter.addFragment(NextofKins.newInstance(), "NEXT OK KINS");
        adapter.addFragment(Medications.newInstance(), "MEDICATIONS");
        adapter.addFragment(MedicalHistories.newInstance(), "MEDICAL HISTORY");
        adapter.addFragment(BloodDonorCert.newInstance(), "BLOOD DONOR CERTIFICATE");

        viewPager.setOffscreenPageLimit(6); //before setAdapter
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
