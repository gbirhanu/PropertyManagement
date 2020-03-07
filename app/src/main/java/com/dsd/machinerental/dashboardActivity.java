package com.dsd.machinerental;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.dsd.machinerental.langhelper.LanguageHelper;

import io.paperdb.Paper;

public class dashboardActivity extends AppCompatActivity {
    LinearLayout linearLayout,forsaleLay,langsettingLay, addHomeHereLay, mViewAllLay,machinaryLayout;
    TextView hello, mRentText,mSaleText,mMachineryText;
    ProgressDialog loading;
    int nSale, nRent;
    String space = " ";

    String forsaleorrent,mUname,mUsrId,mPhone,mStatus, nResult;
    String[] lan = {"English", "Afaan Oromo", "አማርኛ"};
    ArrayAdapter adapter;
    ContentLoadingProgressBar loadingProgressBar;
    boolean isEnglish;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase, "en"));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        linearLayout = findViewById(R.id.forRentLayout);
        forsaleLay = findViewById(R.id.forsaleLayout);
        langsettingLay = findViewById(R.id.languageSetting);
        addHomeHereLay = findViewById(R.id.adhomehereLayout);
        machinaryLayout = findViewById(R.id.gotoMachinery);
        mViewAllLay = findViewById(R.id.viewAllLayout);
        mMachineryText = findViewById(R.id.noofMachnery);
        hello  =   findViewById(R.id.welcome);
        Paper.init(this);
        final String language = Paper.book().read("language");
        getJSON("rent");
        getJSONSALE("sale");
        getMachineryJSON();
        mRentText = findViewById(R.id.nrent);
        mSaleText = findViewById(R.id.nsale);
        //list of items
        if (language == null) {
            Paper.book().write("language", "en");
        }
        mViewAllLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardActivity.this,ViewAllHouse.class);
                startActivity(intent);
            }
        });
        machinaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboardActivity.this,MachineMapsActivity.class);
                intent.putExtra("uName", mUname);
                intent.putExtra("mUsrId", mUsrId);
                intent.putExtra("userPhone", mPhone);
                intent.putExtra("userStatus",mStatus);
                startActivity(intent);
            }
        });
        addHomeHereLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forsaleorrent = "rent";
                String addHome = "addhome";
                Intent intent = new Intent(dashboardActivity.this, MapsActivity.class);
                intent.putExtra("houseisfor", forsaleorrent);
                intent.putExtra("uName", mUname);
                intent.putExtra("mUsrId", mUsrId);
                intent.putExtra("userPhone", mPhone);
                intent.putExtra("userStatus",mStatus);
                intent.putExtra("addmyhome",addHome);
                startActivity(intent);
                finish();
            }
        });
        langsettingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(dashboardActivity.this);
                builder.setTitle(R.string.select_lang);
                builder.setSingleChoiceItems(lan, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String language = Paper.book().read("language");

                                //list of items
                                if (language == null) {
                                    Paper.book().write("language", "en");
                                }
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition == 0){
                                    Paper.book().write("language", "en");
                                    updateView((String) Paper.book().read("language"));
                                    relaunch(dashboardActivity.this);
                                    finish();
                                }
                                if (selectedPosition == 1){
                                    Paper.book().write("language", "om");
                                    updateView((String) Paper.book().read("language"));
                                    relaunch(dashboardActivity  .this);
                                    finish();
                                }
                                if (selectedPosition==2){
                                    Paper.book().write("language", "am");
                                    updateView((String) Paper.book().read("language"));
                                    relaunch(dashboardActivity.this);
                                    finish();
                                }

                            }
                        });



                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }

        });
        forsaleorrent="";
        mUname = getIntent().getStringExtra("uName");
        mUsrId = getIntent().getStringExtra("mUsrId");
        mPhone = getIntent().getStringExtra("userPhone");
        mStatus = getIntent().getStringExtra("userStatus");
        Paper.init(this);
        forsaleLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forsaleorrent = "sale";
                Intent intent = new Intent(dashboardActivity.this, MapsActivity.class);
                intent.putExtra("houseisfor", forsaleorrent);
                intent.putExtra("uName", mUname);
                intent.putExtra("mUsrId", mUsrId);
                intent.putExtra("userPhone", mPhone);
                intent.putExtra("userStatus",mStatus);
                startActivity(intent);
                finish();

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forsaleorrent = "rent";
                Intent intent = new Intent(dashboardActivity.this, MapsActivity.class);
                intent.putExtra("houseisfor", forsaleorrent);
                intent.putExtra("uName", mUname);
                intent.putExtra("mUsrId", mUsrId);
                intent.putExtra("userPhone", mPhone);
                intent.putExtra("userStatus",mStatus);
                startActivity(intent);
                finish();
                // Toast.makeText(dashboardActivity.this, "Amma Galuu Dandeessa", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateView(String language) {
        Context context = LanguageHelper.setLanguage(this, language);
        Resources resources = context.getResources();
        hello.setText(resources.getString(R.string.select_lang));
    }
    public void relaunch(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        Runtime.getRuntime().exit(0);
        activity.finish();
    }
    private void getMachineryJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(dashboardActivity.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                nResult = s;
                //Toast.makeText(dashboardActivity.this, "Result "+nResult, Toast.LENGTH_SHORT).show();
                //if (!nResult.equals(null)) {
                String firstWord = nResult + space;
                String lastWord = getString(R.string.no_of_machninery);
                Spannable spannable = new SpannableString(firstWord+lastWord);
                spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, firstWord.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), firstWord.length(),
                        firstWord.length()+lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mMachineryText.setText( spannable );

                //}
                // Toast.makeText(MapsActivity.this, "house is again "+s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_NUMBER_MACHINERY);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    private void getJSON(final String houseisf){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(dashboardActivity.this,null,getString(R.string.wait_text),false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                nResult = s;
                //Toast.makeText(dashboardActivity.this, "Result "+nResult, Toast.LENGTH_SHORT).show();
                //if (!nResult.equals(null)) {
                String firstWord = nResult + space ;
                String lastWord = getString(R.string.number_of_rent);
                Spannable spannable = new SpannableString(firstWord+lastWord);
                spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, firstWord.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), firstWord.length(),
                        firstWord.length()+lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mRentText.setText( spannable );

                //}
                // Toast.makeText(MapsActivity.this, "house is again "+s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_NUMBER_HOUSE,houseisf);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getJSONSALE(final String houseisf){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                nResult = s;
                //Toast.makeText(dashboardActivity.this, "Result "+nResult, Toast.LENGTH_SHORT).show();
                //if (!nResult.equals(null)) {
                String firstWord = nResult + space;
                String lastWord = getString(R.string.number_of_sale);
                Spannable spannable = new SpannableString(firstWord+lastWord);
                spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, firstWord.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), firstWord.length(),
                        firstWord.length()+lastWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSaleText.setText( spannable );
                //}
                // Toast.makeText(MapsActivity.this, "house is again "+s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_NUMBER_HOUSE,houseisf);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
