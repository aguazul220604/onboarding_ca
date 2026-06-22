package com.casystem.onboarding_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "C4S1st3m4s_Onb04rd1ng_S3cur3_JWT_K3y_2026";
    private static final String ISSUER = "casystem-auth";
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String validateTokenAndRetrieveSubject(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer(ISSUER)
                .build()
                .verify(token);
        return jwt.getSubject();
    }
}