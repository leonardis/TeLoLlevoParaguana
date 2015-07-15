package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class BeginActivity extends Activity {

    private static EditText userText;
    private static Button loginButton, regButton;

    private static Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        loginButton = (Button) findViewById(R.id.btn_Login);
        regButton = (Button) findViewById(R.id.btn_Sign);

        final Activity a = this;
        myContext = this.getApplicationContext();

        userText = (EditText) findViewById(R.id.user_Text);
        userText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Intent main = new Intent(a, MainActivity.class);
                    main.putExtra("QUERY", userText.getText().toString());
                    userText.setText("");
                    startActivity(main);
                    handled = true;
                }
                return handled;

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(a, LoginActivity.class);
                startActivity(login);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(a, SignUpActivity.class);
                startActivity(signup);
            }
        });

        boolean tokenize = false;
        tokenize = LoadPreferences();
        if (tokenize){
            loginButton.setVisibility(View.INVISIBLE);
        }

    }

    private boolean LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String strSavedToken = sharedPreferences.getString("authentication_token", "");
        if(!strSavedToken.equals(""))
            return true;
        else
            return false;
    }

}
