package io.agileintelligence.ppmtool.security;

import io.agileintelligence.ppmtool.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.agileintelligence.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.agileintelligence.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    //generate the token. lecture 85
    public String generateToken(Authentication authentication) {
        User user = (User)authentication.getPrincipal();   //user has been authenticated
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullname", user.getFullname());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    //validate the token
    //lecture86
    public boolean validateToken(String token) {
        if (token == null)
            return false;

        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            System.out.println("Invalid JWT token!");
        }

        return false;
    }

    //get user id from token
    //lecture86

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}
