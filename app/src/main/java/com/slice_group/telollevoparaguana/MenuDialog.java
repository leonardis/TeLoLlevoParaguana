package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by pancracio on 19/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class MenuDialog extends Dialog implements View.OnTouchListener, View.OnClickListener {

    private Activity activity;
    private Context context;

    public MenuDialog(Activity activity){
        super(activity, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.context = activity.getApplicationContext();

        Log.d("AQUI", "DIALOG2");

        WindowManager.LayoutParams wLayoutParams = getWindow().getAttributes();
        wLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        getWindow().setAttributes(wLayoutParams);

        setTitle(null);
        setOnCancelListener(null);

        View menu = getLayoutInflater().inflate(R.layout.drawer_dialog, null);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                Gravity.LEFT|Gravity.TOP);
        linearLayout.addView(menu, layoutParams);
        addContentView(linearLayout, layoutParams);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }
}
