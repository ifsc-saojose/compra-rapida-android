package com.example.beatriz.listatcc.NewList;

/**
 * Created by Beatriz on 23/07/2016.
 */
public class ProductItem {
    private long id;
    private int image;
    private String title;


    public ProductItem(int image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
    public  ProductItem(){

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
