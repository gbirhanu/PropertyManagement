package com.dsd.machinerental;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertMoreMachineryPicture extends AppCompatActivity {
    //Component declarations
    Button mButton, mUpload, mClose;
    RelativeLayout mRelativeLayout;
    TextView mTextView,mSelectedImageText;
    String mHouseId, forsaleorrent,mUserStatus;
    Handler mHandler;
    ArrayList<String> selectedImages;
    int index = 0;
    Uri additionalPhotoUrl;
    Bitmap bitmap;
    private static GridView selectedImageGridView;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value

    private int serverResponseCode = 0;

    private int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog dialog = null;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery

    //Uri to store the image uri
    private Uri filePath;
    private final static String BASE_URL = "http://michutaxi.com/upload/";
    String userId,ownerName,ownerPhone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inset_morepicture);
        selectedImageGridView = findViewById(R.id.selectedImagesGridView);
        mSelectedImageText = findViewById(R.id.selectedImagesText);
        mRelativeLayout  = findViewById(R.id.browseLayout);
        intent = getIntent();
        userId = intent.getStringExtra("mUsrId");
        ownerName = intent.getStringExtra("uName");
        ownerPhone = intent.getStringExtra("userPhone");


        init();
        requestStoragePermission();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InsertMoreMachineryPicture.this, CustomGallery_Activity.class), CustomGallerySelectId);
                mSelectedImageText.setVisibility(View.VISIBLE);
                //mRelativeLayout.setVisibility(View.GONE);
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String path = getPath(filePath);
                dialog = ProgressDialog.show(InsertMoreMachineryPicture.this, "",
                        "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        if (selectedImages!=null) {
                            uploadFile(selectedImages);
                        }else {
                            dialog.dismiss();
                            mHandler = new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message message) {
                                    // This is where you do your work in the UI thread.
                                    // Your worker tells you in the message what to do.

                                    Toast.makeText(InsertMoreMachineryPicture.this, "Please Insert a photo", Toast.LENGTH_SHORT).show();
                                }
                            };

                        }
                    }
                }).start();
            }

        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertMoreMachineryPicture.this, MachineMapsActivity.class);
                intent.putExtra("userStatus","1");
                intent.putExtra("mUsrId",userId);
                intent.putExtra("uName",ownerName);
                intent.putExtra("userPhone",ownerPhone);
                startActivity(intent);
                finish();
            }
        });
        // getSharedImages();
    }

    public int uploadFile(ArrayList<String> sourceFileUri) {

        for (index = 0; index<sourceFileUri.size();index++) {
            final String fileName = sourceFileUri.get(index);

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "---------uiytuhjfndsjk";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(fileName);

            if (!sourceFile.isFile()) {

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
                    URL url = new URL(Config.URL_ADD_MORE_MACHINERY_PICTURE);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    // conn.setRequestProperty("image", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + Config.KEY_HOUSE_ID + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    dos.writeBytes("Content-Length: " + mHouseId.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(mHouseId + lineEnd);
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
                                new InputStreamReader(urlInputStream, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        urlInputStream.close();
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }
                    JSONObject jObj;
                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }

                    Log.d("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                    Log.d("content",
                            "HTTP Response content is : " + ": "
                                    + serverResponseCode);

                    if (serverResponseCode == 200) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                        + " F:/wamp/wamp/www/uploads";
                                if (index==selectedImages.size()) {
                                    Toast.makeText(InsertMoreMachineryPicture.this,
                                            "File Upload Complete.", Toast.LENGTH_SHORT)
                                            .show();
                                }
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
                            Toast.makeText(InsertMoreMachineryPicture.this,
                                    "MalformedURLException", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {
                    dialog.dismiss();

                    e.printStackTrace();
                    Toast.makeText(this, "Exception " + e, Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mTextView.setText("Got Exception : see logcat ");
                            Toast.makeText(InsertMoreMachineryPicture.this,
                                    "Got Exception : see logcat ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.e("Uploadrver Exception",
                            "Exception : " + e.getMessage(), e);
                }


            } // End else block
        }
        dialog.dismiss();
        return serverResponseCode;
    }

    private void init() {
        mHouseId = getIntent().getStringExtra("HouseIds");
        forsaleorrent = getIntent().getStringExtra("houseisfor");
        mButton = findViewById(R.id.additionalphotoButton);
        mTextView = findViewById(R.id.additionalphoto);
        mUpload = findViewById(R.id.upLoad);
        mClose = findViewById(R.id.close);
        mUserStatus = "1";
    }


    //handling the image chooser activity result
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                String path = getPath(filePath);
                mTextView.setText(path);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case CustomGallerySelectId:
                if (resultcode == RESULT_OK) {
                    String imagesArray = imagereturnintent.getStringExtra(CustomGalleryIntentKey);//get Intent data
                    //Convert string array into List by splitting by ',' and substring after '[' and before ']'
                    List<String> selectImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));
                    selectedImages = new ArrayList<String>(selectImages);
                    loadGridView(selectedImages);//call load gridview method by passing converted list into arrayList
                }
                break;
        }
    }

    private void loadGridView(ArrayList<String> imagesArray) {
        GridView_Adapter adapter = new GridView_Adapter(InsertMoreMachineryPicture.this, imagesArray, false);
        selectedImageGridView.setAdapter(adapter);
    }

    //Read Shared Images
    /*private void getSharedImages() {

        // If Intent Action equals then proceed
        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
                && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);//get Parcelabe list
            selectedImages = new ArrayList<>();

            //Loop to all parcelable list
            for (Parcelable parcel : list) {
                Uri uri = (Uri) parcel;//get URI
                String sourcepath = getPath(uri);//Get Path of URI
                selectedImages.add(sourcepath);//add images to arraylist
            }
            loadGridView(selectedImages);//call load gridview
        }

    }*/


    //method to get the file path from uri
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
