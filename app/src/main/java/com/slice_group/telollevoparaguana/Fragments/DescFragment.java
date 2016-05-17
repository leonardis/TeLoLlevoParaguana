package com.slice_group.telollevoparaguana.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slice_group.telollevoparaguana.R;


/**
 * Created by pancracio on 01/08/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class DescFragment extends Fragment {

    public static TextView addressTV, cityTV, catTV, creditTV, debitTV, cashTV;
    private static String myAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.site_desc, container, false);

        addressTV = (TextView) v.findViewById(R.id.address);
        cityTV = (TextView) v.findViewById(R.id.city);
        catTV = (TextView) v.findViewById(R.id.categories);
        creditTV = (TextView) v.findViewById(R.id.credit);
        debitTV = (TextView) v.findViewById(R.id.debit);
        cashTV = (TextView) v.findViewById(R.id.cash);


        return v;
    }



    public static DescFragment newInstance(String text, String address) {

        DescFragment f = new DescFragment();
        Bundle b = new Bundle();

        Log.d("DIR", address);

        setData(address);

        f.setArguments(b);

        return f;
    }

    private static void setData(String address){
        myAddress = address;
    }

}
