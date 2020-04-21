package com.devlog.config;

import com.devlog.filter.JwtAuthenticationFilter;
import com.devlog.filter.JwtAuthorizationFilter;
import com.devlog.properties.JwtProperties;
import com.devlog.repository.UserRepository;
import com.devlog.service.UserPrincipalDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final UserPrincipalDetailService userPrincipalDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() throws Exception {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter();
        authenticationFilter.setJwtProperties(jwtProperties);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter authorizationFilter() throws Exception {
        JwtAuthorizationFilter  authorizationFilter = new JwtAuthorizationFilter(authenticationManagerBean());
        authorizationFilter.setJwtProperties(jwtProperties);
        return authorizationFilter;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // remove csrf and state in session because in jwt we do not need them
            .csrf().disable()
            //  Stateless 로 변경
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(authenticationFilter())

            .authorizeRequests()
                // configure access rules
                .antMatchers("/login", "/test/all", "/").permitAll()
                .antMatchers("/test/manager").hasAuthority("MANAGER")
                .antMatchers("/test/admin").hasAuthority("ADMIN")
                .anyRequest().authenticated();

    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
