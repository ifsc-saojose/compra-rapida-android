package com.example.beatriz.listatcc.Util;

/**
 * Created by Beatriz on 22/10/2016.
 */
public class RelacionamentoEntreSecoes {

    int id;
    int categoria1;
    int categoria2;
    int purchaseTotal;
    double mediaDasOrdens;
    String mercadoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(int purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public double getMediaDasOrdens() {
        return mediaDasOrdens;
    }

    public void setMediaDasOrdens(double mediaDasOrdens) {
        this.mediaDasOrdens = mediaDasOrdens;
    }

    public String getMercadoId() {
        return mercadoId;
    }

    public void setMercadoId(String mercadoId) {
        this.mercadoId = mercadoId;
    }

    public int getCategoria1() {
        return categoria1;
    }

    public void setCategoria1(int categoria1) {
        this.categoria1 = categoria1;
    }

    public int getCategoria2() {
        return categoria2;
    }

    public void setCategoria2(int categoria2) {
        this.categoria2 = categoria2;
    }

}
