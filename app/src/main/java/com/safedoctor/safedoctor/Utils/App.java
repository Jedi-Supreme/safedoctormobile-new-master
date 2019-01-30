package com.safedoctor.safedoctor.Utils;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.safedoctor.safedoctor.localpersistance.Appointmenttbl;
import com.safedoctor.safedoctor.localpersistance.Detailtbl;

/**
 * Created by beulahana on 09/01/2018.
 */

public class App extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        //localdb conf
        Configuration.Builder config=new Configuration.Builder(this);
        config.addModelClasses(Appointmenttbl.class, Detailtbl.class);
        ActiveAndroid.initialize(config.create());
    }
}
