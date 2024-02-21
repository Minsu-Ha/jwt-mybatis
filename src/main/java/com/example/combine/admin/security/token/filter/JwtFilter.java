package com.example.combine.admin.security.token.filter;

import com.example.combine.admin.security.token.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * 매 요청마다 Filtering
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userAuthenticationService;
    /**
     * 요청의 인증 헤더에서 Token을 가져와 인증 진행
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt = "";

        // 인증 Header가 없거나 Bearer로 시작 하지 않으면 필터링
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Authorization Header -> Bearer [token]
        jwt = authHeader.substring(7);
        String userId = jwtService.extractUsername(jwt);
        // refresh Token
        Date expDate = jwtService.extractExpiration(jwt);
        
        long remainTime = expDate.getTime() - System.currentTimeMillis();
        // Token 5분 미만 일때 refresh
        if (remainTime < (1000 * 60 *5)) {
            log.info("Refresh Token!!");
            UserDetails userDetails = this.userAuthenticationService.loadUserByUsername(userId);
            String newToken = jwtService.refreshToken(userDetails);
            response.setHeader("Authorization", "Bearer " + newToken);
            setTokenInContext(userDetails, request);
        }

        // userId가 없거나 ContextHolder에 Authentication이 없으면 Token 정보를 SecurityContextHolder에 저장
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            UserDetails userDetails = this.userAuthenticationService.loadUserByUsername(userId);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                setTokenInContext(userDetails, request);

            }
        }

        filterChain.doFilter(request, response);
    }

    private void setTokenInContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
