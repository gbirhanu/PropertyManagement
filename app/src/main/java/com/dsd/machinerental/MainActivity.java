package com.dsd.machinerental;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity {
    private static GridView selectedImageGridView;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value
    String path;
    LinearLayout linearLayout;
    EditText mHousePrice,mYearBuiltTake,mHouseDescription,mBuildingSize,mCompoundSize;
    Button mRegistration, mHousePhoto, mAddMorePhoto,mCloseD;
    String houseType, houseLayoutof, noofRooms, houseforrentotsale,
            Responses, ownerPhone, ownerName,mLatitude,
            mLongitude,halfBathrooms,fullBathrooms,servantQuarterString,servantQuarterBathroom,isModernKichenAvailable,
            parkingSpace,isWaterTankAvailable,isGeneratorAvailable, houseDescription;
    private int builtYearString;
    private double mLocLat, mLocLng;
    private Uri resultUriSignUP;
    int housePrice, houseId,compoundSize,buildingSize;
    private int serverResponseCode = 0;
    TextView mTextView;
    Handler mHandler;
    String userId;
    int mUserIds;
    private ProgressDialog dialog = null;

    private ArrayList<String> pathList = new ArrayList<>();
    String price, bsize,csize;
    private ArrayList<CustomSpinnerItem> mCustomSpinnerList, mNoofroom, mConsTypeofHouseList,
            mServiceSizeList, mServiceType,mHalfBathroomList,mFullBathroomList,mServantQuarterList,
            mServQuartBathList,mModerKitchenList,mParkingSpaceList,mIsWaterTankList,mIsGenList;
    private McustoemItemAdapter mCustomeSpinnerAdapter;
    private ArrayAdapter mOtheradpter;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =  findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//data from spinner


        //getSharedImages();
        //selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);

        initList();
        noofRoomsList();
        houseConstList();
        serviceTypeList();
        hasfBathRoomList();
        fullBathroomList();
        servantQuarterList();
        servantQuarterBathroomList();
        isModernChickenList();
        parkingList();
        isWaterTankList();
        isGeneratorList();
        //displaying user name
        getDataFromSpinner();
//getting them
        Intent intent = getIntent();
        mLocLat = intent.getDoubleExtra("mLoclatString", 0);
        mLocLng = intent.getDoubleExtra("mLoclongString", 0);
        mLatitude = mLocLat+"";
        mLongitude = mLocLng+"";
        userId = intent.getStringExtra("ownerId");
        // Toast.makeText(this, "User Id  "+userId, Toast.LENGTH_SHORT).show();

        ownerName = intent.getStringExtra("name");
        ownerPhone = intent.getStringExtra("phone");
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
        houseId = 1;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        linearLayout = findViewById(R.id.addPhotoContainer);
        mYearBuiltTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mHousePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

                //startActivityForResult(new Intent(MainActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);

            }
        });
        mCloseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("houseisfor",houseforrentotsale);
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
                    Intent intent = new Intent(MainActivity.this, InsetMorepicture.class);
                    intent.putExtra("HouseIds", Responses);
                    intent.putExtra("sor", houseforrentotsale);
                    startActivity(intent);
                }
            }
        });
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveHouseInformation();
                price = mHousePrice.getText().toString();
                bsize = mBuildingSize.getText().toString();
                csize = mCompoundSize.getText().toString();
                houseDescription = mHouseDescription.getText().toString();
                if (houseDescription.isEmpty()){
                    houseDescription = "For the description of the House please contact the owner of the " +
                            "property";
                }
                if (price.isEmpty()) {
                    mHousePrice.setError("Price is Required");
                    mHousePrice.requestFocus();
                }
                if (bsize.isEmpty()) {
                    mBuildingSize.setError("Building size is Required");
                    mBuildingSize.requestFocus();
                }
                if (csize.isEmpty()) {
                    mCompoundSize.setError("Compound Size is Required");
                    mCompoundSize.requestFocus();
                }
