package com.slice_group.telollevoparaguana;

/**
 * Created by pancracio on 19/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class DrawerModel {

    private String title;
    private int icon;

    public DrawerModel(){}

    public DrawerModel(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
