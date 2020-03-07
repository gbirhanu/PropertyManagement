package com.dsd.machinerental;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(MapsActivity.this, ViewAllHouse.class);
                    intent.putExtra("userIds", mUserId);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intents = new Intent(MapsActivity.this, SearchByPrice.class);
                    intents.putExtra("userIds", mUserId);
                    startActivity(intents);
                    return true;
                case R.id.navigation_profile:
                    Intent intentss = new Intent(MapsActivity.this, ProfileActivity.class);
                    intentss.putExtra("name", displayName);
                    intentss.putExtra("phone", mPhone);
                    intentss.putExtra("statuis", uStatus);
                    startActivity(intentss);
                    return true;
                case R.id.navigation_main:
                    Intent intent2 = new Intent(MapsActivity.this, dashboardActivity.class);
                    intent2.putExtra("uName",displayName);
                    intent2.putExtra("mUsrId",mUserId);
                    intent2.putExtra("userPhone",mPhone);
                    intent2.putExtra("userStatus",uStatus);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
    TextView mWelcomeName;
    SharedPreferences prf;
    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;
    private String JSON_STRING;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private double mLoclatitude, mLocLongitude;
    //Data to be displayed
    String uStatus, homesTypes, homesLayout, homePrices, mPhone, displayName, mUserId, isaddhome = null;
    double homeLatitude, homeLongitude;
    private boolean getDriversAroundStarted;
    private String houseisf, homePhotoString;
    //private SlidingUpPanelLayout mSlidingUpPanel;
    private LatLng destinationLatlng = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mToolbar = findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        NavigationView navigationView =  findViewById(R.id.navi_header);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        mWelcomeName = hView.findViewById(R.id.welcome_name);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar();
        disableNavigationViewScrollbars(navigationView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getDriversAroundStarted = false;
        houseisf = getIntent().getStringExtra("houseisfor");
        displayName=getIntent().getStringExtra("uName");
        mUserId = getIntent().getStringExtra("mUsrId");
        mPhone = getIntent().getStringExtra("userPhone");
        uStatus = getIntent().getStringExtra("userStatus");
        isaddhome = getIntent().getStringExtra("addmyhome");
       // Toast.makeText(this, "In maps  "+uStatus, Toast.LENGTH_LONG).show();
       //displayPhone = getIntent().getStringExtra("mUPhones");
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
       mWelcomeName.setText(getString(R.string.user_welCome_txt)+" "+displayName);
        //  mSlidingUpPanel = findViewById(R.id.slidingUppnelDriver);
       /* FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.myLocationButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    destinationLatlng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatlng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                }
            }
        });*/

        if(!uStatus.equals("1")){
            MenuItem iView = navigationView.getMenu().findItem(R.id.nav_registration);
            MenuItem item = navigationView.getMenu().findItem(R.id.view_your_home);
            iView.setVisible(false);
            item.setVisible(false);
        }
        locationStatusCheck();

    }
    public void locationStatusCheck() {
        final android.location.LocationManager manager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }
    private void buildAlertMessageNoGps() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.enable_loc_txt))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_txt), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.cancel_txt), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

     /*   try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MainActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MainActivity", "Can't find style. Error: ", e);
        }*/
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
//        alertDialogBuilder.setMessage(getString(R.string.please_click_search));
//        alertDialogBuilder.setPositiveButton(R.string.ok_txt,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dilogOk, int arg1) {
//                        //mRequest.setText(getString(R.string.call_taxi));
//                        dilogOk.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1200000);
        mLocationRequest.setFastestInterval(1200000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                checkLocationPermission();
            }
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mLoclatitude = mLastLocation.getLatitude();
                    mLocLongitude =mLastLocation.getLongitude();
                    //Toast.makeText(MapsActivity.this, mLoclatitudeString, Toast.LENGTH_SHORT).show();
                    if (mLastLocation!=null) {
                        if (destinationLatlng==null){
                            destinationLatlng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatlng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }
                    if (!getDriversAroundStarted)
                    getJSON();

                }
            }
        }
    };