//                if (houseDescription.isEmpty()) {
//                    mHouseDescription.setError("House Description is Required");
//                    mHouseDescription.requestFocus();
//                }
                if (resultUriSignUP==null){
                    Toast.makeText(MainActivity.this, getString(R.string.select_photo), Toast.LENGTH_SHORT).show();
                }else {
                    path = getPath(resultUriSignUP);

                }
                if (!price.isEmpty() & !bsize.isEmpty() & !csize.isEmpty()) {
                    dialog = ProgressDialog.show(MainActivity.this, "",
                            "Uploading file...", true);
                }else {
                    Toast.makeText(MainActivity.this, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(MainActivity.this, "Please Insert a photo", Toast.LENGTH_SHORT).show();
                                }
                            };
                        }

                    }
                }).start();
            }
        });

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

        housePrice = Integer.parseInt(price);
        buildingSize = Integer.parseInt(bsize);
        compoundSize = Integer.parseInt(csize);
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
                URL url = new URL(Config.URL_ADD);
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

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_PHONE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + ownerPhone.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(ownerPhone + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_TYPE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + houseType.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(houseType + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_NOOFROOM+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + noofRooms.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(noofRooms + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_CONST+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + houseLayoutof.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(houseLayoutof + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_SERVTYPE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + houseforrentotsale.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(houseforrentotsale + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);


                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_PRICE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + (housePrice+"").length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(housePrice + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_BUILT_YEAR+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + builtYearString+"".length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(builtYearString + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_BUILDING_SIZE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + buildingSize+"".length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(buildingSize + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_COMPOUND_SIZE+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + compoundSize+"".length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(compoundSize + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_FULL_BATH+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + fullBathrooms.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(fullBathrooms + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_HALF_BATH+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + halfBathrooms.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(halfBathrooms + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_SERVANT_QUARTER+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + servantQuarterString.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(servantQuarterString + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_SERVANT_QUARTER_BATH+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + servantQuarterBathroom.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(servantQuarterBathroom + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_MODERN_KITCHEN+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + isModernKichenAvailable.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(isModernKichenAvailable + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_PARKING+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + parkingSpace.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(parkingSpace + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_WATER_TANK+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + isWaterTankAvailable.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(isWaterTankAvailable + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\""+Config.KEY_HOUSE_GENERATOR+"\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + isGeneratorAvailable.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(isGeneratorAvailable + lineEnd);
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

                            Toast.makeText(MainActivity.this,
                                    "File Upload Complete.", Toast.LENGTH_SHORT)
                                    .show();
                            //linearLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(MainActivity.this, InsetMorepicture.class);
                            intent.putExtra("HouseIds", Responses);
                            intent.putExtra("houseisfor", houseforrentotsale);
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
                        Toast.makeText(MainActivity.this,
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
                        Toast.makeText(MainActivity.this,
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

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    houseType = "Single Family";
                } else if (position == 1) {
                    houseType = "Twine Homes";
                } else if (position == 2) {
                    houseType = "Town Homes";
                } else if (position == 3) {
                    houseType = "Apartment";
                } else if (position == 4) {
                    houseType = "Condominiums";
                } else if (position == 5) {
                    houseType = "Commercial Space";
                }else if (position == 6) {
                    houseType = "Office Space";
                }else if (position == 7) {
                    houseType = "Land/lot";
                }else if (position==8){
                    houseType ="Warehouse";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerNor = findViewById(R.id.noRoomsList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mNoofroom);
        spinnerNor.setAdapter(mCustomeSpinnerAdapter);


        spinnerNor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    noofRooms = "Studio";
                } else if (position == 1) {
                    noofRooms = "1 Bedroom";
                } else if (position == 2) {
                    noofRooms = "2 Bedroom";
                } else if (position == 3) {
                    noofRooms = "3 Bedrooms";
                } else if (position == 4) {
                    noofRooms = "4 Bedrooms";
                }else if (position == 5) {
                    noofRooms = "5 Bedrooms";
                }else if (position == 6) {
                    noofRooms = "6 Bedrooms";
                }else if (position == 7) {
                    noofRooms = "7 Bedrooms";
                }else if (position == 8) {
                    noofRooms = "8 Bedrooms";
                }else if (position == 9) {
                    noofRooms = "9 Bedrooms";
                }else if (position == 10) {
                    noofRooms = "10 and Above";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerHouseLayout = findViewById(R.id.houseConstructionTypeList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mConsTypeofHouseList);
        spinnerHouseLayout.setAdapter(mCustomeSpinnerAdapter);


        spinnerHouseLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    houseLayoutof = "Wood and mud";

                } else if (position == 1) {
                    houseLayoutof = "Wood and mud + Stucco";
                } else if (position == 2) {
                    houseLayoutof = "Maison/stone";
                }
                else if (position == 3) {
                    houseLayoutof = "Cement Blocks + Stucco";
                }
                else if (position == 4) {
                    houseLayoutof = "Poured Concrete";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        final Spinner houseisforSaleorRent = findViewById(R.id.serviceTypeList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mServiceType);
        houseisforSaleorRent.setAdapter(mCustomeSpinnerAdapter);
        houseisforSaleorRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    houseforrentotsale = "rent";
                } else if (position == 1) {
                    houseforrentotsale = "sale";
                }
                //Toast.makeText(MainActivity.this, "hous is for "+houseforrentotsale, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner halfBathRoom = findViewById(R.id.halfBathroomsList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mHalfBathroomList);
        halfBathRoom.setAdapter(mCustomeSpinnerAdapter);
        halfBathRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    halfBathrooms = "0 Half Bathroom";
                } else if (position == 1) {
                    halfBathrooms = "1 Half Bathroom";
                }else if (position == 2) {
                    halfBathrooms = "2 Half Bathrooms";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner fullBathRoom = findViewById(R.id.fullBathroomList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mFullBathroomList);
        fullBathRoom.setAdapter(mCustomeSpinnerAdapter);
        fullBathRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    fullBathrooms = "1 Full Bathroom";
                } else if (position == 1) {
                    fullBathrooms = "1 Full Bathrooms";
                }else if (position == 2) {
                    fullBathrooms = "2 Full Bathrooms";
                }else if (position == 3) {
                    fullBathrooms = "3 Full Bathrooms";
                }else if (position == 4) {
                    fullBathrooms = "4 Full Bathrooms";
                }else if (position == 5) {
                    fullBathrooms = "5 Full Bathrooms";
                }
                else if (position == 6) {
                    fullBathrooms = "6+ Full Bathrooms";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner servantQuarterSp = findViewById(R.id.servantQuarterList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mServantQuarterList);
        servantQuarterSp.setAdapter(mCustomeSpinnerAdapter);
        servantQuarterSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    servantQuarterString = "1 Room";
                } else if (position == 1) {
                    servantQuarterString = "2 Rooms";
                }else if (position == 2) {
                    servantQuarterString = "3 Rooms";
                }else if (position == 3) {
                    servantQuarterString = "4 Rooms";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner servantQuarterBathSp = findViewById(R.id.servantQuarterBathroomList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mServQuartBathList);
        servantQuarterBathSp.setAdapter(mCustomeSpinnerAdapter);
        servantQuarterBathSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    servantQuarterBathroom = "1 Bathroom";
                } else if (position == 1) {
                    servantQuarterBathroom = "2 Bathrooms";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner moderKitchenSp = findViewById(R.id.modernKitchenList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mModerKitchenList);
        moderKitchenSp.setAdapter(mCustomeSpinnerAdapter);
        moderKitchenSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isModernKichenAvailable = "Yes";
                } else if (position == 1) {
                    isModernKichenAvailable = "No";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner parkingSp = findViewById(R.id.parkingSpaceList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mParkingSpaceList);
        parkingSp.setAdapter(mCustomeSpinnerAdapter);
        parkingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    parkingSpace = "1 Parking Space";
                } else if (position == 1) {
                    parkingSpace = "2 Parking Spaces";
                }else if (position == 2) {
                    parkingSpace = "3 Parking Spaces";
                }else if (position == 3) {
                    parkingSpace = "4 Parking Spaces";
                }else if (position == 4) {
                    parkingSpace = "5+ Parking Spaces";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner waterTankSp = findViewById(R.id.waterTankList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mIsWaterTankList);
        waterTankSp.setAdapter(mCustomeSpinnerAdapter);
        waterTankSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isWaterTankAvailable = "Yes";
                } else if (position == 1) {
                    isWaterTankAvailable = "No";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner generatorAvailSp = findViewById(R.id.generatorAvailableList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this,mIsGenList);
        generatorAvailSp.setAdapter(mCustomeSpinnerAdapter);
        generatorAvailSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isGeneratorAvailable = "Yes";
                } else if (position == 1) {
                    isWaterTankAvailable = "No";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private void houseConstList() {
        mConsTypeofHouseList = new ArrayList<>();
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.wood_mud)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.wood_mud_stucco)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.myson_stone)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.cement_stucco)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.poured_concrete)));

    }

    private void noofRoomsList() {
        mNoofroom = new ArrayList<>();
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.studio_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.one_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.two_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.three_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.four_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.five_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.six_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.seven_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.eight_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.nine_bedroom_txt)));
        mNoofroom.add(new CustomSpinnerItem(getString(R.string.ten_and_above_bedroom_txt)));
    }
    private void initList() {
        mCustomSpinnerList = new ArrayList<>();
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.single_family_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.twines_home)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.town_homes_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.apartment)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.condo_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.commercial_space_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.office_space_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.lang_lot_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.ware_house_text)));
    }
    private void serviceTypeList() {
        mServiceType = new ArrayList<>();
        mServiceType.add(new CustomSpinnerItem(getString(R.string.for_rent)));
        mServiceType.add(new CustomSpinnerItem(getString(R.string.for_sale)));
    }
    private void hasfBathRoomList(){
        mHalfBathroomList = new ArrayList<>();
        mHalfBathroomList.add(new CustomSpinnerItem(getString(R.string.zero_half_bathroom_txt)));
        mHalfBathroomList.add(new CustomSpinnerItem(getString(R.string.one_half_bathroom_txt)));
        mHalfBathroomList.add(new CustomSpinnerItem(getString(R.string.two_half_bathroom_txt)));
    }
    private void fullBathroomList(){
        mFullBathroomList = new ArrayList<>();
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.one_full_bathroom_txt)));
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.two_full_bathroom_txt)));
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.three_full_bathroom_txt)));
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.four_full_bathroom_txt)));
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.five_full_bathroom_txt)));
        mFullBathroomList.add(new CustomSpinnerItem(getString(R.string.six_full_bathroom_txt)));


    }

    private void servantQuarterList(){
        mServantQuarterList = new ArrayList<>();
        mServantQuarterList.add(new CustomSpinnerItem(getString(R.string.one_room_txt)));
        mServantQuarterList.add(new CustomSpinnerItem(getString(R.string.two_room_txt)));
        mServantQuarterList.add(new CustomSpinnerItem(getString(R.string.three_room_txt)));
        mServantQuarterList.add(new CustomSpinnerItem(getString(R.string.four_room_txt)));

    }

    private void servantQuarterBathroomList(){
        mServQuartBathList = new ArrayList<>();
        mServQuartBathList.add(new CustomSpinnerItem(getString(R.string.one_bathroom_txt)));
        mServQuartBathList.add(new CustomSpinnerItem(getString(R.string.two_bathroom_txt)));

    }
    private void isModernChickenList(){
        mModerKitchenList = new ArrayList<>();
        mModerKitchenList.add(new CustomSpinnerItem(getString(R.string.yes_txt)));
        mModerKitchenList.add(new CustomSpinnerItem(getString(R.string.no_txt)));
    }

    private void parkingList(){
        mParkingSpaceList = new ArrayList<>();
        mParkingSpaceList.add(new CustomSpinnerItem(getString(R.string.parking_one_txt)));
        mParkingSpaceList.add(new CustomSpinnerItem(getString(R.string.parking_two_txt)));
        mParkingSpaceList.add(new CustomSpinnerItem(getString(R.string.parking_three_txt)));
        mParkingSpaceList.add(new CustomSpinnerItem(getString(R.string.parking_four_txt)));
        mParkingSpaceList.add(new CustomSpinnerItem(getString(R.string.parking_five_txt)));

    }
    private void isWaterTankList(){
        mIsWaterTankList = new ArrayList<>();
        mIsWaterTankList.add(new CustomSpinnerItem(getString(R.string.yes_txt)));
        mIsWaterTankList.add(new CustomSpinnerItem(getString(R.string.no_txt)));


    }
    private void isGeneratorList(){
        mIsGenList = new ArrayList<>();
        mIsGenList.add(new CustomSpinnerItem(getString(R.string.yes_txt)));
        mIsGenList.add(new CustomSpinnerItem(getString(R.string.no_txt)));
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
        GridView_Adapter adapter = new GridView_Adapter(MainActivity.this, imagesArray, false);
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
