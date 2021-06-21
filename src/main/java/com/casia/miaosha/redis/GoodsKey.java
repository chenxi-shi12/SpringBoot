package com.casia.miaosha.redis;

public class GoodsKey extends BasePrefix{

    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(20,"goodslist");

    public static GoodsKey getGoodsDetail = new GoodsKey(20,"goodsdetail");
}
