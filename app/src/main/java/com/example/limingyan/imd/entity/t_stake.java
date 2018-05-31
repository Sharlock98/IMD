package com.example.limingyan.imd.entity;

import cn.bmob.v3.BmobObject;

public class t_stake extends BmobObject {
    private int stakeId;
    private int goodsId;

    public int getStakeId() {
        return stakeId;
    }

    public void setStakeId(int stakeId) {
        this.stakeId = stakeId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
