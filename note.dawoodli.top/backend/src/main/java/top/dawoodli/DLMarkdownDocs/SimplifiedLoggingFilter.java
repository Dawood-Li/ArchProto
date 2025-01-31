package top.dawoodli.DLMarkdownDocs;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
@Component
public class SimplifiedLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            
            logRequestAndResponseDetails(requestWrapper, responseWrapper, startTime);
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestAndResponseDetails(ContentCachingRequestWrapper requestWrapper,
                                              ContentCachingResponseWrapper responseWrapper,
                                              long startTime) throws IOException {
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8).trim();
        String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8).trim();
        
        System.out.println("REQ: [" + requestWrapper.getMethod() + "] " + requestWrapper.getRequestURI() +
                ", Body: " + requestBody);
        System.out.println("RES: [" + HttpStatus.valueOf(responseWrapper.getStatus()) + "]" +
                ", Time Taken: " + elapsedTime + "ms, Body: " + responseBody);
    }
}