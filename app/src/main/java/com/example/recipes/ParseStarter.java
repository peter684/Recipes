package com.example.recipes;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class ParseStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // initialization code
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("e78d0d92255a82f6eaffad92f435fd132f584fe6")
                .clientKey("953324cc8e8e3e31f75de46c4ddd39a50e4afff4")
                .server("http://18.197.129.236:80/parse/")
                .build()
        );


        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }

}
