package com.dsd.machinerental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateMachinery extends AppCompatActivity {
    private static GridView selectedImageGridView;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value
    String path;
    LinearLayout linearLayout;
    EditText mHousePrice,mYearBuiltTake,mHouseDescription,mBuildingSize,mCompoundSize;
    Button mRegistration, mHousePhoto, mAddMorePhoto,mCloseD,mEndBrok;
    String machineType, machineModel,
            Responses, ownerPhone, ownerName,mLatitude,
            mLongitude, houseDescription,haveDriver,JSON_STRING,function,isActive;
    private int builtYearString;
    private double mLocLat, mLocLng;
    private Uri resultUriSignUP;
    int machinePrice, machineId;
    private int serverResponseCode = 0;
    TextView mTextView;
    Handler mHandler;
    String userId;
    int mUserIds;
    private ProgressDialog dialog = null;

    private ArrayList<String> pathList = new ArrayList<>();
    String price,uStatus;
    private ArrayList<CustomSpinnerItem> mCustomSpinnerList,mCustomModelSpinner,mHaveDriverSpinner;
    private McustoemItemAdapter mCustomeSpinnerAdapter;
    private ArrayAdapter mOtheradpter;
    private DatePicker datePicker;
    private Calendar calendar;
    private Machine machines;
    private List<Machine> machineList;
    private TextView dateView;
    private int year, month, day;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_machinery);
        mToolbar =  findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        machineList = new ArrayList<>();
//data from spinner
        JSON_STRING = getIntent().getStringExtra("JsonObject");
        function = getIntent().getStringExtra("func");
        isActive = null;
        uStatus = getIntent().getStringExtra("mStatus");

        //getSharedImages();
        //selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);

        JSONObject machine = null;
        try {
            machine = new JSONObject(JSON_STRING);

            machineList.add(machines = new Machine(
                    machine.getInt(Config.TAG_ID),
                    machine.getInt(Config.TAG_USER_ID),
                    machine.getString(Config.TAG_MACHINE_TYPE),
                    machine.getString(Config.TAG_MACHINE_MODEL),
                    machine.getString(Config.TAG_WITH_OR_WITHOUT_DRIVER),
                    machine.getString(Config.TAG_PRICE),
                    machine.getString(Config.TAG_BUILT_YEAR),
                    machine.getString(Config.TAG_DESCRIPTION),
                    machine.getString(Config.TAG_PHOTO),
                    machine.getString(Config.TAG_USER_NAME),
                    machine.getString(Config.TAG_PHONE)
            ));
        }catch (Exception e){

        }



        initList();
        modelListInit();
        driverListInit();
        //displaying user name
        getDataFromSpinner();
