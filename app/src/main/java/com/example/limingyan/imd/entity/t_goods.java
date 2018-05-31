package com.example.limingyan.imd.entity;

import cn.bmob.v3.BmobObject;

public class t_goods extends BmobObject {
    private String images;
    private String originalCost;
    private int goodsId;
    private String goodsName;
    private String origin;
    private String coding;
    private String size;
    private String barcode;
    private String vipPrice;
    private float currentPrice;
    private String thePriceReason;
    private int timeLimitOfThePrice;
    private String type;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(String originalCost) {
        this.originalCost = originalCost;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getThePriceReason() {
        return thePriceReason;
    }

    public void setThePriceReason(String thePriceReason) {
        this.thePriceReason = thePriceReason;
    }

    public int getTimeLimitOfThePrice() {
        return timeLimitOfThePrice;
    }

    public void setTimeLimitOfThePrice(int timeLimitOfThePrice) {
        this.timeLimitOfThePrice = timeLimitOfThePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
