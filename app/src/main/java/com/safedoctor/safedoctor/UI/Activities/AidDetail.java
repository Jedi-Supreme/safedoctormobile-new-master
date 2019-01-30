package com.safedoctor.safedoctor.UI.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.R;

public class AidDetail extends AppCompatActivity {

    private TextView firstAidTitle, firstAidActionsToTake;
    private ImageView firstAidImageView;
    private Aid.Content selectedFirstAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aid_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstAidImageView = findViewById(R.id.aidImageView);
        firstAidTitle = findViewById(R.id.aidTitle);
        firstAidActionsToTake = findViewById(R.id.aidActionsToTake);

        Bundle data = getIntent().getExtras();
        selectedFirstAid = (Aid.Content) getIntent().getSerializableExtra("firstaid");

        if (selectedFirstAid != null){

            if(selectedFirstAid.getImage() != null)
            {
                byte[] imageBytes = Base64.decode(selectedFirstAid.getImage(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                firstAidImageView.setImageBitmap(decodedImage);
            }
            firstAidTitle.setText(selectedFirstAid.getTitle());
            getSupportActionBar().setTitle(selectedFirstAid.getTitle());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                firstAidActionsToTake.setText(Html.fromHtml(selectedFirstAid.getContent(),Html.FROM_HTML_MODE_COMPACT));
            }else{
                firstAidActionsToTake.setText(Html.fromHtml(selectedFirstAid.getContent()));
            }

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.post_detail_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
