package top.dawoodli.DLMarkdownDocs.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.security.SignatureException;
import top.dawoodli.DLMarkdownDocs.Request.LoginRequest;
import top.dawoodli.DLMarkdownDocs.Request.RegisterRequest;
import top.dawoodli.DLMarkdownDocs.Service.AuthService;
import top.dawoodli.DLMarkdownDocs.Service.TokenService;

@RestController
@RequestMapping("/api/Auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/Register")
    public Map<String, Object> register(@RequestBody RegisterRequest req) {
        Long ID = authService.register(req.Email, req.Password, req.Name);
        if (ID == null) {
            return Map.of("err", "注册失败");
        }
        Map<String, Object> userInfo = Map.of(
            "ID", ID,
            "Email", req.Email,
            "Name", req.Name,
            "Role", "user"
        );
        return Map.of(
            "msg", "注册成功",
            "userInfo", userInfo,
            "token", tokenService.genToken(userInfo)
        );
    }

    @PostMapping("/Login")
    public Map<String, Object> login(@RequestBody LoginRequest req) {
        Map<String, Object> userInfo = authService.login(req.Email, req.Password);
        if (userInfo == null) {
            return Map.of("err", "登录失败");
        }
        return Map.of(
            "msg", "登录成功",
            "userInfo", userInfo,
            "token", tokenService.genToken(userInfo)
        );
    }

    // TODO: 临时接口
    // 后续改用Interceptor + Axios拦截器自动处理token续签
    // 并本地缓存token解析结果减少重复解析消耗
    @PostMapping("/RefreshToken")
    public Map<String, Object> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader)
            throws SignatureException, Exception
    {
        return null;
        // if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        //     throw new SignatureException("Token格式错误");
        // }
        
        // String token = authHeader.substring(7);
        // try {
        //     // Token未过期 直接提取claims
        //     Map<String, Object> userInfo = tokenService.parseToken(token);
        //     return Map.of(
        //         "msg", "Token未过期",
        //         "userInfo", userInfo,
        //         "token", token
        //     );
        // } catch (ExpiredJwtException ex) {
        //     // Token过期 从异常中提取claims
        //     Map<String, Object> userInfo = ex.getClaims();
        //     return Map.of(
        //         "msg", "Token已续签",
        //         "userInfo", userInfo,
        //         "token", token
        //     );
        // }
    }
}
