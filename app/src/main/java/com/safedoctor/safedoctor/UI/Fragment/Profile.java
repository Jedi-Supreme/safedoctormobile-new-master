package com.safedoctor.safedoctor.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.R;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class Profile extends Fragment {
    private View rootView;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    public static Fragment newInstance() {
        Profile fragment = new Profile();
        return  fragment;
    }

    public Profile() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        final Toolbar toolbar;
//        toolbar = (Toolbar)getView().findViewById(R.id.toolbar);
       // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        //collapsingToolbarLayout = (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(" ");
        return rootView;



    }
}
