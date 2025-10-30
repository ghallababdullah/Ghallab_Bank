package com.ghallab.Ghallab_Bank.security;

import com.ghallab.Ghallab_Bank.exceptions.CustomAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService ;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint ;

    private final CustomUserDetailsService customUserDetailsService ;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null){
            String email ;
            try {
                email=tokenService.getUsernameFromToken(token);

            } catch (Exception e) {
                log.error("Exception occured while extracting username from the token ");
                AuthenticationException authenticationException = new BadCredentialsException(e.getMessage());
                customAuthenticationEntryPoint.commence(request, response, authenticationException);
                return;
            }
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email) ;

            if (StringUtils.hasText(email)&&tokenService.isTokenValid(token,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails ,
                        null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        try {
            filterChain.doFilter(request, response);
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
    public String getTokenFromRequest (HttpServletRequest request){
        String tokenwithBearer = request.getHeader("Authorization");
        if (tokenwithBearer != null && tokenwithBearer.startsWith("Bearer")){
            return  tokenwithBearer. substring(7) ;

        }
        return null ;

    }
}
