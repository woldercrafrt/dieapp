package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        
        errorDetails.put("status", statusCode);
        errorDetails.put("error", "Error occurred");
        errorDetails.put("message", errorMessage != null ? errorMessage : "Unknown error");
        errorDetails.put("path", requestUri);
        
        if (exception != null) {
            errorDetails.put("exception", exception.getClass().getName());
        }
        
        // Check if this is an OAuth2 callback error
        String queryString = request.getQueryString();
        if (queryString != null && queryString.contains("error=")) {
            errorDetails.put("oauth2_error", true);
            errorDetails.put("oauth2_error_description", "OAuth2 authentication failed");
        }
        
        HttpStatus status = statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;
        
        return ResponseEntity.status(status).body(errorDetails);
    }
} 