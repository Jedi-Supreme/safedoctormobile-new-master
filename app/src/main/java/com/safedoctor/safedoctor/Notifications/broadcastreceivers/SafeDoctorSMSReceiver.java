package com.safedoctor.safedoctor.Notifications.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.safedoctor.safedoctor.Utils.AuxUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.safedoctor.safedoctor.Utils.AppConstants.SAFE_DOCTOR_SENDER_ID;

/**
 * Created by stevkky on 10/21/17.
 */

public class SafeDoctorSMSReceiver extends BroadcastReceiver {


    public interface SafeDoctorSMS
    {
       void OnSMSReceived(String from,String SMS, String time);
    }

    private static SafeDoctorSMS mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            String msg_body;
            if (bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++)
                    {

                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        msg_from = msgs[i].getDisplayOriginatingAddress();
                        msg_body = msgs[i].getMessageBody();

                        if(!msg_body.isEmpty() && msg_from.equalsIgnoreCase(SAFE_DOCTOR_SENDER_ID))
                        {
                            if( mListener != null)
                            {
                                mListener.OnSMSReceived(msg_from,msg_body,AuxUtils.Date.getReadabledate());
                            }

                            String tolog = "Log: ["+ AuxUtils.Date.getReadabledate()+"] New Message: "+msg_body+" - "+msg_from+"\n";
                            Log.i("Safe Doctor- SMS",tolog);
                        }
                        else
                        {
                            Log.e("Safe Doctor-SMS : ","Not Required sender ID: "+ msg_from + " :" +msg_body );
                        }


                    }
                }
                catch(Exception e) {
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }

    }

    public static void bindListener(SafeDoctorSMS listener) {
        mListener = listener;
    }
}
