package com.slice_group.telollevoparaguana.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.slice_group.telollevoparaguana.BeginActivity;
import com.slice_group.telollevoparaguana.ProductAdapter;
import com.slice_group.telollevoparaguana.ProductModel;
import com.slice_group.telollevoparaguana.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pancracio on 01/08/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class MenuFragment extends Fragment {

    private static ProductAdapter productAdapter;
    private static ListView listView;
    private static ArrayList<ProductModel> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.site_menu, container, false);

        listView = (ListView) v.findViewById(R.id.list_menu);
        arrayList = new ArrayList<ProductModel>();

        return v;
    }

    public static MenuFragment newInstance(String text, Activity activity) {

        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();

        SiteProfile siteProfile = new SiteProfile(activity);
        siteProfile.execute(text);

        f.setArguments(b);

        return f;
    }

    static class SiteProfile extends AsyncTask<String, String, String> {


        private Activity activity;

        SiteProfile(Activity activity){
            this.activity = activity;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            String query = params[0].toString();

            HttpGet request =
                    new HttpGet("http://qcomerenparaguana.com/api/establishments/" + query + "/dishes");

            request.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(request);
                String respStr = EntityUtils.toString(resp.getEntity());

                Log.d("DATA MENU", respStr);

                resultado = respStr;
            }
            catch(Exception ex)
            {
                resultado = "Error "+ex;
            }

            return resultado;

        }

        @Override
        protected void onPostExecute(String result){

            Log.d("AQUI MENU", result);
            //arrayList.removeAll(arrayList);
            //productList.setAdapter(null);


            try {
                JSONObject obj = new JSONObject(result);

                JSONArray respJSON = (JSONArray) (obj.get("dishes"));

                if (obj.getInt("totalItems")>0) {
                    for (int i = 0; i<respJSON.length(); i++) {
                        JSONObject obj2 = respJSON.getJSONObject(i);

                        arrayList.add(new ProductModel(obj2.getString("name"), "SITIO", "Sitio", "", activity.getResources().getString(R.string.bs) + " 300", true));
                    }
                }
                productAdapter = new ProductAdapter(activity, arrayList, activity.getResources(),"MENU");
                listView.setAdapter(productAdapter);



            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
                Toast.makeText(activity.getApplicationContext(), "Se produjo un error al realizar la Busqueda.", Toast.LENGTH_LONG).show();
                /*Intent main = new Intent(activity.getApplicationContext(), BeginActivity.class);
                startActivity(main);
                activity.finish();*/
            }


        }
    }
}
