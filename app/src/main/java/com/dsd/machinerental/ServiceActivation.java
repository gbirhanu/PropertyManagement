package com.dsd.machinerental;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

public class ServiceActivation extends AppCompatActivity {
    EditText mPhoneActivation;
    Button mActvateButton;
    String userID,JSON_STRING,mUserStatus,userName, userPhone;
    String mStatusPhone,mPhoneChecck;
    Toolbar mToolbar;
    SharedPreferences pref;
    String houseisf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_activation);
        mToolbar = findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPhoneActivation = findViewById(R.id.actvateWithPhone);
        mActvateButton = findViewById(R.id.actvationButton);
        userID = getIntent().getStringExtra("mUsrId");
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        houseisf = getIntent().getStringExtra("houseisfor");
        userName = getIntent().getStringExtra("uName");
        userPhone = getIntent().getStringExtra("userPhone");
        mActvateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusPhone = mPhoneActivation.getText().toString();
                class AddHouse extends AsyncTask<Void, Void, String> {
                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(ServiceActivation.this, null, getString(R.string.sign_text), false, false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        if (s.equals("1")) {
                            JSON_STRING = s;
                            getNewStatus(JSON_STRING);
                        } else {
                            nosuchPhoneExist();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put(Config.KEY_STATUS, "1");
                        params.put(Config.KEY_PHONE, mStatusPhone);
                        params.put(Config.KEY_USER_ID, userID);
                        RequestHandler rh = new RequestHandler();
                        String re = rh.sendPostRequest(Config.URL_ACTIVATE, params);
                        return re;
                    }
                }
                AddHouse ah = new AddHouse();
                ah.execute();

            }
        });
    }
                private void getNewStatus (String JSON_STRING){
                    try {
                        mUserStatus = JSON_STRING;
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userStatus", mUserStatus);
                        editor.apply();
                        Intent intent = new Intent(ServiceActivation.this, MapsActivity.class);
                        intent.putExtra("userStatus", mUserStatus);
                        intent.putExtra("houseisfor",houseisf);
                        intent.putExtra("mUsrId",userID);
                        intent.putExtra("uName",userName);
                        intent.putExtra("userPhone",userPhone);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(ServiceActivation.this, "wrong Credential", Toast.LENGTH_LONG).show();
                    }
                }


                private void nosuchPhoneExist () {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceActivation.this);
                    alertDialogBuilder.setMessage(getString(R.string.no_such_phone_num));
                    alertDialogBuilder.setPositiveButton(R.string.con_ok_message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dilogOk, int arg1) {
                                    //mRequest.setText(getString(R.string.call_taxi));
                                    dilogOk.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

