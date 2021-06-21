package com.casia.miaosha.redis;

import com.casia.miaosha.domain.MiaoshaUser;

public class MiaoshaUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600;

    public MiaoshaUserKey(int expire, String prefix) {
        super(expire, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");

    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");
}
