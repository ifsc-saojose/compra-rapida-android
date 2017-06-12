package com.example.beatriz.listatcc.Model;

import com.example.beatriz.listatcc.NewList.ProductItem;

import java.util.List;

/**
 * Created by Beatriz on 16/07/2016.
 */
public class Lists {

    private long id;
    private String name;
    private long date;
    private List<ProductItem> productItemList;

    public Lists() {
    }

    public Lists(String name, long date) {
        this.name = name;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductItem> getProductItemList() {
        return productItemList;
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }
}
