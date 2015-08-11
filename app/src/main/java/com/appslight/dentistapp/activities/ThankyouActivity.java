package com.appslight.dentistapp.activities;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.util.CustomTypefaceSpan;
import com.appslight.dentistapp.util.SessionManager;

public class ThankyouActivity extends ActionBarActivity {

    TextView btnShare, btnRate, btnFB, btnTweet;
    TextView txtFive, tvThanks, tvTitle, tvLoveTo;

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_dummy);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Toothpic: Good bye");
        tvTitle.setTypeface(typeFace);

        tvLoveTo = (TextView) findViewById(R.id.tvLoveTo);
        txtFive = (TextView) findViewById(R.id.txtFive);
        tvThanks = (TextView) findViewById(R.id.tvThanks);
        btnShare = (TextView) findViewById(R.id.btnShare);
        btnRate = (TextView) findViewById(R.id.btnRate);
        btnFB = (TextView) findViewById(R.id.btnFB);
        btnTweet = (TextView) findViewById(R.id.btnTweet);

        String string3 = "Toothpic would love to hear from you:";//getResources().getString(R.string.instruction3);
        int upto = string3.indexOf(" ");
        SpannableString spannableString3 = new SpannableString(string3);
        spannableString3.setSpan(new CustomTypefaceSpan("", typeFace), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new RelativeSizeSpan(1.6f), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoveTo.setText(spannableString3);


        String string = getResources().getString(R.string.dent);
        String toothpic = "Toothpic";
        int start = string.indexOf(" ");
        int length = toothpic.length();
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.dent));

        spannableString.setSpan(new CustomTypefaceSpan("", typeFace), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.6f), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 52, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 52, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtFive.setText(spannableString);

        String thanks = getResources().getString(R.string.thanku);
        int last = thanks.lastIndexOf(" ");

        SpannableString spannableStringThanks = new SpannableString(getResources().getString(R.string.thanku));

        spannableStringThanks.setSpan(new CustomTypefaceSpan("", typeFace), last, thanks.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringThanks.setSpan(new RelativeSizeSpan(1.6f), last, thanks.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringThanks.setSpan(new StyleSpan(Typeface.BOLD), last, thanks.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvThanks.setText(spannableStringThanks);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thankyou, menu);
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
