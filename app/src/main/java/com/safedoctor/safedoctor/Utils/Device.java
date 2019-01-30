package com.safedoctor.safedoctor.Utils;

/**
 * Created by Stevkkys on 9/8/2017.
 */


import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class Device
{

    private Context context;

    public  Device(Context _context)
    {
        this.context = _context;
    }

    public String getMyPhoneNumber()
    {
        try {
            String num =  ((TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE))
                    .getLine1Number();

            if(null == num || num.isEmpty() || num.contains("?"))
            {
                return  "";
            }
            return num;
        }catch (Exception ex)
        {
            Log.e("Sefe",ex.getMessage());
            ex.printStackTrace();
            return "";
        }
    }


    public static String readKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is), 1024);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }


    public static String getDeviceModelNumber() {
        String manufacturer = Build.VERSION.CODENAME;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static  String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String GetRandomNum(long max)
    {
        String rand="";
        Random r = new Random();
        rand = r.nextGaussian() +""+r.nextGaussian();

        return rand;

    }
    // get System info.
    public static String OSNAME = System.getProperty("os.name");
    public static String OSVERSION = System.getProperty("os.version");
    public static String RELEASE = android.os.Build.VERSION.RELEASE;
    public static String DEVICE = android.os.Build.DEVICE;
    public static String MODEL = android.os.Build.MODEL;
    public static String PRODUCT = android.os.Build.PRODUCT;
    public static String BRAND = android.os.Build.BRAND;
    public static String DISPLAY = android.os.Build.DISPLAY;
    public static String CPU_ABI = android.os.Build.CPU_ABI;
    public static String CPU_ABI2 = android.os.Build.CPU_ABI2;
    public static String UNKNOWN = android.os.Build.UNKNOWN;
    public static String HARDWARE = android.os.Build.HARDWARE;
    public static String ID = android.os.Build.ID;
    public static String MANUFACTURER = android.os.Build.MANUFACTURER;
    public static String USER = android.os.Build.USER;
    public static String HOST = android.os.Build.HOST;
    public static int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
    public static String IMEI;
    public static String PHONENUMBER;
    public static String NETWORKOPERATORNAME;
    public static String NETWORKOPERATORCODE;

    public static String DEVICE_CODE =Integer.toString((android.os.Build.ID + "_"+android.os.Build.HARDWARE+"_"+android.os.Build.CPU_ABI+""+android.os.Build.CPU_ABI2).hashCode());


}
