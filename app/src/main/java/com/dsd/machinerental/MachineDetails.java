package com.dsd.machinerental;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MachineDetails extends AppCompatActivity {
    TextView machineTypeText, machineModelText, machinePriceText, machineDriverText, textViewUname, textViewUphone,machineDescrptionText;
    TextView txtBuildyear, txtimageInfo;
    ImageView imageView;
    Button mViewOwner;
    ProgressBar progressBar;
    private String JSON_STRING;
    List<Machine> houselists;
    Machine houses;
    String phoneNo;
    String msBody;
    Toolbar mToolbar;
    //String for language translation
    String machineType, machineModel, isDriver, builtyears, machineDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_details);
        // mToolbar = findViewById(R.id.navigation_action);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewOwner = findViewById(R.id.ownerInfo);
        progressBar = findViewById(R.id.homeprogress);
        machineTypeText = findViewById(R.id.machineTypes);
        machineModelText = findViewById(R.id.machineModelss);
        machinePriceText = findViewById(R.id.machinePrices);
        machineDriverText = findViewById(R.id.withOrWithout);
        textViewUname = findViewById(R.id.ownerUserName);
        textViewUphone = findViewById(R.id.ownerPhone);
        machineDescrptionText = findViewById(R.id.descRiption);
        txtBuildyear = findViewById(R.id.builtYears);
        JSON_STRING = getIntent().getStringExtra("JsonObject");
        houselists = new ArrayList<>();
        imageView = findViewById(R.id.imageView);
        txtimageInfo = findViewById(R.id.imageInfo);
        checkLocationPermission();
        phoneNo = "6093";
        msBody = "HR";
        /**
         * house information will be extracted from json array
         * which come from server
         */
        JSONObject machine = null;
        try {
            machine = new JSONObject(JSON_STRING);

            houselists.add(houses = new Machine(
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
            DisplayMetrics dis = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dis);
            int height = dis.heightPixels;
            int width = dis.widthPixels;
            imageView.setMaxHeight(height);
            imageView.setMaxWidth(width);
            /**
             * load image to image viewer using picasso library
             *
             */
            try {
                Picasso.get().load(houses.getWithorWithoutDrivers()).centerInside().placeholder(R.color.colorPrimaryDark).fit().error(R.color.colorPrimaryDark).fit().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        txtimageInfo.setText(getString(R.string.click_here_txt));
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);

                    }
                });
            } catch (Exception ex) {

            }
            // final String houseImageString = house.getImage();
            // byte[] imageAsBytes = Base64.decode(houseImageString.getBytes(), Base64.DEFAULT);
            // final String imageTy=house.getType();
            // holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            //mIds = String.valueOf(mId);
            int firstColor = Color.BLACK;
            int secondColor = Color.BLUE;
            //handleLanguage(houses);
            String firstWord = getString(R.string.owned_by)+":";
            String lastWord = houses.getPrice();
            Spannable spannable = new SpannableString(firstWord+lastWord);
            spannable.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(secondColor), firstWord.length(),
                    firstWord.length()+lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewUname.setText( spannable );
            //textViewUname.setText(getString(R.string.owned_by) + ": " + houses.getmUname());
            String firstWord1 = getString(R.string.contact)+":";
            String lastWord1 = houses.getMachineDescription();
            Spannable spannable1 = new SpannableString(firstWord1+lastWord1);
            spannable1.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord1.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable1.setSpan(new ForegroundColorSpan(secondColor), firstWord1.length(),
                    firstWord1.length()+lastWord1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewUphone.setText(spannable1);
            // textViewUphone.setText(getString(R.string.contact) + ": " + houses.getmPhone());
            String firstWord2 = getString(R.string.machinery_type_txt)+":";
            String lastWord2 = houses.getmUname();
            Spannable spannable2 = new SpannableString(firstWord2+lastWord2);
            spannable2.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord2.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable2.setSpan(new ForegroundColorSpan(secondColor), firstWord2.length(),
                    firstWord2.length()+lastWord2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            machineTypeText.setText(spannable2);
            // textViewTitle.setText(getString(R.string.property_type_txt) + ": " + homeType);
            String firstWord3 = getString(R.string.machine_model)+":";
            String lastWord3 = houses.getmPhone();
            Spannable spannable3 = new SpannableString(firstWord3+lastWord3);
            spannable3.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord3.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable3.setSpan(new ForegroundColorSpan(secondColor), firstWord3.length(),
                    firstWord3.length()+lastWord3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            machineModelText.setText(spannable3);


            //txtParking.setText(getString(R.string.parking_sp_txt) + ": " + haveParking);
            if (houses.getYearManufactured()!=null) {
                String firstWord7 = getString(R.string.built_year_txt) + ":";
                String lastWord7 = houses.getMachineModel();
                Spannable spannable7 = new SpannableString(firstWord7 + lastWord7);
                spannable7.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord7.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable7.setSpan(new ForegroundColorSpan(secondColor), firstWord7.length(),
                        firstWord7.length() + lastWord7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtBuildyear.setText(spannable7);
            }else {
                Toast.makeText(this, "Year "+houses.getYearManufactured(), Toast.LENGTH_SHORT).show();
            }
            //txtBuildyear.setText(getString(R.string.built_year_txt) + ": " + houses.getBuiltYear());

            //txtCompoundSizze.setText(getString(R.string.compound_square_meter) + ": " + houses.getPcsize());
            String firstWord22 = getString(R.string.machine_with_what)+":";
            String lastWord22 = houses.getMachineType();
            Spannable spannable22 = new SpannableString(firstWord22+lastWord22);
            spannable22.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord22.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable22.setSpan(new ForegroundColorSpan(secondColor), firstWord22.length(),
                    firstWord22.length()+lastWord22.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            machineDriverText.setText(spannable22);
            //txtServiceType.setText(getString(R.string.serv_type_txt) + ": " + homeFor);

            //textViewRating.setText(getString(R.string.bed_room_txt) + ": " + bedRoom);
            String firstWord44 = getString(R.string.price)+":";
            String lastWord44 = String.valueOf(houses.getImage()) +" "+getString(R.string.currency);
            Spannable spannable44 = new SpannableString(firstWord44+lastWord44);
            spannable44.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord44.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable44.setSpan(new ForegroundColorSpan(secondColor), firstWord44.length(),
                    firstWord44.length()+lastWord44.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            machinePriceText.setText(spannable44);
            //textViewPrice.setText(getString(R.string.price) + ": " + String.valueOf(houses.getPrice()) + "  " + getString(R.string.currency));
            String firstWord444 = getString(R.string.house_info_txt)+"\n";
            String lastWord444 = "    "+houses.getYearManufactured();
            Spannable spannable444 = new SpannableString(firstWord444+lastWord444);
            spannable444.setSpan(new ForegroundColorSpan(firstColor), 0, firstWord444.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable444.setSpan(new ForegroundColorSpan(secondColor), firstWord444.length(),
                    firstWord444.length()+lastWord444.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable444.setSpan(new RelativeSizeSpan(1.1f),0, firstWord444.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size
            spannable444.setSpan(new RelativeSizeSpan(0.8f), firstWord444.length(), firstWord444.length() +
                    lastWord444.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size

            machineDescrptionText.setText(spannable444);
            //textViewPrice.setText(getString(R.string.price) + ": " + String.valueOf(houses.getPrice()) + "  " + getString(R.string.currency));

            // txtHousDesc.setText(getString(R.string.house_info_txt) + "\n " + houses.getHouseDesc());
            mViewOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* for (int i = 1; i <= 2; i++) {
                        smsSendMessage(phoneNo, msBody);
                    }*/
                    textViewUname.setVisibility(View.VISIBLE);
                    textViewUphone.setVisibility(View.VISIBLE);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mId = houses.getMachineId();
                    Intent intent = new Intent(MachineDetails.this, ImageShower.class);
                    intent.putExtra("id", mId);
                    //  intent.putExtra("image",houseImageString);
                    intent.putExtra("type","machine");
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "Sorry Some error Encountered" + e, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*  public void smsSendMessage(String phoneNo, String sms) {
          try {
              SmsManager smsManager = SmsManager.getDefault();
              smsManager.sendTextMessage(Uri.parse(phoneNo).toString(), null, sms, null, null);

          } catch (Exception e) {
              Toast.makeText(getApplicationContext(),
                      "SMS faild, please try again later!" +e,
                      Toast.LENGTH_LONG).show();
              e.printStackTrace();
          }
      }*/
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MachineDetails.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MachineDetails.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase
                        (android.Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // smsSendMessage(phoneNo, msBody);
                    // Permission was granted. Enable sms button.
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide Permisiion", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
