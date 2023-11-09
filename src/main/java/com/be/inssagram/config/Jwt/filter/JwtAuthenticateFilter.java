package com.be.inssagram.config.Jwt.filter;

import com.be.inssagram.config.Jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Check if the request URI matches a pattern where token is not required
        if (isPublicPath(requestURI)) {
            filterChain.doFilter(request, response);  // Allow the request to proceed without a token
            return;
        }

        String token = this.resolveToken(request);

        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("토큰 정보가 없거나 만료되었습니다");
            return;
        }
        filterChain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "").trim();
        }
        return null;
    }

    private boolean isPublicPath(String requestURI) {
        // Define the public URL patterns that do not require a token
        List<String> publicPaths = Arrays.asList("/signup", "/signin", "/search", "/member/detail", "/"
                , "/post/search-detail", "/post/all", "/ws-stomp", "/exchange", "/pub");

        // Check if the request URI matches a public path
        return publicPaths.stream().anyMatch(requestURI::startsWith);
    }
}