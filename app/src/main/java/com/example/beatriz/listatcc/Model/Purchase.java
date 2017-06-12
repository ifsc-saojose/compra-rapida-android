package com.example.beatriz.listatcc.Model;

/**
 * Created by Beatriz on 06/09/2016.
 */
public class Purchase {

    private long id;
    private String name;
    private long fromList;
    private String marketId;
    private int active;
    private long date;

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

    public long getFromList() {
        return fromList;
    }

    public void setFromList(long fromList) {
        this.fromList = fromList;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
