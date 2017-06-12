package com.example.beatriz.listatcc.Model;

/**
 * Created by Beatriz on 05/06/2016.
 */
public class Mercado {
    private String id;
    private String name;
    private String rua;
    private String cidade;
    private Double lat, longt;

    public Mercado() {
    }

    public Mercado(String id, String name, String rua, Double lat, Double longt) {
        this.id = id;
        this.name = name;
        this.rua = rua;
        this.lat = lat;
        this.longt = longt;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongt() {
        return longt;
    }

    public void setLongt(Double longt) {
        this.longt = longt;
    }
}
