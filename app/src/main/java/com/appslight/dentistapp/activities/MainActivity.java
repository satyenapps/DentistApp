package com.appslight.dentistapp.activities;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.calendarstock.ColorPickerDialog;
import com.appslight.dentistapp.calendarstock.ColorPickerSwatch;
import com.appslight.dentistapp.util.RealPathUtil;
import com.appslight.dentistapp.util.Utils;
import com.appslight.dentistapp.view.DrawingView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    // Vars
    String fp;
    Drawable d;
    private int mSelectedColorCal0 = 0;
    private int preColor = 0;
    FrameLayout.LayoutParams layoutParams;
    String msg = "drag event";

    // UI Comps
    FrameLayout frameLayout;
    RelativeLayout relDraw;
    Button btnSelect;
    Button btnColorPicker;
    Button btnEraser;
    Button btnAddView;
    Button btnSave;
    DrawingView mDrawingView;
    ImageView mDrawingPad;

    ArrayList<AutoCompleteTextView> autoCompleteTextViews = new ArrayList<>();
    String str[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mDrawingView = new DrawingView(this, MainActivity.this);
        setContentView(R.layout.screen_drawing_room);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnEraser = (Button) findViewById(R.id.btnEraser);
        btnAddView = (Button) findViewById(R.id.btnAddView);
        btnSave = (Button) findViewById(R.id.btnSave);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        relDraw = (RelativeLayout) findViewById(R.id.relDraw);

        final String str[] = {"Arun", "Mathev", "Vishnu", "Vishal", "Arjun",
                "Arul", "Balaji", "Babu", "Boopathy", "Godwin", "Nagaraj"};

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawingView.eraserMode) {
                    mDrawingView.eraserMode = false;
                    releaseEraser();
                } else {
                    startEraser();
                    mDrawingView.eraserMode = true;
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = loadBitmapFromView(frameLayout);
                mDrawingPad.setImageBitmap(bitmap);
            }
        });

        btnAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawingView.touched)
                    mDrawingView.touched = false;
                else
                    mDrawingView.touched = true;
               /* AutoCompleteTextView auto = new AutoCompleteTextView(MainActivity.this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                        ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.leftMargin = 80;
                params.topMargin = 130;

                auto.setLayoutParams(params);
                auto.setEms(10);

                ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, str);

                auto.setTag("AutoTextView");
                auto.setThreshold(1);
                auto.setAdapter(adp);
                frameLayout.addView(auto);
                autoCompleteTextViews.add(auto);
                setListeners(autoCompleteTextViews);
*/

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImagefrmGallery();
            }
        });

        final int[] mColor = Utils.ColorUtils.colorChoice(this);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawingView.eraserMode) {
                    mDrawingView.eraserMode = false;
                    releaseEraser();
                }

                ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(
                        R.string.color_picker_default_title, mColor,
                        mSelectedColorCal0, 5,
                        Utils.isTablet(MainActivity.this) ? ColorPickerDialog.SIZE_LARGE
                                : ColorPickerDialog.SIZE_SMALL);

                colorcalendar.setOnColorSelectedListener(colorcalendarListener);
                colorcalendar.show(getFragmentManager(), "cal");
            }
        });
        mDrawingPad = (ImageView) findViewById(R.id.view_drawing_pad);
        frameLayout.addView(mDrawingView);

        mDrawingView.mPaint.setStrokeWidth(5);
    }

    public void addView(int x, int y) {
        AutoCompleteTextView auto = new AutoCompleteTextView(MainActivity.this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (100, 50);

        params.leftMargin = x;
        params.topMargin = y;

        auto.setLayoutParams(params);
        auto.setEms(10);

        final String str[] = {"Arun", "Mathev", "Vishnu", "Vishal", "Arjun",
                "Arul", "Balaji", "Babu", "Boopathy", "Godwin", "Nagaraj"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, str);

        auto.setTag("AutoTextView");
        auto.setThreshold(1);
        auto.setAdapter(adp);
        relDraw.addView(auto, params);
    }

    public void setListeners(ArrayList<AutoCompleteTextView> autoList) {

        for (int a = 0; a < autoList.size(); a++) {
            AutoCompleteTextView auto = autoList.get(a);
            auto.setOnLongClickListener(new View.OnLongClickListener() {
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
            });
            auto.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            layoutParams = (FrameLayout.LayoutParams)
                                    v.getLayoutParams();
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                            // Do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                            int x_cord = (int) event.getX();
                            int y_cord = (int) event.getY();
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            layoutParams.leftMargin = x_cord - v.getWidth() / 2;
                            layoutParams.topMargin = y_cord - v.getHeight() / 2;
                            v.setLayoutParams(layoutParams);
                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            x_cord = (int) event.getX();
                            y_cord = (int) event.getY();
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                            // Do nothing
                            break;
                        case DragEvent.ACTION_DROP:
                            Log.d(msg, "ACTION_DROP event");

                            // View view = (View) event.getLocalState();

//                        view.setVisibility(View.VISIBLE);
                            // Do nothing
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }


    public void setListeners(AutoCompleteTextView auto) {
        auto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData.Item clItem = new ClipData.Item((CharSequence) view.getTag());
                String[] mimiTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(view.getTag().toString(), mimiTypes, clItem);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                // Starts the drag
                view.startDrag(null,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        view,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                // view.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        auto.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (FrameLayout.LayoutParams)
                                v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                        // Do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");

                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord - v.getWidth() / 2;
                        layoutParams.topMargin = y_cord - v.getHeight() / 2;
                        v.setLayoutParams(layoutParams);
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
                        // Do nothing
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");

                        // View view = (View) event.getLocalState();

//                        view.setVisibility(View.VISIBLE);
                        // Do nothing
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public Bitmap loadBitmapFromView(View v) {
        // mDrawingPad.setImageResource(R.drawable.ic_launcher_);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        v.draw(c);
        v.invalidate();
        try {
            FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + new Date() + "_file.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void startEraser() {
        mDrawingView.mPaint.setAlpha(0);
        preColor = mSelectedColorCal0;
        mSelectedColorCal0 = Color.TRANSPARENT;
        mDrawingView.mPaint.setColor(Color.TRANSPARENT);
        mDrawingView.mPaint.setStrokeWidth(10);
        mDrawingView.mPaint.setStyle(Paint.Style.STROKE);
        mDrawingView.mPaint.setMaskFilter(null);
        mDrawingView.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDrawingView.mPaint.setAntiAlias(true);
    }

    public void releaseEraser() {
        mSelectedColorCal0 = preColor;
        mDrawingView.mPaint.setAntiAlias(true);
        mDrawingView.mPaint.setDither(true);
        mDrawingView.mPaint.setColor(mSelectedColorCal0);
        mDrawingView.mPaint.setStyle(Paint.Style.STROKE);
        mDrawingView.mPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawingView.mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawingView.mPaint.setXfermode(null);
        mDrawingView.mPaint.setStrokeWidth(5);
    }

    // Implement listener to get selected color value
    ColorPickerSwatch.OnColorSelectedListener colorcalendarListener = new ColorPickerSwatch.OnColorSelectedListener() {

        @Override
        public void onColorSelected(int color) {
            mSelectedColorCal0 = color;
            mDrawingView.mPaint.setColor(color);
            //NsMenuItemModel item = mAdapter.getItem(mLastPosition);
            /*if (item != null)
                item.colorSquare = color;
            mAdapter.notifyDataSetChanged();*/
        }
    };

    public void setImagefrmGallery() {
        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        // To handle when an image is selected from the browser, add the following to your Activity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String realPath;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

                // currImageURI is the global variable I?m using to hold the content:// URI of the image
                /*Uri currImageURI = data.getData();
                System.out.println("Hello=======" + currImageURI);
                String s =getRealPathFromURI(currImageURI);*/
                File file = new File(realPath);

                if (file.exists()) {
                    fp = file.getAbsolutePath();
                    d = Drawable.createFromPath(file.getAbsolutePath());
//                    mDrawingPad.setImageDrawable(d);
                    mDrawingPad.setImageURI(data.getData());
                } else {
                    System.out.println("File Not Found");
                }
            }
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
