package com.safedoctor.safedoctor.UI.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
import com.safedoctor.safedoctor.UI.Fragment.LandingPage;
import com.safedoctor.safedoctor.UI.Fragment.Notifications;
import com.safedoctor.safedoctor.UI.Fragment.OnlineMedicalRecords;
import com.safedoctor.safedoctor.UI.Fragment.PartnerHospitals;
import com.safedoctor.safedoctor.UI.Fragment.Peripherals;
import com.safedoctor.safedoctor.UI.Fragment.Referral;
import com.safedoctor.safedoctor.UI.Fragment.SettingsFragment;
import com.safedoctor.safedoctor.UI.Fragment.VitalsList;
import com.safedoctor.safedoctor.Utils.ApiUtils;
import com.safedoctor.safedoctor.Utils.AppConstants;
import com.safedoctor.safedoctor.Utils.IOnBackPressed;
import com.safedoctor.safedoctor.Utils.InformationDialog;
import com.safedoctor.safedoctor.Utils.SessionManagement;
import com.safedoctor.safedoctor.Utils.Tools;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safedoctor.safedoctor.Api.Common.CurrentConsultationID;
import static com.safedoctor.safedoctor.Utils.App.context;

/**
 * Created by stevkky on 10/23/17.
 */

public class ActivityLandingPage extends AppCompatActivity implements OnTaskCompleted {

    public static final String TAG = ActivityLandingPage.class.getSimpleName();
    private SafeDoctorService mSafeDoctorService;
    private ProgressDialog mainProgressDialog;
    private SwagArrayResponseModel logoutResponse;
    private TinyDB tinyDB;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Menu menu_navigation;

    private DrawerLayout drawer;
    private View navigation_header;
    private static boolean is_account_mode = false;

    private TextView lblname, lblphone;
    private CircularImageView avatar;

    private Common common;
    private static NavigationView nav_view;
    private String[] tips = {"Do you want to talk to a doctor? Tap on appointment to see available doctors and speak to them",
            "See all your safe doktor clinical history", "More tips coming.."};
    private int tipsindex = 0;
    private SessionManagement sessionManagement;
    private IOnBackPressed iOnBackPressed;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        iniToolbar();
        initNavigationMenu();

        mSafeDoctorService = ApiUtils.getAPIService();
        common = new Common(getApplicationContext(), this);

        mainProgressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        startService();


        if (!AppConstants.IS_LOGIN) {
            displayTips();
        } else {
            performBackgroundSync();
        }

