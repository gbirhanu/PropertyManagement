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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllMachinery extends AppCompatActivity {

    private String JSON_STRING, JSON_STRING_USER;

    private String bedroomNumbers;
    ProgressBar progressBar;
    List<Machine> houselists, filteredHouseList;
    String rentorsale;
    Long minPrice, maxPrice;
    RecyclerView recyclerView;
    TextView mType, mLayout, mPrice, mLocation, mService;
    EditText mPriceEdit, mMaxPrice, broomText;
    String houseType, houseLayoutof, machineType, machineModel, haveDriver, housePrice;
    ImageView mHome;
    Toolbar mToolbar;
    RadioGroup mCategory;
    RadioButton mButton1, mButton2;
    RelativeLayout mLayoutContainer;
    private McustoemItemAdapter mCustomeSpinnerAdapter;

    private ArrayList<CustomSpinnerItem> mCustomSpinnerList, mCustomModelSpinner,mHaveDriverSpinner;
    private boolean isVisible = false;
    int countVisiblity = 0;
    private ExpandOrCollapse mAnimationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_machinery);
        recyclerView = findViewById(R.id.recylcerView);
        progressBar = findViewById(R.id.viewAllProgress);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initializing the productlist
        houselists = new ArrayList<>();
        getAllJSON();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHouseInfo() {
        progressBar.setVisibility(View.GONE);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject machine = result.getJSONObject(i);
                houselists.add(new Machine(
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
                CustomMachineAdapter adapter = new CustomMachineAdapter(ViewAllMachinery.this, houselists, JSON_STRING);
                recyclerView.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, houselists);
        //recyclerView.setAdapter(adapter);
    }



    private void getAllJSON() {
        // price = mPriceEdit.getText().toString().trim();

        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllMachinery.this, null, getString(R.string.wait_text), false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showHouseInfo();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL_MACHINERY);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
