package br.com.fiap.espb.es.ddd.taskManager.infrastructure.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtHelper {

    private final String SECRET = "dçvkn[p1o3i5hv]psdinv[0194hvqwbvn31";
    private final long EXPIRATION_MS = 86400000L;
    private final long EXPIRATION_REFRESH_TOKEN_MS = 604800000L;

    public String generateToken( UserDetails userDetails){
        return Jwts.builder()
                .setSubject( userDetails.getUsername() )
                .setIssuedAt( new Date() )
                .setExpiration( new Date( System.currentTimeMillis() + this.EXPIRATION_MS) )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(this.SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUserName( final String token ){
        return Jwts.parserBuilder().setSigningKey(this.getSigningKey())
                .build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean isTokenValid( final String token, UserDetails userDetails ){
        final String username = this.extractUserName( token );
        return username.equals(userDetails.getUsername()) && !isTokenExpired( token );
    }

    public boolean isTokenExpired( final String token ){
        return Jwts.parserBuilder().setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }

    public String generateRefreshToken(UserDetails userDetails ) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.EXPIRATION_REFRESH_TOKEN_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
