package com.smart.ticketing.backendless;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pedro.library.AutoPermissions;
import com.smart.ticketing.R;
import com.smart.ticketing.agriculture.HomeActivity;
import com.smart.ticketing.agriculture.LoginActivity;
import com.smart.ticketing.carpooling.MainActivity;

import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    private Unbinder unbinderknife;

    public static String name = "", username, password, phone, address;

    public static Class sClass = MainActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        AutoPermissions.Companion.loadAllPermissions(this, 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /*if(LibraryUtils.getLoginStatus(SplashActivity.this)){
                    finish();
                    Intent intent = new Intent(SplashActivity.this, com.smart.ticketing.library.ViewBooksActivity.class);
                    sClass = MainActivity.class;
                    startActivity(intent);
                }else{
                    finish();
                    Intent intent = new Intent(SplashActivity.this, com.smart.ticketing.library.LoginActivity.class);
                    sClass = MainActivity.class;
                    startActivity(intent);
                }*/

                finish();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                sClass = HomeActivity.class;
                startActivity(intent);

            }
        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
