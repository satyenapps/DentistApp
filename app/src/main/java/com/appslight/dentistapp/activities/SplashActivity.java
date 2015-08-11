package com.appslight.dentistapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.util.SessionManager;

/**
 * Created by Satyen on 27/05/2015.
 */
public class SplashActivity extends Activity {

    Typeface typeFace;

    TextView txtTagLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        txtTagLine = (TextView) findViewById(R.id.txtTagLine);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.tagline));
        spannableString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        txtTagLine.setText(spannableString);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SessionManager sessionManager = new SessionManager(SplashActivity.this);
                    Intent inte = null;
                    if (sessionManager.isAgreed()) {
                        inte = new Intent(SplashActivity.this,
                                InstructionActivity.class);
                    } else {
                        inte = new Intent(SplashActivity.this,
                                AboutActivity.class);
                    }
                    startActivity(inte);
                    SplashActivity.this.finish();
                }
            }
        };
        thread.start();
    }
}
