package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slice_group.telollevoparaguana.Fragments.DescFragment;
import com.slice_group.telollevoparaguana.Fragments.MenuFragment;
import com.slice_group.telollevoparaguana.ListProductLazy.LazyImageLoadAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileAcrivity extends ActionBarActivity {

    private static View mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.action_bar_custom_simple, null);

        TextView siteName = (TextView) findViewById(R.id.siteName);
        siteName.setShadowLayer(10,3,3, Color.RED);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        final LinearLayout descLayout = (LinearLayout) findViewById(R.id.descLayout);
        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        final ImageView descSelected = (ImageView) findViewById(R.id.descSelected);
        final ImageView menuSelected = (ImageView) findViewById(R.id.menuSelected);

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        descLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                descSelected.setBackgroundColor(getResources().getColor(R.color.tab_selected));
                menuSelected.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                menuSelected.setBackgroundColor(getResources().getColor(R.color.tab_selected));
                descSelected.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return DescFragment.newInstance("FirstFragment, Instance 1");
                case 1: return MenuFragment.newInstance("SecondFragment, Instance 2");


                default: return DescFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    class SiteProfile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        private Activity activity;

        SiteProfile(Activity activity){
            progressDialog = new ProgressDialog(activity);
            this.activity = activity;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Buscando...");
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.circle_progress));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String resultado = "";

            HttpClient httpClient = new DefaultHttpClient();

            String query = params[0].toString();

            HttpGet request =
                    new HttpGet("http://qcomerenparaguana.com/api/establishments/" + query);

            request.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(request);
                String respStr = EntityUtils.toString(resp.getEntity());

                Log.d("DATA", respStr);

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

            Log.d("AQUI", result);
            arrayList.removeAll(arrayList);
            productList.setAdapter(null);


            try {
                JSONObject obj = new JSONObject(result);

                JSONArray respJSON = (JSONArray) (obj.get("dishes"));

                Log.d("success",obj.getString("dishes"));
                if((obj.getInt("totalItems")>0)&&progressDialog.isShowing()){

                    nProducto = new String[respJSON.length()];
                    nSitio = new String[respJSON.length()];
                    idProducto = new int[respJSON.length()];
                    precioProducto = new Double[respJSON.length()];
                    pedidos = new Boolean[respJSON.length()];

                    for (int i=0; i<respJSON.length(); i++){
                        JSONObject obj2 = respJSON.getJSONObject(i);

                        pedidos[i] = false;

                        int id = obj2.getInt("id");
                        Log.d("ID", Integer.toString(id));

                        JSONObject obj3 = respJSON.getJSONObject(i).getJSONObject("establishment");

                        Log.d("obj2",obj2.getString("name"));

                        JSONObject obj4 = respJSON.getJSONObject(i).getJSONObject("image");

                        nProducto[i] = obj2.getString("name");
                        nSitio[i] = obj3.getString("name");
                        idProducto[i] = obj2.getInt("id");
                        precioProducto[i] = Double.parseDouble(obj2.getString("price"));

                        String nomSite = "";
                        if (obj3.getString("name").length()>=22) {
                            nomSite = obj3.getString("name").substring(0, 22);
                            arrayList.add(new ProductModel(obj2.getString("name"),obj2.getString("description"),"http://qcomerenparaguana.com"+obj4.getString("url"),nomSite+"...",activity.getResources().getString(R.string.bs)+" "+obj2.getString("price"), false));

                        }else{
                            nomSite = obj3.getString("name");
                            arrayList.add(new ProductModel(obj2.getString("name"),obj2.getString("description"),"http://qcomerenparaguana.com"+obj4.getString("url"),nomSite,activity.getResources().getString(R.string.bs)+" "+obj2.getString("price"), false));

                        }

                    }

                    progressDialog.dismiss();


                }

                // Create custom adapter for listview
                adapter=new LazyImageLoadAdapter(activity, arrayList);

                //Set adapter to listview
                productList.setAdapter(adapter);



            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
                progressDialog.dismiss();
                Toast.makeText(activity.getApplicationContext(), "Se produjo un error al realizar la Busqueda.", Toast.LENGTH_LONG).show();
                Intent main = new Intent(activity.getApplicationContext(), BeginActivity.class);
                startActivity(main);
                finish();
            }


        }
    }

}
