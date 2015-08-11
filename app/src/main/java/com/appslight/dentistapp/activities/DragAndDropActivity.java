package com.appslight.dentistapp.activities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.util.Utils;

import java.util.ArrayList;

public class DragAndDropActivity extends ActionBarActivity {

    Button btnAddView;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams layoutParams;
    String msg = "drag";
    public static int autoCount = 0;
    RelativeLayout.LayoutParams relaLayoutParams;
    int width, height;

    ArrayList<AutoCompleteTextView> autoCompleteTextViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_drawing_room);
        btnAddView = (Button) findViewById(R.id.btnAddView);
        frameLayout = (FrameLayout) findViewById(R.id.frame);

        String sizeString = Utils.getScreenResolution(DragAndDropActivity.this);
        String[] sizeArray = sizeString.split(",");
        width = Integer.parseInt(sizeArray[0]);
        height = Integer.parseInt(sizeArray[1]);


        relaLayoutParams = new RelativeLayout.LayoutParams(
                width * 25 / 100, height * 15 / 100);

        frameLayout.setOnDragListener(new MyDragListener());

        final String str[] = {"Arun", "Mathev", "Vishnu", "Vishal", "Arjun",
                "Arul", "Balaji", "Babu", "Boopathy", "Godwin", "Nagaraj"};

        btnAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView auto = new AutoCompleteTextView(DragAndDropActivity.this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                        (width, height);

                params.leftMargin = 80;
                params.topMargin = 130;

                auto.setLayoutParams(params);
                auto.setEms(10);

                ArrayAdapter<String> adp = new ArrayAdapter<String>(DragAndDropActivity.this,
                        android.R.layout.simple_dropdown_item_1line, str);

                auto.setTag("AutoTextView" + autoCount);
                auto.setThreshold(1);
                auto.setAdapter(adp);
                frameLayout.addView(auto);
                //auto.setOnDragListener(new MyDragListener());
//                auto.setOnTouchListener(new MyTouchListener());
                autoCompleteTextViews.add(auto);
                //  auto.setOnLongClickListener(new MyLongTouchListener());
                //setListeners(autoCompleteTextViews);
                autoCount++;
                setLongClickListeners();

            }
        });

    }

    public void setLongClickListeners() {
        for (int a = 0; a < autoCompleteTextViews.size(); a++) {
            autoCompleteTextViews.get(a).setOnLongClickListener(getOnLongClick(autoCompleteTextViews.get(a)));
        }
    }

    View.OnLongClickListener getOnLongClick(final AutoCompleteTextView autoCompleteTextView) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData.Item clItem = new ClipData.Item((CharSequence) view.getTag());
                String[] mimiTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(view.getTag().toString(), mimiTypes, clItem);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                // Starts the drag
                view.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        autoCompleteTextView,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                // view.setVisibility(View.INVISIBLE);
                return true;
            }
        };
    }

    final class MyLongTouchListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            ClipData.Item clItem = new ClipData.Item((CharSequence) view.getTag());
            String[] mimiTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData(view.getTag().toString(), mimiTypes, clItem);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
            // Starts the drag
            view.startDrag(dragData,  // the data to be dragged
                    myShadow,  // the drag shadow builder
                    view,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );
            // view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {


        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing

                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                   /* View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);*/
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                    View view = (View) event.getLocalState();
                    layoutParams = (FrameLayout.LayoutParams)
                            view.getLayoutParams();
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    layoutParams.leftMargin = x_cord - view.getWidth() / 2;
                    layoutParams.topMargin = y_cord - view.getHeight() / 2;
                    view.setLayoutParams(layoutParams);
                    Log.d("Tag", view.getTag().toString());
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drag_and_drop, menu);
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
