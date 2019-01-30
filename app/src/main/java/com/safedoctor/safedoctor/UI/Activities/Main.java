package com.safedoctor.safedoctor.UI.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.system.Os;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.safedoctor.safedoctor.Api.Common;
import com.safedoctor.safedoctor.Api.OnTaskCompleted;
import com.safedoctor.safedoctor.Api.SafeDoctorService;
import com.safedoctor.safedoctor.Api.TinyDB;
import com.safedoctor.safedoctor.Backgroundservices.MainService;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.TokenString;
import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.UI.Fragment.Appointments;
import com.safedoctor.safedoctor.UI.Fragment.ChangePassword;
import com.safedoctor.safedoctor.UI.Fragment.ChangePhonenumber;
import com.safedoctor.safedoctor.UI.Fragment.ChatRoom;
import com.safedoctor.safedoctor.UI.Fragment.ClinicVisitRecords;
import com.safedoctor.safedoctor.UI.Fragment.FirstAid;
import com.safedoctor.safedoctor.UI.Fragment.HealthPosts;
import com.safedoctor.safedoctor.UI.Fragment.Notifications;
import com.safedoctor.safedoctor.UI.Fragment.OnlineMedicalRecords;
import com.safedoctor.safedoctor.UI.Fragment.PartnerDiagnostic;
import com.safedoctor.safedoctor.UI.Fragment.PartnerHospitals;
import com.safedoctor.safedoctor.UI.Fragment.PartnerPharmacies;
import com.safedoctor.safedoctor.UI.Fragment.PartnerSpecialists;
import com.safedoctor.safedoctor.UI.Fragment.Peripherals;
import com.safedoctor.safedoctor.UI.Fragment.Referral;
import com.safedoctor.safedoctor.UI.Fragment.VitalsList;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity implements OnTaskCompleted {

    public static final String TAG = Main.class.getSimpleName();
    private SafeDoctorService mSafeDoctorService;
    private ProgressDialog mainProgressDialog;
    private SwagArrayResponseModel logoutResponse;
    private TinyDB tinyDB;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Menu menu_navigation;
    private  AlertDialog alertDialog;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    private TextView lblname, lblphone;
    private CircularImageView avatar;

    private Common common;
    private NavigationView nav_view;

    private SessionManagement sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        initToolbar();
        initNavigationMenu();

        mSafeDoctorService = ApiUtils.getAPIService();

        mainProgressDialog = new ProgressDialog(this);
        tinyDB = new TinyDB(this);

        common = new Common(getApplicationContext(), this);

        performBackgroundSync();

        sessionManagement = new SessionManagement(getApplicationContext());

        startService();

    }

    private void startService()
    {
        Log.e(TAG,"Starting service");
        Intent service = new Intent(getApplicationContext(), MainService.class);
        getApplicationContext().startService(service);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setBackgroundColor(getResources().getColor(R.color.pink_600));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Safe Doctor");
        // Tools.setSystemBarColor(this, R.color.pink_700);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initNavigationMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);


        nav_view = (NavigationView) findViewById(R.id.nav_view);
        // nav_view.setNavigationItemSelectedListener(this);
        nav_view.getMenu().getItem(0).setChecked(true);
        setToolbarTitle(getString(R.string.appointments_txt));

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Appointments.newInstance());
        transaction.commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                return menuSelected(item);
            }
        });


        //nav_view.addDr

        // open drawer at start
        // drawer.openDrawer(GravityCompat.START);
        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        // navigation header
        navigation_header = nav_view.getHeaderView(0);
        (navigation_header.findViewById(R.id.bt_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_hide = Tools.toggleArrow(view);
                is_account_mode = is_hide;
                menu_navigation.clear();
                if (is_hide) {

                    menu_navigation.add(1, 1000, 100, "Profile").setIcon(R.drawable.ic_profile);
                    //menu_navigation.addSubMenu("PERSONAL");
                    menu_navigation.add(1, 5000, 100, "Set your Location").setIcon(R.drawable.ic_location);
                    menu_navigation.add(1, 3000, 100, "Change Password").setIcon(R.drawable.ic_accountedit);
                    menu_navigation.add(1, 4000, 100, "Change Phone number").setIcon(R.drawable.ic_phone);
                    menu_navigation.add(1, 2000, 100, "Log Out").setIcon(R.drawable.ic_logout);
                    menu_navigation.addSubMenu(" ");
                    menu_navigation.add(2, 1, 100, "Settings").setIcon(R.drawable.ic_settings);
                    menu_navigation.add(2, 2, 100, "Help & Feedback").setIcon(R.drawable.ic_help);
                } else {
                    nav_view.inflateMenu(R.menu.activity_main_drawer);
                    updateCounter(nav_view);
                }
            }
        });

        lblname = (TextView) navigation_header.findViewById(R.id.lblname);
        lblphone = (TextView) navigation_header.findViewById(R.id.lblphone);
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile_Picture.class));
                setToolbarTitle("Profile Picture");
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        lblname.setText(AppConstants.patientName);
        lblphone.setText(AppConstants.patientPhoneNumber);

        //lblname.setText(sessionManagement.getPatientDetail().getFirstname()+" "+sessionManagement.getPatientDetail().getMiddlename()+" "+sessionManagement.getPatientDetail().getLastname());

        if (AppConstants.profilePictureString != null && !AppConstants.profilePictureString.isEmpty()) {
            byte[] imageBytes = Base64.decode(AppConstants.profilePictureString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            avatar.setImageBitmap(decodedImage);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Are you sure you want to logout from Safe Doctor?");
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Might want to do something here later
                }
            });

            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                    //finish();
                }
            });

            AlertDialog alert = dialog.create();
            alert.show();

        }
    }


    public boolean menuSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, ActivityLandingPage.class).putExtra("showtip",false));
                //selectedFragment = LandingPage.newInstance();
                setToolbarTitle("SAFE DOKTOR");
                break;
            case R.id.action_health_posts:
                selectedFragment = HealthPosts.newInstance();
                setToolbarTitle(getString(R.string.health_posts_txt));
                break;
            case R.id.action_appointments:
                selectedFragment = Appointments.newInstance();
                setToolbarTitle(getString(R.string.appointments_txt));
                break;
            case 1000: // profile
                startActivity(new Intent(this, ManageProfile.class));
                setToolbarTitle(getString(R.string.bio_data_txt));
                break;
            case 5000: // profile
                startActivity(new Intent(this, CurrentPlaceLocation.class));
                setToolbarTitle("Set your location");
                break;
           case R.id.action_chat_room:
               selectedFragment = ChatRoom.newInstance();
               setToolbarTitle("Chat Room");
               break;
            case R.id.action_clinic_visit_medical_records:
                selectedFragment = ClinicVisitRecords.newInstance();
                setToolbarTitle(getString(R.string.clinic_visit_medical_records_txt));
                break;
            case R.id.action_online_medical_records:
                selectedFragment = OnlineMedicalRecords.newInstance();
                setToolbarTitle(getString(R.string.online_medical_records_txt));
                break;
            case R.id.action_manage_vitals:
                selectedFragment = VitalsList.newInstance();
                setToolbarTitle(getString(R.string.vitalstatistics_txt));
                break;
            case R.id.action_reminder:
                selectedFragment = Notifications.newInstance();
                setToolbarTitle(getString(R.string.reminder_txt));
                break;

            case R.id.action_referral:
                selectedFragment = Referral.newInstance();
                setToolbarTitle(getString(R.string.referral_txt));
                break;
            case R.id.action_first_aid:
                selectedFragment = FirstAid.newInstance();
                setToolbarTitle(getString(R.string.first_aid_txt));
                break;
            case R.id.action_pharmacies:
                Log.d("safe", "safe");
                selectedFragment = PartnerPharmacies.newInstance();
                setToolbarTitle("Pharmacies");
                break;
            case R.id.action_diagnostics:
                Log.d("safe", "safe");
                selectedFragment = PartnerDiagnostic.newInstance();
                setToolbarTitle("Diagnostics");
                break;
            case R.id.action_hospitals:
                Log.d("safe", "safe");
                selectedFragment = PartnerHospitals.newInstance();
                setToolbarTitle("Hospitals");
                break;
            case R.id.action_specialists:
                Log.d("safe", "safe");
                selectedFragment = PartnerSpecialists.newInstance();
                setToolbarTitle("Specialists");
                break;
            case 2000: //logout
                logout();
                break;

            case 3000://change password
                selectedFragment= ChangePassword.newInstance();
                setToolbarTitle("Change Password");
                break;
            case 4000://change phone number
                selectedFragment= ChangePhonenumber.newInstance();
                setToolbarTitle("Change Phone Number");
                break;
            default:
                break;
        }


        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePhoneNumber(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View  rootView = inflater.inflate(R.layout.popup_change_phonenumber,null);

        final EditText txtphonenumber=(EditText)rootView.findViewById(R.id.txtphonenumber);
        TextView btncancelvalidate=(TextView)rootView.findViewById(R.id.btncancelvalidate);
        btncancelvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        TextView btnconfirm=(TextView)rootView.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(txtphonenumber.getText().toString())){
                    //changePhoneNumber(txtphonenumber.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Phone number cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setCancelable(false);
        builder.setView(rootView);
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void showChangePassword(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View  rootView = inflater.inflate(R.layout.popup_change_password,null);

        final EditText oldpassword=(EditText)rootView.findViewById(R.id.oldpassword);
        final EditText newpassword=(EditText)rootView.findViewById(R.id.newpassword);
        final EditText confirmpassword=(EditText)rootView.findViewById(R.id.confirmpassword);

        TextView btncancelvalidate=(TextView)rootView.findViewById(R.id.btncancelvalidate);
        btncancelvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        TextView btnconfirm=(TextView)rootView.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(oldpassword.getText().toString())&& !TextUtils.isEmpty(newpassword.getText().toString())&&!TextUtils.isEmpty(confirmpassword.getText().toString())){
                    if(newpassword.getText().toString().equals(oldpassword.getText().toString())==true){
                        //changePassword(new PatientChangeCredentials(newpassword.getText().toString(),oldpassword.getText().toString()));
                    }else{
                        Toast.makeText(getApplicationContext(),"New password and confirm password do not match",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Phone number cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setCancelable(false);
        builder.setView(rootView);
        alertDialog = builder.create();
        alertDialog.show();
    }

private void killAll()
{

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
        finishAndRemoveTask();
    }
    else
    {
        finish();
    }
}


    private void logout() {
        sessionManagement.logoutUserPermanent();

        mainProgressDialog.setMessage(getResources().getString(R.string.alert_working));
        mainProgressDialog.show();
        Call<SwagArrayResponseModel> call = mSafeDoctorService.patientLogout(TokenString.tokenString, AppConstants.patientID);
        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                mainProgressDialog.dismiss();

                killAll();

            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable t) {
                mainProgressDialog.dismiss();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");
                //startActivity(new Intent(Main.this, FormLogin.class));
                //finish();

                killAll();
            }
        });

    }


    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;
        Menu m = nav.getMenu();
        ((TextView) m.findItem(R.id.action_referral).getActionView().
                findViewById(R.id.text)).setText(String.valueOf(AppConstants.referrals));

        TextView badgePrioInbx = ((TextView) m.findItem(R.id.action_reminder).getActionView().findViewById(R.id.text));
        //badgePrioInbx.setText(AppConstants.newreminders +" New");
        badgePrioInbx.setText(AppConstants.maxbooked + " New");
        badgePrioInbx.setBackgroundColor(getResources().getColor(R.color.red_300));

        ((TextView) m.findItem(R.id.action_clinic_visit_medical_records).getActionView().
                findViewById(R.id.text)).setText(String.valueOf(AppConstants.clinicrecords));

        ((TextView) m.findItem(R.id.action_online_medical_records).getActionView().
                findViewById(R.id.text)).setText(String.valueOf(AppConstants.onlinerecords));


        TextView badgeSocial = (TextView) m.findItem(R.id.action_appointments).getActionView().findViewById(R.id.text);
        badgeSocial.setText(AppConstants.maxbooked + " booked");
        badgeSocial.setBackgroundColor(getResources().getColor(R.color.green_500));
    }

    private void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onTaskCompleted(Object result) {
        if (result.getClass() == boolean.class) // make sure i identify the type of object i get here
        {
            // TODO might do something here

        }
    }

    private void performBackgroundSync() {
        common.performBackgroundSync();
    }

}
