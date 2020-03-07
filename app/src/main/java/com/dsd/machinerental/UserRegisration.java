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

import java.util.HashMap;

public class UserRegisration extends AppCompatActivity {
Button mRegistration;
EditText mUserName,mPassword,mPhone;
String userName,userPass,userPhones,userId;
TextView mGotoLogin;
    SharedPreferences pref;
String mUserStatus = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regisration);
        mRegistration=findViewById(R.id.userRegisration);
        mUserName = findViewById(R.id.usrNames);
        mPassword = findViewById(R.id.userPasswords);
        mPhone = findViewById(R.id.userPhone);
        mGotoLogin = findViewById(R.id.gotoLogin);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        mGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegisration.this,LoginActivity.class));
                finish();
            }
        });
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }
    private void saveUserInformation(){
          userName=mUserName.getText().toString();
          userPass = mPassword.getText().toString();
          userPhones=mPhone.getText().toString();
        if (userName.isEmpty()) {
            mUserName.setError("Name is Required");
            mUserName.requestFocus();
            return;
        }
        if (userPhones.isEmpty()) {
            mPhone.setError("Phone is Required");
            mPhone.requestFocus();
            return;
        }
        if (userPass.isEmpty()) {
            mPassword.setError("Password is Required");
            mPassword.requestFocus();
            return;
        }
        if (mPassword.length() < 6) {
            mPassword.setError("Password should be more than 6");
            mPassword.requestFocus();
            return;
        }
        class AddHouse extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserRegisration.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                userId = s;
                if (userId.length()>=1) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("uName", userName);
                    editor.putString("mUsrId", userId);
                    editor.putString("userPhone", userPhones);
                    editor.putString("userStatus", mUserStatus);
                    editor.apply();
                    Toast.makeText(UserRegisration.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserRegisration.this, dashboardActivity.class);
                    intent.putExtra("uName", userName);
                    intent.putExtra("mUsrId", userId);
                    intent.putExtra("userPhone", userPhones);
                    intent.putExtra("userStatus", mUserStatus);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UserRegisration.this, getString(R.string.con_check_message), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                params.put(Config.KEY_USER_NAME,userName);
                params.put(Config.KEY_PASSWORD,userPass);
                params.put(Config.KEY_PHONE,userPhones);
                params.put(Config.KEY_LOGGED_IN,0+"");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_USER, params);
                return res;

            }
        }
        AddHouse ah = new AddHouse();
        ah.execute();

    }
}
