package org.sejong.sulgamewiki.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sejong.sulgamewiki.util.auth.CustomUserDetails;
import org.sejong.sulgamewiki.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.access-exp-time}")
  private Long accessTokenExpTime;

  @Value("${jwt.refresh-exp-time}")
  private Long refreshTokenExpTime;

  private final String ROLE ="role";

  private final MemberService memberService;

  public String createAccessToken(CustomUserDetails customUserDetails){
    return createToken(customUserDetails, accessTokenExpTime);
  }

  public String createRefreshToken(CustomUserDetails customUserDetails){
    return createToken(customUserDetails, refreshTokenExpTime);
  }

  private String createToken(CustomUserDetails customUserDetails, Long expiredAt) {

    Date now = new Date();
    Map<String, Object> headers = new HashMap<>();
    headers.put("typ", "JWT");


    return Jwts.builder()
        .header()
        .add(headers)
        .and()
        .issuer("arom")
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expiredAt ))
        // ROLE, MEMBERID
        .subject(customUserDetails.getUsername()) // 단일값 , 공식 클레임
        .claim(ROLE, customUserDetails.getMember().getRole())
        .signWith(getSigningKey())
        .compact();

  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException |
             MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);

    Set<SimpleGrantedAuthority> authorities
        = Collections.singleton(new SimpleGrantedAuthority(claims.get(ROLE, String.class)));

    CustomUserDetails customUserDetails
        = memberService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(customUserDetails, token, authorities);
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }


  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }


}
