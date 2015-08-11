package com.appslight.dentistapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.util.CustomTypefaceSpan;

/**
 * Created by Satyen on 15/06/2015.
 */
public class InstructionActivity extends Activity {

    Button btnCamera, btnGallery, btnSend;
    TextView txtInstruction1, txtInstruction3, tvTitle;
    ImageView imgTeeth1, imgTeeth2, imgTeeth3;

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Toothpic: How to use");
        tvTitle.setTypeface(typeFace);

        txtInstruction1 = (TextView) findViewById(R.id.instruction1);
        txtInstruction3 = (TextView) findViewById(R.id.instruction3);
        btnSend = (Button) findViewById(R.id.btnSend);
        imgTeeth1 = (ImageView) findViewById(R.id.teeth1);
        imgTeeth2 = (ImageView) findViewById(R.id.teeth2);
        imgTeeth3 = (ImageView) findViewById(R.id.teeth3);

        String string = getResources().getString(R.string.instruction1);
        String toothpic = "Toothpic";
        int start = string.indexOf(" ");
        int length = toothpic.length();

        int start_2 = string.lastIndexOf("\n");

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.instruction1));

        spannableString.setSpan(new CustomTypefaceSpan("", typeFace), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.6f), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, start + length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        txtInstruction1.setText(spannableString);

        String string3 = getResources().getString(R.string.instruction3);
        int upto = string3.indexOf(" ");
        SpannableString spannableString3 = new SpannableString(getResources().getString(R.string.instruction3));

        spannableString3.setSpan(new CustomTypefaceSpan("", typeFace), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new RelativeSizeSpan(1.6f), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), 0, upto, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtInstruction3.setText(spannableString3);

        //btnSend.setText("Let\'s Toothpic");
        btnSend.setTransformationMethod(null);
        //   btnSend.setTypeface(typeFace, Typeface.BOLD);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstructionActivity.this, MainAppActivity.class);
                startActivity(intent);
            }
        });

        imgTeeth1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(InstructionActivity.this);
                dialog.setContentView(R.layout.customdialog);
                dialog.setTitle("Front");

                // set the custom dialog components - text, image and button
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.sample_f);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        imgTeeth2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(InstructionActivity.this);
                dialog.setContentView(R.layout.customdialog);
                dialog.setTitle("Lower");

                // set the custom dialog components - text, image and button
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.sample_l);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        imgTeeth3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(InstructionActivity.this);
                dialog.setContentView(R.layout.customdialog);
                dialog.setTitle("Upper");

                // set the custom dialog components - text, image and button
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.sample_u);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
}
