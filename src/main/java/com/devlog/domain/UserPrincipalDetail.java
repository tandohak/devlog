package com.devlog.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserPrincipalDetail implements UserDetails {
    private String id;
    private String password;
    private String authority;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(authority));
        return auth;
    }

    @Override
    public String getUsername() {
        return id;
    }

}