//getting them
        Intent intent = getIntent();
        mLocLat = intent.getDoubleExtra("mLoclatString", 0);
        mLocLng = intent.getDoubleExtra("mLoclongString", 0);
        mLatitude = mLocLat+"";
        mLongitude = mLocLng+"";
        userId = intent.getStringExtra("ownerId");

        ownerName = machines.getPrice();
        ownerPhone = machines.getMachineDescription();
        mUserIds = Integer.valueOf(userId);
        //mHouseLocation = findViewById(R.id.locations);
        mYearBuiltTake = findViewById(R.id.enterYear);
        mHousePrice = findViewById(R.id.price);
        mRegistration = findViewById(R.id.registerBtn);
        mHousePhoto = findViewById(R.id.browsePhoto);
        mAddMorePhoto = findViewById(R.id.addphotos);
        mTextView = findViewById(R.id.housePhoto);
        mHouseDescription = findViewById(R.id.houseDescription);
        mBuildingSize = findViewById(R.id.buildSquareMeter);
        mCompoundSize = findViewById(R.id.compoundSquareM);
        mCloseD = findViewById(R.id.closeD);
        machineId = machines.getMachineId();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        linearLayout = findViewById(R.id.addPhotoContainer);
        mEndBrok = findViewById(R.id.endBroking);

        if (function.equals("inactive")){
            mEndBrok.setText(getString(R.string.activate_txt));
        }
        mEndBrok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(UpdateHome.this, "h Id"+houses.getId(), Toast.LENGTH_SHORT).show();
                if (function.equals("active")) {
                    deActivateTheHouse(machines.getMachineId());
                }else if (function.equals("inactive")){
                    activateTheHouse(machines.getMachineId());
                }
            }
        });



        if (!machines.getMachineModel().equals(null)){
            builtYearString = Integer.parseInt(machines.getMachineModel());
            mYearBuiltTake.setText(machines.getMachineModel());
        }

        if (!machines.getImage().equals(null)){
            mHousePrice.setText(machines.getImage());
        }
        mYearBuiltTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateMachinery.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
      //  Toast.makeText(this, "machine Id  "+machineId, Toast.LENGTH_SHORT).show();

        // Toast.makeText(this, "Uname + u phone "+ownerName+ " "+machineId, Toast.LENGTH_SHORT).show();
        mHousePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

                //startActivityForResult(new Intent(UpdateMachinery.this, CustomGallery_Activity.class), CustomGallerySelectId);

            }
        });
        mCloseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateMachinery.this,MachineMapsActivity.class);
                intent.putExtra("mUsrId",userId);
                intent.putExtra("uName",ownerName);
                intent.putExtra("userPhone",ownerPhone);

                startActivity(intent);
                finish();
            }
        });
        mAddMorePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Responses != null) {
                    Intent intent = new Intent(UpdateMachinery.this, InsertMoreMachineryPicture.class);
                    intent.putExtra("HouseIds", Responses);
                    startActivity(intent);
                }
            }
        });
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveHouseInformation();
                price = mHousePrice.getText().toString();
                houseDescription = mHouseDescription.getText().toString();
                if (houseDescription.isEmpty()){
                    houseDescription = "For the description of the House please contact the owner of the " +
                            "property";
                }
                if (price.isEmpty()) {
                    mHousePrice.setError("Price is Required");
                    mHousePrice.requestFocus();
                }
