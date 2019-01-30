package com.safedoctor.safedoctor.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Model.responses.Patientcontactperson;
import com.safedoctor.safedoctor.Model.ListItemCategory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static List<String> getStringsShort(Context ctx) {
        List<String> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.strings_short);
        for (String s : name_arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }

    public static List<Integer> getNatureImages(Context ctx) {
        List<Integer> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.sample_images);
        for (int i = 0; i < drw_arr.length(); i++) {
            items.add(drw_arr.getResourceId(i, -1));
        }
        Collections.shuffle(items);
        return items;
    }

    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }



    public static List<Patientcontactperson> getContactPersons(Context ctx) {
        List<Patientcontactperson> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);

        for (int i = 0; i < drw_arr.length(); i++) {
            Patientcontactperson obj = new Patientcontactperson();
            //obj.image = drw_arr.getResourceId(i, -1);
            obj.setName(name_arr[i]);
            obj.setEmails(Tools.getEmailFromName(name_arr[i]));
            obj.setContactnumbers( ctx.getResources().getString(R.string.lorem_ipsum));
            obj.setRelationshipid(1);
           // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }


    public static List<ListItemCategory> getProfileItems(Context ctx) {
        List<ListItemCategory> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.profile_icon);
        String title_arr[] = ctx.getResources().getStringArray(R.array.profile_title);
        String brief_arr[] = ctx.getResources().getStringArray(R.array.profile_brief);
        for (int i = 0; i < drw_arr.length(); i++) {
            ListItemCategory obj = new ListItemCategory();
            obj.itemid = i;
            obj.image = drw_arr.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.brief = brief_arr[i];
            //obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            obj.imageDrw =  ContextCompat.getDrawable(ctx,obj.image);

            items.add(obj);
        }
        return items;
    }


    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }

    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