        if (getIntent().getExtras() != null) {
            boolean showtip = getIntent().getExtras().getBoolean("showtip", false);
            if (showtip) {
                showTipDialog();
            }
        }


    }

    private void startService() {
        Log.e(TAG, "Starting service");
        Intent service = new Intent(getApplicationContext(), MainService.class);
        getApplicationContext().startService(service);
    }

    private void iniToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SAFE DOKTOR");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("SAFE DOKTOR");
    }


    private void displayTips() {

    }

    private void initNavigationMenu() {
        drawer = (DrawerLayout) findViewById(R.id.main_layout);

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        // nav_view.setNavigationItemSelectedListener(this);
        nav_view.getMenu().getItem(0).setChecked(true);
        setToolbarTitle(getResources().getString(R.string.app_name));

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, LandingPage.newInstance());
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
                is_account_mode = AppConstants.IS_LOGIN;
                menu_navigation.clear();
                if (is_hide) {

                    if (AppConstants.IS_LOGIN) {
                        menu_navigation.add(1, 1000, 100, "Profile").setIcon(R.drawable.ic_profile);
                        //menu_navigation.addSubMenu("PERSONAL");
                        menu_navigation.add(1, 5000, 100, "Set your Location").setIcon(R.drawable.ic_location);
                        menu_navigation.add(1, 3000, 100, "Change Password").setIcon(R.drawable.ic_accountedit);
                        menu_navigation.add(1, 4000, 100, "Change Phone number").setIcon(R.drawable.ic_phone);
                        menu_navigation.add(1, 2000, 100, "Log Out").setIcon(R.drawable.ic_logout);
                    }
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

        if (!(new SessionManagement(getApplicationContext())).isLoggedIn()) {
            (navigation_header.findViewById(R.id.avatar)).setVisibility(View.GONE);
            (navigation_header.findViewById(R.id.sidebar_header_info)).setVisibility(View.GONE);
            Button login_btn = (Button) navigation_header.findViewById(R.id.login);
            login_btn.setVisibility(View.VISIBLE);
            //avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);
            avatar.setVisibility(View.GONE);
            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), FormLogin.class));
                }
            });
            return;
        }


        avatar.setVisibility(View.VISIBLE);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile_Picture.class));
                setToolbarTitle("Profile Picture");
            }
        });
    }

    private void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((new SessionManagement(getApplicationContext())).isLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_homepage_popup, menu);

            final MenuItem searchItem = menu.findItem(R.id.action_search);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()

                {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (searchItem != null) {

                            searchItem.collapseActionView();
                        }
                        return false;

                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                        if (currFragment instanceof HealthPosts) {
                            HealthPosts.searchitem(newText);
                        } else if (currFragment instanceof FirstAid) {
                            FirstAid.searchitem(newText);
                        }


                        return false;

                    }
                });

                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()

                {

                    @Override
                    public boolean onSuggestionSelect(int position)

                    {
                        return false;
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        if (searchItem != null)

                        {
                            searchItem.collapseActionView();
                        }
                        return false;

                    }
                });

            }

        }

        return true;
    }


    public static void updateCounter(NavigationView nav) {
        //if (A) return;
        if(nav==null){
            nav=nav_view;
        }

        try{
            Menu m = nav.getMenu();
            ((TextView) m.findItem(R.id.action_referral).getActionView().
                    findViewById(R.id.text)).setText(String.valueOf(AppConstants.referrals));

            TextView badgePrioInbx = ((TextView) m.findItem(R.id.action_reminder).getActionView().findViewById(R.id.text));
            //badgePrioInbx.setText(AppConstants.newreminders +" New");
            badgePrioInbx.setText(AppConstants.maxbooked + " New");
            badgePrioInbx.setBackgroundColor(context.getResources().getColor(R.color.red_300));

            ((TextView) m.findItem(R.id.action_clinic_visit_medical_records).getActionView().
                    findViewById(R.id.text)).setText(String.valueOf(AppConstants.clinicrecords));

            ((TextView) m.findItem(R.id.action_online_medical_records).getActionView().
                    findViewById(R.id.text)).setText(String.valueOf(AppConstants.onlinerecords));


            TextView badgeSocial = (TextView) m.findItem(R.id.action_appointments).getActionView().findViewById(R.id.text);
            badgeSocial.setText(AppConstants.maxbooked + " booked");
            badgeSocial.setBackgroundColor(context.getResources().getColor(R.color.green_500));

        }catch (Exception e) {

        }

    }


    public boolean menuSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment selectedFragment = null;
        Bundle bundle=new Bundle();
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, ActivityLandingPage.class).putExtra("showtip", false));
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
                Log.d("SaveDoc", "Test the click");
                if ((new SessionManagement(this).isLoggedIn())) {
                    selectedFragment = ClinicVisitRecords.newInstance();
                    setToolbarTitle(getString(R.string.clinic_visit_medical_records_txt));
                } else {
                    InformationDialog.showTipDialog(this,
                            "view all medical facilities you have visited?"
                    );
                }
                break;
            case R.id.action_online_medical_records:
                if ((new SessionManagement(this).isLoggedIn())) {
                    selectedFragment = OnlineMedicalRecords.newInstance();
                    setToolbarTitle(getString(R.string.online_medical_records_txt));
                } else {
                    InformationDialog.showTipDialog(this,
                            "view your medical history at the comfort of your home?"
                    );
                }
                break;
            case R.id.action_manage_vitals:
                if ((new SessionManagement(this).isLoggedIn())) {
                    selectedFragment = VitalsList.newInstance();
                    setToolbarTitle(getString(R.string.vitalstatistics_txt));
                } else {
                    InformationDialog.showTipDialog(this,
                            "Manage and monitor your vital statistics trend at the comfort of your home?"
                    );
                }
                break;
            case R.id.action_reminder:
                if ((new SessionManagement(this).isLoggedIn())) {
                    selectedFragment = Notifications.newInstance();
                    setToolbarTitle(getString(R.string.reminder_txt));
                } else {
                    InformationDialog.showTipDialog(this,
                            "view all your hospital appointment?"
                    );
                }
                break;

            case R.id.action_referral:
                if ((new SessionManagement(this).isLoggedIn())) {
                    selectedFragment = Referral.newInstance();
                    setToolbarTitle(getString(R.string.referral_txt));
                } else {
                    InformationDialog.showTipDialog(this,
                            "view all your medial referrals in a simple view?"
                    );
                }
                break;

            case R.id.action_first_aid:
                selectedFragment = FirstAid.newInstance();
                setToolbarTitle(getString(R.string.first_aid_txt));
                break;

            case R.id.action_pharmacies:
                Log.d("safe", "safe");
                selectedFragment = PartnerHospitals.newInstance();

                bundle.putString("type","p");
                selectedFragment.setArguments(bundle);

                setToolbarTitle("Pharmacies");
                break;
            case R.id.action_diagnostics:
                Log.d("safe", "safe");
                selectedFragment = PartnerHospitals.newInstance();
                bundle.putString("type","d");
                selectedFragment.setArguments(bundle);
                setToolbarTitle("Diagnostics");
                break;
            case R.id.action_hospitals:
                Log.d("safe", "safe");
                selectedFragment = PartnerHospitals.newInstance();
                bundle.putString("type","h");
                selectedFragment.setArguments(bundle);

                setToolbarTitle("Hospitals");
                break;
            case R.id.action_specialists:
                Log.d("safe", "safe");
                selectedFragment = PartnerHospitals.newInstance();
                bundle.putString("type","s");
                selectedFragment.setArguments(bundle);
                setToolbarTitle("Specialists");
                break;
            case 2000: // logout
                logout();
                break;
            case 3000://change password
                selectedFragment = ChangePassword.newInstance();
                setToolbarTitle("Change Password");
                break;
            case 4000://change phone number
                selectedFragment = ChangePhonenumber.newInstance();
                setToolbarTitle("Change Phone Number");
                break;
            case 2://help and feedback
                showTipDialog();
                break;
            case 1://settings

                selectedFragment = SettingsFragment.newInstance();
                setToolbarTitle("Settings");

                break;
            default:
                break;
        }


        if (selectedFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.home:
            case 16908332:
                drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.nav_help:
                showTipDialog();
                break;

            case R.id.action_change_password:
                transaction.replace(R.id.frame_layout, ChangePassword.newInstance());
                transaction.commit();
                setToolbarTitle("Change Password");
                break;

            case R.id.action_change_phone:
                transaction.replace(R.id.frame_layout, ChangePhonenumber.newInstance());
                transaction.commit();
                setToolbarTitle("Change Phone Number");
                break;
            case R.id.action_settings:
                transaction.replace(R.id.frame_layout, SettingsFragment.newInstance());
                transaction.commit();
                setToolbarTitle("Settings");
                break;
            case R.id.action_peripherals:
                transaction.replace(R.id.frame_layout, Peripherals.newInstance());
                transaction.commit();
                setToolbarTitle("Peripherals Manager");
                break;
            case R.id.action_search:

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        if (!AppConstants.IS_LOGIN) {
            finish();
            return;
        }
        AppConstants.IS_LOGIN = false;
        mainProgressDialog.setMessage(getResources().getString(R.string.alert_working));
        mainProgressDialog.show();
        Call<SwagArrayResponseModel> call = mSafeDoctorService.patientLogout(TokenString.tokenString, AppConstants.patientID);
        call.enqueue(new Callback<SwagArrayResponseModel>() {
            @Override
            public void onResponse(Call<SwagArrayResponseModel> call, Response<SwagArrayResponseModel> response) {
                try {
                    if (mainProgressDialog != null) {
                        mainProgressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(),"Logout successful",Toast.LENGTH_LONG).show();
                    sessionManagement.logoutUserPermanent();
                    killAll();
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<SwagArrayResponseModel> call, Throwable t) {
                mainProgressDialog.dismiss();
                Log.i("Safe", "Fetching error: " + t.getMessage() + "");
                sessionManagement.logoutSessionExpired();
                //startActivity(new Intent(ActivityLandingPage.this, FormLogin.class));
                killAll();
            }
        });

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

    private void showTipDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_tip_of_day);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView content = (TextView) dialog.findViewById(R.id.content);

        content.setText(tips[tipsindex]);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipsindex < tips.length - 1) {
                    tipsindex = tipsindex + 1;
                    content.setText(tips[tipsindex]);
                }

            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_prev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipsindex > 0) {
                    tipsindex = tipsindex - 1;
                    content.setText(tips[tipsindex]);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onTaskCompleted(Object result) {

    }

    private void performBackgroundSync() {
        //preloadAppt();
        common.performBackgroundSync();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        try{

            lblname.setText(AppConstants.patientName);
            lblphone.setText(AppConstants.patientPhoneNumber);

            if (AppConstants.profilePictureString != null && !AppConstants.profilePictureString.isEmpty()) {
                byte[] imageBytes = Base64.decode(AppConstants.profilePictureString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                avatar.setImageBitmap(decodedImage);
            }

        }catch (Exception e){

        }


    }



    public void setOnBackPressedListener(IOnBackPressed iOnBackPressed) {
        this.iOnBackPressed = iOnBackPressed;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {



            if(iOnBackPressed!=null){
                iOnBackPressed.onBackPressed();
                return;
            }else{


            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Are you sure you want to logout from Safe Doctor?");
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Might want to do something here later
                    if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                        finishAffinity();
                    } else if(Build.VERSION.SDK_INT>=21){
                        finishAndRemoveTask();
                    }


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




    }


    @Override
    protected void onDestroy() {
        CurrentConsultationID=null;
        super.onDestroy();

    }
}
