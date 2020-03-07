package com.dsd.machinerental;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchMachinery extends AppCompatActivity {
    Button mSearch;
    private String JSON_STRING, JSON_STRING_USER;
    //private String houseType;
    //private String houseLocation;
    //private String housePrice;
    // private String houseImageString;
    private String bedroomNumbers;
    Button bincrement, bdecrement;
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
        setContentView(R.layout.activity_search_machinery);
        mType = findViewById(R.id.propertyType);
        mToolbar = findViewById(R.id.navigation_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPriceEdit = findViewById(R.id.searchBox);
        mSearch = findViewById(R.id.searchBtn);
        recyclerView = findViewById(R.id.recylcerViewPrice);
        recyclerView.setHasFixedSize(true);
        mMaxPrice = findViewById(R.id.max_val);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategory = findViewById(R.id.category);
        mButton1 = findViewById(R.id.frent);
        mButton2 = findViewById(R.id.fsale);
        mLayoutContainer = findViewById(R.id.filter_container);
        progressBar = findViewById(R.id.viewAllProgress);
        bincrement = findViewById(R.id.incrementBedroomNo);
        bdecrement = findViewById(R.id.decrementBedroomNo);
        broomText = findViewById(R.id.nofbedroom);
        mAnimationManager = new ExpandOrCollapse();
        getAllJSON();
        modelListInit();
        driverListInit();
        initList();
        getDataFromSpinner();
        mPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showFilteredHouseInfoMore();
            }
        });
        mMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showFilteredHouseInfoMore();
            }
        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilteredHouseInfoMore();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilteredHouseInfoMore();
            }
        });
        bincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broomText.setTextColor(Color.BLACK);

                bedroomNumbers = broomText.getText().toString().trim();
                if (bedroomNumbers.isEmpty()) {
                    Toast.makeText(SearchMachinery.this, "Please Insert Number of bedroom", Toast.LENGTH_SHORT).show();
                }
                int bn = Integer.parseInt(bedroomNumbers);
                bn++;
                broomText.setText(bn + "");
                showFilteredHouseInfoMore();

            }
        });
        bdecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bedroomNumbers = broomText.getText().toString().trim();
                if (bedroomNumbers.isEmpty()) {
                    Toast.makeText(SearchMachinery.this, "Please Insert Built Year", Toast.LENGTH_SHORT).show();
                }
                int bn = Integer.parseInt(bedroomNumbers);
                if (bn == 0) {
                    broomText.setTextColor(Color.RED);
                } else {
                    bn--;

                    broomText.setText(bn + "");
                    showFilteredHouseInfoMore();
                    //Toast.makeText(SearchByPrice.this, "Amma" + bn, Toast.LENGTH_SHORT).show();
                }
            }
        });
        houselists = new ArrayList<>();
        filteredHouseList = new ArrayList<>();
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    mAnimationManager.collapse(mLayoutContainer, 1000, 0);
                    isVisible = false;
                } else if (!isVisible){
                    countVisiblity++;
                    DisplayMetrics dis = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dis);
                    int height = dis.heightPixels-100;
                    int width = dis.widthPixels;
                    mAnimationManager.expand(mLayoutContainer, 1000, height/2);
                    isVisible = true;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
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


    private void getDataFromSpinner() {
        Spinner spinnerType = findViewById(R.id.propertyTypeList);
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
                if (JSON_STRING!=null && countVisiblity!=1) {
                    showFilteredHouseInfoMore();
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
                if (JSON_STRING!=null && countVisiblity!=1) {
                    showFilteredHouseInfoMore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void showFilteredHouseInfoMore() {
        progressBar.setVisibility(View.GONE);
        filteredHouseList.clear();
        Integer builtyear = Integer.parseInt(broomText.getText().toString().trim());
        boolean isFiltered = false;
        int idRentOrSale = mCategory.getCheckedRadioButtonId();
        if (idRentOrSale == R.id.frent) {
            rentorsale = "Yes";
        } else if (idRentOrSale == R.id.fsale){
            rentorsale = "No";
        }
        if (mPriceEdit.getText().toString().isEmpty()) {
            minPrice = 0L;
        } else {
            minPrice = Long.parseLong(mPriceEdit.getText().toString().trim());

        }
        if (mMaxPrice.getText().toString().isEmpty()) {
            maxPrice = Long.MAX_VALUE;
        } else {
            maxPrice = Long.parseLong(mMaxPrice.getText().toString().trim());
        }
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject machine = result.getJSONObject(i);
                int filteredPrice = Integer.parseInt(machine.getString(Config.TAG_PRICE));
                String filterRS = machine.getString(Config.TAG_WITH_OR_WITHOUT_DRIVER);

                int filterBR = Integer.parseInt(machine.getString(Config.TAG_BUILT_YEAR));
                String filterCT = machine.getString(Config.TAG_MACHINE_TYPE);
                String filterHT = machine.getString(Config.TAG_MACHINE_MODEL);
                if (filteredPrice >= minPrice && filteredPrice <= maxPrice && filterRS.equals(rentorsale) &&
                        filterBR >= (builtyear) && filterCT.equals(machineType)
                        && filterHT.equals(machineModel)
                ) {
                    isFiltered = true;
                    filteredHouseList.add(new Machine(
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
                    CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, filteredHouseList, JSON_STRING);
                    recyclerView.setAdapter(adapter);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isFiltered) {
            Toast.makeText(this, getString(R.string.no_house_txt), Toast.LENGTH_LONG).show();
            CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, filteredHouseList, JSON_STRING);
            recyclerView.setAdapter(adapter);
        }


        // CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, houselists);
        //recyclerView.setAdapter(adapter);
    }

    private void showFilteredHouseInfo() {
        progressBar.setVisibility(View.GONE);

        filteredHouseList.clear();
        boolean isFiltered = false;
        int idRentOrSale = mCategory.getCheckedRadioButtonId();
        if (idRentOrSale == R.id.frent) {
            rentorsale = "rent";
        } else {
            rentorsale = "sale";
        }
        minPrice = Long.parseLong(mPriceEdit.getText().toString().trim());
        maxPrice = Long.parseLong(mMaxPrice.getText().toString().trim());

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject machine = result.getJSONObject(i);
                int filteredPrice = Integer.parseInt(machine.getString(Config.TAG_PRICE));
                String filterRS = machine.getString(Config.TAG_SERVTYPE);
                if (filteredPrice >= minPrice && filteredPrice <= maxPrice && filterRS.equals(rentorsale)) {
                    isFiltered = true;
                    filteredHouseList.add(new Machine(
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
                    CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, filteredHouseList, JSON_STRING);
                    recyclerView.setAdapter(adapter);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isFiltered && rentorsale.equals("sale")) {
            Toast.makeText(this, getString(R.string.no_house_txt), Toast.LENGTH_LONG).show();
            CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, filteredHouseList, JSON_STRING);
            recyclerView.setAdapter(adapter);
        } else if (!isFiltered && rentorsale.equals("rent")) {
            Toast.makeText(this, getString(R.string.no_houseforrent_txt), Toast.LENGTH_LONG).show();
            CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, filteredHouseList, JSON_STRING);
            recyclerView.setAdapter(adapter);
        }


        // CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, houselists);
        //recyclerView.setAdapter(adapter);
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
                CustomMachineAdapter adapter = new CustomMachineAdapter(SearchMachinery.this, houselists, JSON_STRING);
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
                loading = ProgressDialog.show(SearchMachinery.this, null, getString(R.string.wait_text), false, false);
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
