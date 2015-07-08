package com.slice_group.telollevoparaguana;

/**
 * Created by pancracio on 17/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class ProductModel {

    private String nomSite="";
    private String imgProduct="";
    private String nomProduct="";
    private String tmpDelivery="";
    private String valProduct="";

    public ProductModel(String nomSite, String imgProduct, String nomProduct, String tmpDelivery, String valProduct){
        this.nomSite = nomSite;
        this.imgProduct = imgProduct;
        this.nomProduct = nomProduct;
        this.tmpDelivery = tmpDelivery;
        this.valProduct = valProduct;
    }

    public void setNomSite(String nomSite){
        this.nomSite = nomSite;
    }

    public String getNomSite(){
        return this.nomSite;
    }

    public void setImgProduct(String imgProduct){
        this.imgProduct = imgProduct;
    }

    public String getImgProduct(){
        return this.imgProduct;
    }

    public void setNomProduct(String nomProduct){
        this.nomProduct = nomProduct;
    }

    public String getNomProduct(){
        return this.nomProduct;
    }

    public void setTmpDelivery(String tmpDelivery){
        this.tmpDelivery = tmpDelivery;
    }

    public String getTmpDelivery(){
        return this.tmpDelivery;
    }

    public void setValProduct(String valProduct){
        this.valProduct = valProduct;
    }

    public String getValProduct(){
        return this.valProduct;
    }

}
