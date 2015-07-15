package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends Activity {

    private Context context;

    private static EditText txtName, txtEmail, txtPass, txtRepass;
    private static Button btnSign;

    private static SignUp signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        context = this.getApplicationContext();

        signUp = new SignUp(this);

        txtName = (EditText) findViewById(R.id.name_Text);
        txtEmail = (EditText) findViewById(R.id.mail_Text);
        txtPass = (EditText) findViewById(R.id.pass_Text);
        txtRepass = (EditText) findViewById(R.id.repass_Text);

        btnSign = (Button) findViewById(R.id.btn_Sign);

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String mail = txtEmail.getText().toString();
                String pass = txtPass.getText().toString();
                String repass = txtRepass.getText().toString();

                if(name.equals("")||pass.equals("")||mail.equals("")||repass.equals("")){
                    Toast.makeText(context, "Los campos no pueden estar vacios!", Toast.LENGTH_LONG).show();
                }else {
                    signUp.execute(name, mail, pass, repass);
                }

            }
        });

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

    class SignUp extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        private Activity activity;

        SignUp(Activity activity){
            progressDialog = new ProgressDialog(activity);
            this.activity = activity;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Enviando Datos");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://testing.tremmelweb.com/api/authenticate/sign_up");

            post.setHeader("content-type", "application/json");

            try {

                JSONObject datos = new JSONObject();

                datos.put("name", params[0].toString());
                datos.put("email", params[1].toString());
                datos.put("password", params[2].toString());
                datos.put("password_confirmation", params[3].toString());


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
                    Log.d("id", user.getString("id"));

                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    Intent main = new Intent(activity.getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                    LoginActivity.activity.finish();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    txtPass.setText("");
                    txtRepass.setText("");
                }

            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
            }

        }
    }
}
