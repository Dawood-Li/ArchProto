package top.dawoodli.DLMarkdownDocs.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    // 密钥
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // token过期时间 单位 毫秒
    private static final long EXPIRE_TIME_MS = 1000 * 60 * 60 * 2; // 2小时
    
    // 生成token
    public String genToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME_MS))
            .signWith(SECRET_KEY)
            .compact();
    }

    // 解析token
    public Map<String, Object> parseToken(String token)
        throws ExpiredJwtException, SignatureException
    {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            throw new SignatureException("Token无效");
        }
    }
}
