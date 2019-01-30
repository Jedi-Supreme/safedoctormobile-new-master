package com.safedoctor.safedoctor.Api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.safedoctor.safedoctor.Adapter.BaseObjectAdapter;
import com.safedoctor.safedoctor.Adapter.OtherBasicObjectAdapter;
import com.safedoctor.safedoctor.Adapter.PartnerHospitalsAdapter;
import com.safedoctor.safedoctor.Adapter.ProfileBaseObjectAdapter;
import com.safedoctor.safedoctor.Adapter.SignalMessageAdapter;
import com.safedoctor.safedoctor.Adapter.SlotAdapter;
import com.safedoctor.safedoctor.Model.Chat;
import com.safedoctor.safedoctor.Model.ChatMessage;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.DataModel;
import com.safedoctor.safedoctor.Model.LoginDataModel;
import com.safedoctor.safedoctor.Model.LoginModel;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.PatientPhotoModel;
import com.safedoctor.safedoctor.Model.Peripheral;
import com.safedoctor.safedoctor.Model.Referral;
import com.safedoctor.safedoctor.Model.SignalMessage;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.TokenModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.Denomination;
import com.safedoctor.safedoctor.Model.responses.District;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.DrugOut;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.Model.responses.OtherBasicObject;
import com.safedoctor.safedoctor.Model.responses.PatientProfileFormDataOut;
import com.safedoctor.safedoctor.Model.responses.Patientcontactperson;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedication;
import com.safedoctor.safedoctor.Model.responses.ProfileBasicObject;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Reviewtype;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.Model.responses.Town;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.ActivityLandingPage;
import com.safedoctor.safedoctor.UI.Activities.Consultation;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.UI.Fragment.LandingPage;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.App;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.StringUtils;
import com.safedoctor.safedoctor.widget.LineItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.UI.Fragment.PartnerHospitals.sortList;
import static com.safedoctor.safedoctor.Utils.App.context;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.formateDate;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getNow;

/**
 * Created by Stevkkys on 9/8/2017.
 */

public class Common {
    private Context context;

    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<LoginDataModel>> swagLoginResponse;
    private List<LoginDataModel> loginData;
    private PatientModel patient;
    private TokenModel token;
    private OnTaskCompleted listener;
    private static SessionManagement sessionManagement=new SessionManagement(App.context);

    private  long remainingsec;
    public static Long CurrentConsultationID;



    public Common(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.mSafeDoctorService = ApiUtils.getAPIService();
        this.listener = listener;
    }

    public Common(Context context) {
        this.context = context;
        this.mSafeDoctorService = ApiUtils.getAPIService();
    }

    public void setOnTaskCompletedListener(OnTaskCompleted listener) {
        this.listener = listener;
    }

    public void performBackgroundSync() {
        getPeripherals();
        getSpecialties();
        getPatientFormData();
        getPatientContactPersons();
        getNextofKins();
        getMedications();
        getReviewAnswerMax();
    }

    public interface onConsultationStart{
        public void onStart(Long consultationid);
    }


    public void displayTownList(Context context, List<Town> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        List<BasicObject> objlist = new ArrayList<BasicObject>();
        for (Town t : items) {
            objlist.add(new BasicObject(t.getId(), t.getName()));
        }

        displayDropDownList(context, objlist, v, title, dependants);
    }

    public void displayDistrictList(Context context, List<District> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        List<BasicObject> objlist = new ArrayList<BasicObject>();
        for (District t : items) {
            objlist.add(new BasicObject(t.getId(), t.getName()));
        }

        displayDropDownList(context, objlist, v, title, dependants);
    }

    public void displayDenominationList(Context context, List<Denomination> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        List<BasicObject> objlist = new ArrayList<BasicObject>();
        for (Denomination t : items) {
            objlist.add(new BasicObject(t.getId(), t.getName()));
        }

        displayDropDownList(context, objlist, v, title, dependants);
    }

    public void displayDrugList(Context context, List<DrugOut> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        List<OtherBasicObject> objlist = new ArrayList<OtherBasicObject>();
        for (DrugOut t : items) {
            objlist.add(new OtherBasicObject(t.getDrugid(), t.getName()));
        }

        displayCharBasedDropDownList(context, objlist, v, title, dependants);
    }