//                if (houseDescription.isEmpty()) {
//                    mHouseDescription.setError("House Description is Required");
//                    mHouseDescription.requestFocus();
//                }
                if (resultUriSignUP==null){
                    Toast.makeText(UpdateMachinery.this, getString(R.string.select_photo), Toast.LENGTH_SHORT).show();
                }else {
                    path = getPath(resultUriSignUP);

                }
                if (!price.isEmpty()) {
                    dialog = ProgressDialog.show(UpdateMachinery.this, "",
                            "Uploading file...", true);
                }else {
                    Toast.makeText(UpdateMachinery.this, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
                }
                new Thread(new Runnable() {
                    public void run() {
                        if (path!=null){
                            uploadFile(path);
                        }else {
                            if (dialog==null) {

                            }else {
                                dialog.dismiss();
                            }
                            mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message message) {
                                    // This is where you do your work in the UI thread.
                                    // Your worker tells you in the message what to do.

                                    Toast.makeText(UpdateMachinery.this, "Please Insert a photo", Toast.LENGTH_SHORT).show();
                                }
                            };
                        }

                    }
                }).start();
            }
        });

    }
    private void activateTheHouse(int hid) {
        final String id = String.valueOf(hid);
        class DeactivateHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateMachinery.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                isActive = s;
                if (!isActive.equals(null)) {
                    Toast.makeText(UpdateMachinery.this, "Successfully Activated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateMachinery.this,MachineMapsActivity.class);
                    intent.putExtra("mUsrId",userId);
                    intent.putExtra("userStatus",uStatus);
                    intent.putExtra("uName",machines.getPrice());
                    intent.putExtra("userPhone",machines.getMachineDescription());
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UpdateMachinery.this, "Something Goes Wrong", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            protected String doInBackground(Void... v) {
                //HashMap<String,String> params = new HashMap<>();

                //params.put(Config.KEY_HOUSE_ID,hId);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(Config.URL_ACTIVATE_MACHINERY, id);
                return res;

            }
        }
        DeactivateHouse dh = new DeactivateHouse();
        dh.execute();

    }
    private void deActivateTheHouse(final int hId) {
        final String id = String.valueOf(hId);
        class DeactivateHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateMachinery.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                isActive = s;
                if (!isActive.equals(null)) {
                    Toast.makeText(UpdateMachinery.this, "Successfully deactivated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateMachinery.this,MachineMapsActivity.class);
                    intent.putExtra("mUsrId",userId);
                    intent.putExtra("userStatus",uStatus);
                    intent.putExtra("uName",machines.getPrice());
                    intent.putExtra("userPhone",machines.getMachineDescription());
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UpdateMachinery.this, "Something Goes Wrong", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            protected String doInBackground(Void... v) {
                //HashMap<String,String> params = new HashMap<>();

                //params.put(Config.KEY_HOUSE_ID,hId);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(Config.URL_DEACT_MACHINERY, id);
                return res;

            }
        }
        DeactivateHouse dh = new DeactivateHouse();
        dh.execute();

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            builtYearString =year;
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mYearBuiltTake.setText(getString(R.string.built_year_txt)+": "+builtYearString+"");
        }

    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public int uploadFile(String sourceFileUri) {
        //serviceType = mHouseServiceType.getText().toString();
        //noofRooms =    mHouseNoRooms.getText().toString();
        //houseforrentotsale = mHouseForrentOrsale.getText().toString().trim();

        machinePrice = Integer.parseInt(price);
        final String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "---------uiytuhjfndsjk";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + pathList);
            runOnUiThread(new Runnable() {
                public void run() {
                    mTextView.setText("Source File not exist :" + fileName);
                }
            });
            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(Config.URL_UPDATE_MACHINERY);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                // conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8");
                // conn.setRequestProperty("image", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_USER_ID+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + userId.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(userId + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_USER_NAME+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + ownerName.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(ownerName + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_MACHINE_ID+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + machineId+"".length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(machineId + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_PHONE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + ownerPhone.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(ownerPhone + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_MACHINE_TYPE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + machineType.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(machineType + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_MACHINE_MODEL+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + machineModel.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(machineModel + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);


                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_WITH_OR_WITHOUT_DRIVER+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + haveDriver.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(haveDriver + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_PRICE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + (machinePrice+"").length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(machinePrice + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_BUILT_YEAR+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + builtYearString+"".length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(builtYearString + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);


                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_DESCRIPTION+"\"" + lineEnd);
                dos.writeBytes("Content-Type:text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + houseDescription.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(URLEncoder.encode(houseDescription, "Utf-8") + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_LATITUDE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + mLatitude.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(mLatitude + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_LONGITUDE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + mLongitude.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(mLongitude + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                InputStream urlInputStream = conn.getInputStream();
                String json = null;
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(urlInputStream, "iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) { sb.append(line + "\n");
                    }
                    urlInputStream.close();
                    Responses = sb.toString();


                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
                JSONObject jObj;
                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(Responses);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                Log.d("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                Log.d("content",
                        "HTTP Response content is : " + Responses.toString() + ": "
                                + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(UpdateMachinery.this,
                                    "File Upload Complete.", Toast.LENGTH_SHORT)
                                    .show();
                            //linearLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(UpdateMachinery.this, InsertMoreMachineryPicture.class);
                            intent.putExtra("HouseIds", Responses);
                            intent.putExtra("mUsrId",userId);
                            intent.putExtra("uName", ownerName);
                            intent.putExtra("userPhone",ownerPhone);
                            intent.putExtra("userStatus","1");

                            startActivity(intent);
                            finish();
                        }
                    });
                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        mTextView.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UpdateMachinery.this,
                                "MalformedURLException", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        mTextView.setText("Connection Time Out ");
                        Toast.makeText(UpdateMachinery.this,
                                "Connection Problem ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Uploadrver Exception",
                        "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
    private void getDataFromSpinner() {
        Spinner spinnerType = findViewById(R.id.propertyTypeList);
        spinnerType.setPrompt(getString(R.string.property_type_txt));
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mCustomSpinnerList);
        spinnerType.setAdapter(mCustomeSpinnerAdapter);
        if(machines.getmUname() != null) {
            int spinnerPosition = getPositionMachineType(machines.getmUname());
            spinnerType.setSelection(spinnerPosition);
        }
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    machineType = "Excavators";
                } else if (position == 1) {
                    machineType = "Backhoe";
                } else if (position == 2) {
                    machineType = "Dragline Excavator";
                } else if (position == 3) {
                    machineType = "Bulldozers";
                } else if (position == 4) {
                    machineType = "Graders";
                } else if (position == 5) {
                    machineType = "Wheel Tractor Scraper";
                } else if (position == 6) {
                    machineType = "Trenchers";
                } else if (position == 7) {
                    machineType = "Loaders";
                } else if (position == 8) {
                    machineType = "Tower Cranes";
                }else if (position == 9) {
                    machineType = "Pavers";
                }else if (position == 10) {
                    machineType = "Compactors";
                }else if (position == 11) {
                    machineType = "Telehandlers";
                }else if (position == 12) {
                    machineType = "Feller Bunchers";
                }else if (position == 13) {
                    machineType = "Dump Trucks";
                }else if (position == 14) {
                    machineType = "Pile Boring Machine";
                }else if (position == 15) {
                    machineType = "Pile Driving Machine";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner modelSpinner = findViewById(R.id.machineModels);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mCustomModelSpinner);
        modelSpinner.setAdapter(mCustomeSpinnerAdapter);
        if(machines.getmPhone() != null) {
            int spinnerPosition = getPositionMachineModel(machines.getmPhone());
            modelSpinner.setSelection(spinnerPosition);
        }
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    machineModel = "Small";
                } else if (position == 1) {
                    machineModel = "Medium";
                } else if (position == 2) {
                    machineModel = "Big";
                } else if (position == 3) {
                    machineModel = "Huge";
                } else if (position == 4) {
                    machineModel = "Very Huge";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner driverSpinner = findViewById(R.id.isDriverAvailable);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mHaveDriverSpinner);
        driverSpinner.setAdapter(mCustomeSpinnerAdapter);
        if(machines.getMachineType() != null) {
            int spinnerPosition = getPositionMachineForWhat(machines.getMachineType());
            driverSpinner.setSelection(spinnerPosition);
        }
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    haveDriver = "Yes";
                } else if (position == 1) {
                    haveDriver = "No";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int getPositionMachineForWhat(String machineDriver) {
        if (machineDriver.equals("Yes")){
            return 0;
        }else if(machineDriver.equals("No")){
            return 1;
        }
        return -1;
    }

    private int getPositionMachineModel(String getmModel) {
        if (getmModel.equals("Small")){
            return 0;
        }else if (getmModel.equals("Medium")){
            return 1;
        }else if (getmModel.equals("Big")){
            return 2;
        }else if (getmModel.equals("Huge")){
            return 3;
        }else if (getmModel.equals("Very Huge")){
            return 4;
        }
        return -1;
    }


    private int getPositionMachineType(String machineType) {
        if (machineType.equals("Excavators")){
            return 0;
        }else if (machineType.equals("Backhoe")){
            return 1;
        }else if (machineType.equals("Dragline Excavator")){
            return 2;
        }else if (machineType.equals("Bulldozers")){
            return 3;
        }else if (machineType.equals("Graders")){
            return 4;
        }else if (machineType.equals("Wheel Tractor Scraper")){
            return 5;
        }else if (machineType.equals("Trenchers")){
            return 6;
        }else if (machineType.equals("Loaders")){
            return 7;
        }else if (machineType.equals("Tower Cranes")){
            return 8;
        }else if (machineType.equals("Pavers")){
            return 9;
        }else if (machineType.equals("Compactors")){
            return 10;
        }else if (machineType.equals("Telehandlers")){
            return 11;
        }else if (machineType.equals("Feller Bunchers")){
            return 12;
        }else if (machineType.equals("Dump Trucks")){
            return 13;
        }else if (machineType.equals("Pile Boring Machine")){
            return 14;
        }else if (machineType.equals("Pile Driving Machine")){
            return 15;
        }
        return -1;
    }


    private void initList() {
        mCustomSpinnerList = new ArrayList<>();
        mCustomSpinnerList.add(new CustomSpinnerItem("Excavators"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Backhoe"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Dragline Excavator"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Bulldozers"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Graders"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Wheel Tractor Scraper"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Trenchers"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Loaders"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Tower Cranes"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Pavers"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Compactors"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Telehandlers"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Feller Bunchers"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Dump Trucks"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Pile Boring Machine"));
        mCustomSpinnerList.add(new CustomSpinnerItem("Pile Driving Machine"));




    }
    private void modelListInit() {
        mCustomModelSpinner = new ArrayList<>();
        mCustomModelSpinner.add(new CustomSpinnerItem("Small"));
        mCustomModelSpinner.add(new CustomSpinnerItem("Medium"));
        mCustomModelSpinner.add(new CustomSpinnerItem("Big"));
        mCustomModelSpinner.add(new CustomSpinnerItem("Huge"));
        mCustomModelSpinner.add(new CustomSpinnerItem("Very Huge"));

    }
    private void driverListInit() {
        mHaveDriverSpinner = new ArrayList<>();
        mHaveDriverSpinner.add(new CustomSpinnerItem("Yes"));
        mHaveDriverSpinner.add(new CustomSpinnerItem("No"));
    }

   /* protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case CustomGallerySelectId:
                if (resultcode == RESULT_OK) {
                    String imagesArray = imagereturnintent.getStringExtra(CustomGalleryIntentKey);//get Intent data
                    //Convert string array into List by splitting by ',' and substring after '[' and before ']'
                    List<String> selectedImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));
                    initRecyclerView(new ArrayList<String>(selectedImages));//call load gridview method by passing converted list into arrayList
                }
                break;
        }
    }
    private void initRecyclerView(ArrayList<String> imageUrl){
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView  = findViewById(R.id.mRecycler);
        recyclerView.setLayoutManager(llm);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(imageUrl,this);
        recyclerView.setAdapter(adapter);
    }*/
    //Load GridView
   /* private void loadGridView(ArrayList<String> imagesArray) {
        GridView_Adapter adapter = new GridView_Adapter(UpdateMachinery.this, imagesArray, false);
        selectedImageGridView.setAdapter(adapter);
    }
*/
    //Read Shared Images
   /* private void getSharedImages() {

        If Intent Action equals then proceed
        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
                && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);//get Parcelabe list
            ArrayList<String> selectedImages = new ArrayList<>();

            //Loop to all parcelable list
            for (Parcelable parcel : list) {
                Uri uri = (Uri) parcel;//get URI
                String sourcepath = getPath(uri);//Get Path of URI
                selectedImages.add(sourcepath);//add images to arraylist
            }
            loadGridView(selectedImages);//call load gridview
        }*/



    //get actual path of uri
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Uri imageUri = data.getData();

            resultUriSignUP = imageUri;
            String mm= getPath(resultUriSignUP);
            String nn=mm.substring(mm.lastIndexOf("/")+1);
            TextView tv = findViewById(R.id.housePhoto);
            tv.setText(nn + "");
            //Glide.with(getApplication()).load(resultUriSignUP).apply(RequestOptions.circleCropTransform()).into(mHousePhoto);

            //  mProfileImage.setImageURI(resultUri);
        }

    }


/*
    public String getPath(Uri uri) {
        if (uri!=null) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        }else {
            Toast.makeText(this, "Please insert Photo", Toast.LENGTH_SHORT).show();
            return null;
        }

    }
    */
}
