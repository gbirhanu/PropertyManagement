package com.dsd.machinerental;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    String[] mImages;
    LayoutInflater inflater;
    TextView mTextView;
    ImageView imageView;
    ImageButton mRotation;
    RelativeLayout mRelay;
    int r;

    public ViewPagerAdapter(Activity activity, String[] mImages) {
        this.activity = activity;
        this.mImages = mImages;
    }
    boolean rot = false;

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        mTextView = itemView.findViewById(R.id.endofPager);
         imageView = itemView.findViewById(R.id.m_image);
         mRotation = itemView.findViewById(R.id.rotate);
         mRelay = itemView.findViewById(R.id.mRelativeLay);
       // imageView.setBackgroundColor(activity.getResources().getColor(R.color.cardview_dark_background));
        final ProgressBar progressBar = itemView.findViewById(R.id.homeprogress);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        imageView.setMaxHeight(height);
        imageView.setMaxWidth(width);
        imageView.setMinimumHeight(height);
        imageView.setMinimumWidth(width);
        r = activity.getResources().getConfiguration().orientation;

        mRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(r==2){
                     activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    // Toast.makeText(activity, "My Or "+r, Toast.LENGTH_SHORT).show();
                 }else if (r==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                     //Toast.makeText(activity, "My Or "+r, Toast.LENGTH_SHORT).show();

                     activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                 }
                //Toast.makeText(activity, "My Or Land "+ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, Toast.LENGTH_SHORT).show();
                //Toast.makeText(activity, "My Or Port"+ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, Toast.LENGTH_SHORT).show();

            }
        });
        try {
            Picasso.get().load(mImages[position]).centerInside().placeholder(R.color.black_color).fit().error(R.mipmap.ic_launcher).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    mTextView.setText(activity.getString(R.string.slide_txt));
                    progressBar.setVisibility(View.GONE);
                }
                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Picasso.get().load(mImages[position]).fit().rotate(90).placeholder(R.color.black_color).error(R.mipmap.ic_launcher).into(imageView);
                }
            });

        } catch (Exception ex) {

        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



}