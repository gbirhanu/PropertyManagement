package com.dsd.machinerental;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import com.dsd.machinerental.langhelper.LanguageHelper;

import io.paperdb.Paper;

public class ChangeLanguage extends AppCompatActivity {
    private Toolbar mToolbar;
    TextView mTextCustomer, mTextDriver;
    Button mSelectLanguageButton;
    TextView hello;
    Spinner mSpinner;
    private RadioGroup radioGroup;
    Button mContinue;
    String forsaleorrent,mUname,mUsrId,mPhone,mStatus;
    String[] lan = {"English", "አማርኛ"};
    ArrayAdapter adapter;
    ContentLoadingProgressBar loadingProgressBar;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        mSelectLanguageButton = findViewById(R.id.btnSelectLanguage);
        hello  =   findViewById(R.id.welcome);
        Paper.init(this);
        mSelectLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLanguage.this);
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
                                    relaunch(ChangeLanguage.this);
                                    finish();
                                }
                                if (selectedPosition==1){
                                    Paper.book().write("language", "am");
                                    updateView((String) Paper.book().read("language"));
                                    relaunch(ChangeLanguage.this);
                                    finish();
                                }

                            }
                        });



                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();            }
        });

        forsaleorrent="";
        mContinue = findViewById(R.id.btnContinue);
        mUname = getIntent().getStringExtra("uName");
        mUsrId = getIntent().getStringExtra("mUsrId");
        mPhone = getIntent().getStringExtra("usephone");
        mStatus = getIntent().getStringExtra("userStatus");
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup = findViewById(R.id.radioGroups);
                int id = radioGroup.getCheckedRadioButtonId();
                if(id==R.id.forRent){
                    forsaleorrent = "rent";
                    Intent intent = new Intent(ChangeLanguage.this, MapsActivity.class);
                    intent.putExtra("houseisfor", forsaleorrent);
                    intent.putExtra("mUnames", mUname);
                    intent.putExtra("mUseIds", mUsrId);
                    intent.putExtra("mPhones", mPhone);
                    intent.putExtra("mStatus",mStatus);
                    startActivity(intent);
                    finish();
                }else if (id==R.id.forSale){
                    forsaleorrent = "sale";
                    Intent intent = new Intent(ChangeLanguage.this, MapsActivity.class);
                    intent.putExtra("houseisfor", forsaleorrent);
                    intent.putExtra("mUnames", mUname);
                    intent.putExtra("mUseIds", mUsrId);
                    intent.putExtra("mPhones", mPhone);
                    intent.putExtra("mStatus",mStatus);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(ChangeLanguage.this, "Please Select One", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateView(String language) {
        Context context = LanguageHelper.setLanguage(this, language);
        Resources resources = context.getResources();
        hello.setText(resources.getString(R.string.select_lang));
    }



    public void relaunch(Activity activity) {
        Intent intent = new Intent(activity, ChangeLanguage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        Runtime.getRuntime().exit(0);
        activity.finish();
    }



}