Marker mMarker;
    private void getAllHouses() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                homesTypes = languageTranslate(jo.getString(Config.TAG_TYPE));
                String hfor = jo.getString(Config.TAG_SERVTYPE);
                homePrices = jo.getString(Config.TAG_PRICE);
                String isWTA = languageTranslate(jo.getString(Config.TAG_NOOFROOM));
                homePhotoString = jo.getString(Config.TAG_PHOTO);
               // homeLocation = jo.getString(Config.TAG_LOC);
                //Toast.makeText(this, "House is "+homesTypes, Toast.LENGTH_SHORT).show();
                homesLayout = languageTranslate(jo.getString(Config.TAG_CONSTYPE));
                homeLatitude = Double.valueOf(jo.getString(Config.TAG_LATITUDE));
                //Toast.makeText(this, "House is "+homesTypes, Toast.LENGTH_SHORT).show();
                homeLongitude = Double.valueOf(jo.getString(Config.TAG_LONGITUDE));
               // Toast.makeText(this, "You just caalled mer ", Toast.LENGTH_SHORT).show();
                LatLng latLng = new LatLng(homeLatitude,homeLongitude);
                IconGenerator iconGenerator = new IconGenerator(this);
                if (hfor.equals("rent")) {
                    iconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
                }else if(hfor.equals("sale")){
                    iconGenerator.setStyle(IconGenerator.STYLE_PURPLE);
                }
                Bitmap bitmap = iconGenerator.makeIcon(homePrices+" "+getString(R.string.currency));
                mMarker= mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(getString(R.string.property_type_txt)+": "+homesTypes)
                        .snippet(getString(R.string.bed_room_txt)+": "+isWTA+ "\n" + getString(R.string.cons_type_txt)+": "+homesLayout)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                mMarker.setTag(i);

            }
        } catch (JSONException e) {
        }
    }

    /**
     *
     * @param value
     * @return String in amharic or Engliash String based user setting
     */
    private String languageTranslate(String value) {
        if (value.equals("Single Family")) {
            return getString(R.string.single_family_txt);
        } else if (value.equals("Twine Homes")) {
           return getString(R.string.twines_home);
        } else if (value.equals("Town Homes")) {
            return getString(R.string.town_homes_txt);
        } else if (value.equals("Apartment")) {
            return getString(R.string.apartment);
        } else if (value.equals("Condominiums")) {
            return getString(R.string.condo_txt);
        } else if (value.equals("Commercial Space")) {
            return getString(R.string.commercial_space_txt);
        } else if (value.equals("Office Space")) {
            return getString(R.string.office_space_txt);
        } else if (value.equals("Land/lot")){
            return getString(R.string.lang_lot_txt);
        }else if (value.equals("Warehouse")){
            return getString(R.string.ware_house_text);
        }
        //Bed room
        else if (value.equals("Studio")) {
           return   getString(R.string.studio_txt);
        } else if (value.equals("1 Bedroom")) {
            return getString(R.string.one_bedroom_txt);
        } else if (value.equals("2 Bedroom")) {
            return getString(R.string.two_bedroom_txt);
        } else if (value.equals("3 Bedrooms")) {
            return getString(R.string.three_bedroom_txt);
        } else if (value.equals("4 Bedrooms")) {
            return getString(R.string.four_bedroom_txt);
        } else if (value.equals("5 Bedrooms")) {
            return getString(R.string.five_bedroom_txt);
        } else if (value.equals("6 Bedrooms")) {
            return getString(R.string.six_bedroom_txt);
        } else if (value.equals("7 Bedrooms")) {
            return getString(R.string.seven_bedroom_txt);
        } else if (value.equals("8 Bedrooms")) {
            return getString(R.string.eight_bedroom_txt);
        } else if (value.equals("9 Bedrooms")) {
            return getString(R.string.nine_bedroom_txt);
        } else if (value.equals("10 and Above")){
            return getString(R.string.ten_and_above_bedroom_txt);
        }
        //Construction Type
        else if (value.equals("Wood and mud")){
            return getString(R.string.wood_mud);
        }else if (value.equals("Wood and mud + Stucco")){
            return getString(R.string.wood_mud_stucco);
        }else if (value.equals("Maison/stone")){
            return getString(R.string.myson_stone);
        }else if (value.equals("Cement Blocks + Stucco")){
            return getString(R.string.cement_stucco);
        }else if (value.equals("Poured Concrete")){
            return getString(R.string.poured_concrete);
        }
        return null;
    }

    private void getJSON(){
        getDriversAroundStarted = true;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                getAllHouses();
                if (isaddhome!=null){
                    if (mLastLocation!=null){
                        if (uStatus.equals("1")) {
                            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                            intent.putExtra("mLoclatString", mLoclatitude);
                            intent.putExtra("mLoclongString", mLocLongitude);
                            intent.putExtra("ownerId", mUserId);
                            intent.putExtra("name", displayName);
                            intent.putExtra("phone", mPhone);
                            intent.putExtra("houseisfor",houseisf);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(MapsActivity.this, "You have no privilege to register home", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MapsActivity.this, "Enable your Location Service", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //Toast.makeText(MapsActivity.this, "Am I null "+isaddhome, Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(MapsActivity.this, "house is again "+s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_HOUSE_RENTORSALE,houseisf);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_registration) {
          if (mLastLocation!=null){
              if (uStatus.equals("1")) {
                  Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                  intent.putExtra("mLoclatString", mLoclatitude);
                  intent.putExtra("mLoclongString", mLocLongitude);
                  intent.putExtra("ownerId", mUserId);
                  intent.putExtra("name", displayName);
                  intent.putExtra("phone", mPhone);
                  startActivity(intent);
                  finish();
              }else {
                  Toast.makeText(this, "You have no previlage to register home", Toast.LENGTH_SHORT).show();
              }
          }else {
              Toast.makeText(this, "Enable your Location Service", Toast.LENGTH_SHORT).show();
          }
        }
         else if (id == R.id.nav_search_home) {
            Intent intent = new Intent(MapsActivity.this,SearchByPrice.class);
            intent.putExtra("userIds",mUserId);
            startActivity(intent);
        }else if (id == R.id.nav_view_all) {
            Intent intent = new Intent(MapsActivity.this,ViewAllHouse.class);
            intent.putExtra("userIds",mUserId);
            startActivity(intent);
        }else if (id==R.id.nav_logout){
            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.apply();
            clearUserSession(mUserId);

        }else if (id == R.id.nav_about_app) {

        }  else if (id == R.id.verify_user) {
            if (uStatus.equals("1")) {
                alreadyVerifiedDialog();
            }else {
                verifyDialog();
            }

        }else if (id == R.id.view_your_home){
            Intent intent = new Intent(MapsActivity.this,MyHouseList.class);
            intent.putExtra("userIds",mUserId);
            intent.putExtra("houseisfor",houseisf);
            intent.putExtra("mStatus",uStatus);
            intent.putExtra("func","active");
            startActivity(intent);
        }else if (id == R.id.activate_home){
            Intent intent = new Intent(MapsActivity.this,MyHouseList.class);
            intent.putExtra("userIds",mUserId);
            intent.putExtra("houseisfor",houseisf);
            intent.putExtra("mStatus",uStatus);
            intent.putExtra("func","inactive");
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            //intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, getString(R.string.share_to_other)));
      } else if (id == R.id.nav_send) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello,");
            startActivity(Intent.createChooser(shareIntent, "Share the App to Your friends"));
        } else if (id == R.id.nav_contactus) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"michu_taxi2019@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email_txt));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
            try {
                startActivity(Intent.createChooser(i, getString(R.string.sending_email_txt)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MapsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearUserSession(final String mUserId) {
        class AddHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MapsActivity.this,"Successfully Logout",Toast.LENGTH_LONG).show();
                startActivity(new Intent(MapsActivity.this,LoginActivity.class));
                finish();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                params.put(Config.KEY_USER_ID,mUserId);
                params.put(Config.KEY_LOGGED_IN,0+"");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_LOGOUT_USER, params);
                return res;

            }
        }
        AddHouse ah = new AddHouse();
        ah.execute();

    }


    private void verifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter your Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if (m_Text.isEmpty()) {
                    input.setError("Password id Required");
                    input.requestFocus();
                }
                if (m_Text.equals("hr123d")) {
                    Intent intent = new Intent(MapsActivity.this, ServiceActivation.class);
                    intent.putExtra("mUsrId",mUserId);
                    intent.putExtra("houseisfor",houseisf);
                    intent.putExtra("uName",displayName);
                    intent.putExtra("userPhone",mPhone);
                    startActivity(intent);

                } else {
                    input.setError("Wrong Password");
                    input.requestFocus();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void alreadyVerifiedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Already Verified");


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String index = marker.getTag().toString();
        int ind = Integer.valueOf(index);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject jo = result.getJSONObject(ind);
            Intent intent= new Intent(MapsActivity.this,HouseDetails.class);
            intent.putExtra("index",index);
            intent.putExtra("JsonObject",jo.toString());
            startActivity(intent);

        } catch (JSONException e) {
        e.printStackTrace();
    }

      // finish();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.info_window_layout,null,false);
        //CircleImageView circleImageView = view.findViewById(R.id.markerImage);
        //Glide.with(getApplicationContext());

        TextView mType= view.findViewById(R.id.h_type);
        TextView mDetail = view.findViewById(R.id.h_service_type);
        //mType.setText(marker.getTitle());
        //Detail Text
        String [] details = marker.getSnippet().split("\n");
        String [] nofroom = details[0].split(":");
        String noofroomSub = nofroom[0]+":";
        String noofroomVal = nofroom[1];
        String [] consType = details[1].split(":");
        String consSub = consType[0]+":";
        String consVal = consType[1];
        String newLine = "\n";
        Spannable spannabl = new SpannableString(noofroomSub+noofroomVal+newLine+consSub+consVal);
        int newLineStarts  = noofroomSub.length()+noofroomVal.length();
        spannabl.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, noofroomSub.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannabl.setSpan(new ForegroundColorSpan(Color.BLUE), noofroomSub.length(),
                noofroomSub.length()+noofroomVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannabl.setSpan(new ForegroundColorSpan(Color.BLUE), newLineStarts+consSub.length(),
                newLineStarts+consSub.length()+consVal.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannabl.setSpan(new ForegroundColorSpan(Color.MAGENTA), newLineStarts+1,
                newLineStarts+consSub.length()+1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mDetail.setText(spannabl);

        //Title Coloring and Styling
        String [] titles= marker.getTitle().split(":");
        String firstWord = titles[0]+":";
        String lastWord = titles[1];
        Spannable spannable = new SpannableString(firstWord+lastWord);
        spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, firstWord.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), firstWord.length(),
                firstWord.length()+lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        mType.setText( spannable );
        return view;    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        destinationLatlng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        //Toast.makeText(this, "I am clicked ", Toast.LENGTH_SHORT).show();
    }
}
