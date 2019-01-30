package com.safedoctor.safedoctor.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.TokenModel;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.localpersistance.Appointmenttbl;
import com.safedoctor.safedoctor.localpersistance.Detailtbl;

import java.util.HashMap;

import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by beulahana on 09/01/2018.
 */

public class SessionManagement {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME ="Safedoktor";
    private static final String IS_LOGIN="isloggedin";
    private static final String NOTI_STATUS="notistatus";

    public static final String KEY_TOKEN_TYPE = "tokentype";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_CREATED_DATE = "datecreated";
    public static final String KEY_EXPIRES_IN = "expires";

    public static final String KEY_PATIENT_ID = "patientid";
    public static final String KEY_PATIENT_PHONE = "patientphone";
    public static final String KEY_PATIENT_FN = "patientfn";
    public static final String KEY_PATIENT_LN = "patientln";
    public static final String KEY_PATIENT_MN = "patientmn";
    public static final String KEY_PATIENT_FULLN = "patientfullname";

    public static final String KEY_PATIENT_IMAGE = "patientimage";
    public static final String KEY_PATIENT_DOB = "patientdob";
    public static final String KEY_PATIENT_EMAIL = "patientemail";
    public static final String KEY_PATIENT_HEIGHT = "patientheight";
    public static final String KEY_PATIENT_WEIGHT = "patientweight";


    public static final String KEY_PATIENT_GGID = "patientggid";
    public static final String KEY_PATIENT_Street = "patientstreet";
    public static final String KEY_PATIENT_Address = "patientaddress";
    public static final String KEY_PATIENT_hme = "patienthme";
    public static final String KEY_PATIENT_hmetel = "patienthmetel";
    public static final String KEY_PATIENT_wrkstreet = "patientwrkstreet";


    public static final String KEY_PATIENT_Worktelno = "patientWorktelno";
    public static final String KEY_PATIENT_Idnumber = "patientIdnumber";
    public static final String KEY_PATIENT_Bloodgroup = "patientBloodgroup";
    public static final String KEY_PATIENT_Title = "patientTitle";
    public static final String KEY_PATIENT_Maritalstatus = "patientMaritalstatus";

    public static final String KEY_PATIENT_Regionid = "patientRegionid";
    public static final String KEY_PATIENT_District = "patientDistrict";
    public static final String KEY_PATIENT_Town = "patientTown";
    public static final String KEY_PATIENT_Homeregionid = "patientHomeregionid";
    public static final String KEY_PATIENT_Homedistrictid = "patientHomedistrictid";


    public static final String KEY_PATIENT_Hometownid = "patientHometownid";
    public static final String KEY_PATIENT_Workregionid = "patientWorkregionid";
    public static final String KEY_PATIENT_Workdistrictid = "Workdistrictid";
    public static final String KEY_PATIENT_Worktownid = "Worktownid";
    public static final String KEY_PATIENT_Identificationtypeid = "Identificationtypeid";
    public static final String KEY_PATIENT_Ethnicityid = "Ethnicityid";

    public static final String KEY_PATIENT_Nationalityid = "Nationalityid";


    public static final String KEY_PATIENT_Occupationid = "Occupationid";
    public static final String KEY_PATIENT_Educationallevelid = "Educationallevelid";
    public static final String KEY_PATIENT_Religionid = "Religionid";
    public static final String KEY_PATIENT_Denominationid = "Denominationid";

    public static final String KEY_PATIENT_Regtime = "Regtime";
    public static final String KEY_PATIENT_Createtime = "Createtime";
    public static final String KEY_PATIENT_Servertime = "Servertime";
    public static final String KEY_PATIENT_latitude = "latitude";
    public static final String KEY_PATIENT_longitude = "longitude";
    public static final String KEY_PATIENT_smsalert = "smsalert";


    public static final String KEY_PATIENT_islocked = "islocked";

    public static final String KEY_PATIENT_accountnumber = "accountnumber";
    public static final String KEY_PATIENT_cellphoneno = "cellphoneno";
    public static final String KEY_PATIENT_isactive = "isactive";
    public static final String KEY_PATIENT_registrationstatus = "registrationstatus";
    public static final String KEY_PATIENT_patientcategorytypeid = "patientcategorytypeid";
    public static final String KEY_PATIENT_patientstatusid = "patientstatusid";









    public SessionManagement( Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();


    }

    public void setNotificationStatus(boolean status){
        editor.putBoolean(NOTI_STATUS,status);
        editor.commit();

    }

    public boolean getNotificationStatus(){
        Boolean n=sharedPreferences.getBoolean(NOTI_STATUS,true);
        return n;
    }

