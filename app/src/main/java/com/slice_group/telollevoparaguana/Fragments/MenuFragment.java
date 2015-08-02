package com.slice_group.telollevoparaguana.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.slice_group.telollevoparaguana.R;

/**
 * Created by pancracio on 01/08/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.site_menu, container, false);

        ListView menuList = (ListView) v.findViewById(R.id.list_menu);


        return v;
    }

    public static MenuFragment newInstance(String text) {

        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
