package com.dsd.machinerental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMyMachinery extends AppCompatActivity {
    private String JSON_STRING,ustat;
    private String function;
    private String machinceType;
    private String machinePrice;
    private String mUserId,hfsor;
    List<Machine> machineList;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_machinery);
        // mToolbar =  findViewById(R.id.navigation_action);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initializing the productlist
        machineList = new ArrayList<>();
        mUserId  = getIntent().getStringExtra("userIds");
        ustat = getIntent().getStringExtra("mStatus");
        function = getIntent().getStringExtra("func");
        progressBar  = findViewById(R.id.viewAllProgress);
        if (function.equals("active")) {
            getJSON();
        }else if (function.equals("inactive")) {
            getJSONDeactivated();
        }
        //Toast.makeText(this, "User Id "+mUserId, Toast.LENGTH_SHORT).show();
        //this method will fetch and parse json
        //to display it in recyclerview
    }

    private void getJSONDeactivated() {
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListMyMachinery.this,null,getString(R.string.wait_text),false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHouses();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_MACHINE_BYUID,mUserId);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showHouses(){
        progressBar.setVisibility(View.GONE);

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            if (result.length()>0) {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject machine = result.getJSONObject(i);
                    machineList.add(new Machine(
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
                    MyCustomMachineAdapter adapter = new MyCustomMachineAdapter(ListMyMachinery.this, machineList, JSON_STRING, hfsor, ustat, function);
                    recyclerView.setAdapter(adapter);
                }
            }else {
                Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListMyMachinery.this,null,getString(R.string.wait_text),false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHouses();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_MACHINE_BYUID,mUserId);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
