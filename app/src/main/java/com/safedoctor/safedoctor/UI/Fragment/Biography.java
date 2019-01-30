package com.safedoctor.safedoctor.UI.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Activities.FormLogin;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.AuxUtils;
import com.safedoctor.safedoctor.Utils.StringUtils;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Utils.ViewAnimation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stevkkys on 9/12/2017.
 */

public class Biography extends Fragment  implements View.OnClickListener,OnTaskCompleted
{

    private BottomNavigationView navigation;
    private View parent_view;
    private NestedScrollView nested_scroll_view;
    private TextView tv_booking_code;
    private ImageButton bt_toggle_info, bt_toggle_basic,bt_toggle_gen;
    private Button bt_hide_info,bt_hide_basic,bt_hide_gen;
    private View lyt_expand_info,lyt_expand_basic,lyt_expand_gen ;
    private int genderid=0;

    private Calendar mCalendar;
    private SimpleDateFormat dateFormatter;

    private DatePickerDialog dateOfBirthDatePickerDialog;

    private View root;

    private Common common;
    private SafeDoctorService mSafeDoctorService;
    private SwagArrayResponseModel<List<PatientModel>> dataresponse;
    private ProgressDialog progress;
    private String TAG="Biography";


    //Start form Variables
    private AppCompatEditText txtfirstname,txtlastname,txtothernames,txtdob,txtemail,
            spnbloodgroup,txtheight,txtweight,spntitle,spnmaritalstatus;

    private AppCompatEditText spnregion,spndistrict,spntown,txtstreet,txtaddress,spnhregion,spnhdistrict,spnhtown,
            txthstreet,txthometel,spnwregion,spnwdistrict,spnwtown,txtwstreet,txtworktel;

    private AppCompatEditText spnidtype,txtidnumber,spnnationality,spnethnicity,spnoccupation,spneducationlevel,
            spnreligion,spndenomination;

    private AppCompatRadioButton radio_female,radio_male;

    private  List<AppCompatEditText> dependants = new ArrayList<AppCompatEditText>();

