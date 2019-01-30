package com.safedoctor.safedoctor.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.safedoctor.safedoctor.Utils.SessionManagement;

/**
 * Created by stevkky on 10/18/17.
 */

public class SplashActivity extends AppCompatActivity

{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManagement sessionManagement=new SessionManagement(getApplicationContext());
        if(sessionManagement.isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), ActivityLandingPage.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, ActivityWelcome.class);
            startActivity(intent);

        }

        finish();


    }
}
