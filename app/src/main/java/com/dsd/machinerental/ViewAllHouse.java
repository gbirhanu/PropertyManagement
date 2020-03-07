package com.dsd.machinerental;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllHouse extends AppCompatActivity {
    private String JSON_STRING,JSON_STRING_USER;
    private String houseType;
    private String houseLocation;
    private String housePrice;
    private String mUserId;
    List<House> houselists;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_house);
       // mToolbar =  findViewById(R.id.navigation_action);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recylcerView);
        progressBar = findViewById(R.id.viewAllProgress);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initializing the productlist
        houselists = new ArrayList<>();
        getJSON();
        //this method will fetch and parse json
        //to display it in recyclerview
    }
    private void showHouses(){
        progressBar.setVisibility(View.GONE);

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject house = result.getJSONObject(i);
                houselists.add(new House(
                        house.getInt(Config.TAG_ID),
                        house.getInt(Config.TAG_USER_ID),
                        house.getString(Config.TAG_TYPE),
                        house.getString(Config.TAG_CONSTYPE),
                        house.getString(Config.TAG_BUILDING_SIZE),
                        house.getString(Config.TAG_COMPOUND_SIZE),
                        house.getString(Config.TAG_KITCHEN),
                        house.getString(Config.TAG_PARKING),
                        house.getString(Config.TAG_GENERATOR),
                        house.getString(Config.TAG_QUARTER),
                        house.getString(Config.TAG_QUARTER_BATH),
                        house.getString(Config.TAG_FULL_BATH),
                        house.getString(Config.TAG_HALF_BATH),
                        house.getString(Config.TAG_WATER_TANK),
                        house.getString(Config.TAG_SERVTYPE),
                        house.getString(Config.TAG_NOOFROOM),
                        house.getString(Config.TAG_PRICE),
                        house.getString(Config.TAG_BUILT_YEAR),
                        house.getString(Config.TAG_PHOTO),
                        house.getString(Config.TAG_USER_NAME),
                        house.getString(Config.TAG_PHONE),
                        house.getString(Config.TAG_DESCRIPTION)
                ));
                CustomAdapter adapter = new CustomAdapter(ViewAllHouse.this, houselists,JSON_STRING);
                recyclerView.setAdapter(adapter);
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
                loading = ProgressDialog.show(ViewAllHouse.this,null,getString(R.string.wait_text),false,false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


}
