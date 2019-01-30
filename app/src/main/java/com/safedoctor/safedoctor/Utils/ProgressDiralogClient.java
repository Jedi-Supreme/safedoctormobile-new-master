package com.safedoctor.safedoctor.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Alvin on 18/08/2017.
 */

public class ProgressDiralogClient {

    public static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context)
    {
         progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog()
    {
        progressDialog.dismiss();
    }

}
