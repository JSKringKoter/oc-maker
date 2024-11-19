package com.ocmaker.server.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.ocmaker.common.result.ErrorTypes;
import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果请求的是动态方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求的url: {}", url);

        //判断请求中是否含有login，如果包含，则放行
        if (url.contains("login")) {
            log.info("用户登陆，放行");
            return true;
        }

        //获取请求头中的authorization
        String authorization = request.getHeader("authorization");

        //判断令牌是否存在
        if (!StringUtils.hasLength(authorization)) {
            log.info("请求头token为空，用户未登录");
            Result error = Result.error(ErrorTypes.NOT_LOGIN_ERROR);
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            response.setStatus(401);
            return false;
        }

        //解析authorization中的jwt
        String jwt = null;
        log.info(authorization);
        if (authorization.startsWith("Bearer")) {
            jwt = authorization.substring(7);
        }

        //解析token，如果解析失败，返回错误结果
        try {
            Claims claims = JwtUtils.parseJwt(jwt);
            //将user_uid存在请求体中
            Integer userUid = claims.get("user_uid", Integer.class);
            request.setAttribute("user_uid", userUid);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("令牌解析失败，返回未登录的错误信息");
            Result error = Result.error(ErrorTypes.NOT_LOGIN_ERROR);
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            response.setStatus(401);
            return false;
        }

        log.info("令牌合法， 放行");
        return true;
    }

}
