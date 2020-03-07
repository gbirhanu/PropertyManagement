package com.dsd.machinerental;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    Button mLogin;
    EditText mUserName,mPassword,mPhone;
    String userName,userPass,usrId,mUserNamePass,mUserPhone,JSON_STRING,mUserStatus,mIsLogged;
    TextView mGotoRegistration;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin=findViewById(R.id.userLoging);
        mUserName = findViewById(R.id.usrNames);
        mPassword = findViewById(R.id.userPasswords);
        mPhone = findViewById(R.id.userPhone);
        mGotoRegistration = findViewById(R.id.gotoRegistration);


        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("uName") && pref.contains("userPhone")&& pref.contains("mUsrId")){
            mUserNamePass = pref.getString("uName",null);
            usrId = pref.getString("mUsrId",null);
            mUserPhone = pref.getString("userPhone",null);
            mUserStatus = pref.getString("userStatus",null);
            Intent intent = new Intent(LoginActivity.this, dashboardActivity.class);
            intent.putExtra("uName",mUserNamePass);
            intent.putExtra("mUsrId",usrId);
            intent.putExtra("userPhone",mUserPhone);
            intent.putExtra("userStatus",mUserStatus);
            startActivity(intent);
            finish();
        }
        mGotoRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,UserRegisration.class));
                finish();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }
    private void saveUserInformation(){

        userName=mUserName.getText().toString();
        userPass = mPassword.getText().toString();
        if (userName.isEmpty()) {
            mUserName.setError("Name is Required");
            mUserName.requestFocus();
            return;
        }
        if (userPass.isEmpty()) {
            mPassword.setError("Password is Required");
            mPassword.requestFocus();
            return;
        }

        class AddHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,null,getString(R.string.sign_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                 if (!s.isEmpty()) {
                     JSON_STRING = s;
                     getUserNameAndPhone(JSON_STRING);
                 }else {
                     Toast.makeText(LoginActivity.this, "Sorry Some thing going wrong", Toast.LENGTH_SHORT).show();
                 }
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_USER_NAME,userName);
                params.put(Config.KEY_PASSWORD,userPass);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_LOGIN_USER, params);
                return res;
            }
        }
        AddHouse ah = new AddHouse();
        ah.execute();
    }
    private void getUserNameAndPhone(String JSON_STRING) {
        try {
            JSONObject jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            usrId   =c.getString(Config.TAG_USER_ID);
            mUserNamePass = c.getString(Config.TAG_USER_NAME);
            mUserPhone = c.getString(Config.TAG_PHONE);
            mUserStatus = c.getString(Config.TAG_STATUS);
            mIsLogged = c.getString(Config.TAG_LOGGED_IN);
            if (mIsLogged.equals("0")) {
                //Toast.makeText(LoginActivity.this, "Successfully Lo", Toast.LENGTH_LONG).show();
                setUserSession(usrId);

            }else {
                Toast.makeText(this, "Somebody already Logged in with your account", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "wrong Credential ", Toast.LENGTH_SHORT).show();
        }
    }
    private void setUserSession(final String mUserId) {
        class AddHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // Toast.makeText(LoginActivity.this, "user: "+s, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("uName", userName);
                editor.putString("mUsrId", usrId);
                editor.putString("userPhone", mUserPhone);
                editor.putString("userStatus", mUserStatus);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, dashboardActivity.class);
                intent.putExtra("uName", mUserNamePass);
                intent.putExtra("mUsrId", usrId);
                intent.putExtra("userPhone", mUserPhone);
                intent.putExtra("userStatus", mUserStatus);
                startActivity(intent);
                finish();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                params.put(Config.KEY_USER_ID,mUserId);
                params.put(Config.KEY_LOGGED_IN,1+"");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_LOGOUT_USER, params);
                return res;

            }
        }
        AddHouse ah = new AddHouse();
        ah.execute();

    }
}