    private void bindControls()
    {
        txtfirstname = (AppCompatEditText)root.findViewById(R.id.txtfirstname);
        txtlastname = (AppCompatEditText)root.findViewById(R.id.txtlastname);
        txtothernames = (AppCompatEditText)root.findViewById(R.id.txtothernames);
        txtdob = (AppCompatEditText)root.findViewById(R.id.txtdob);
        txtdob.setOnClickListener(this);
        txtemail = (AppCompatEditText)root.findViewById(R.id.txtemail);
        spnbloodgroup = (AppCompatEditText)root.findViewById(R.id.spnbloodgroup);
        spnbloodgroup.setOnClickListener(this);
        txtheight = (AppCompatEditText)root.findViewById(R.id.txtheight);
        txtweight = (AppCompatEditText)root.findViewById(R.id.txtweight);
        spntitle = (AppCompatEditText)root.findViewById(R.id.spntitle);
        spntitle.setOnClickListener(this);
        spnmaritalstatus = (AppCompatEditText)root.findViewById(R.id.spnmaritalstatus);
        spnmaritalstatus.setOnClickListener(this);

        radio_female = (AppCompatRadioButton) root.findViewById(R.id.radio_female);
        radio_male = (AppCompatRadioButton) root.findViewById(R.id.radio_male);
        radio_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getTitlebyGendergroup();
                genderid=1;
            }
        });
        radio_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getTitlebyGendergroup(1);
                genderid=2;
            }
        });

        spnregion = (AppCompatEditText) root.findViewById(R.id.spnregion);
        spnregion.setOnClickListener(this);
        spndistrict = (AppCompatEditText) root.findViewById(R.id.spndistrict);
        spndistrict.setOnClickListener(this);
        spntown = (AppCompatEditText) root.findViewById(R.id.spntown);
        spntown.setOnClickListener(this);
        txtstreet = (AppCompatEditText) root.findViewById(R.id.txtstreet);
        txtaddress = (AppCompatEditText) root.findViewById(R.id.txtaddress);
        spnhregion = (AppCompatEditText) root.findViewById(R.id.spnhregion);
        spnhregion.setOnClickListener(this);
        spnhdistrict = (AppCompatEditText) root.findViewById(R.id.spnhdistrict);
        spnhdistrict.setOnClickListener(this);
        spnhtown = (AppCompatEditText) root.findViewById(R.id.spnhtown);
        spnhtown.setOnClickListener(this);
        txthstreet = (AppCompatEditText) root.findViewById(R.id.txthstreet);
        txthometel = (AppCompatEditText) root.findViewById(R.id.txthometel);
        spnwregion = (AppCompatEditText) root.findViewById(R.id.spnwregion);
        spnwregion.setOnClickListener(this);
        spnwdistrict = (AppCompatEditText) root.findViewById(R.id.spnwdistrict);
        spnwdistrict.setOnClickListener(this);
        spnwtown = (AppCompatEditText) root.findViewById(R.id.spnwtown);
        spnwtown.setOnClickListener(this);
        txtwstreet = (AppCompatEditText) root.findViewById(R.id.txtwstreet);
        txtworktel = (AppCompatEditText) root.findViewById(R.id.txtworktel);


        spnidtype =(AppCompatEditText) root.findViewById(R.id.spnidtype);
        spnidtype.setOnClickListener(this);
        txtidnumber =(AppCompatEditText) root.findViewById(R.id.txtidnumber);
        spnnationality =(AppCompatEditText) root.findViewById(R.id.spnnationality);
        spnnationality.setOnClickListener(this);
        spnethnicity =(AppCompatEditText) root.findViewById(R.id.spnethnicity);
        spnethnicity.setOnClickListener(this);
        spnoccupation =(AppCompatEditText) root.findViewById(R.id.spnoccupation);
        spnoccupation.setOnClickListener(this);
        spneducationlevel =(AppCompatEditText) root.findViewById(R.id.spneducationlevel);
        spneducationlevel.setOnClickListener(this);
        spnreligion =(AppCompatEditText) root.findViewById(R.id.spnreligion);
        spnreligion.setOnClickListener(this);
        spndenomination =(AppCompatEditText) root.findViewById(R.id.spndenomination);
        spndenomination.setOnClickListener(this);

    }


    private List<BasicObject> getTitlebyGendergroup(){
        List<BasicObject> gendertitles=new ArrayList<>();
        if(genderid!=0 || AppConstants.allTitles.size()==0){
            if(AppConstants.allTitles.size()>0){
                for (BasicObject obj:AppConstants.allTitles) {
                    if(obj.getGendergroupid()==genderid || obj.getGendergroupid()==3){//3 is for all gender groups
                        gendertitles.add(obj);
                    }

                }
            }
        }else{
            return AppConstants.CACHE_FORMDATA.getTitle();
        }



        /*
        if(AppConstants.CACHE_FORMDATA!=null){
            if(AppConstants.CACHE_FORMDATA.getTitle()!=null){
                if(AppConstants.CACHE_FORMDATA.getTitle().size()!=0){
                    for (BasicObject obj:AppConstants.allTitles) {
                        if(obj.getGendergroupid()==groupid || obj.getGendergroupid()==3){//3 is for all gender groups
                            gendertitles.add(obj);
                        }

                    }
                }
            }

        }*/

        return gendertitles;


    }

    private void initControls()
    {
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        //card 1
        dateOfBirthDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtdob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        txtfirstname.setText(AppConstants.PatientObj.getFirstname());
        txtothernames.setText(AppConstants.PatientObj.getMiddlename());
        txtlastname.setText(AppConstants.PatientObj.getLastname());
        txtdob.setText(AppConstants.PatientObj.getDateofbirth());
        //txtdob.setText( AuxUtils.Date.formateDate(AppConstants.PatientObj.getDateofbirth()));
        txtemail.setText(AppConstants.PatientObj.getEmail());
        AuxUtils.setNumericValue(AppConstants.PatientObj.getHeight(),txtheight);
        AuxUtils.setNumericValue(AppConstants.PatientObj.getWeight(),txtweight);

        if(AppConstants.PatientObj.getGendergroupid() == 1)
        {
            radio_male.setChecked(true);
        }
        else
        {
            radio_female.setChecked(true);
        }


        //card 2
        txtstreet.setText(AppConstants.PatientObj.getStreetaddress());
        txtaddress.setText(AppConstants.PatientObj.getAddress());
        txthstreet.setText(AppConstants.PatientObj.getHomestreet());
        txthometel.setText(AppConstants.PatientObj.getHometelno());
        txtwstreet.setText(AppConstants.PatientObj.getWorkstreet());
        txtworktel.setText(AppConstants.PatientObj.getWorktelno());

        //card 3
        txtidnumber.setText(AppConstants.PatientObj.getIdnumber());


        //Set values for custom Spinners
        //card 1
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getBloodgroup(),
                AppConstants.PatientObj.getBloodgroupid()),spnbloodgroup);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getTitle(),
                AppConstants.PatientObj.getTitleid()),spntitle);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getMaritalstatus(),
                AppConstants.PatientObj.getMaritalstatusid()),spnmaritalstatus);

        //card 2
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getRegion(),
                AppConstants.PatientObj.getRegionid()),spnregion);
        AuxUtils.setDropdownValue(AuxUtils.getDistrictByValue(AppConstants.CACHE_FORMDATA.getDistrict(),
                AppConstants.PatientObj.getDistrictid()),spndistrict);
        AuxUtils.setDropdownValue(AuxUtils.getTownByValue(AppConstants.CACHE_FORMDATA.getTown(),
                AppConstants.PatientObj.getTownid()),spntown);

        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getRegion(),
                AppConstants.PatientObj.getHomeregionid()),spnhregion);
        AuxUtils.setDropdownValue(AuxUtils.getDistrictByValue(AppConstants.CACHE_FORMDATA.getDistrict(),
                AppConstants.PatientObj.getHomedistrictid()),spnhdistrict);
        AuxUtils.setDropdownValue(AuxUtils.getTownByValue(AppConstants.CACHE_FORMDATA.getTown(),
                AppConstants.PatientObj.getHometownid()),spnhtown);

        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getRegion(),
                AppConstants.PatientObj.getWorkregionid()),spnwregion);
        AuxUtils.setDropdownValue(AuxUtils.getDistrictByValue(AppConstants.CACHE_FORMDATA.getDistrict(),
                AppConstants.PatientObj.getWorkdistrictid()),spnwdistrict);
        AuxUtils.setDropdownValue(AuxUtils.getTownByValue(AppConstants.CACHE_FORMDATA.getTown(),
                AppConstants.PatientObj.getWorktownid()),spnwtown);

