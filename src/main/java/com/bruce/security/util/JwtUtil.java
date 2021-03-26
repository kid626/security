package com.bruce.security.util;

import com.bruce.security.exceptions.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {


    public static final String SIGNING_KEY = "spring-security-@Jayyet!&Secret^#avwfta";

    public static String encode(String subject, Long exp) {
        String token = Jwts.builder()
            .setSubject(subject)
            .setExpiration(new Date(exp * 1000))
            .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
            .compact();
        return token;
    }

    public static String decode(String authorization) {
        try {
            String jwtToken = Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(authorization)
                .getBody()
                .getSubject();
            return jwtToken;
        } catch (ExpiredJwtException e) {
            log.error("Token已过期: {} " + authorization, e);
            throw new InvalidTokenException("token 过期!");
        }
    }
}
