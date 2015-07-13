package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    private Context context;

    private static EditText userText, passText;
    private static Button loginButton;
    private static LinearLayout newPassLayout, regUserLayout;

    private static Login login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this.getApplicationContext();

        login = new Login(this);

        userText = (EditText) findViewById(R.id.user_Text);
        passText = (EditText) findViewById(R.id.pass_Text);
        loginButton = (Button) findViewById(R.id.btn_Login);
        newPassLayout = (LinearLayout) findViewById(R.id.new_PassLayout);
        regUserLayout = (LinearLayout) findViewById(R.id.reg_UserLayout);

        LoadPreferences();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String user = userText.getText().toString();
            String pass = passText.getText().toString();

                if(user.equals("")||pass.equals("")){
                    Toast.makeText(context, "Los campos no pueden estar vacios!", Toast.LENGTH_LONG).show();
                }else {
                    login.execute(user, pass);
                }
            }
        });

        newPassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "PASSWORD LOST", Toast.LENGTH_LONG).show();
            }
        });

        regUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signActivity = new Intent(getApplicationContext(), SingUpActivity.class);
                startActivity(signActivity);
            }
        });

    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String strSavedEmail = sharedPreferences.getString("email", "");
        userText.setText(strSavedEmail);
        if(!userText.equals(""))
            passText.requestFocus();
            passText.setCursorVisible(true);
    }

    private void SavePreferences(String email, String role_name, String name, String token){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("role_name", role_name);
        editor.putString("name", name);
        editor.putString("authentication_token", token);
        editor.commit();
    }

    class Login extends AsyncTask<String, String, String>{

        private ProgressDialog progressDialog;

        private Activity activity;

        Login(Activity activity){
            progressDialog = new ProgressDialog(activity);
            this.activity = activity;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Iniciando Sesión");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://testing.tremmelweb.com/api/authenticate");

            post.setHeader("content-type", "application/json");

            try {

                JSONObject datos = new JSONObject();

                datos.put("email", params[0].toString());
                datos.put("password", params[1].toString());

                StringEntity entity = new StringEntity(datos.toString());
                post.setEntity(entity);

                HttpResponse response = httpClient.execute(post);
                String respStr = EntityUtils.toString(response.getEntity());

                Log.d("JSON", respStr);

                resultado = respStr;

            }catch (Exception ex){

                resultado = "Error "+ex;

            }

            return resultado;

        }

        @Override
        protected void onPostExecute(String result){

            try {
                JSONObject obj = new JSONObject(result);

                Log.d("success",obj.getString("success"));
                if((obj.getString("success").equals("true"))&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                    JSONObject user = new JSONObject(obj.getString("user"));

                    SavePreferences(user.getString("email"), user.getString("role_name"), user.getString("name"), user.getString("authentication_token"));
                    Log.d("id",user.getString("id"));

                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    Intent main = new Intent(activity.getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    passText.setText("");
                }

            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
            }

        }
    }

}
