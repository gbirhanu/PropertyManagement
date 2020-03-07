package com.dsd.machinerental;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageShower extends AppCompatActivity {
//ImageView mImageView;
//TextView mTextView;
private String JSON_STRING, photoType;
    int id;
ArrayList houselists;
HackyViewPager viewPager;
ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shower);
        //mImageView=findViewById(R.id.m_mage);
        //mTextView=findViewById(R.id.image_description);
       // getIntetVariable()
        //Fresco.initialize(this);
        houselists = new ArrayList<>();
        id=getIntent().getIntExtra("id",0);
        photoType = getIntent().getStringExtra("type");
      //  id=getIntent().getStringExtra("id");
        if (photoType.equals("house")) {
            getJSON();
        }else  if (photoType.equals("machine")){
            getJSONMachine();
        }
        viewPager=findViewById(R.id.viewPager);
    }

    private void getJSONMachine() {
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageShower.this,null,"Fetching Data...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                getAllthePhoto();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ALL_MACHINERY_PHOTO,id+"");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void getAllthePhoto() {
        JSONObject jsonObject = null;

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            final String [] images=new String[result.length()];

            for (int i = 0; i < result.length(); i++) {
                JSONObject house = result.getJSONObject(i);

                 images[i]=house.getString(Config.TAG_PHOTO);
            }
            adapter = new ViewPagerAdapter(ImageShower.this,images);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position==images.length-1 && images.length!=1){

                       adapter.mTextView.setText(getString(R.string.endof_page_txt));
                        Toast.makeText(ImageShower.this, getString(R.string.endof_page_txt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            //Toast.makeText(this, images.toString(), Toast.LENGTH_SHORT).show();
            adapter = new ViewPagerAdapter(ImageShower.this,images);
            viewPager.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageShower.this,null,"Fetching Data...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                 getAllthePhoto();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ALL_PHOTO,id+"");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
