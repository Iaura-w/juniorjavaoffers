package com.javaoffers.security.jwt;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ERROR_MESSAGE = "Token cannot be trusted {}";
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            if (!isAuthorizationHeaderValid(authorizationHeader)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.replace(TOKEN_PREFIX, "");
            if (isTokenValid(token)) {
                String username = jwtUtils.getUsernameFromJwtToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String token) {
        return !Strings.isNullOrEmpty(token) && jwtUtils.isJwtTokenValid(token);
    }

    private static boolean isAuthorizationHeaderValid(String authorizationHeader) {
        return !Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX);
    }
}