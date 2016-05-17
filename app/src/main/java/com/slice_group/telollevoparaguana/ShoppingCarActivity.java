package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCarActivity extends ActionBarActivity {

    private static ArrayList<String> nombres;

    private static ProductAdapter productAdapter;
    private static ListView listView;
    private static ArrayList<ProductModel> arrayList;

    private static Activity activity;

    private static View mCustomView;
    private static TextView counterText;

    String[] Payments = {"Tarjeta de Credito", "Transferencia", "Efectivo"};
    Dialog dialog, dialog2;

    int contador = 1;

    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);

        activity = this;

        listView = (ListView) findViewById(R.id.listOrdersDetail);
        arrayList = new ArrayList<ProductModel>();

        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.action_bar_custom_simple, null);
        counterText = (TextView) mCustomView.findViewById(R.id.counter);
        counterText.setVisibility(View.INVISIBLE);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        final ImageButton payButton = (ImageButton) findViewById(R.id.goToPay);

        // Create custom dialog object
        dialog = new Dialog(activity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_payment);
        // Set dialog title
        dialog.setTitle("Recuperación de contraseña");

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerPay);
        final LinearLayout btnAccept = (LinearLayout) dialog.findViewById(R.id.aceptar);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_spinner_item, Payments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        dialog2 = new Dialog(activity);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_edit);
        dialog2.setCancelable(true);

        final ImageButton more = (ImageButton) dialog2.findViewById(R.id.moreButton);
        final ImageButton less = (ImageButton) dialog2.findViewById(R.id.lessButton);
        final TextView low = (TextView) dialog2.findViewById(R.id.lowNumber);
        final TextView selected = (TextView) dialog2.findViewById(R.id.selectedNumber);
        final TextView grown = (TextView) dialog2.findViewById(R.id.grownNumber);

        low.setText("");
        selected.setText(Integer.toString(contador));
        grown.setText(Integer.toString(contador+1));

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                low.setText(Integer.toString(contador));
                contador+=1;
                selected.setText(Integer.toString(contador));
                grown.setText(Integer.toString(contador+1));
            }
        });

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contador>1){
                    contador -= 1;
                    if (contador > 1) {
                        grown.setText(Integer.toString(contador + 1));
                        selected.setText(Integer.toString(contador));
                        low.setText(Integer.toString(contador - 1));
                    } else if (contador==1){
                        grown.setText(Integer.toString(contador + 1));
                        selected.setText(Integer.toString(contador));
                        low.setText("");
                    }
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            nombres = new ArrayList<String>();
            nombres = extras.getStringArrayList("NOMBRE");
            for (int i=0; i<nombres.size(); i++){
                Log.d("Nombres", nombres.get(i));
                arrayList.add(new ProductModel(nombres.get(i),"","Sitio","",activity.getResources().getString(R.string.bs)+" 300",true));
            }
            productAdapter = new ProductAdapter(this, arrayList, this.getResources(), "SHOP");
            listView.setAdapter(productAdapter);
        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                //popupWindow.showAsDropDown(payButton, 50, -30);
            }
        });



        btnAccept.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void onItemClick(int mPosition) {
        dialog2.show();
    }


}
