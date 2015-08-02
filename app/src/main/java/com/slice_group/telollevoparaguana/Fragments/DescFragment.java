package com.slice_group.telollevoparaguana.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slice_group.telollevoparaguana.R;

/**
 * Created by pancracio on 01/08/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class DescFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.site_desc, container, false);


        return v;
    }

    public static DescFragment newInstance(String text) {

        DescFragment f = new DescFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
