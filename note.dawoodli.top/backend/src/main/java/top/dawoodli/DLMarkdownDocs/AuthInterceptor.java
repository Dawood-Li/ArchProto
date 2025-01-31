package top.dawoodli.DLMarkdownDocs;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.dawoodli.DLMarkdownDocs.Exception.AccessDeniedException;
import top.dawoodli.DLMarkdownDocs.Exception.AuthenticationException;
import top.dawoodli.DLMarkdownDocs.Service.TokenService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    private String[] allowedRoles;

    public AuthInterceptor(String[] allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        
        // 检查token是否存在
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new AuthenticationException("请先登录");
        }

        // 解析token
        String token = auth.substring(7);
        Map<String, Object> userInfo = tokenService.parseToken(token);
       
        // 检查权限是否正确
        if (! Arrays.asList(allowedRoles).contains(userInfo.get("role"))) {
            throw new AccessDeniedException("权限不足");
        }

        // 携带userInfo信息 避免二次解析token
        request.setAttribute("userInfo", userInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Do nothing
    }
}




