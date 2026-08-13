package com.aioveu.common.security.resource.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class DebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        log.error("✅ DEBUG FILTER ACTIVE, URI={}", request.getRequestURI());
        System.err.println("🚨 DEBUG FILTER");
        System.err.println("URI: " + request.getRequestURI());
        System.err.println("Authorization: " + request.getHeader("Authorization"));
        System.err.println("Authentication: " + auth);

        chain.doFilter(request, response);
    }


}
