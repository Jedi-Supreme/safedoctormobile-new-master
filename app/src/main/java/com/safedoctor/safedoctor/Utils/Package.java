package com.safedoctor.safedoctor.Utils;

/**
 * Created by Stevkkys on 9/8/2017.
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Context;

public class Package
{
    PackageInfo p;

    public Package(Context context)
    {
        try {
            p = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int getVersionCode()
    {

        return p.versionCode;
    }

    public String getVersionName()
    {
        return p.versionName;
    }

}
