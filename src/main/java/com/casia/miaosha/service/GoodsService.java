package com.casia.miaosha.service;

import com.casia.miaosha.dao.GoodsDao;
import com.casia.miaosha.domain.MiaoshaGoods;
import com.casia.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long id) {
        return goodsDao.getGoodsById(id);
    }

    public int reduceStock(long goodsId) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsId);
        return goodsDao.reduceStock(g);
    }
}
