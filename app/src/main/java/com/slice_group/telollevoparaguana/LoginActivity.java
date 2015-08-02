package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class LoginActivity extends Activity {

    private Context context;

    private static EditText userText, passText;
    private static Button loginButton;
    private static LinearLayout newPassLayout, regUserLayout;

    private static Login login;
    private static ChangePassword chPassword;

    public static Activity activity;
    private static Dialog dialog;

    private static boolean bandera;

    private String token;
    private static String email = "";
    private static String oldPass = "";
    private static String newPass = "";
    private static String rePass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bandera = false;

        context = this.getApplicationContext();
        activity = this;

        login = new Login(this);
        chPassword = new ChangePassword(this);

        userText = (EditText) findViewById(R.id.user_Text);
        passText = (EditText) findViewById(R.id.pass_Text);
        loginButton = (Button) findViewById(R.id.btn_Login);
        newPassLayout = (LinearLayout) findViewById(R.id.new_PassLayout);
        regUserLayout = (LinearLayout) findViewById(R.id.reg_UserLayout);

        LoadPreferences();

        // Create custom dialog object
        dialog = new Dialog(activity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_password);
        // Set dialog title
        dialog.setTitle("Recuperación de contraseña");

        final TextView dEmail = (TextView) dialog.findViewById(R.id.user);
        final TextView oldPassword = (TextView) dialog.findViewById(R.id.oldPassword);
        final TextView newPassword = (TextView) dialog.findViewById(R.id.newPassword);
        final TextView rePassword = (TextView) dialog.findViewById(R.id.rePassword);

        LinearLayout cancelar = (LinearLayout) dialog.findViewById(R.id.cancelar);
        LinearLayout aceptar = (LinearLayout) dialog.findViewById(R.id.aceptar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String user = userText.getText().toString();
            String pass = passText.getText().toString();

                if(user.equals("")||pass.equals("")){
                    Toast.makeText(context, "Los campos no pueden estar vacios!", Toast.LENGTH_LONG).show();
                }else {
                    bandera = false;
                    login = new Login(activity);
                    login.execute(user, pass);
                }
            }
        });

        newPassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "PASSWORD LOST", Toast.LENGTH_LONG).show();

                dialog.show();
            }
        });

        regUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signActivity = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signActivity);
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = dEmail.getText().toString();
                oldPass = oldPassword.getText().toString();
                newPass = newPassword.getText().toString();
                rePass = rePassword.getText().toString();


                if(email.equals("")||oldPass.equals("")||newPass.equals("")||rePass.equals("")){
                    Toast.makeText(context, "Los campos no pueden estar vacios!", Toast.LENGTH_LONG).show();
                }else{
                    bandera = true;
                    login = new Login(activity);
                    login.execute(email, oldPass);

                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_password, null))
                // Add action buttons
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }*/


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
            if (!bandera)
                progressDialog.setMessage("Iniciando Sesión");
            else
                progressDialog.setMessage("Consultando datos de sesión!");

            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://qcomerenparaguana.com/api/authenticate");

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
                    Log.d("id", user.getString("id"));

                    token = user.getString("authentication_token");

                    if (!bandera) {
                        Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                        Intent main = new Intent(activity.getApplicationContext(), BeginActivity.class);
                        startActivity(main);
                        finish();
                    }else{
                        chPassword = new ChangePassword(activity);
                        chPassword.execute(oldPass, newPass, rePass);
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    passText.setText("");
                    isCancelled();
                    this.cancel(true);
                }

            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
                progressDialog.dismiss();
                Toast.makeText(activity.getApplicationContext(), "Error de conexión con el servidor!\n Vuelva a intentarlo más tarde.", Toast.LENGTH_LONG).show();
                passText.setText("");
                isCancelled();
                this.cancel(true);
            }

        }
    }

    class ChangePassword extends AsyncTask<String, String, String>{

        private ProgressDialog progressDialog;

        private Activity activity;

        ChangePassword(Activity activity){
            progressDialog = new ProgressDialog(activity);
            this.activity = activity;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Guardando nueva contraseña");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut put = new HttpPut("http://qcomerenparaguana.com/api/settings/password");

            put.setHeader("content-type", "application/json");
            put.setHeader("X-auth-token", token);

            try {

                JSONObject datos = new JSONObject();

                datos.put("password", params[1].toString());
                datos.put("password_confirmation", params[2].toString());
                datos.put("actual_password", params[0].toString());


                StringEntity entity = new StringEntity("{\"user\":"+datos.toString()+"}");
                Log.d("DATOS", "{\"user\":"+datos.toString()+"}");
                Log.d("ENTITY", entity.toString());
                put.setEntity(entity);

                HttpResponse response = httpClient.execute(put);
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
                if((obj.getString("success").equals("CORRECT"))&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                    /*JSONObject user = new JSONObject(obj.getString("user"));

                    SavePreferences(user.getString("email"), user.getString("role_name"), user.getString("name"), user.getString("authentication_token"));
                    Log.d("id",user.getString("id"));

                    Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                    Intent main = new Intent(activity.getApplicationContext(), BeginActivity.class);
                    startActivity(main);
                    finish();*/

                }else{
                    progressDialog.dismiss();
                    /*Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    passText.setText("");
                    isCancelled();
                    this.cancel(true);*/
                }

            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
                progressDialog.dismiss();
            }

        }
    }

}