    public void createLoginSession(TokenModel tokenModel){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //token model
        editor.putString(KEY_TOKEN_TYPE, tokenModel.getTokenType());
        editor.putString(KEY_TOKEN, tokenModel.getToken());
        editor.putString(KEY_CREATED_DATE, tokenModel.getCreatedDate());
        editor.putString(KEY_EXPIRES_IN, tokenModel.getExpiresOn());

        // commit changes
        editor.commit();
        Log.e("SharedPreferences","shared preferences saved");
    }

    public void createPatientDetail(PatientModel patient){
        editor.putString(KEY_PATIENT_PHONE,patient.getPhonenumber());
        editor.putString(KEY_PATIENT_FN,patient.getFirstname());
        editor.putString(KEY_PATIENT_LN,patient.getLastname());
        editor.putString(KEY_PATIENT_MN,patient.getMiddlename()==null?"":patient.getMiddlename());
        editor.putString(KEY_PATIENT_DOB,patient.getDateofbirth());
        editor.putString(KEY_PATIENT_EMAIL,patient.getEmail());
        editor.putString(KEY_PATIENT_WEIGHT,String.valueOf(patient.getWeight()!=null?patient.getWeight():0));
        editor.putString(KEY_PATIENT_HEIGHT,String.valueOf(patient.getHeight()!=null?patient.getHeight():0));
        editor.putString(KEY_PATIENT_FULLN,StringUtils.join(" ",patient.getFirstname(),patient.getMiddlename(),patient.getLastname()));
        editor.putString(KEY_PATIENT_Street,patient.getStreetaddress());
        editor.putString(KEY_PATIENT_Address,patient.getAddress());
        editor.putString(KEY_PATIENT_hme,patient.getHomestreet());
        editor.putString(KEY_PATIENT_hmetel,patient.getHometelno());
        editor.putString(KEY_PATIENT_wrkstreet,patient.getWorkstreet());
        editor.putString(KEY_PATIENT_Worktelno,patient.getWorktelno());
        editor.putString(KEY_PATIENT_Idnumber,patient.getIdnumber());
        editor.putString(KEY_PATIENT_Regtime,patient.getRegistrationtime());
        editor.putString(KEY_PATIENT_Createtime,patient.getCreatetime());
        editor.putString(KEY_PATIENT_Servertime,patient.getServertime());
        editor.putString(KEY_PATIENT_longitude,patient.getLocationlongitude());
        editor.putString(KEY_PATIENT_longitude,patient.getLocationlatitude());
        editor.putBoolean(KEY_PATIENT_smsalert,patient.getSmsalert());
        editor.putBoolean(KEY_PATIENT_islocked,patient.getIslocked());
        editor.putString(KEY_PATIENT_accountnumber,patient.getAccountnumber());
        editor.putString(KEY_PATIENT_cellphoneno,patient.getCellphoneno());
        editor.putBoolean(KEY_PATIENT_isactive,patient.getIsactive());
        editor.putString(KEY_PATIENT_registrationstatus,patient.getRegistrationstatus());

        editor.putInt(KEY_PATIENT_ID,patient.getPatientid()!=null?patient.getPatientid():0);


        editor.putInt(KEY_PATIENT_Maritalstatus,patient.getMaritalstatusid()!=null?patient.getMaritalstatusid():0);
        editor.putInt(KEY_PATIENT_GGID,patient.getGendergroupid()!=null?patient.getGendergroupid():0);
        editor.putInt(KEY_PATIENT_Identificationtypeid,patient.getIdentificationtypeid()!=null?patient.getIdentificationtypeid():0);
        editor.putInt(KEY_PATIENT_Nationalityid,patient.getNationalityid()!=null?patient.getNationalityid():0);
        editor.putInt(KEY_PATIENT_Ethnicityid,patient.getEthnicityid()!=null?patient.getEthnicityid():0);
        editor.putInt(KEY_PATIENT_Occupationid,patient.getOccupationid()!=null?patient.getOccupationid():0);
        editor.putInt(KEY_PATIENT_Educationallevelid,patient.getEducationallevelid()!=null?patient.getEducationallevelid():0);
        editor.putInt(KEY_PATIENT_Religionid,patient.getReligionid()!=null?patient.getReligionid():0);
        editor.putInt(KEY_PATIENT_Denominationid,patient.getDenominationid()!=null?patient.getDenominationid():0);
        editor.putInt(KEY_PATIENT_Title,patient.getTitleid()!=null?patient.getTitleid():0);
        editor.putInt(KEY_PATIENT_Regionid,patient.getRegionid()!=null?patient.getRegionid():0);
        editor.putInt(KEY_PATIENT_District,patient.getDistrictid()!=null?patient.getDistrictid():0);
        editor.putInt(KEY_PATIENT_Town,patient.getTownid()!=null?patient.getTownid():0);
        editor.putInt(KEY_PATIENT_Homeregionid,patient.getHomeregionid()!=null?patient.getHomeregionid():0);
        editor.putInt(KEY_PATIENT_Homedistrictid,patient.getHomedistrictid()!=null?patient.getHomedistrictid():0);
        editor.putInt(KEY_PATIENT_Hometownid,patient.getHometownid()!=null?patient.getHometownid():0);
        editor.putInt(KEY_PATIENT_Workregionid,patient.getWorkregionid()!=null?patient.getWorkregionid():0);
        editor.putInt(KEY_PATIENT_Workdistrictid,patient.getWorkdistrictid()!=null?patient.getWorkdistrictid():0);
        editor.putInt(KEY_PATIENT_Worktownid,patient.getWorktownid()!=null?patient.getWorktownid():0);
        editor.putInt(KEY_PATIENT_Bloodgroup,patient.getBloodgroupid()!=null?patient.getBloodgroupid():0);
        editor.putInt(KEY_PATIENT_patientcategorytypeid,patient.getPatientcategorytypeid()!=null?patient.getPatientcategorytypeid():0);
        editor.putInt(KEY_PATIENT_patientstatusid,patient.getPatientstatusid()!=null?patient.getPatientstatusid():0);






        editor.commit();
    }

