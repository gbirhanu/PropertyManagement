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

public class RegisterMachinery extends AppCompatActivity {
    private static GridView selectedImageGridView;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value
    String path;
    LinearLayout linearLayout;
    EditText mHousePrice,mYearBuiltTake,mHouseDescription,mBuildingSize,mCompoundSize;
    Button mRegistration, mHousePhoto, mAddMorePhoto,mCloseD;
    String machineType, machineModel,
            Responses, ownerPhone, ownerName,mLatitude,
            mLongitude, houseDescription,haveDriver;
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
    String price;
    private ArrayList<CustomSpinnerItem> mCustomSpinnerList,mCustomModelSpinner,mHaveDriverSpinner;
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
        setContentView(R.layout.activity_register_machinery);
        mToolbar =  findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//data from spinner


        //getSharedImages();
        //selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);

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
        machineId = 1;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        linearLayout = findViewById(R.id.addPhotoContainer);
        mYearBuiltTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterMachinery.this, date, calendar
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

                //startActivityForResult(new Intent(RegisterMachinery.this, CustomGallery_Activity.class), CustomGallerySelectId);

            }
        });
        mCloseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterMachinery.this,MachineMapsActivity.class);
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
                    Intent intent = new Intent(RegisterMachinery.this, InsertMoreMachineryPicture.class);
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
                    Toast.makeText(RegisterMachinery.this, getString(R.string.select_photo), Toast.LENGTH_SHORT).show();
                }else {
                    path = getPath(resultUriSignUP);

                }
                if (!price.isEmpty()) {
                    dialog = ProgressDialog.show(RegisterMachinery.this, "",
                            "Uploading file...", true);
                }else {
                    Toast.makeText(RegisterMachinery.this, getString(R.string.insert_info), Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(RegisterMachinery.this, "Please Insert a photo", Toast.LENGTH_SHORT).show();
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
                URL url = new URL(Config.URL_ADD_MACHINERY);
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

                            Toast.makeText(RegisterMachinery.this,
                                    "File Upload Complete.", Toast.LENGTH_SHORT)
                                    .show();
                            //linearLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(RegisterMachinery.this, InsertMoreMachineryPicture.class);
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
                        Toast.makeText(RegisterMachinery.this,
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
                        Toast.makeText(RegisterMachinery.this,
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
        GridView_Adapter adapter = new GridView_Adapter(RegisterMachinery.this, imagesArray, false);
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
