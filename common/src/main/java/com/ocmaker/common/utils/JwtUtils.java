package com.ocmaker.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Date;
import java.util.Map;

public class JwtUtils {
    private static String signKey = "ocmaker";
    private static Long expire = 43200000L;

    /**
     * 生成jwt令牌
     * @param claims 负载中储存的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims) {

        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt   JWT令牌
     * @return      负载中的内容
     */
    public static Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