    public void setProfileImage(String image){
        editor.putString(KEY_PATIENT_IMAGE,image);
        editor.commit();
    }



    public String getProfileImage(){
        return sharedPreferences.getString(KEY_PATIENT_IMAGE,"");
    }

    public PatientModel getPatientDetail(){

        PatientModel patientModel=new PatientModel();


        patientModel.setPatientid(sharedPreferences.getInt(KEY_PATIENT_ID,0)==0?0:sharedPreferences.getInt(KEY_PATIENT_ID,0));

        patientModel.setPhonenumber(sharedPreferences.getString(KEY_PATIENT_PHONE,null));
        patientModel.setFirstname(sharedPreferences.getString(KEY_PATIENT_FN,null));
        patientModel.setLastname(sharedPreferences.getString(KEY_PATIENT_LN,null));
        patientModel.setMiddlename(sharedPreferences.getString(KEY_PATIENT_MN,null));

        patientModel.setDateofbirth(sharedPreferences.getString(KEY_PATIENT_DOB,null));
        patientModel.setEmail(sharedPreferences.getString(KEY_PATIENT_EMAIL,null));
        patientModel.setWeight(Double.valueOf(sharedPreferences.getString(KEY_PATIENT_WEIGHT,"0").length()>0?Double.valueOf(sharedPreferences.getString(KEY_PATIENT_WEIGHT,"0")):0));
        patientModel.setHeight(Double.valueOf(sharedPreferences.getString(KEY_PATIENT_HEIGHT,"0")));

        patientModel.setGendergroupid(sharedPreferences.getInt(KEY_PATIENT_GGID,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_GGID,0));
        patientModel.setStreetaddress(sharedPreferences.getString(KEY_PATIENT_Street,null));
        patientModel.setAddress(sharedPreferences.getString(KEY_PATIENT_Address,null));
        patientModel.setHomestreet(sharedPreferences.getString(KEY_PATIENT_hme,null));
        patientModel.setHometelno(sharedPreferences.getString(KEY_PATIENT_hmetel,null));
        patientModel.setWorkstreet(sharedPreferences.getString(KEY_PATIENT_wrkstreet,null));
        patientModel.setWorktelno(sharedPreferences.getString(KEY_PATIENT_Worktelno,null));

        patientModel.setIdnumber(sharedPreferences.getString(KEY_PATIENT_Idnumber,null));

        patientModel.setTitleid(sharedPreferences.getInt(KEY_PATIENT_GGID,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_GGID,0));
        patientModel.setRegionid(sharedPreferences.getInt(KEY_PATIENT_Regionid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Regionid,0));
        patientModel.setDistrictid(sharedPreferences.getInt(KEY_PATIENT_District,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_District,0));
        patientModel.setTownid(sharedPreferences.getInt(KEY_PATIENT_Town,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Town,0));


        patientModel.setHomeregionid(sharedPreferences.getInt(KEY_PATIENT_Homeregionid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Homeregionid,0));
        patientModel.setHomedistrictid(sharedPreferences.getInt(KEY_PATIENT_Homedistrictid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Homedistrictid,0));
        patientModel.setHometownid(sharedPreferences.getInt(KEY_PATIENT_Hometownid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Hometownid,0));
        patientModel.setWorkregionid(sharedPreferences.getInt(KEY_PATIENT_Workregionid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Workregionid,0));
        patientModel.setWorkdistrictid(sharedPreferences.getInt(KEY_PATIENT_Workdistrictid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Workdistrictid,0));
        patientModel.setWorktownid(sharedPreferences.getInt(KEY_PATIENT_Worktownid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Worktownid,0));

        patientModel.setIdentificationtypeid(sharedPreferences.getInt(KEY_PATIENT_Identificationtypeid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Identificationtypeid,0));
        patientModel.setNationalityid(sharedPreferences.getInt(KEY_PATIENT_Nationalityid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Nationalityid,0));
        patientModel.setEthnicityid(sharedPreferences.getInt(KEY_PATIENT_Ethnicityid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Ethnicityid,0));
        patientModel.setOccupationid(sharedPreferences.getInt(KEY_PATIENT_Occupationid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Occupationid,0));
        patientModel.setEducationallevelid(sharedPreferences.getInt(KEY_PATIENT_Educationallevelid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Educationallevelid,0));
        patientModel.setReligionid(sharedPreferences.getInt(KEY_PATIENT_Religionid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Religionid,0));
        patientModel.setDenominationid(sharedPreferences.getInt(KEY_PATIENT_Denominationid,0)==0?null:sharedPreferences.getInt(KEY_PATIENT_Denominationid,0));
        //patientModel.setGendergroupid(sharedPreferences.getInt(KEY_PATIENT_GGID,0));

        patientModel.setRegistrationtime(sharedPreferences.getString(KEY_PATIENT_Regtime,null));
        patientModel.setCreatetime(sharedPreferences.getString(KEY_PATIENT_Createtime,null));
        patientModel.setServertime(sharedPreferences.getString(KEY_PATIENT_Servertime,null));
        patientModel.setLocationlatitude(sharedPreferences.getString(KEY_PATIENT_latitude,null));
        patientModel.setLocationlongitude(sharedPreferences.getString(KEY_PATIENT_longitude,null));

        patientModel.setSmsalert(sharedPreferences.getBoolean(KEY_PATIENT_smsalert,false));

        if(sharedPreferences.getInt(KEY_PATIENT_Bloodgroup,0)==0){
            patientModel.setBloodgroupid(null);
        }else{
            patientModel.setBloodgroupid(sharedPreferences.getInt(KEY_PATIENT_Bloodgroup,0));

        }




        if(sharedPreferences.getInt(KEY_PATIENT_Maritalstatus,0)==0){
            patientModel.setMaritalstatusid(null);
        }else{
            patientModel.setMaritalstatusid(sharedPreferences.getInt(KEY_PATIENT_Maritalstatus,0));
        }

        patientModel.setAccountnumber(sharedPreferences.getString(KEY_PATIENT_accountnumber,null));
        patientModel.setCellphoneno(sharedPreferences.getString(KEY_PATIENT_cellphoneno,null));
        patientModel.setIslocked(sharedPreferences.getBoolean(KEY_PATIENT_islocked,false));

        patientModel.setIsactive(sharedPreferences.getBoolean(KEY_PATIENT_isactive,false));
        patientModel.setRegistrationstatus(sharedPreferences.getString(KEY_PATIENT_registrationstatus,null));

        if(sharedPreferences.getInt(KEY_PATIENT_patientcategorytypeid,0)==0){
            patientModel.setPatientcategorytypeid(null);
        }else{
            patientModel.setPatientcategorytypeid(sharedPreferences.getInt(KEY_PATIENT_patientcategorytypeid,0));

        }

        if(sharedPreferences.getInt(KEY_PATIENT_patientstatusid,0)==0){
            patientModel.setPatientcategorytypeid(null);
        }else{
            patientModel.setPatientstatusid(sharedPreferences.getInt(KEY_PATIENT_patientstatusid,0));

        }



        return patientModel;


    }


    public HashMap<String, String> getLoginSession(){
        HashMap<String, String> session = new HashMap<String, String>();

        session.put(KEY_TOKEN, sharedPreferences.getString(KEY_TOKEN, null));
        session.put(KEY_CREATED_DATE, sharedPreferences.getString(KEY_CREATED_DATE, null));
        session.put(KEY_TOKEN_TYPE, sharedPreferences.getString(KEY_TOKEN_TYPE, null));
        session.put(KEY_EXPIRES_IN, sharedPreferences.getString(KEY_EXPIRES_IN, null));

        return session;
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logoutUserPermanent(){
        AppConstants.maxbooked=0;

        new Delete().from(Appointmenttbl.class).execute();
        new Delete().from(Detailtbl.class).execute();
        ActiveAndroid.getDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME='Appointment'");
        ActiveAndroid.getDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME='Detail'");

        editor.clear();
        editor.commit();

        Intent i = new Intent(context, FormLogin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);





    }

    public void logoutSessionExpired(){
        Toast.makeText(context, "Your session has expired please login again", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, FormLogin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }


}