//Card 3
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getIdentificationtype(),
                AppConstants.PatientObj.getIdentificationtypeid()),spnidtype);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getCountry(),
                AppConstants.PatientObj.getNationalityid()),spnnationality);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getEthnicity(),
                AppConstants.PatientObj.getEthnicityid()),spnethnicity);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getOccupation(),
                AppConstants.PatientObj.getOccupationid()),spnoccupation);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getEducationlevel(),
                AppConstants.PatientObj.getEducationallevelid()),spneducationlevel);
        AuxUtils.setDropdownValue(AuxUtils.getBaseObjectByValue(AppConstants.CACHE_FORMDATA.getReligion(),
                AppConstants.PatientObj.getReligionid()),spnreligion);
        AuxUtils.setDropdownValue(AuxUtils.getDenominationByValue(AppConstants.CACHE_FORMDATA.getDenomination(),
                AppConstants.PatientObj.getDenominationid()),spndenomination);


    }


    public static Biography newInstance()
    {
        Biography biography = new Biography();
        return biography;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_biography, container, false);

        parent_view = root.findViewById(android.R.id.content);

        common = new Common(getActivity().getApplicationContext(),this);
        initComponent();

        bindControls();
        initControls();

        navigation = (BottomNavigationView) root.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navigation_tip:

                       return true;
                    case R.id.navigation_save:
                        saveBioData();
                        return true;
                }
                return false;
            }
        });

        nested_scroll_view = (NestedScrollView) root.findViewById(R.id.nested_scroll_view);
       /* nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateNavigation(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateNavigation(true);
                }
            }
        });
        */

        return root;
    }



    private void initComponent() {
        // info item_section
        //basic details
        mSafeDoctorService = ApiUtils.getAPIService();
        progress =  new ProgressDialog(this.getContext());
        progress.setMessage("Working...Please Wait");

        bt_toggle_basic = (ImageButton) root.findViewById(R.id.bt_toggle_basic);
        bt_hide_basic = (Button) root.findViewById(R.id.bt_hide_basic);
        lyt_expand_basic = (View) root.findViewById(R.id.lyt_expand_basic);

        bt_toggle_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_basic,lyt_expand_basic);
            }
        });

        bt_hide_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_basic,lyt_expand_basic);
            }
        });


        //location details
        bt_toggle_info = (ImageButton) root.findViewById(R.id.bt_toggle_info);
        bt_hide_info = (Button) root.findViewById(R.id.bt_hide_info);
        lyt_expand_info = (View) root.findViewById(R.id.lyt_expand_info);


        bt_toggle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info,lyt_expand_info);
            }
        });

        bt_hide_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info,lyt_expand_info);
            }
        });

        //General Info
        bt_toggle_gen = (ImageButton) root.findViewById(R.id.bt_toggle_gen);
        bt_hide_gen = (Button) root.findViewById(R.id.bt_hide_gen);
        lyt_expand_gen = (View) root.findViewById(R.id.lyt_expand_gen);


        bt_toggle_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info,lyt_expand_gen);
            }
        });

        bt_hide_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info,lyt_expand_gen);
            }
        });

    }



    private void toggleSectionInfo(View view, final View expandview) {
        boolean show = toggleArrow(view);
        if (show)
        {

            ViewAnimation.expand(expandview, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, expandview);
                }
            });
        }
        else
        {
            ViewAnimation.collapse(expandview);
        }
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    boolean isNavigationHide = false;

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }


    @Override
    public void onClick(View v) {
        dependants.clear();
        switch (v.getId())
        {
            case R.id.txtdob:
                dateOfBirthDatePickerDialog.show();
                break;
            case R.id.spnmaritalstatus: //marital status
                common.displayDropDownList(this.getContext(), AppConstants.CACHE_FORMDATA.getMaritalstatus(),spnmaritalstatus,null,null);
                break;
            case R.id.spnbloodgroup: //blodd group
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getBloodgroup(),spnbloodgroup,null,null);
                break;
            case R.id.spntitle: //title
                common.displayDropDownList(this.getContext(),getTitlebyGendergroup(),spntitle,null,null);
                break;
            case R.id.spnregion:
                dependants.add(spndistrict);
                dependants.add(spntown);
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getRegion(),spnregion,null,dependants);
                break;
            case R.id.spndistrict:
                dependants.add(spntown);
                common.displayDistrictList(this.getContext(),AppConstants.CACHE_FORMDATA.getDistrict(AuxUtils.getTagValue(spnregion.getTag())),spndistrict,null,dependants);
                break;
            case R.id.spntown:
                common.displayTownList(this.getContext(),AppConstants.CACHE_FORMDATA.getTown(AuxUtils.getTagValue(spndistrict.getTag())),spntown,null,null);
                break;
            case R.id.spnhregion:
                dependants.add(spnhdistrict);
                dependants.add(spnhtown);
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getRegion(),spnhregion,null,dependants);
                break;
            case R.id.spnhdistrict:
                dependants.add(spnhtown);
                common.displayDistrictList(this.getContext(),AppConstants.CACHE_FORMDATA.getDistrict(AuxUtils.getTagValue(spnhregion.getTag())),spnhdistrict,null,dependants);
                break;
            case R.id.spnhtown:
                common.displayTownList(this.getContext(),AppConstants.CACHE_FORMDATA.getTown(AuxUtils.getTagValue(spnhdistrict.getTag())),spnhtown,null,null);
                break;
            case R.id.spnwregion:
                dependants.add(spnwdistrict);
                dependants.add(spnwtown);
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getRegion(),spnwregion,null,dependants);
                break;
            case R.id.spnwdistrict:
                dependants.add(spnwtown);
                common.displayDistrictList(this.getContext(),AppConstants.CACHE_FORMDATA.getDistrict(AuxUtils.getTagValue(spnwregion.getTag())),spnwdistrict,null,dependants);
                break;
            case R.id.spnwtown:
                common.displayTownList(this.getContext(),AppConstants.CACHE_FORMDATA.getTown(AuxUtils.getTagValue(spnwdistrict.getTag())),spnwtown,null,null);
                break;
            case R.id.spnidtype:
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getIdentificationtype(),spnidtype,null,null);
                break;
            case R.id.spnnationality:
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getCountry(),spnnationality,null,null);
                break;
            case R.id.spnethnicity:
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getEthnicity(),spnethnicity,null,null);
                break;
            case R.id.spnoccupation:
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getOccupation(),spnoccupation,null,null);
                break;
            case R.id.spneducationlevel:
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getEducationlevel(),spneducationlevel,null,null);
                break;
            case R.id.spnreligion:
                dependants.add(spndenomination);
                common.displayDropDownList(this.getContext(),AppConstants.CACHE_FORMDATA.getReligion(),spnreligion,null,dependants);
                break;
            case R.id.spndenomination:
                common.displayDenominationList(this.getContext(),AppConstants.CACHE_FORMDATA.getDenomination(AuxUtils.getTagValue(spnreligion.getTag())),spndenomination,null,null);
                break;
        }
    }

    @Override
    public void onTaskCompleted(Object result) {

    }

    public void saveBioData()
    {
        progress.show();

        PatientModel patient = prepareToSave(AppConstants.PatientObj);


        Call<SwagArrayResponseModel<List<PatientModel>>> call = mSafeDoctorService.updatePatient(TokenString.tokenString,patient);
        call.enqueue(new Callback<SwagArrayResponseModel<List<PatientModel>>>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel<List<PatientModel>>> call, Response<SwagArrayResponseModel<List<PatientModel>>> response) {
                progress.dismiss();
                if (response.code() ==  HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getContext(), "Your session has timed out. Please login again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), FormLogin.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (response.isSuccessful()){
                    Log.i("Safe", "successful");
                    dataresponse = response.body();

                    if(dataresponse.getData() != null && dataresponse.getData().size() > 0)
                    {
                        AppConstants.PatientObj = dataresponse.getData().get(0);
                        AppConstants.sessionManagement.createPatientDetail(dataresponse.getData().get(0));

                        AppConstants.patientName = StringUtils.join(" ",AppConstants.PatientObj.getFirstname(),AppConstants.PatientObj.getMiddlename(),AppConstants.PatientObj.getLastname());

                        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Your details have been saved", Snackbar.LENGTH_LONG).show();
                    }
                    Log.i("Safe", "Patient number is " + AppConstants.patientPhoneNumber);

                }
                else
                {
                    try {
                        String e=response.errorBody().string();

                        Log.e(TAG,"error body "+e);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Could not perform action. Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel<List<PatientModel>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Network Error, please try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progress.dismiss();
            }
        });

    }

    private PatientModel prepareToSave( PatientModel pat)
    {
        //Card 1
        pat.setFirstname(AuxUtils.getTextValue(txtfirstname.getText(),false));
        pat.setLastname(AuxUtils.getTextValue(txtlastname.getText(), false));
        pat.setMiddlename(AuxUtils.getTextValue(txtothernames.getText(), true));

        if(radio_male.isChecked())
        {
            pat.setGendergroupid(1);

        }
        else
        {
            pat.setGendergroupid(2);

        }

        pat.setDateofbirth(AuxUtils.getTextValue(txtdob.getText(), false));

        pat.setEmail(AuxUtils.getTextValue(txtemail.getText(), true));

        pat.setBloodgroupid(AuxUtils.getTagValue(spnbloodgroup.getTag(), true));
        pat.setHeight(AuxUtils.getDoubleValue(txtheight.getText(), true));
        pat.setWeight(AuxUtils.getDoubleValue(txtweight.getText(), true));
        pat.setTitleid(AuxUtils.getTagValue(spntitle.getTag(), true));
        pat.setMaritalstatusid(AuxUtils.getTagValue(spnmaritalstatus.getTag(), true));

        //Card 2
        pat.setRegionid(AuxUtils.getTagValue(spnregion.getTag(), true));
        pat.setDistrictid(AuxUtils.getTagValue(spndistrict.getTag(), true));
        pat.setTownid(AuxUtils.getTagValue(spntown.getTag(), true));
        pat.setStreetaddress(AuxUtils.getTextValue(txtstreet.getText(), true));
        pat.setAddress(AuxUtils.getTextValue(txtaddress.getText(), true));

        pat.setHomeregionid(AuxUtils.getTagValue(spnhregion.getTag(), true));
        pat.setHomedistrictid(AuxUtils.getTagValue(spnhdistrict.getTag(), true));
        pat.setHometownid(AuxUtils.getTagValue(spnhtown.getTag(), true));

        pat.setHomestreet(AuxUtils.getTextValue(txthstreet.getText(), true));
        pat.setHometelno(AuxUtils.getTextValue(txthometel.getText(), true));

        pat.setWorkregionid(AuxUtils.getTagValue(spnwregion.getTag(), true));
        pat.setWorkdistrictid(AuxUtils.getTagValue(spnwdistrict.getTag(), true));
        pat.setWorktownid(AuxUtils.getTagValue(spnwtown.getTag(), true));

        pat.setWorkstreet(AuxUtils.getTextValue(txtwstreet.getText(), true));
        pat.setWorktelno(AuxUtils.getTextValue(txtworktel.getText(), true));


        //Card 3
        pat.setIdnumber(AuxUtils.getTextValue(txtidnumber.getText(), true));

        pat.setIdentificationtypeid(AuxUtils.getTagValue(spnidtype.getTag(), true));
        pat.setNationalityid(AuxUtils.getTagValue(spnnationality.getTag(), true));
        pat.setEthnicityid(AuxUtils.getTagValue(spnethnicity.getTag(), true));
        pat.setOccupationid(AuxUtils.getTagValue(spnoccupation.getTag(), true));
        pat.setEducationallevelid(AuxUtils.getTagValue(spneducationlevel.getTag(), true));
        pat.setReligionid(AuxUtils.getTagValue(spnreligion.getTag(), true));
        pat.setDenominationid(AuxUtils.getTagValue(spndenomination.getTag(), true));





        return pat;
    }
}
