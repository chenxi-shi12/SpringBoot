package com.casia.miaosha.service;

import com.casia.miaosha.domain.Goods;
import com.casia.miaosha.domain.MiaoshaUser;
import com.casia.miaosha.domain.OrderInfo;
import com.casia.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        Goods g = new Goods();
        g.setId(goods.getId());
        g.setGoodsStock(goods.getStockCount());
        goodsService.reduceStock(goods.getId());
        OrderInfo orderInfo = orderService.createOrder(user, goods);
        return orderInfo;
    }
}
