package com.safedoctor.safedoctor.UI.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safedoctor.safedoctor.R;
import com.safedoctor.safedoctor.Utils.Tools;
import com.safedoctor.safedoctor.Utils.ViewAnimation;
import com.safedoctor.safedoctor.Utils.DepthPageTransformer;

/**
 * Created by stevkky on 10/18/17.
 */

public class ActivityWelcome extends AppCompatActivity
{
    private static final int MAX_STEP = 5;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    private View back_drop;
    private boolean rotate = false;
    private View lyt_reg;
    private View lyt_login;


    private String about_title_array[] = {
            "WELCOME TO SAFE DOKTOR",
            "ENTER PHONE NUMBER",
            "PHONE NUMBER VALIDATION",
            "CONTINUE REGISTRATION",
            "FINISHED!"
    };
    private String about_description_array[] = {
            "\nBringing health care to your doorstep at your comfort zone. \n\n\nSpeak to qualified doctors,\nGet first aid tips \nand much more....",
            "\nValidate your phone number in 2 simple steps.\n\n First enter the number",
            "\nEnter 6 digit code sent to you via SMS to the number provided",
            "\nGive us your Name, Sex, Date of Birth and choose a Password",
            "\nSafe Doktor is that simple! Go on!\n\n Just try it!"
    };
    private int about_images_array[] = {
            R.drawable.ic_safedoctor_n1,
            R.drawable.img_wizard_2,
            R.drawable.sms_token,
            R.drawable.user_details,
            R.drawable.register_user
    };

    private int bg_images_array[] = {
            R.drawable.image_15,
            R.drawable.image_9,
            R.drawable.image_12,
            R.drawable.image_20,
            R.drawable.image_11
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_welcome_page);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setPageTransformer(true, new DepthPageTransformer());

        Tools.setSystemBarColor(this, R.color.grey_20);

        initComponent();

       // displayNotification();
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_reg);
            ViewAnimation.showIn(lyt_login);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_reg);
            ViewAnimation.showOut(lyt_login);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void initComponent() {

        back_drop = findViewById(R.id.back_drop);

        final FloatingActionButton fab_register = (FloatingActionButton) findViewById(R.id.fab_register);
        final FloatingActionButton fab_login = (FloatingActionButton) findViewById(R.id.fab_login);
        final FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);


        lyt_reg = findViewById(R.id.lyt_reg);
        lyt_login = findViewById(R.id.lyt_login);
        ViewAnimation.initShowOut(lyt_reg);
        ViewAnimation.initShowOut(lyt_login);
        back_drop.setVisibility(View.GONE);

       final Animation animation = AnimationUtils.loadAnimation(ActivityWelcome.this, R.anim.fab_grow);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab_add.clearAnimation();
                animation.cancel();
                animation.reset();
                toggleFabMode(v);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });


        fab_add.startAnimation(animation);

        fab_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        fab_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        lyt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login();
            }
        });

        lyt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }



    private void register()
    {
        Intent intent = new Intent(getApplicationContext() ,Enter_Phonenumber.class);
        startActivity(intent);
        finish();
    }

    private void login()
    {
        Intent intent = new Intent(getApplicationContext() ,FormLogin.class);
        startActivity(intent);
        finish();
    }
    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Button btnNext;
        private Button btn_skip;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_welcome_page, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);
            ((ImageView) view.findViewById(R.id.image_bg)).setImageResource(bg_images_array[position]);


            btnNext = (Button) view.findViewById(R.id.btn_next);

            if (position == about_title_array.length - 1) {
                btnNext.setText("Get Started");
            } else {
                btnNext.setText("Next");
            }


            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = viewPager.getCurrentItem() + 1;
                    if (current < MAX_STEP) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext() ,Enter_Phonenumber.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            btn_skip = (Button)  view.findViewById(R.id.btn_skip);
            btn_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext() ,ActivityLandingPage.class);
                    startActivity(intent);
                    finish();
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void displayNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Sample Notification")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentText("Safe Doktor Appointment Today")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.photo_male_5))
                .setSmallIcon(R.drawable.safe);
        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        manager.notify(1, notification);
    }
}