    public void displayCharBasedDropDownList(Context context, List<OtherBasicObject> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        if (items.size() == 0) {
            return;
        }

        Context c = this.context;
        TextView dropdowntitle;

        if (null != context) {
            c = context;
        }

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_basicobject);
        dialog.setCancelable(true);

        dropdowntitle = (TextView) dialog.findViewById(R.id.dropdowntitle);

        if (null != title) {
            dropdowntitle.setText(title);
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        OtherBasicObjectAdapter _adapter = new OtherBasicObjectAdapter(c, items);
        recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new OtherBasicObjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OtherBasicObject obj, int position) {
                resetDependantList(dependants);

                v.setTag(obj.getId());
                v.setText(obj.getName());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void displaySlotList(Context context, List<TimeSlot> items, String title, String specialtytext) {
        if(items==null)return;
        if (items.size() == 0) return;

        /*
        SimpleDateFormat otimeFormatter = new SimpleDateFormat("HH:mm:s");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date tday=new Date(System.currentTimeMillis());
        String currtime=otimeFormatter.format(tday);

        List<TimeSlot> newlist=new ArrayList<>();//to sort slots that are before current time

        Date cdate=new Date();
        try {
            String curdate=dateFormatter.format(cdate);
            Date currdate=dateFormatter.parse(curdate);
            Date slotdate=dateFormatter.parse(items.get(0).getDate());
            if(currdate.equals(slotdate)){
                for (TimeSlot slot:items) {
                    try {
                        Date slotendtime=otimeFormatter.parse(slot.getEndtime());
                        Date now=otimeFormatter.parse(currtime);
                        if(!slotendtime.before(now)){
                            newlist.add(slot);

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }else{
                newlist=items;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/






        Context c = this.context;
        TextView dropdowntitle;

        if (null != context) {
            c = context;
        }

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_basicobject);
        dialog.setCancelable(true);

        dropdowntitle = (TextView) dialog.findViewById(R.id.dropdowntitle);

        String newtitle="";
        if(items.size()==0){
            newtitle="No slots available within this time.";
        }else{
            newtitle=title;
        }
        dropdowntitle.setText(newtitle);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        SlotAdapter _adapter = new SlotAdapter(c, items, specialtytext);
        recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new SlotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TimeSlot obj, int position) {
                dialog.dismiss();

                if (listener != null) {
                    listener.onTaskCompleted(obj);
                }
            }
        });

        //dialog.getWindow().setType(WindowManager.LayoutParams. TYPE_SYSTEM_ALERT);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void displayDropDownList(Context context, List<BasicObject> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        if (items.size() == 0) {
            return;
        }

        Context c = this.context;
        TextView dropdowntitle;

        if (null != context) {
            c = context;
        }

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_basicobject);
        dialog.setCancelable(true);

        dropdowntitle = (TextView) dialog.findViewById(R.id.dropdowntitle);

        if (null != title) {
            dropdowntitle.setText(title);
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        BaseObjectAdapter _adapter = new BaseObjectAdapter(c, items);
        recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new BaseObjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BasicObject obj, int position) {

                resetDependantList(dependants);

                v.setTag(obj.getId());
                v.setText(obj.getName());
                dialog.dismiss();
            }
        });

        //dialog.getWindow().setType(WindowManager.LayoutParams. TYPE_SYSTEM_ALERT);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void displayDoctorsList(Context context, List<DoctorOutObj> items, final AppCompatEditText v, String title, final List<AppCompatEditText> dependants) {
        List<OtherBasicObject> objlist = new ArrayList<OtherBasicObject>();
        for (DoctorOutObj t : items) {
            OtherBasicObject obj = new OtherBasicObject(t.getDoctor().getId(), StringUtils.join(" ", t.getDoctor().getFirstname(), t.getDoctor().getOthername(), t.getDoctor().getLastname()));
            if (t.getPicture() != null) {
                obj.image = t.getPicture().getPhoto();
            }
            obj.slotcount=t.getSlotcount();

            objlist.add(obj);
        }

        displayCharBasedDropDownList(context, objlist, v, title, dependants);
    }

    public void displayPopUpList(Context context, List<ProfileBasicObject> items, String title, Drawable icondrawable) {
        if (items.size() == 0) {
            return;
        }

        Context c = this.context;
        TextView dropdowntitle;
        ImageView icon;
        ImageButton bt_close;

        if (null != context) {
            c = context;
        }

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_popoup_list);
        dialog.setCancelable(true);

        dropdowntitle = (TextView) dialog.findViewById(R.id.dropdowntitle);
        icon = (ImageView) dialog.findViewById(R.id.icon);
        bt_close = (ImageButton) dialog.findViewById(R.id.bt_close);

        if (null != title) {
            dropdowntitle.setText(title);
        }

        icon.setImageDrawable(icondrawable);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        recyclerView.addItemDecoration(new LineItemDecoration(c, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        ProfileBaseObjectAdapter _adapter = new ProfileBaseObjectAdapter(c, items);
        recyclerView.setAdapter(_adapter);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void resetDependantList(List<AppCompatEditText> items) {
        if (null == items) {
            return;
        }
        for (AppCompatEditText v : items) {
            v.setTag(null);
            v.setText("");
        }
    }

    public Integer getVitalTypeID(String deviceid)
    {
        if(AppConstants.CACHE_PERIPHERALS != null)
        {
            for( Peripheral p :  AppConstants.CACHE_PERIPHERALS)
            {
                if(p.getId().equalsIgnoreCase(deviceid))
                {
                    return p.getVitaltypeid();
                }

            }
        }

        return null;

    }


    public void getPeripherals() {
        Call<SwagResponseModel<List<Peripheral>>> call = mSafeDoctorService.getPeripherals(TokenString.tokenString);

        call.enqueue(new Callback<SwagResponseModel<List<Peripheral>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Peripheral>>> call, Response<SwagResponseModel<List<Peripheral>>> response) {
                //  Log.i("Safe", "loadServices Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {


                } else if (response.isSuccessful()) {
                    DataModel<List<Peripheral>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null && responsedata.getContent().size() > 0) {
                        AppConstants.CACHE_PERIPHERALS = responsedata.getContent();
                    }

                    listener.onTaskCompleted(true);
                } else {

                    try {
                        String error = response.errorBody().string();
                        tokenerror(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Peripheral>>> call, Throwable t) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(String username, String password) {
        LoginModel l = new LoginModel(username, password);
        login(l);
    }

    public void login(final LoginModel loginModel) {

        Call<SwagArrayResponseModel<List<LoginDataModel>>> call = mSafeDoctorService.patientLogin(loginModel);
        call.enqueue(new Callback<SwagArrayResponseModel<List<LoginDataModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Response<SwagArrayResponseModel<List<LoginDataModel>>> response) {
                Log.i("Safe", "we're in");

                if (response.isSuccessful()) {
                    Log.i("Safe", "successful");
                    swagLoginResponse = response.body();
                    loginData = swagLoginResponse.getData();

                    if (null != loginData && loginData.size() > 0) {
                        patient = loginData.get(0).getPatient();
                        token = loginData.get(0).getTokenModel();

                        sessionManagement.createLoginSession(token);
                        sessionManagement.createPatientDetail(patient);

                        AppConstants.PatientObj = patient;
                        TokenString.tokenString = token.getTokenType() + " " + token.getToken();
                        AppConstants.patientID = patient.getPatientid();
                        AppConstants.patientPhoneNumber = patient.getPhonenumber();
                        AppConstants.patientName = StringUtils.join(" ", patient.getFirstname(), patient.getMiddlename(), patient.getLastname());

                        fetchProfilePicture();
                        Log.i("Safe", "Patient number is " + AppConstants.patientPhoneNumber);

                        listener.onTaskCompleted(true);

                        AppConstants.IS_LOGIN = true;
                    }
                } else {
                    Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<LoginDataModel>>> call, Throwable t) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void fetchProfilePicture() {

        Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call = mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID);

        Log.i("Safe", mSafeDoctorService.getProfilePicture(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagArrayResponseModel<List<PatientPhotoModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Response<SwagArrayResponseModel<List<PatientPhotoModel>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    listener.onTaskCompleted(false);
                } else if (response.isSuccessful()) {
                    SwagArrayResponseModel<List<PatientPhotoModel>> photoResponse;
                    List<PatientPhotoModel> photoList;

                    photoResponse = response.body();
                    photoList = photoResponse.getData();

                    if (null != photoList && photoList.size() > 0) {
                        listener.onTaskCompleted(photoList.get(0).getPhoto());
                    } else {
                        listener.onTaskCompleted(false);
                    }
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<PatientPhotoModel>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getPatientFormData() {
        Call<SwagArrayResponseModel<List<PatientProfileFormDataOut>>> call = mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID);
        call.enqueue(new Callback<SwagArrayResponseModel<List<PatientProfileFormDataOut>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<PatientProfileFormDataOut>>> call, Response<SwagArrayResponseModel<List<PatientProfileFormDataOut>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    SwagArrayResponseModel<List<PatientProfileFormDataOut>> responsedata;

                    responsedata = response.body();

                    if (responsedata.getData() != null && responsedata.getData().size() > 0) {
                        AppConstants.CACHE_FORMDATA = responsedata.getData().get(0);

                        getAllTitles();
                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<PatientProfileFormDataOut>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getAllTitles(){
        Call<SwagResponseModel<List<BasicObject>>> call = mSafeDoctorService.getAllTitle(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<BasicObject>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<BasicObject>>> call, Response<SwagResponseModel<List<BasicObject>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    SwagResponseModel<List<BasicObject>> responsedata;

                    responsedata = response.body();

                    if (responsedata.getData() != null) {
                        AppConstants.allTitles = responsedata.getData().getContent();


                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<BasicObject>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getSpecialtyDoctors(int specialtyid) {
        if (specialtyid == 0) {
            return;
        }

        Call<SwagArrayResponseModel<List<DoctorOutObj>>> call = mSafeDoctorService.getClinicalSpecialtyDoctors(TokenString.tokenString, specialtyid);

        //Log.i("Safe", mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagArrayResponseModel<List<DoctorOutObj>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<DoctorOutObj>>> call, Response<SwagArrayResponseModel<List<DoctorOutObj>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    SwagArrayResponseModel<List<DoctorOutObj>> responsedata;

                    responsedata = response.body();

                    if (responsedata.getData() != null && responsedata.getData().size() > 0) {
                        AppConstants.CACHE_DOCTORS=new ArrayList<>();
                        for (DoctorOutObj doc:responsedata.getData()) {
                            if(!doc.getDoctor().isLocked() && doc.getDoctor().getUserprofileid()==2){
                                AppConstants.CACHE_DOCTORS.add(doc);
                            }

                        }

                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<DoctorOutObj>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void getPatientContactPersons() {
        Call<SwagResponseModel<List<Patientcontactperson>>> call = mSafeDoctorService.getContactPersons(TokenString.tokenString, AppConstants.patientID);

        //Log.i("Safe", mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagResponseModel<List<Patientcontactperson>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Patientcontactperson>>> call, Response<SwagResponseModel<List<Patientcontactperson>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    DataModel<List<Patientcontactperson>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_CONTACT_PERSONS = responsedata.getContent();
                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Patientcontactperson>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getNextofKins() {
        Call<SwagResponseModel<List<Patientcontactperson>>> call = mSafeDoctorService.getNextofKins(TokenString.tokenString, AppConstants.patientID);

        //Log.i("Safe", mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagResponseModel<List<Patientcontactperson>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Patientcontactperson>>> call, Response<SwagResponseModel<List<Patientcontactperson>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    DataModel<List<Patientcontactperson>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_NEXT_OFKINS = responsedata.getContent();
                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Patientcontactperson>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getMedications() {
        Call<SwagResponseModel<List<Patientprofilemedication>>> call = mSafeDoctorService.getMedications(TokenString.tokenString, AppConstants.patientID);

        //Log.i("Safe", mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagResponseModel<List<Patientprofilemedication>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Patientprofilemedication>>> call, Response<SwagResponseModel<List<Patientprofilemedication>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    DataModel<List<Patientprofilemedication>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_MEDICATIONS = responsedata.getContent();
                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Patientprofilemedication>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getSpecialties() {
        Call<SwagResponseModel<List<BasicObject>>> call = mSafeDoctorService.getClinicalSpecialties(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<BasicObject>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<BasicObject>>> call, Response<SwagResponseModel<List<BasicObject>>> response) {
                //  Log.i("Safe", "loadServices Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {


                } else if (response.isSuccessful()) {
                    DataModel<List<BasicObject>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null && responsedata.getContent().size() > 0) {
                        AppConstants.CACHE_SPECIALTIES = responsedata.getContent();
                        getSpecialtySlots(0);
                        getSpecialtyDoctors(AppConstants.CACHE_SPECIALTIES.get(0).getId());
                    }

                    listener.onTaskCompleted(true);
                } else {

                    try {
                        String error = response.errorBody().string();
                        tokenerror(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<BasicObject>>> call, Throwable t) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSpecialtySlots(int specialityid) {
        HashMap<String, String> queryparams = new HashMap<String, String>();
        //queryparams.put("patientid", String.valueOf(AppConstants.patientID));
        //queryparams.put("patientid", "");

        String[] monthlimits = AuxUtils.Date.getMonthBeginEndDates(Calendar.getInstance(), false);
        if (monthlimits == null) {
            return;
        }

        queryparams.put("from", monthlimits[0]);
        queryparams.put("to", monthlimits[1]);

        Call<SwagArrayResponseModel<List<TimeSlot>>> call = mSafeDoctorService.getSpecialtyAvailableServices(TokenString.tokenString, specialityid, queryparams);

        call.enqueue(new Callback<SwagArrayResponseModel<List<TimeSlot>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<TimeSlot>>> call, Response<SwagArrayResponseModel<List<TimeSlot>>> response) {
                //  Log.i("Safe", "loadServices Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    sessionManagement.logoutSessionExpired();


                } else if (response.isSuccessful()) {
                    List<TimeSlot> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null & responsedata.size() > 0) {
                        AppConstants.CACHE_SLOTS = responsedata;
                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<TimeSlot>>> call, Throwable t) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void getReviewAnswers() {

        Call<SwagResponseModel<List<Reviewanswer>>> call = mSafeDoctorService.getReviewAnswers(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<Reviewanswer>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Reviewanswer>>> call, Response<SwagResponseModel<List<Reviewanswer>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful() && response.code() == 200) {
                    DataModel<List<Reviewanswer>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_REVIEW_ANSWERS = responsedata.getContent();
                    }
                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Reviewanswer>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });

    }



    public void getReviewTypes() {

        Call<SwagResponseModel<List<Reviewtype>>> call = mSafeDoctorService.getReviewTypes(TokenString.tokenString);
        call.enqueue(new Callback<SwagResponseModel<List<Reviewtype>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Reviewtype>>> call, Response<SwagResponseModel<List<Reviewtype>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful() && response.code() == 200) {
                    DataModel<List<Reviewtype>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_REVIEW_TYPE = responsedata.getContent();
                    }
                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Reviewtype>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void getReviewAnswerMax() {
        Call<SwagResponseModel<List<Reviewanswer>>> call = mSafeDoctorService.getReviewAnswers(TokenString.tokenString);

        //Log.i("Safe", mSafeDoctorService.getFormData(TokenString.tokenString, AppConstants.patientID).request().url().toString());

        call.enqueue(new Callback<SwagResponseModel<List<Reviewanswer>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Reviewanswer>>> call, Response<SwagResponseModel<List<Reviewanswer>>> response) {
                Log.i("TheSafe", response.code() + "");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    DataModel<List<Reviewanswer>> responsedata;

                    responsedata = response.body().getData();

                    if (responsedata != null) {
                        AppConstants.CACHE_REVIEW_ANSWERS = responsedata.getContent();
                        AppConstants.REVIEW_ANSWER_MAX = 0d;

                        for (Reviewanswer ans : AppConstants.CACHE_REVIEW_ANSWERS) {
                            if (ans.getRate() > AppConstants.REVIEW_ANSWER_MAX) {
                                AppConstants.REVIEW_ANSWER_MAX = ans.getRate();
                            }
                        }

                    }

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Reviewanswer>>> call, Throwable throwable) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkBookingStatus(int bookingid) {
        Call<SwagArrayResponseModel> call = mSafeDoctorService.getBookingStatus(TokenString.tokenString, bookingid);

        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                //  Log.i("Safe", "loadServices Called");
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    listener.onTaskCompleted(false);
                } else if (response.isSuccessful()) {
                    SwagArrayResponseModel bookingStatusResponse;

                    bookingStatusResponse = response.body();

                    listener.onTaskCompleted(true);
                } else {
                    listener.onTaskCompleted(false);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable t) {
                listener.onTaskCompleted(false);
                Toast.makeText(context, context.getResources().getString(R.string.alert_networkissues), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DataModel<List<Facilityinfo>> mHospitalsList;

    /*
    * Hospital: facilityid=1
    * Pharmacy:facilityid=2
    * */

    public void getFacilities(final RecyclerView recyclerView, final String type, final Activity activity, final SwipeRefreshLayout mSwipeRefresh) {
        mSafeDoctorService.getFacilityInfo()
                .enqueue(new Callback<SwagResponseModel<List<Facilityinfo>>>() {
                    @Override
                    public void onResponse(Call<SwagResponseModel<List<Facilityinfo>>> call, Response<SwagResponseModel<List<Facilityinfo>>> response) {

                        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {

                            context.startActivity(new Intent(context, FormLogin.class));
                            ((Activity) context).finish();
                        } else if (response.isSuccessful() && response.code()==200) {
                            if(response.body().getData()!=null){
                                Common.this.mHospitalsList = response.body().getData();
                                Log.d("safe", ((Common.this.mHospitalsList.getContent()).get(0)).getName());
//                            FirstAidAdapter firstAidAdapter = new FirstAidAdapter(getActivity(), mAidList);

                                AppConstants.CACHE_FACILITY_INFO=Common.this.mHospitalsList.getContent();


                                List<Facilityinfo> hospitaltypelist=new ArrayList<>();
                                hospitaltypelist= sortList(type,AppConstants.CACHE_FACILITY_INFO,hospitaltypelist);
                                if(hospitaltypelist.size()!=0){
                                    Log.e("getFacilities","# list size "+hospitaltypelist.size());
                                    PartnerHospitalsAdapter partnerHospitalsAdapter = new PartnerHospitalsAdapter(hospitaltypelist, activity);
                                    recyclerView.setAdapter(partnerHospitalsAdapter);
                                }else{
                                    Toast.makeText(context,"No service providers available yet",Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(context,"No service providers available yet",Toast.LENGTH_LONG).show();

                            }



                        }

                        mSwipeRefresh.setRefreshing(false);


                    }

                    @Override
                    public void onFailure(Call<SwagResponseModel<List<Facilityinfo>>> call, Throwable throwable) {
                        mSwipeRefresh.setRefreshing(false);
                        throwable.printStackTrace();
                    }
                });
    }

    public void getSystemUsers(final RecyclerView recyclerView, final String type, final Activity activity, final SwipeRefreshLayout mSwipeRefresh) {
        mSafeDoctorService.getUsersInfo(TokenString.tokenString)
                .enqueue(new Callback<SwagResponseModel<List<UserAccount>>>() {
                    @Override
                    public void onResponse(Call<SwagResponseModel<List<UserAccount>>> call, Response<SwagResponseModel<List<UserAccount>>> response) {

                        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mSwipeRefresh.setRefreshing(false);
                            context.startActivity(new Intent(context, FormLogin.class));
                            ((Activity) context).finish();
                        } else if (response.isSuccessful() && response.code()==200) {
                            DataModel<List<UserAccount>> responseModel = response.body().getData();
                            List<UserAccount> userAccountList=responseModel.getContent();
                            if (listener != null) {
                                listener.onTaskCompleted(userAccountList);
                            }
                            mSwipeRefresh.setRefreshing(false);



                           // AppConstants.CACHE_SYSTEM_USERS=responseModel.getContent();



                        }else {

                            String d= null;
                            try {
                                d = response.errorBody().string();
                            Toast.makeText(context,d,Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mSwipeRefresh.setRefreshing(false);
                            Log.e("",""+d);

                        }


                    }

                    @Override
                    public void onFailure(Call<SwagResponseModel<List<UserAccount>>> call, Throwable throwable) {
                        Toast.makeText(context,"Error occurred",Toast.LENGTH_LONG).show();
                        mSwipeRefresh.setRefreshing(false);
                        throwable.printStackTrace();
                    }
                });
    }


    public void getConsultationReferral(long patientid) {
        Call<SwagArrayResponseModel<List<Referral>>> call = mSafeDoctorService.getReferral(TokenString.tokenString,patientid);

        call.enqueue(new Callback<SwagArrayResponseModel<List<Referral>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<Referral>>> call, Response<SwagArrayResponseModel<List<Referral>>> response) {
                List<Referral> referralList=new ArrayList<>();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                } else if (response.isSuccessful()) {
                    referralList =response.body().getData();

                    listener.onTaskCompleted(referralList );
                } else {
                    listener.onTaskCompleted(referralList);
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<Referral>>> call, Throwable t) {
                Toast.makeText(context,"Fetching Referrals....Error occured ",Toast.LENGTH_LONG).show();
                try{
                    Log.e("getConsultationReferral","Fetching error"+ t.getMessage());
                }catch (Exception e){

                }

            }
        });


    }


    public void getOnlineVisits(long patientid, final SwipeRefreshLayout swipeRefreshLayout,String from,String to) {
        if(to==null){
            to=getNow();
        }
        if(from==null){
            from=formateDate("2018-02-01 00:00:00");
        }

        //String from=formateDate("2018-02-01 00:00:00");
        Call<SwagArrayResponseModel<List<ConsultationProfile>>> call = mSafeDoctorService.getOnlineVisits(TokenString.tokenString,patientid,from,to);

        call.enqueue(new Callback<SwagArrayResponseModel<List<ConsultationProfile>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<ConsultationProfile>>> call, Response<SwagArrayResponseModel<List<ConsultationProfile>>> response) {
                List<ConsultationProfile> visitList=new ArrayList<>();
                if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    sessionManagement.logoutSessionExpired();


                } else if (response.isSuccessful()) {
                    visitList =response.body().getData();
                    AppConstants.CACHE_ONLINE_VISIT=visitList;

                    listener.onTaskCompleted(visitList );
                } else {
                    try {
                        //Log.e("getConsultationReferral",response.errorBody().string().toString());
                        String error=response.errorBody().string().toString();
                        Log.e("getOnlineVisits",error);
                        tokenerror(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onTaskCompleted(visitList);
                }

                if(swipeRefreshLayout!=null){
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<ConsultationProfile>>> call, Throwable t) {
                //Toast.makeText(context,"Fetching Online Visits....Network Error occured",Toast.LENGTH_LONG).show();
                try{
                    Log.e("getConsultationReferral","Fetching error"+ t.getMessage());
                }catch (Exception e){

                }

                if(swipeRefreshLayout!=null){
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });


    }

    public static void tokenerror(String error){
        if(!error.isEmpty()) {

            try {
                JSONObject jsonObject = new JSONObject(error);

                String message = jsonObject.getString("message");
                if (message.equals("Internal Server Error - Could not validate authorization token")) {
                    sessionManagement.logoutSessionExpired();
                }else if(message.equals("Internal Server Error - Token authorization validation error. Please log in again")){
                    sessionManagement.logoutSessionExpired();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


   public  void saveMessage(Chat chat){

        mSafeDoctorService.saveChat(TokenString.tokenString,chat).enqueue(new Callback<SwagArrayResponseModel<List<Chat>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<Chat>>> call, Response<SwagArrayResponseModel<List<Chat>>> response) {
                if(response.isSuccessful()){


                }else if(response.code()==HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED){
                    Toast.makeText(context, "Session expired. Login again", Toast.LENGTH_SHORT).show();

                    sessionManagement.logoutSessionExpired();

                }else{

                    try{
                        String error=response.errorBody().string();
                        if(error!=null){
                            Log.e("Common Calls saveChat",""+error);
                        }

                    }catch(Exception e){

                    }

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<Chat>>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public  void getChatMessage( long consultationid){

        mSafeDoctorService.getChat(consultationid,TokenString.tokenString).enqueue(new Callback<SwagResponseModel<List<Chat>>>() {
            @Override
            public void onResponse(Call<SwagResponseModel<List<Chat>>> call, Response<SwagResponseModel<List<Chat>>> response) {
                if(response.isSuccessful()){

                    if(response.body().getData()!=null){
                        listener.onTaskCompleted(response.body().getData().getContent());
                    }else{
                        listener.onTaskCompleted(null);
                    }



                }else if(response.code()==HttpURLConnection.HTTP_FORBIDDEN || response.code()==HttpURLConnection.HTTP_UNAUTHORIZED){
                    Toast.makeText(context, "Session expired. Login again", Toast.LENGTH_SHORT).show();

                    sessionManagement.logoutSessionExpired();

                }else{

                    try{
                        String error=response.errorBody().string();
                        if(error!=null){
                            Log.e("Common Calls getChat",""+error);
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SwagResponseModel<List<Chat>>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void  fragmentInitOnbackpressed(final Activity activity){
        try{
            if(activity!=null){
                ((ActivityLandingPage)activity).setOnBackPressedListener(new IOnBackPressed() {
                    @Override
                    public boolean onBackPressed() {
                        FragmentTransaction transaction=((ActivityLandingPage)activity).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, LandingPage.newInstance());
                        transaction.commit();
                        ((ActivityLandingPage)activity).getSupportActionBar().setTitle("Home");
                        /*
                        Intent intent=new Intent(context,ActivityLandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);*/
                        return false;
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void loadPreviousData(Common common, final SignalMessageAdapter signalMessageAdapter,Long consultationid){
        final Gson gson = new Gson();

        common.getChatMessage(consultationid);
        common.setOnTaskCompletedListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object result) {
                SignalMessage signalMessage;
                List<SignalMessage> signalMessageList=new ArrayList<>();
                List<Chat> chatList = (List<Chat> )result;
                if(chatList!=null){
                    for (Chat chat:chatList) {
                        ChatMessage chatMessage=new ChatMessage(chat.getMessage(),chat.getSender(),chat.getCreatedtime(),null,null,true);
                        if(chat.getSenderid().equals(String.valueOf(AppConstants.patientID))){
                            signalMessage=new SignalMessage(gson.toJson(chatMessage),false);

                        }else{
                            signalMessage=new SignalMessage(gson.toJson(chatMessage),true);

                        }

                        signalMessageList.add(0,signalMessage);
                    }

                    signalMessageAdapter.addAll(signalMessageList);
                }

            }
        });
    }

    public  CountDownTimer systemMsg(final Activity activity, String type, String data, CountDownTimer countDownTimer, long currtime, TextView textChatCountDown, onConsultationStart onConsultationStart){
        if(type.equals("ext")){
            Log.e("Chat Cons","ext time  :"+data);
            if(data!=null){
                int ext=Integer.parseInt(data);
                long extInMilliseconds=ext*60*1000;
                Log.e("Chat Cons","cons time  :"+currtime);
                currtime=remainingsec+extInMilliseconds;
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }

                countDownTimer=updateCountDownTimer(activity,currtime,countDownTimer,textChatCountDown);
                Log.e("Chat Cons","new time  :"+currtime);
                return countDownTimer;
            }


        }else if(type.equals("end")){
            Log.e("Chat Cons","end time  ");
            Toast.makeText(activity.getApplicationContext(),"Doctor has ended the Consultation",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.finish();
                }
            },5000);
        }else if(type.equals("start")){
            Toast.makeText(activity.getApplicationContext(), "Doctor has joined",Toast.LENGTH_LONG).show();

            if(onConsultationStart!=null){
                if(data!=null){
                    CurrentConsultationID=Long.parseLong(data);
                    onConsultationStart.onStart(Long.parseLong(data));
                }

            }
            //consultationid=Long.parseLong(data);
            countDownTimer=setCountDownTimer(activity,currtime,countDownTimer,textChatCountDown);
            return countDownTimer;
        }
        return countDownTimer;
    }

    public  CountDownTimer setCountDownTimer(final Activity activity, long time, CountDownTimer countDownTimer, final TextView textChatCountDown){

        remainingsec=0;
        if (!textChatCountDown.getText().toString().contains("min"))
        {
            countDownTimer=new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    textChatCountDown.setText(""+String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    remainingsec=millisUntilFinished;
                    //  CountDownTime =
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    //    countDownTv.setText("done!");
                    activity.finish();

                }

            }.start();
        }

        return countDownTimer;

    }

    public  CountDownTimer updateCountDownTimer(final Activity activity, long totaltime, CountDownTimer countDownTimer, final TextView textChatCountDown){



        countDownTimer=new CountDownTimer(totaltime, 1000) {

            public void onTick(long millisUntilFinished) {
                textChatCountDown.setText(""+String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                remainingsec=millisUntilFinished;
                //  CountDownTime =
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //    countDownTv.setText("done!");
                activity.finish();

            }

        }.start();


        return countDownTimer;

    }
}
