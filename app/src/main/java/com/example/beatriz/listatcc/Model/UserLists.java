package com.example.beatriz.listatcc.Model;

public class UserLists {

    public String id;
    public String listName;
    public String owner;


    public UserLists() {

    }

    public UserLists(String id, String listName, String owner) {
        this.id = id;
        this.listName = listName;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public String getOwner() {
        return owner;
    }


}
