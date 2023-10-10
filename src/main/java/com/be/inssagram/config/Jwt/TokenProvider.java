package com.be.inssagram.config.Jwt;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
    private final MemberRepository memberRepository;
    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        var claims = this.parsedClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String jwt) {
        Member member = memberRepository.findByEmail(getEmailFromToken(jwt));
        return new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());
    }

    public String getEmailFromToken(String token) {
        String jwtToken = token.substring(7);
        return parsedClaims(jwtToken).getSubject();
    }

    private Claims parsedClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
