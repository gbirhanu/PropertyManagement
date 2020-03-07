package com.dsd.machinerental;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eyalbira.loadingdots.LoadingDots;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Timer timer;
    private LoadingDots progressBar;
    private int i = 0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        final long period = 30;
        timer = new Timer();
        boolean isConnected = ConnectivityReceiver.isConnected();
        timer = new Timer();
        if (isConnected) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this repeats every 100 ms
                    if (i < period) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  progressDialog.show();
                                progressBar.setVisibility(View.VISIBLE);
                                //progressBar.setProgress(i);
                                i++;
                            }
                        });
                    } else {
                        //closing the timer
                        timer.cancel();
                        Intent intent = new Intent(Splash.this, LoginActivity.class);
                        startActivity(intent);
                        // close this activity
                        finish();
                    }
                }
            }, 0, period);

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Splash.this);
            alertDialogBuilder.setMessage(R.string.con_check_message);
            alertDialogBuilder.setPositiveButton(R.string.con_ok_message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            relaunch(Splash.this);
                        }
                    });

            alertDialogBuilder.setNegativeButton(R.string.cancel_txt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //Toast.makeText(getApplicationContext(), R.string.con_check_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


    public void relaunch(Activity activity) {
        Intent intent = new Intent(activity, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        Runtime.getRuntime().exit(0);
        activity.finish();
    }
}
