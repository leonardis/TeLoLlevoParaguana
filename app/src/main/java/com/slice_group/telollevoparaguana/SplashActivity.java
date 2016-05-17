package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;


public class SplashActivity extends Activity {

    private static Context myContext;
    private static ProgressBar progressBar;
    private int progressStatus = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setProgress(0);
        progressBar.setMax(100);


        myContext = this;

        Thread timer = new Thread(){
            public void run(){
                while (progressStatus < 100)
                {
                    progressStatus += 5;
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setProgress(progressStatus);
                            Log.d("PROGRESS", Integer.toString(progressStatus));
                        }
                    });
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (progressStatus==100)
                {
                    boolean tokenize = false;
                    tokenize = LoadPreferences();
                    if (tokenize){
                        Intent begin = new Intent(myContext, MainActivity.class);
                        startActivity(begin);
                    }else{
                        Intent i = new Intent(myContext, LoginActivity.class);
                        startActivity(i);
                    }

                }

                /*try {
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent mainActivity = new Intent(myContext, MainActivity.class);
                    startActivity(mainActivity);
                }*/
            }
        };
        timer.start();
    }

    private boolean LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String strSavedToken = sharedPreferences.getString("authentication_token", "");
        if(!strSavedToken.equals(""))
            return true;
        else
            return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
