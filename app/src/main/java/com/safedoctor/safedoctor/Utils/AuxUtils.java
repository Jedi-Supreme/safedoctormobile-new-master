package com.safedoctor.safedoctor.Utils;

/**
 * Created by Stevkkys on 9/8/2017.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.Denomination;
import com.safedoctor.safedoctor.Model.responses.District;
import com.safedoctor.safedoctor.Model.responses.OtherBasicObject;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Town;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AuxUtils
{
    public static class Date
    {
        public static String TAG="Date";
        public  static long SECONDS_IN_A_MINUTE = 60;
        public  static long MINUTES_IN_AN_HOUR = 60;
        public  static long HOURS_IN_A_DAY = 24;
        public  static long DAYS_IN_A_MONTH = 30;
        public  static long MONTHS_IN_A_YEAR = 12;

        public static int dateDiff(String date) throws ParseException
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date d1 = sdf.parse(date);
                int i=  new java.util.Date().compareTo(d1);

                return i;
            }catch(Exception ex){ex.printStackTrace(); return 0;}

        }
        public static int dateDiff(String date1, String date2) throws ParseException
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date d1 = sdf.parse(date1);
            java.util.Date d2 = sdf.parse(date2);
            return d1.compareTo(d2);

        }

        public static String getReadabledate() {
            long milles = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());
            java.util.Date resultdate = new java.util.Date(milles);
            return df.format(resultdate);
        }

        public static String getMessageTime(String time){
            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());
            try {
                java.util.Date date=df.parse(time);

                df.applyPattern("h:mm a");
               return df.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";

        }

        public static String getNow(String format)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Calendar c = Calendar.getInstance();
                return sdf.format(c.getTime());
            }catch(Exception ex){ex.printStackTrace();}
            return null;
        }

        public static String getNow()
        {
            return getNow("yyyy-MM-dd");

        }

        public static String formateDate(String date)
        {
            return formateDate(date ,"yyyy-MM-dd");
        }

        public static String formateTime(String time )
        {
            try
            {
                SimpleDateFormat otimeFormatter = new SimpleDateFormat("HH:mm:s");

                /*
                Calendar cal = Calendar.getInstance();
                String[] timeparts = time.split(":");
                cal.set(Calendar.HOUR,Integer.parseInt(timeparts[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(timeparts[1]));*/

                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");

               //return timeFormatter.format(cal.getTime());

                return timeFormatter.format(otimeFormatter.parse(time));

            }catch (Exception ex) {
                Log.e("Safe Doctor",ex.getMessage());}

            return time;
        }

        public static String formateDate(String date, String format)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                if(date!=null){
                    date = date.replace("T"," ");
                    date = date.replace("Z","");
                    java.util.Date d = sdf.parse(date);
                    sdf.applyPattern(format);

                    return  sdf.format(d);
                }else{
                    return "";
                }

            } catch (ParseException e) {
                Log.e(TAG,"formateDate "+ e.getMessage());
                e.printStackTrace();
            }

                return "";
        }

        public static List<String> generateDateList( String datefrom, String dateto)
        {
            List<String> dates = new ArrayList<String>();
             Calendar cal =  Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try
            {
                cal.setTime(sdf.parse(datefrom));
                java.util.Date dcounter = cal.getTime();
                java.util.Date dend = sdf.parse(dateto);

                while(dcounter.before(dend) || dcounter.equals(dend))
                {
                    dates.add(sdf.format(dcounter));

                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    dcounter = cal.getTime();
                }
            }
            catch (Exception ex) {}

            return dates;

        }

        public static Calendar getCalendarDate(String date)
        {
            Calendar cal =  Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                cal.setTime(sdf.parse(date));
            }catch (Exception ex) {}

            return  cal;
        }

        public static String getTimeDifference(String starttime, String endtime)
        {
            Calendar cal = Calendar.getInstance();
            String[] timeparts = starttime.split(":");
            cal.set(Calendar.HOUR,Integer.parseInt(timeparts[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(timeparts[1]));

           java.util.Date start = cal.getTime();

            timeparts = endtime.split(":");
            cal.set(Calendar.HOUR,Integer.parseInt(timeparts[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(timeparts[1]));

            java.util.Date end = cal.getTime();

            long durationInSeconds  = TimeUnit.MILLISECONDS.toSeconds(end.getTime() - start.getTime());



            long sec = (durationInSeconds >= SECONDS_IN_A_MINUTE) ? durationInSeconds % SECONDS_IN_A_MINUTE : durationInSeconds;
            long min = (durationInSeconds /= SECONDS_IN_A_MINUTE) >= MINUTES_IN_AN_HOUR ? durationInSeconds%MINUTES_IN_AN_HOUR : durationInSeconds;
            long hrs = (durationInSeconds /= MINUTES_IN_AN_HOUR) >= HOURS_IN_A_DAY ? durationInSeconds % HOURS_IN_A_DAY : durationInSeconds;
            long days = (durationInSeconds /= HOURS_IN_A_DAY) >= DAYS_IN_A_MONTH ? durationInSeconds % DAYS_IN_A_MONTH : durationInSeconds;
            long months = (durationInSeconds /=DAYS_IN_A_MONTH) >= MONTHS_IN_A_YEAR ? durationInSeconds % MONTHS_IN_A_YEAR : durationInSeconds;
            long years = (durationInSeconds /= MONTHS_IN_A_YEAR);

            return getDuration(sec,min,hrs,days,months,years);

        }

        private static String getDuration(long secs, long mins, long hrs, long days, long months, long years)
        {
            StringBuffer sb = new StringBuffer();
            String EMPTY_STRING = "";
            sb.append(years > 0 ? years + (years > 1 ? " years " : " year "): EMPTY_STRING);
            sb.append(months > 0 ? months + (months > 1 ? " months " : " month "): EMPTY_STRING);
            sb.append(days > 0 ? days + (days > 1 ? " days " : " day "): EMPTY_STRING);
            sb.append(hrs > 0 ? hrs + (hrs > 1 ? " hours " : " hour "): EMPTY_STRING);
            sb.append(mins > 0 ? mins + (mins > 1 ? " minutes " : " minute "): EMPTY_STRING);
            sb.append(secs > 0 ? secs + (secs > 1 ? " seconds " : " second "): EMPTY_STRING);
            return sb.toString();
        }


        public static String[] getMonthBeginEndDates(Calendar month, boolean allowolddates)
        {
            if(month == null)
            {
                return null;
            }

            month.set(Calendar.DATE, 1);
            java.util.Date firstDateOfMonth = month.getTime();


            if(!allowolddates && month.getTime().compareTo(Calendar.getInstance().getTime()) < 0)
            {
                firstDateOfMonth = Calendar.getInstance().getTime();
            }

            month.set(Calendar.DATE, month.getActualMaximum(Calendar.DAY_OF_MONTH));

            java.util.Date lastDateOfMonth = month.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(!allowolddates && sdf.format(month.getTime()).compareTo(sdf.format(Calendar.getInstance().getTime()))==0){
                return new String[] {
                        sdf.format(firstDateOfMonth),
                        sdf.format(lastDateOfMonth)
                };

            }else if(!allowolddates && month.getTime().compareTo(Calendar.getInstance().getTime()) < 0)
            {
                return null;
            }



            return new String[] {
                    sdf.format(firstDateOfMonth),
                    sdf.format(lastDateOfMonth)
            };
        }

    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int getTagValue(Object tag)
    {
        if(null == tag)
        {
            return  0;
        }

       return Integer.parseInt(tag.toString());
    }

    public static Integer getTagValue(Object tag, boolean nullable)
    {
        if(null == tag || tag.getClass() != Integer.class )
        {
            if(nullable)
            {
                return  null;
            }
            return 0;
        }

        return Integer.parseInt(tag.toString());
    }

    public static String getCharTagValue(Object tag, boolean nullable)
    {
        if(null == tag )
        {
            if(nullable)
            {
                return  null;
            }
            return "";
        }

        return tag.toString();
    }

    public static Double getDoubleValue(Object tag, boolean nullable)
    {
        if(null == tag  || (tag.toString().isEmpty()))
        {
            if(nullable)
            {
                return  null;
            }
            return 0d;
        }

        return Double.parseDouble(tag.toString());
    }

    public static String getTextValue(Editable tag, boolean nullable)
    {
        if(null == tag  || (tag.toString().isEmpty()))
        {
            if(nullable)
            {
                return  null;
            }
            return "";
        }

        return tag.toString();
    }

    public static void setDropdownValue(BasicObject obj, View v)
    {
        if(v.getClass() == AppCompatEditText.class)
        {
            AppCompatEditText txt = (AppCompatEditText) v;
            txt.setText(obj.getName());
            txt.setTag(obj.getId());
        }
    }

    public static void setDropdownValue(OtherBasicObject obj, View v)
    {
        if(v.getClass() == AppCompatEditText.class)
        {
            AppCompatEditText txt = (AppCompatEditText) v;
            txt.setText(obj.getName());
            txt.setTag(obj.getId());
        }
    }

    public static void setNumericValue(Object obj, View v)
    {
        if(null == obj)
        {
            return;
        }

        if(v.getClass() == AppCompatEditText.class)
        {
            AppCompatEditText txt = (AppCompatEditText) v;
            txt.setText(obj.toString());
        }
    }

    public static BasicObject getBaseObjectByValue(List<BasicObject> items, Integer id)
    {
        if(items == null || id == null || id == 0)
        {
            return new BasicObject();
        }

        for(BasicObject obj : items)
        {
            if(obj.getId() == id)
            {
                return obj;
            }
        }

        return new BasicObject();

    }

    public static OtherBasicObject getBaseObjectByValue(List<OtherBasicObject> items, String id)
    {
        if(items == null || id == null || id.isEmpty())
        {
            return new OtherBasicObject();
        }

        for(OtherBasicObject obj : items)
        {
            if(obj.getId().equalsIgnoreCase(id))
            {
                return obj;
            }
        }

        return new OtherBasicObject();

    }


    public static BasicObject getDistrictByValue(List<District> items, Integer id)
    {
        if(items == null || id == null || id == 0)
        {
            return new BasicObject();
        }

        for(BasicObject obj : items)
        {
            if(obj.getId() == id)
            {
                return obj;
            }
        }

        return new BasicObject();

    }

    public static BasicObject getTownByValue(List<Town> items, Integer id)
    {
        if(items == null || id == null || id == 0)
        {
            return new BasicObject();
        }

        for(BasicObject obj : items)
        {
            if(obj.getId() == id)
            {
                return obj;
            }
        }

        return new BasicObject();

    }

    public static BasicObject getDenominationByValue(List<Denomination> items, Integer id)
    {
        if(items == null || id == null || id == 0)
        {
            return new BasicObject();
        }

        for(BasicObject obj : items)
        {
            if(obj.getId() == id)
            {
                return obj;
            }
        }

        return new BasicObject();

    }

    public static int getAnswerTypeColor(int answertypeid)
    {
        double converted = 0;
        if(AppConstants.REVIEW_ANSWER_MAX == null)
        {
            converted = (float)answertypeid / 5.0 * 100;
        }
        else
        {
            for(Reviewanswer ans : AppConstants.CACHE_REVIEW_ANSWERS)
            {
                    if(ans.getId() == answertypeid)
                    {
                        converted = (float)ans.getRate() / AppConstants.REVIEW_ANSWER_MAX * 100;
                    }
            }

        }

        return getRatingColor(converted);
    }

    public static int getRatingColor(double rate)
    {
        int reviewcolor = 0xff00cd00;
        if (rate >= 0 && rate <= 20)
        {
            reviewcolor = 0xffff6347;
        }
        else if (rate >= 21 && rate <= 40)
        {
            reviewcolor = 0xffd02090;
        }
        else if (rate >= 41 && rate <= 60)
        {
            reviewcolor = 0xff8b2252;
        }
        else if (rate >= 61 && rate <= 70)
        {
            reviewcolor = 0xffeeee00;
        }
        else if (rate >= 71 && rate <= 80)
        {
            reviewcolor = 0xffcdcd00;
        }
        else if (rate >= 81 && rate <= 90)
        {
            reviewcolor = 0xffadff2f;
        }

        return reviewcolor;
    }

    public static String getPlural(long value, String singular)
    {
        if( singular == null)
        {
            return null;
        }
        if( singular.length() == 1 )
        {
            return  singular;
        }

        if( value <= 1)
        {
            return  singular;
        }
        else
        {
            char lastchar = singular.toLowerCase().charAt(singular.length() -1);
            if(lastchar == 111 )
            {
                return singular + "es";
            }
            else if (lastchar == 121)
            {
                return singular.substring(0,singular.length() -1) + "ies";
            }
            else
            {
                return singular + "s";
            }
        }
    }

    public static String getSpecialtyByID(int id)
    {
        for(BasicObject obj : AppConstants.CACHE_SPECIALTIES)
        {
            if( obj.getId() == id)
            {
                return obj.getName();
            }
        }

        return null;
    }

}
