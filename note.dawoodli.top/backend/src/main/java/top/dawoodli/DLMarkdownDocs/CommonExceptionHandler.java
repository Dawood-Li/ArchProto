package top.dawoodli.DLMarkdownDocs;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import top.dawoodli.DLMarkdownDocs.Exception.AccessDeniedException;
import top.dawoodli.DLMarkdownDocs.Exception.AuthenticationException;
import top.dawoodli.DLMarkdownDocs.Service.LogService;

@RestControllerAdvice
public class CommonExceptionHandler {

    @Autowired
    private LogService logService;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Map<String, Object> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ) {
        logService.error("springboot 参数校验", "参数校验失败: " + ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; ")) + "\n" + getStackTrace(ex));

        return Map.of("err", "参数校验失败: " + 
            ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "))
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public Map<String, Object> handleExpiredJwtException(ExpiredJwtException ex) {
        logService.error("springboot token过期", ex.getMessage() + "\n" + getStackTrace(ex));
        return Map.of("err", "Token已过期，请重新登录。");
    }
    
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public Map<String, Object> handleSignatureException(SignatureException ex) {
        logService.error("springboot token无效", ex.getMessage() + "\n" + getStackTrace(ex));
        return Map.of("err", "Token无效。");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Map<String, Object> handleAuthenticationException(AuthenticationException ex) {
        logService.error("springboot 认证失败", ex.getMessage() + "\n" + getStackTrace(ex));
        return Map.of("err", "未登录，请先登录。");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Map<String, Object> handleAccessDeniedException(AccessDeniedException ex) {
        logService.error("springboot 权限不足", ex.getMessage() + "\n" + getStackTrace(ex));
        return Map.of("err", "权限不足，请联系管理员。");
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(Exception ex) {
        logService.error("springboot 兜底异常", ex.getMessage() + "\n" + getStackTrace(ex));
        return Map.of("err", "业务繁忙，请稍后再试。");
    }

    private String getStackTrace(Throwable ex) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
}
