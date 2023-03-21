package com.gomtechsolutions.attendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class Splash extends Activity {
    ImageView img;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img = findViewById(R.id.img);
        pd = new ProgressDialog(this);
        Thread timer = new Thread(){
            public  void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    call();

                }
            }
        };timer.start();
    }
    private void call() {
        if (isNetworkAvailable()){
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
        }
        else {
            img.setVisibility(View.INVISIBLE);
            pd.dismiss();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.img),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    pd.setCancelable(false);
                    pd.setMessage("\t Connecting...");
                    pd.show();
                    call();
                }
            });
            snackbar.show();
        }

    }
    public boolean isNetworkAvailable(){
        try {
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return  networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e){
            return  false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}