package com.appslight.dentistapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appslight.dentistapp.R;
import com.appslight.dentistapp.gcmpackage.GCMRegistration;
import com.appslight.dentistapp.util.Constants;
import com.appslight.dentistapp.util.DBHelper;
import com.appslight.dentistapp.util.Detail;
import com.appslight.dentistapp.util.RealPathUtil;
import com.appslight.dentistapp.util.SessionManager;
import com.appslight.dentistapp.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainAppActivity extends ActionBarActivity implements View.OnClickListener {

    //    UI Comps
    TextView tvTitle;
    Spinner spinGen, spinLoc;
    AutoCompleteTextView edtName;
    EditText edtDOB, edtEmail;
    ImageView img1, img2, img3, img4;
    Button btnCamera, btnGallery, btnSend;

    DatePickerDialog dobPickerDialog;

    // Vars
    String[] genArray;
    String[] locArray;
    String gender;
    String location;
    String name;
    String dob;
    String email;
    private SimpleDateFormat dateFormatter;

    DBHelper dbHelper;
    Cursor userCursor;
    ArrayList<String> userList = new ArrayList<>();

    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;
    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;
    String finalImagePath = "";
    ArrayList<String> imageArrayList = new ArrayList<>();

    // Session
    SessionManager sessionManager;
    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");
        sessionManager = new SessionManager(MainAppActivity.this);

        btnSend = (Button) findViewById(R.id.btnSend);

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvTitle.setText("Toothpic: Welcome to the Clinic");
        tvTitle.setTypeface(typeFace);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        imageArrayList.clear();
        dbHelper = new DBHelper(MainAppActivity.this);
        userCursor = dbHelper.getUsers();

        while (userCursor.moveToNext()) {
            userList.add(userCursor.getString(userCursor.getColumnIndexOrThrow(DBHelper.USERS_COLUMN_NAME)));
        }


        genArray = getResources().getStringArray(R.array.gender_array);
        locArray = getResources().getStringArray(R.array.location_array);

        btnSend = (Button) findViewById(R.id.btnSend);
        spinGen = (Spinner) findViewById(R.id.spinGen);
        spinLoc = (Spinner) findViewById(R.id.spinLoc);
        edtName = (AutoCompleteTextView) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDOB = (EditText) findViewById(R.id.edtDOB);

        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        edtName.setAdapter(adapter);

        SessionManager sessionManager = new SessionManager(MainAppActivity.this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobPickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edtDOB.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

//        spinGen.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genArray));
        spinLoc.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, locArray) {
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v.findViewById(android.R.id.text1)).setTextColor(getResources().getColor(R.color.btncolor));
                return v;
            }
        });
        spinLoc.setSelection(spinLoc.getAdapter().getCount());
        ArrayAdapter<String> genAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, genArray) {

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v.findViewById(android.R.id.text1)).setTextColor(getResources().getColor(R.color.btncolor));
                return v;
            }
        };
        spinGen.setAdapter(genAdapter);
        //genAdapter.addAll(genArray);
        spinGen.setSelection(genAdapter.getCount());

        spinGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = genArray[i];
                //adapterView.setBackgroundColor(getResources().getColor(R.color.boxcolor));
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.btncolor));
                ((TextView) adapterView.getChildAt(0)).setPadding(0, 0, 0, 0);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(5);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = "";
            }
        });

        spinLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = locArray[i];
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.btncolor));
                ((TextView) adapterView.getChildAt(0)).setPadding(0, 0, 0, 0);
                ((TextView) adapterView.getChildAt(0)).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                location = "";
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = edtName.getText().toString();
                dob = edtDOB.getText().toString();
                email = edtEmail.getText().toString();

                dbHelper.insertUser(name, dob, gender, email, location);

                if (name.equals("") || dob.equals("") || gender.equals("") || location.equals("")) {
                    Utils.showToast(MainAppActivity.this, "Please enter all details");
                } else {
                    SessionManager sessionManager = new SessionManager(MainAppActivity.this);
                    Utils.showProgressDialog(MainAppActivity.this);
                   /* if (sessionManager.isLogin()) {
                        postImages(sessionManager.getUserId(), imageArrayList.size());
                    } else {*/
                    submitData();

//                    }
                }
                //startActivity(new Intent(MainAppActivity.this, FinalActivity.class));
            }
        });

    }

    public void submitData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(getUserMap());
        String registerUrl = Constants.MAIN_URL;
        StringBuilder stringBuilder = new StringBuilder(registerUrl);
        stringBuilder.append("register_user.php");
        Log.d("register url", stringBuilder.toString());
        client.post(stringBuilder.toString(), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        int id = Integer.parseInt(response.getString(Constants.ID));
                        String name = response.getString(Constants.NAME);
                        String dob = response.getString(Constants.DOB);
                        String gender = response.getString(Constants.GENDER);
                        String location = response.getString(Constants.LOCATION);

                        Detail detail = new Detail(id, name, dob, "", gender, location);
                        SessionManager sessionManager = new SessionManager(MainAppActivity.this);
                        sessionManager.createSession(detail);
                        postImages(sessionManager.getUserId(), imageArrayList.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void postImages(int id, int count) {
        switch (count) {
            case 1:
                postOneImage(id);
                break;
            case 2:
                postTwoImage(id);
                break;
            case 3:
                postThreeImage(id);
                break;
            case 4:
                postFourImage(id);
                break;
        }
    }

    public void postOneImage(int id) {

        File file = new File(imageArrayList.get(0));
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadimage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postTwoImage(int id) {

        File file1 = new File(imageArrayList.get(0));
        File file2 = new File(imageArrayList.get(1));

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file1);
            requestParams.put("image2", file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadimage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postThreeImage(int id) {

        File file1 = new File(imageArrayList.get(0));
        File file2 = new File(imageArrayList.get(1));
        File file3 = new File(imageArrayList.get(2));

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file1);
            requestParams.put("image2", file2);
            requestParams.put("image3", file3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadimage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postFourImage(int id) {

        File file1 = new File(imageArrayList.get(0));
        File file2 = new File(imageArrayList.get(1));
        File file3 = new File(imageArrayList.get(2));
        File file4 = new File(imageArrayList.get(3));

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file1);
            requestParams.put("image2", file2);
            requestParams.put("image3", file3);
            requestParams.put("image4", file4);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadimage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void executeRequest(String url, RequestParams requestParams) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(1000000);
        client.setResponseTimeout(1000000);
        client.setTimeout(100000);
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("Response", response.toString());
                Utils.dismissDialog();
                Intent intent = new Intent(MainAppActivity.this, ThankyouActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Utils.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Utils.dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("error", responseString);
                Utils.dismissDialog();
            }
        });
    }

    public Map<String, String> getUserMap() {
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", edtName.getText().toString());
        userMap.put("dob", edtDOB.getText().toString());
        userMap.put("location", location);
        userMap.put("gender", gender);
        userMap.put("device_id", GCMRegistration.getRegistrationId(MainAppActivity.this));
        return userMap;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
        if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
        if (savedInstanceState.containsKey(CAPTURED_PHOTO_URI_KEY)) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Getters and setters.
     */

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }

    /**
     * Start the camera by dispatching a camera intent.
     */
    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = MainAppActivity.this;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(context, "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(MainAppActivity.this, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                setCapturedImageURI(fileUri);
                setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void setImagefrmGallery() {
        // To open up a gallery browser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        // To handle when an image is selected from the browser, add the following to your Activity
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intent
        setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            addPhotoToGallery();
            finalImagePath = getCurrentPhotoPath();

            if (sessionManager.getImage().equals("img1")) {
                Utils.setFullImageFromFilePath(finalImagePath, img1);
            } else if (sessionManager.getImage().equals("img2")) {
                Utils.setFullImageFromFilePath(finalImagePath, img2);
            } else if (sessionManager.getImage().equals("img3")) {
                Utils.setFullImageFromFilePath(finalImagePath, img3);
            } else if (sessionManager.getImage().equals("img4")) {
                Utils.setFullImageFromFilePath(finalImagePath, img4);
            }
            imageArrayList.add(finalImagePath);

        } else if (resultCode == RESULT_OK && requestCode == 1) {

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

            File file = new File(realPath);

            if (file.exists()) {
                finalImagePath = file.getAbsolutePath();
                /*Intent intent = new Intent(MainAppActivity.this, MainAppActivity.class);
                intent.putExtra("imagePath", finalImagePath);
                startActivity(intent);*/
                if (sessionManager.getImage().equals("img1")) {
                    Utils.setFullImageFromFilePath(finalImagePath, img1);
                } else if (sessionManager.getImage().equals("img2")) {
                    Utils.setFullImageFromFilePath(finalImagePath, img2);
                } else if (sessionManager.getImage().equals("img3")) {
                    Utils.setFullImageFromFilePath(finalImagePath, img3);
                } else if (sessionManager.getImage().equals("img4")) {
                    Utils.setFullImageFromFilePath(finalImagePath, img4);
                }

                imageArrayList.add(finalImagePath);
               /* fp = file.getAbsolutePath();
                d = Drawable.createFromPath(file.getAbsolutePath());
                mDrawingPad.setImageDrawable(d);
                mDrawingPad.setImageURI(data.getData());*/
            } else {
                System.out.println("File Not Found");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_app, menu);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

         /*   case R.id.btnSend:
                Intent intent = new Intent(MainAppActivity.this, MainAppActivity.class);
                intent.putStringArrayListExtra("imagePaths", imageArrayList);
                startActivity(intent);
                break;*/
            case R.id.img1:
                sessionManager.saveImage("img1");
                showDialog();
                break;
            case R.id.img2:
                showDialog();
                sessionManager.saveImage("img2");
                break;
            case R.id.img3:
                showDialog();
                sessionManager.saveImage("img3");
                break;
            case R.id.img4:
                showDialog();
                sessionManager.saveImage("img4");
                break;

        }

    }

    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Image chooser").setMessage("Capture / Choose Image")
                .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                })
                .setNegativeButton("Choose from Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setImagefrmGallery();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.show();
    }
}
