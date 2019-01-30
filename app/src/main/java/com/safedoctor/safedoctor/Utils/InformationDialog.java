package com.safedoctor.safedoctor.Utils;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.UI.Activities.Login;

/**
 * Created by oteng on 11/01/2018.
 */

public class InformationDialog {
    public static void showTipDialog(final Activity app, String message) {
        final Dialog dialog = new Dialog(app);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_login_suggestion);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView content = (TextView) dialog.findViewById(R.id.content);

        content.setText(message);


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(app, FormLogin.class);
                app.startActivity(i);
                (app).finish();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
