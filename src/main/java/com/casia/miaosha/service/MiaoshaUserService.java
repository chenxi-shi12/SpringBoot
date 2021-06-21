package com.casia.miaosha.service;

import com.casia.miaosha.dao.MiaoshaUserDao;
import com.casia.miaosha.domain.MiaoshaUser;
import com.casia.miaosha.exception.GlobalException;
import com.casia.miaosha.redis.MiaoshaUserKey;
import com.casia.miaosha.redis.RedisService;
import com.casia.miaosha.result.CodeMsg;
import com.casia.miaosha.util.MD5Util;
import com.casia.miaosha.util.UUIDUtil;
import com.casia.miaosha.vo.LoginVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

/*不要调别人的Dao，因为Service里可能有缓存没更新*/

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(Long id) {
        return miaoshaUserDao.getById(id);
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoshaUser user = getById(Long.parseLong(mobile));
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.inputPassToDBPass(password, saltDB);
//        System.out.println(calcPass);
//        if (!calcPass.equals(dbPass)) {
//            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
//        }
        if (user != null) {
            addCookie(response, user);
        }
        return true;
    }

    public MiaoshaUser getById(long id) {
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null) {
            return user;
        }

        user = miaoshaUserDao.getById(id);
        if(user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }

        return user;
    }

    public boolean updatePassword(String token, long id, String passwordNew) {
        MiaoshaUser user = miaoshaUserDao.getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        user.setPassword(toBeUpdate.getPassword());

        redisService.set(MiaoshaUserKey.token,token,user);
        redisService.delete(MiaoshaUserKey.getById,""+id);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmptyOrWhitespaceOnly(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(response, user);
        }
        return redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

    }

    private void addCookie(HttpServletResponse response, MiaoshaUser user){
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
