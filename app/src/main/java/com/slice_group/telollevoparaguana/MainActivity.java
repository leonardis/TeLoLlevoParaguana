package com.slice_group.telollevoparaguana;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.slice_group.telollevoparaguana.ListProductLazy.LazyImageLoadAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by pancracio on 21/07/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class MainActivity extends AppCompatActivity {
    ListView productList;
    LazyImageLoadAdapter adapter;
    private static ArrayList<ProductModel> arrayList;

    private Activity activity;

    private static View mCustomView;
    private static TextView counterText;

    private static String nProducto[];
    private static String nSitio[];
    private static int idProducto[];
    private static Double precioProducto[];
    private static Boolean pedidos[];
    private static ArrayList<String> nombreProducto;

    private static int counter = 0;

    private static ImageButton goToCar, clearButton, searchButton, overflowButton, backButton;
    private static EditText searchEdit;

    private String sitePermalink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;




        productList=(ListView)findViewById(R.id.listProduct);
        //productList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        arrayList = new ArrayList<ProductModel>();
        nombreProducto = new ArrayList<String>();

        String query = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            MainList mainList = new MainList(this);

            query = extras.getString("QUERY");
            String[] splited = query.split("\\s+");
            mainList.execute(query);

        }


        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        /*mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);*/

        //mCustomView = mInflater.inflate(R.layout.action_bar_custom, null);
        counterText = (TextView) mToolbar.findViewById(R.id.counter);
        counterText.setVisibility(View.INVISIBLE);

        goToCar = (ImageButton) mToolbar.findViewById(R.id.car_button);
        goToCar.setVisibility(View.INVISIBLE);

        goToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nProducto != null) {
                    int cont = 0;
                    for (int i = 0; i < nProducto.length; i++) {
                        if (pedidos[i]) {
                            cont+=1;
                            Log.d("NOMBRE", nProducto[i]);
                            Log.d("CHECKLST", pedidos[i].toString());
                            nombreProducto.add(nProducto[i]);
                        }
                    }
                }
                if(nombreProducto != null){
                    Intent shoppingCar = new Intent(activity, ShoppingCarActivity.class);
                    shoppingCar.putExtra("NOMBRE", nombreProducto);
                    startActivity(shoppingCar);
                }
            }
        });

        clearButton = (ImageButton) mToolbar.findViewById(R.id.clearButton);
        clearButton.setVisibility(View.INVISIBLE);

        searchButton = (ImageButton) mToolbar.findViewById(R.id.search_button);
        overflowButton = (ImageButton) mToolbar.findViewById(R.id.overButton);
        //backButton = (ImageButton) mCustomView.findViewById(R.id.backButton);
        //backButton.setVisibility(View.INVISIBLE);

        searchEdit = (EditText) mToolbar.findViewById(R.id.searchEdit);
        searchEdit.setVisibility(View.INVISIBLE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clearButton.isShown()) {
                    overflowButton.setVisibility(View.INVISIBLE);
                    searchEdit.setVisibility(View.VISIBLE);
                    clearButton.setVisibility(View.VISIBLE);
                    goToCar.setVisibility(View.INVISIBLE);
                    counterText.setVisibility(View.INVISIBLE);
                }else{
                    productList.setAdapter(null);
                    arrayList.clear();
                    MainList mainList = new MainList(activity);
                    mainList.execute(searchEdit.getText().toString());
                    searchEdit.setText("");
                    Log.d("THE SEARCH", searchEdit.getText().toString());
                    overflowButton.setVisibility(View.VISIBLE);
                    searchEdit.setVisibility(View.INVISIBLE);
                    clearButton.setVisibility(View.INVISIBLE);
                    if(counter<=0) {
                        goToCar.setVisibility(View.INVISIBLE);
                        counterText.setVisibility(View.INVISIBLE);
                    }else {
                        goToCar.setVisibility(View.VISIBLE);
                        counterText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overflowButton.setVisibility(View.VISIBLE);
                searchEdit.setVisibility(View.INVISIBLE);
                clearButton.setVisibility(View.INVISIBLE);
                if(counter<=0) {
                    goToCar.setVisibility(View.INVISIBLE);
                    counterText.setVisibility(View.INVISIBLE);
                }else {
                    goToCar.setVisibility(View.VISIBLE);
                    counterText.setVisibility(View.VISIBLE);
                }
            }
        });


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (nProducto != null) {
                    int cont = 0;
                    for (int i = 0; i < nProducto.length; i++) {
                        if (pedidos[i]) {
                            cont+=1;
                            Log.d("NOMBRE", nProducto[i]);
                            Log.d("CHECKLST", pedidos[i].toString());
                            nombreProducto.add(nProducto[i]);
                        }
                    }
                }
                if(nombreProducto != null){
                    Intent shoppingCar = new Intent(activity, ShoppingCarActivity.class);
                    shoppingCar.putExtra("NOMBRE", nombreProducto);
                    startActivity(shoppingCar);
                }
            }
        });*/

        //mActionBar.setCustomView(mCustomView);
        //pizzmActionBar.setDisplayShowCustomEnabled(true);

    }

    @Override
    public void onDestroy()
    {
        productList.setAdapter(null);
        super.onDestroy();
    }

    public void onRestClick(){
        Intent profile = new Intent(activity, ProfileAcrivity.class);
        profile.putExtra("PERMALINK", sitePermalink);
        startActivity(profile);
    }

    public void onItemClick(int mPosition, FloatingActionButton addToCar, Boolean check[]){

        if(!check[mPosition]){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = false;
            Bitmap remove = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clear_white_24dp, options);

            counter+=1;

            pedidos[mPosition] = true;

            counterText.setText(Integer.toString(counter));
            if(!clearButton.isShown()){
                counterText.setVisibility(View.VISIBLE);
                goToCar.setVisibility(View.VISIBLE);
            }else{
                counterText.setVisibility(View.INVISIBLE);
                goToCar.setVisibility(View.INVISIBLE);
            }
            addToCar.setImageBitmap(remove);
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = false;
            Bitmap add = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_white_24dp, options);

            counter-=1;

            pedidos[mPosition] = false;

            if(counter!=0)
                counterText.setText(Integer.toString(counter));
            else{
                counterText.setVisibility(View.INVISIBLE);
                goToCar.setVisibility(View.INVISIBLE);
            }

            addToCar.setImageBitmap(add);
        }

        /*if(!check[mPosition]) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = false;
            Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.check, options);

            RoundedAvatarDrawable roundedAvatar = new RoundedAvatarDrawable(avatar);

            imgCheck.setImageDrawable(roundedAvatar);
            imagen.setVisibility(View.INVISIBLE);
            imgCheck.setVisibility(View.VISIBLE);
            vi.setBackgroundColor(getResources().getColor(R.color.font_gray));

            counter+=1;

            counterText.setText(Integer.toString(counter));
            counterText.setVisibility(View.VISIBLE);

            pedidos[mPosition] = true;
            fab.show();


        }else{
            imgCheck.setVisibility(View.INVISIBLE);
            imagen.setVisibility(View.VISIBLE);
            vi.setBackgroundColor(getResources().getColor(R.color.white_main));

            counter-=1;

            pedidos[mPosition] = false;

            if(counter!=0)
                counterText.setText(Integer.toString(counter));
            else{
                counterText.setVisibility(View.INVISIBLE);
                fab.hide();
            }
        }*/
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

            //String query = ;

            //String query = URLEncoder.encode("apples oranges", "utf-8");

            String cadena = null;
            try {
                cadena = "http://qcomerenparaguana.com/api/search?query=" + URLEncoder.encode(params[0].toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            HttpGet request =
                    new HttpGet(cadena.trim());

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
                        sitePermalink = obj3.getString("permalink");

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
