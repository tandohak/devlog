package com.devlog.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devlog.properties.JwtProperties;
import com.devlog.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Getter
@Setter
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProperties jwtProperties;
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            log.info("DO FILTER");
            // Read thw authorizationHeader, where the JWT Token should be
            String header = request.getHeader(jwtProperties.getHeaderString());

            log.info("Header {}", header);

            if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
                chain.doFilter(request, response);
            } else {
                Authentication authentication = getUsernamePasswordAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Continue filter execution
                chain.doFilter(request, response);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getHeaderString());

        log.info("token {}", token);

        if (token != null) {
           // parse the token and validate it.
            String username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret().getBytes()))
                    .build()
                    .verify(token.replace(jwtProperties.getTokenPrefix(), ""))
                    .getSubject();

            log.info("username {}", username);

            if(username != null){
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }

}
