package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pancracio on 17/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class ProductAdapter extends BaseAdapter implements OnClickListener{

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

        public TextView siteName;
        public TextView prodctName;
        public TextView productPrice;
        public RelativeLayout editButton;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            vi = inflater.inflate(R.layout.item_detail_history, null);

            holder = new ViewHolder();
            holder.siteName = (TextView) vi.findViewById(R.id.n_item);
            holder.prodctName = (TextView)vi.findViewById(R.id.n_rest);
            holder.productPrice = (TextView)vi.findViewById(R.id.price);
            holder.editButton = (RelativeLayout)vi.findViewById(R.id.editButton);

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
            holder.productPrice.setText( listValues.getValProduct() );

        }

        holder.editButton.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

    @Override
    public void onClick(View v) {

    }

    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            ShoppingCarActivity sct = (ShoppingCarActivity)activity;
            sct.onItemClick(mPosition);


        }
    }
}
