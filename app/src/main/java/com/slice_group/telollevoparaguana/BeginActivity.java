package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private static Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        loginButton = (Button) findViewById(R.id.btn_Login);

        final Activity a = this;

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
                Intent main = new Intent(a, MainActivity.class);
                main.putExtra("QUERY", userText.getText().toString());
                startActivity(main);
            }
        });

    }



}
