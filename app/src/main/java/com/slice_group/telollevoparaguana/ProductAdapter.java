package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pancracio on 17/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class ProductAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList datos;
    private static LayoutInflater inflater = null;
    public Resources resources;
    ProductModel listValues = null;
    int i=0;

    public ProductAdapter(Activity a, ArrayList d, Resources res) {
        activity = a;
        datos = d;
        resources = res;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (datos.size()<=0)
            return 1;
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public ImageView productImg;
        public TextView siteName;
        public TextView prodctName;
        public TextView timeDelivery;
        public TextView productPrice;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            vi = inflater.inflate(R.layout.item_products, null);

            holder = new ViewHolder();
            holder.productImg = (ImageView)vi.findViewById(R.id.productImg);
            holder.siteName = (TextView) vi.findViewById(R.id.siteName);
            holder.prodctName = (TextView)vi.findViewById(R.id.productName);
            holder.timeDelivery = (TextView)vi.findViewById(R.id.timeDelivery);
            holder.productPrice = (TextView)vi.findViewById(R.id.productPrice);

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(datos.size()<=0)
        {
            holder.siteName.setText("No Data");

        }
        else
        {
            listValues=null;
            listValues = ( ProductModel ) datos.get( position );

            holder.siteName.setText( listValues.getNomSite() );
            holder.prodctName.setText( listValues.getNomProduct() );
            holder.timeDelivery.setText( listValues.getTmpDelivery() );
            holder.productPrice.setText( listValues.getValProduct() );
            holder.productImg.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher));

        }
        return vi;
    }



}
