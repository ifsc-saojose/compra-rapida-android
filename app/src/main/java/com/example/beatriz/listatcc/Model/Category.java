package com.example.beatriz.listatcc.Model;

/**
 * Created by Beatriz on 08/08/2016.
 */
public class Category {
    private String name;
    private long id;
    private boolean isVisible;

    public Category() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return isVisible;
    }

}
