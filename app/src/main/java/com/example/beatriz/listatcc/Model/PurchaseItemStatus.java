package com.example.beatriz.listatcc.Model;

/**
 * Created by Beatriz on 10/09/2016.
 */
public class PurchaseItemStatus {

    private long id;
    private long purchaseId;
    private long productId;
    private int status;
    private long gotTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getGotTime() {
        return gotTime;
    }

    public void setGotTime(long gotTime) {
        this.gotTime = gotTime;
    }
}
