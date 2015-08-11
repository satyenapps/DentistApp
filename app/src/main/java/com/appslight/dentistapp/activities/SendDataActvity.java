package com.appslight.dentistapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SendDataActvity extends ActionBarActivity {

    //    UI Comps
    Spinner spinGen, spinLoc;
    Button btnSend;
    AutoCompleteTextView edtName;
    EditText edtDOB, edtEmail;

    DatePickerDialog dobPickerDialog;

    // Vars
    ArrayList<String> selectedImagePaths = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data_actvity);

        dbHelper = new DBHelper(SendDataActvity.this);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        edtName.setAdapter(adapter);

        SessionManager sessionManager = new SessionManager(SendDataActvity.this);

       /* if (sessionManager.isLogin()) {
            edtName.setText(sessionManager.getUserName());
            edtDOB.setText(sessionManager.getUserIDob());
            for (int i = 0; i < genArray.length; i++) {
                if (sessionManager.getUserGender().equals(genArray[i]))
                    spinGen.setSelection(i);
            }

            for (int i = 0; i < locArray.length; i++) {
                if (sessionManager.getUserLocation().equals(locArray[i]))
                    spinLoc.setSelection(i);
            }

        }*/

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

        spinGen.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genArray));
        spinLoc.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locArray));

        spinGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = genArray[i];
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.quecolor));
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
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.quecolor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                location = "";
            }
        });

        if (getIntent() != null) {
            selectedImagePaths = getIntent().getStringArrayListExtra("imagePaths");
//            Utils.setFullImageFromFilePath(selectedImagePath, imgMain);
            //Drawable d = Drawable.createFromPath(selectedImagePath);
            //imgMain.setImageDrawable(d);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                dob = edtDOB.getText().toString();
                email = edtEmail.getText().toString();

                dbHelper.insertUser(name, dob, gender, email, location);

                if (name.equals("") || dob.equals("") || gender.equals("") || location.equals("")) {
                    Utils.showToast(SendDataActvity.this, "Please enter all details");
                } else {
                    SessionManager sessionManager = new SessionManager(SendDataActvity.this);
                    Utils.showProgressDialog(SendDataActvity.this);
                   /* if (sessionManager.isLogin()) {
                        postImages(sessionManager.getUserId(), selectedImagePaths.size());
                    } else {*/
                    submitData();
//                    }
                }
                //startActivity(new Intent(SendDataActvity.this, FinalActivity.class));
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
                        SessionManager sessionManager = new SessionManager(SendDataActvity.this);
                        sessionManager.createSession(detail);
                        postImages(sessionManager.getUserId(), selectedImagePaths.size());
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
            case 5:
                postFiveImage(id);
                break;
        }
    }

    public void postOneImage(int id) {

        File file = new File(selectedImagePaths.get(0));
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadOneImage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postTwoImage(int id) {

        File file1 = new File(selectedImagePaths.get(0));
        File file2 = new File(selectedImagePaths.get(1));

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file1);
            requestParams.put("image2", file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadTwoImage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postThreeImage(int id) {

        File file1 = new File(selectedImagePaths.get(0));
        File file2 = new File(selectedImagePaths.get(1));
        File file3 = new File(selectedImagePaths.get(2));

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
        imageUrlBuilder.append("uploadThreeImage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postFourImage(int id) {

        File file1 = new File(selectedImagePaths.get(0));
        File file2 = new File(selectedImagePaths.get(1));
        File file3 = new File(selectedImagePaths.get(2));
        File file4 = new File(selectedImagePaths.get(3));

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
        imageUrlBuilder.append("uploadFourImage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void postFiveImage(int id) {

        File file1 = new File(selectedImagePaths.get(0));
        File file2 = new File(selectedImagePaths.get(1));
        File file3 = new File(selectedImagePaths.get(2));
        File file4 = new File(selectedImagePaths.get(3));
        File file5 = new File(selectedImagePaths.get(4));

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        try {
            requestParams.put("image1", file1);
            requestParams.put("image2", file2);
            requestParams.put("image3", file3);
            requestParams.put("image4", file4);
            requestParams.put("image5", file5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder imageUrlBuilder = new StringBuilder(Constants.MAIN_URL);
        imageUrlBuilder.append("uploadFiveImage.php");
        executeRequest(imageUrlBuilder.toString(), requestParams);
    }

    public void executeRequest(String url, RequestParams requestParams) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("Response", response.toString());
                Utils.dismissDialog();
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
        userMap.put("device_id", GCMRegistration.getRegistrationId(SendDataActvity.this));
        return userMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_data_actvity, menu);
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
