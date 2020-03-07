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

public class SearchByPrice extends AppCompatActivity {
    Button mSearch;
    private String JSON_STRING, JSON_STRING_USER;
    //private String houseType;
    //private String houseLocation;
    //private String housePrice;
    // private String houseImageString;
    private String bedroomNumbers;
    Button bincrement, bdecrement;
    ProgressBar progressBar;
    List<House> houselists, filteredHouseList;
    String rentorsale;
    Long minPrice, maxPrice;
    RecyclerView recyclerView;
    TextView mType, mLayout, mPrice, mLocation, mService;
    EditText mPriceEdit, mMaxPrice, broomText;
    String houseType, houseLayoutof, houseLocation, houseImageString, price, housePrice;
    ImageView mHome;
    Toolbar mToolbar;
    RadioGroup mCategory;
    RadioButton mButton1, mButton2;
    RelativeLayout mLayoutContainer;
    private McustoemItemAdapter mCustomeSpinnerAdapter;

    private ArrayList<CustomSpinnerItem> mCustomSpinnerList, mConsTypeofHouseList;
    private boolean isVisible = false;
     int countVisiblity = 0;
    private ExpandOrCollapse mAnimationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_price);
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
        houseConstList();
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
                    Toast.makeText(SearchByPrice.this, "Please Insert Number of bedroom", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SearchByPrice.this, "Please Insert Number of bedroom", Toast.LENGTH_SHORT).show();
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

    private void houseConstList() {
        mConsTypeofHouseList = new ArrayList<>();
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.wood_mud)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.wood_mud_stucco)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.myson_stone)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.cement_stucco)));
        mConsTypeofHouseList.add(new CustomSpinnerItem(getString(R.string.poured_concrete)));

    }


    private void initList() {
        mCustomSpinnerList = new ArrayList<>();
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.single_family_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.twines_home)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.town_homes_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.apartment)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.condo_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.commercial_space_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.office_space_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.lang_lot_txt)));
        mCustomSpinnerList.add(new CustomSpinnerItem(getString(R.string.ware_house_text)));
    }


    private void getDataFromSpinner() {
        Spinner spinnerType = findViewById(R.id.serviceTypeList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mCustomSpinnerList);
        spinnerType.setAdapter(mCustomeSpinnerAdapter);
        spinnerType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                showFilteredHouseInfoMore();
            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    houseType = "Single Family";
                } else if (position == 1) {
                    houseType = "Twine Homes";
                } else if (position == 2) {
                    houseType = "Town Homes";
                } else if (position == 3) {
                    houseType = "Apartment";
                } else if (position == 4) {
                    houseType = "Condominiums";
                } else if (position == 5) {
                    houseType = "Commercial Space";
                } else if (position == 6) {
                    houseType = "Office Space";
                } else if (position == 7) {
                    houseType = "Land/lot";
                } else if (position == 8) {
                    houseType = "Warehouse";
                }

                if (JSON_STRING!=null && countVisiblity!=1) {
                    showFilteredHouseInfoMore();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinnerHouseLayout = findViewById(R.id.houseConstructionTypeList);
        mCustomeSpinnerAdapter = new McustoemItemAdapter(this, mConsTypeofHouseList);
        spinnerHouseLayout.setAdapter(mCustomeSpinnerAdapter);
        spinnerHouseLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                showFilteredHouseInfoMore();
            }
        });

        spinnerHouseLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    houseLayoutof = "Wood and mud";

                } else if (position == 1) {
                    houseLayoutof = "Wood and mud + Stucco";
                } else if (position == 2) {
                    houseLayoutof = "Maison/stone";
                } else if (position == 3) {
                    houseLayoutof = "Cement Blocks + Stucco";
                } else if (position == 4) {
                    houseLayoutof = "Poured Concrete";
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
        bedroomNumbers = broomText.getText().toString().trim();
        int bn = Integer.parseInt(bedroomNumbers);
        String nbedroom = bedroomEncoding(bn);
        boolean isFiltered = false;
        int idRentOrSale = mCategory.getCheckedRadioButtonId();
        if (idRentOrSale == R.id.frent) {
            rentorsale = "rent";
        } else if (idRentOrSale == R.id.fsale){
            rentorsale = "sale";
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
                JSONObject house = result.getJSONObject(i);
                int filteredPrice = Integer.parseInt(house.getString(Config.TAG_PRICE));
                String filterRS = house.getString(Config.TAG_SERVTYPE);
                String filterBR = house.getString(Config.TAG_NOOFROOM);
                String filterCT = house.getString(Config.TAG_CONSTYPE);
                String filterHT = house.getString(Config.TAG_TYPE);
                if (filteredPrice >= minPrice && filteredPrice <= maxPrice && filterRS.equals(rentorsale) &&
                        filterBR.equals(nbedroom) && filterCT.equals(houseLayoutof)
                        && filterHT.equals(houseType)
                ) {
                    isFiltered = true;
                    filteredHouseList.add(new House(
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
                    CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, filteredHouseList, JSON_STRING);
                    recyclerView.setAdapter(adapter);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isFiltered) {
            Toast.makeText(this, getString(R.string.no_house_txt), Toast.LENGTH_LONG).show();
            CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, filteredHouseList, JSON_STRING);
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
                JSONObject house = result.getJSONObject(i);
                int filteredPrice = Integer.parseInt(house.getString(Config.TAG_PRICE));
                String filterRS = house.getString(Config.TAG_SERVTYPE);
                if (filteredPrice >= minPrice && filteredPrice <= maxPrice && filterRS.equals(rentorsale)) {
                    isFiltered = true;
                    filteredHouseList.add(new House(
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
                    CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, filteredHouseList, JSON_STRING);
                    recyclerView.setAdapter(adapter);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isFiltered && rentorsale.equals("sale")) {
            Toast.makeText(this, getString(R.string.no_house_txt), Toast.LENGTH_LONG).show();
            CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, filteredHouseList, JSON_STRING);
            recyclerView.setAdapter(adapter);
        } else if (!isFiltered && rentorsale.equals("rent")) {
            Toast.makeText(this, getString(R.string.no_houseforrent_txt), Toast.LENGTH_LONG).show();
            CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, filteredHouseList, JSON_STRING);
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
                CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, houselists, JSON_STRING);
                recyclerView.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // CustomAdapter adapter = new CustomAdapter(SearchByPrice.this, houselists);
        //recyclerView.setAdapter(adapter);
    }

    private String bedroomEncoding(int position) {
        String noofRooms = "";
        if (position == 0) {
            noofRooms = "Studio";
        } else if (position == 1) {
            noofRooms = "1 Bedroom";
        } else if (position == 2) {
            noofRooms = "2 Bedroom";
        } else if (position == 3) {
            noofRooms = "3 Bedrooms";
        } else if (position == 4) {
            noofRooms = "4 Bedrooms";
        } else if (position == 5) {
            noofRooms = "5 Bedrooms";
        } else if (position == 6) {
            noofRooms = "6 Bedrooms";
        } else if (position == 7) {
            noofRooms = "7 Bedrooms";
        } else if (position == 8) {
            noofRooms = "8 Bedrooms";
        } else if (position == 9) {
            noofRooms = "9 Bedrooms";
        } else if (position == 10) {
            noofRooms = "10 and Above";
        }
        return noofRooms;
    }

    private void getAllJSON() {
        // price = mPriceEdit.getText().toString().trim();

        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchByPrice.this, null, getString(R.string.wait_text), false, false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getJSON() {
        price = mPriceEdit.getText().toString().trim();

        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchByPrice.this, null, getString(R.string.wait_text), false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                mPriceEdit.setHint("Here are the Lists");
                JSON_STRING = s;
                showHouseInfo();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ALL, price);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
