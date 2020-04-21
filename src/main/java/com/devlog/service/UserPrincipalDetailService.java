package com.devlog.service;

import com.devlog.domain.UserPrincipalDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPrincipalDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User Detail Service : LoadUserByUserName");

        return UserPrincipalDetail.builder()
                                  .id(username)
                                  .password(new BCryptPasswordEncoder().encode("password"))
                                  .enabled(true)
                                  .authority("MANAGER")
                                  .accountNonExpired(true)
                                  .accountNonLocked(true)
                                  .credentialsNonExpired(true)
                                  .build();
    }
}
