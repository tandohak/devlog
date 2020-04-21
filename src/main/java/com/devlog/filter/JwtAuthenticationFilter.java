package com.devlog.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlog.domain.LoginViewModel;
import com.devlog.domain.UserPrincipalDetail;
import com.devlog.properties.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtProperties jwtProperties;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            LoginViewModel credentials = null;

            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginViewModel.class);

            // Create login token
            UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), new ArrayList<>());

            Authentication auth = getAuthenticationManager().authenticate(authenticationToken);

            return auth;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipalDetail principal = (UserPrincipalDetail) authResult.getPrincipal();

        log.info("SuccessfulAuthentication principal {}", principal);

        // Create JWT Token
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret().getBytes()));

        // Add token in response
        response.addHeader(jwtProperties.getHeaderString(), jwtProperties.getTokenPrefix() + token);

    }
}
