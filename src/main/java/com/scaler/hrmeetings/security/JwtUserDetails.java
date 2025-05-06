package com.scaler.hrmeetings.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final Long callerId;
    private final String role;

    public JwtUserDetails(Long callerId, String role) {
        this.callerId = callerId;
        this.role = role;
    }

    public Long getCallerId() {
        return callerId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override public String getUsername() { return String.valueOf(callerId); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getPassword() { return null; }
}
