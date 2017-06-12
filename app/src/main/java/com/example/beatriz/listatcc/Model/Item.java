package com.example.beatriz.listatcc.Model;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Item {

    private String name;
    private long categoryId;
    private long id;

    private int nodeId;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String owner;
    private int status;

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item() {

    }

    public Item(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int setNodeId) {
        this.nodeId = setNodeId;
    }

}
