package com.slice_group.telollevoparaguana;

/**
 * Created by pancracio on 17/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class ProductModel {

    private String nomSite="";
    private String imgProduct="";
    private String nomProduct="";
    private String valProduct="";
    private String description="";
    private Boolean check=false;

    public ProductModel(String nomSite, String description, String imgProduct, String nomProduct, String valProduct, Boolean check){
        this.nomSite = nomSite;
        this.imgProduct = imgProduct;
        this.nomProduct = nomProduct;
        this.valProduct = valProduct;
        this.check = check;
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setValProduct(String valProduct){
        this.valProduct = valProduct;
    }

    public String getValProduct(){
        return this.valProduct;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

}
