package com.slice_group.telollevoparaguana.ListProductLazy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.slice_group.telollevoparaguana.GUIS.CircularImageView;
import com.slice_group.telollevoparaguana.MainActivity;
import com.slice_group.telollevoparaguana.ProductModel;
import com.slice_group.telollevoparaguana.R;
import com.slice_group.telollevoparaguana.RoundedAvatarDrawable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pancracio on 21/07/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class LazyImageLoadAdapter extends BaseAdapter implements OnClickListener {

    private Activity activity;
    public static ArrayList data;
    private static LayoutInflater inflater=null;
    private ImageLoader imageLoader;
    private ProductModel listValues = null;
    public static Boolean checkList[];
    private View vi;

    public LazyImageLoadAdapter(Activity activity, ArrayList d) {
        this.activity = activity;
        data=d;
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView plato;
        public TextView rest;
        public TextView descripcion;
        public TextView precio;
        public ImageView imagen;
        public FloatingActionButton addToCar;
    }

    public View getView(int position, final View convertView, ViewGroup parent) {

        vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_products, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.plato = (TextView) vi.findViewById(R.id.productName);
            holder.rest = (TextView)vi.findViewById(R.id.siteName);
            holder.descripcion = (TextView)vi.findViewById(R.id.desc);
            holder.precio = (TextView)vi.findViewById(R.id.productPrice);
            holder.imagen = (ImageView)vi.findViewById(R.id.productImg);
            holder.precio.setShadowLayer(20, 0, 0, Color.BLACK);
            holder.addToCar = (FloatingActionButton)vi.findViewById(R.id.addToCar);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.plato.setText("No Data");

        }
        else {
            listValues = null;
            listValues = (ProductModel) data.get(position);

            checkList = new Boolean[data.size()];

            holder.plato.setText(listValues.getNomSite());
            holder.rest.setText(listValues.getNomProduct().toUpperCase());
            holder.descripcion.setText(listValues.getDescription());
            holder.precio.setText(listValues.getValProduct());

            final ImageView image = holder.imagen;

            //DisplayImage function from ImageLoader Class
            imageLoader.DisplayImage(activity, listValues.getImgProduct(), image);

            checkList[position] = listValues.getCheck();

        }

        holder.addToCar.setOnClickListener(new OnItemClickListener(position, holder.addToCar, checkList));

        holder.rest.setOnClickListener(new OnSelectRestaurant());

        //vi.setOnClickListener(new OnItemClickListener(position, vi, holder.imagen, holder.imgCheck, checkList));
        return vi;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    private class OnSelectRestaurant implements  OnClickListener{

        OnSelectRestaurant(){

        }

        @Override
        public void onClick(View v) {
            MainActivity sct = (MainActivity)activity;
            sct.onRestClick();
        }
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;
        private FloatingActionButton mAddToCar;
        private Boolean mCheckList[];

        OnItemClickListener(int position, FloatingActionButton addToCar, Boolean checkList[]){
            mPosition = position;
            mCheckList = checkList;
            mAddToCar = addToCar;
        }

        @Override
        public void onClick(View arg0) {
            /*ViewHolder holder = new ViewHolder();
            holder.imagen = (ImageView) vi.findViewById(R.id.productImg);*/
            MainActivity sct = (MainActivity)activity;
            if(mCheckList[mPosition]) {
                sct.onItemClick(mPosition, mAddToCar, mCheckList);
                mCheckList[mPosition] = false;
            }else{
                sct.onItemClick(mPosition, mAddToCar, mCheckList);
                mCheckList[mPosition] = true;
            }

        }
    }
}