package com.casia.miaosha.controller;

import com.casia.miaosha.dao.GoodsDao;
import com.casia.miaosha.domain.MiaoshaUser;
import com.casia.miaosha.domain.User;
import com.casia.miaosha.redis.GoodsKey;
import com.casia.miaosha.redis.RedisService;
import com.casia.miaosha.result.Result;
import com.casia.miaosha.service.GoodsService;
import com.casia.miaosha.service.MiaoshaUserService;
import com.casia.miaosha.vo.GoodsDetailVo;
import com.casia.miaosha.vo.GoodsVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.casia.miaosha.redis.GoodsKey.getGoodsDetail;
import static com.casia.miaosha.redis.GoodsKey.getGoodsList;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletResponse response,
                         HttpServletRequest request,
                         Model model,
                         MiaoshaUser user) {
        model.addAttribute("user", user);
//        return "goods_list";
        String html = redisService.get(getGoodsList, "", String.class);
        if (!StringUtils.isEmptyOrWhitespaceOnly(html)) {
            return html;
        }
        List<GoodsVo> goodslist = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodslist);
        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);

        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (!StringUtils.isEmptyOrWhitespaceOnly(html)) {
            redisService.set(getGoodsList, "", html);
        }
        return html;
    }


    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(HttpServletResponse response,
                           HttpServletRequest request,
                           Model model,
                           MiaoshaUser user,
                           @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (startAt - now) / 1000;
        } else if (now > endAt) {
            miaoshaStatus = 2;
        } else {
            miaoshaStatus = 1;
        }
        model.addAttribute("user", user);
        model.addAttribute("goods", goods);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        return "goods_detail";
        String html = redisService.get(getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmptyOrWhitespaceOnly(html)) {
            return html;
        }
        SpringWebContext context = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if (!StringUtils.isEmptyOrWhitespaceOnly(html)) {
            redisService.set(getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletResponse response,
                                        HttpServletRequest request,
                                        Model model,
                                        MiaoshaUser user,
                                        @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (startAt - now) / 1000;
        } else if (now > endAt) {
            miaoshaStatus = 2;
        } else {
            miaoshaStatus = 1;
        }
//        return "goods_detail";
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }

}
