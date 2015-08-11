package com.appslight.dentistapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.gcmpackage.GCMRegistration;
import com.appslight.dentistapp.util.CustomTypefaceSpan;
import com.appslight.dentistapp.util.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class AboutActivity extends ActionBarActivity {

    Button imgNext;
    TextView txtTC, txtSix, txtFive, tvTitle;

    Typeface typeface;
    String regId = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");

        imgNext = (Button) findViewById(R.id.imgNext);
        txtTC = (TextView) findViewById(R.id.txtTC);
        txtSix = (TextView) findViewById(R.id.txtSix);
        txtFive = (TextView) findViewById(R.id.txtFive);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvTitle.setText("Toothpic & You");
        tvTitle.setTypeface(typeface);

        String string = getResources().getString(R.string.point6);
        int upto = string.indexOf(" ");
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.point6));

        spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.6f), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSix.setText(spannableString);

        txtFive.setText(Html.fromHtml(getResources().getString(R.string.point5)), TextView.BufferType.SPANNABLE);
        txtTC.setText(Html.fromHtml("<u>Terms and Conditions</u>"));

        txtTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutActivity.this, TermsConditionsActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* SessionManager sessionManager = new SessionManager(AboutActivity.this);
                sessionManager.setAgree();*/
                startActivity(new Intent(AboutActivity.this, InstructionActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        if (checkPlayServices()) {
            if (TextUtils.isEmpty(regId)) {
                regId = GCMRegistration.registerGCM(AboutActivity.this);
                //UIUtils.log("GCM RegId: " + regId);
            }
        } else {
            //UIUtils.log("No valid Google Play Services APK found.");
        }
    }

    // Check Google Play Service Available or Not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
