package com.example.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        Intent intent = new Intent(getApplicationContext(),LogonSignIn.class);
        startActivity(intent);
    }
}
