package com.safedoctor.safedoctor.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safedoctor.safedoctor.R;

/**
 * Created by Kwabena Berko on 7/16/2017.
 */

public class Services extends Fragment {
    private View rootView;
    public static Fragment newInstance() {
        Services fragment = new Services();
        return  fragment;
    }

    public Services() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_services, container, false);
        return rootView;
    }
}
