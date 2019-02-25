package com.example.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LogonSignIn extends AppCompatActivity implements View.OnKeyListener {
    EditText txtUserName, txtPassword;

    //log on or sign in, depending on current state of button
    public void loginOrSignup(){

        String username = txtUserName.getText().toString();
        String password = txtPassword.getText().toString();
        Button button = (Button)findViewById(R.id.btnLoginSignup);
        String btnText = button.getText().toString();


        if (username == null || username.length()==0) {
            Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null|| password.length()==0) {
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (btnText == getResources().getString(R.string.signup)) {
            //sign up new user
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null) {
                        Toast.makeText(LogonSignIn.this, "User account successfully created", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LogonSignIn.this,"sign up failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            //log in existing user
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e != null) {
                        Toast.makeText(LogonSignIn.this, "Login error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(LogonSignIn.this, "OK, you're in.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RecipeListActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }

    //hide keyboard when user clicks in background
    public void onClickBackground(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    //change text of button from login to sign up or vice versa
    public void changeBtn(View view){
        Button button = (Button)findViewById(R.id.btnLoginSignup);
        TextView altText = (TextView)  findViewById(R.id.txtSignupLogin);
        String btnText =  button.getText().toString();
        if (btnText == getResources().getString(R.string.login)) {
            button.setText(getResources().getString(R.string.signup));
            altText.setText(getResources().getString(R.string.login_instead));
        }
        else{
            button.setText(getResources().getString(R.string.login));
            altText.setText(getResources().getString(R.string.signup_instead));
        }
    }

    //button login/signup action
    public void btnLoginSignup(View view){
        loginOrSignup();
    }

    //old stuff for reference
    public void dump(){

    /*
    ParseObject tweet = new ParseObject("Tweet");

    tweet.put("user", "Peter");
    tweet.put("time", Calendar.getInstance().getTime());
    tweet.put("tweet","This is my first tweet. Hello everybody!");
    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e==null){
            Log.i("Parse msg","save in background successful");
        }
        else{
            Log.i("Parse msg","save in background NOT successful, error: "+e.toString());
        }

      }
    });

*/
/*
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.whereEqualTo("userName","Peter");
    query.setLimit(10);
    query.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if (list != null && e == null){
                for (ParseObject parseObject:list) {
                    Log.i("Parse msg: ", "Score timestamp: " + parseObject.getDate("time")
                            + "; user: " + parseObject.getSring("userName")
                            + "; score: " + parseObject.getInt("score"));
                    int sc =  parseObject.getInt("score");
                    if (sc> 100){
                        parseObject.put("score",sc+50);
                        parseObject.saveInBackground();
                    }
                }
            }
            else{
             Log.i("Parse msg","Error: "+e.toString())   ;

            }
        }
    });
*/
/*
      ParseUser user = new ParseUser();
      user.setUsername("Peter");
      user.setPassword("password");
      user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
              if (e==null) {
                  Log.i("Parse msg","Signup successfull");
              }
              else{
                  Log.i("Parse msg","signup failed");
              }
          }
      });
*/
/*
        ParseUser.logInInBackground("Peter", "erjgh", new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null) {
                    Log.i("Parse msg", "Login error: " + e);
                }
            }
        });
*/
    }

    //listen for enter key event in password field & log on/sign in when enter is pressed
    public boolean onKey(View view, int i, KeyEvent keyEvent){
        // KeyEvent is fired twice (key pressed & key released).
        // Select ACTION_DOWN to avoid trying to sign up twice
        if (i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            loginOrSignup();
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon_sign_in);

        setTitle("Please log in");
        txtUserName  =(EditText)findViewById(R.id.userName);
        txtPassword = (EditText) findViewById(R.id.passWord);
        txtPassword.setOnKeyListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}


