package com.example.siddhant.bvcoelibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = (TextView)findViewById(R.id.textView8);
        if(checkConnection()==false) {
            tv.setText("No internet connection available");
            tv.setBackgroundColor(Color.RED);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(SplashScreen.this, SignInOrRegister.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }
    }

    public boolean checkConnection()
    {
        boolean connected=false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }
}