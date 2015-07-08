package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    private Context context;

    public static ListView productList;
    private static ProductAdapter productAdapter;
    private static ArrayList<ProductModel> arrayList;
    //public static ProductModel productModel;
    private static Button orderButton;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<DrawerModel> navDrawerItems;
    private DrawerAdapter adapter;

    private static MenuDialog menuDialog;

    private static MainList mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom);

        mainList = new MainList(this);

        productList = (ListView) findViewById(R.id.list_products);
        arrayList = new ArrayList<ProductModel>();

        productAdapter = new ProductAdapter(this, arrayList, this.getResources());
        productList.setAdapter(productAdapter);

        View view = (View) LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_products, null);
        orderButton = (Button) view.findViewById(R.id.orderButton);

        String query = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            query = extras.getString("QUERY");
            mainList.execute(query);

        }

        /*orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ProductModel.getNomSite(), Toast.LENGTH_LONG).show();
            }
        });

        /*navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_products);

        navDrawerItems = new ArrayList<DrawerModel>();

        for(int i=0; i<=navMenuTitles.length-1; i++){
            navDrawerItems.add(new DrawerModel(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }

        navMenuIcons.recycle();

        adapter = new DrawerAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        //View customActionBarView = actionBar.getCustomView();

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View customBar = mInflater.inflate(R.layout.action_bar_custom, null);

        actionBar.setCustomView(customBar, params);*/



        /*final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.gravity= Gravity.LEFT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        menuDialog = new MenuDialog(this);
        final ImageButton menu = (ImageButton) findViewById(R.id.drawerButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog.getWindow().setAttributes(lp);
                menuDialog.getWindow().setBackgroundDrawableResource(R.color.black_trans);
                menuDialog.setCanceledOnTouchOutside(true);
                /*menuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                menuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d("","");
                        dialog.dismiss();
                    }
                });*
                menuDialog.show();
            }
        });*/



    }

    class MainList extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        private Activity activity;

        MainList(Activity activity){
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
                    new HttpGet("http://testing.tremmelweb.com/api/search?query=" + query);

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

                    for (int i=0; i<respJSON.length(); i++){
                        JSONObject obj2 = respJSON.getJSONObject(i);

                        int id = obj2.getInt("id");
                        Log.d("ID", Integer.toString(id));

                        JSONObject obj3 = respJSON.getJSONObject(i).getJSONObject("establishment");

                        Log.d("obj3",obj3.getString("name"));

                        String nomSite = "";
                        nomSite = obj3.getString("name").substring(0, 22);

                        /*productModel = new ProductModel();

                        productModel.setNomProduct(nomSite+"...");
                        productModel.setNomSite(obj2.getString("name"));
                        productModel.setTmpDelivery(activity.getResources().getString(R.string.time) + obj2.getString("delivery_time"));
                        productModel.setValProduct(activity.getResources().getString(R.string.bs)+" "+obj2.getString("price"));
                        */
                        arrayList.add(new ProductModel(obj2.getString("name"),"",nomSite+"...",activity.getResources().getString(R.string.time) + obj2.getString("delivery_time"),activity.getResources().getString(R.string.bs)+" "+obj2.getString("price")));
                    }

                    progressDialog.dismiss();


                }
                productAdapter = new ProductAdapter(activity, arrayList, activity.getResources());
                productList.setAdapter(productAdapter);

            }catch (JSONException ex){
                Log.e("ERROR JSON", ex.toString());
            }

            productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("","CLICK CLICK CLICK");
                }
            });

        }
    }

}
