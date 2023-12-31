package com.taskmanagement.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {

    String email;
    String password;
    Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails fromUserEntityToCustomUserDetails(User user) {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.email = user.getEmail();
        userDetails.password = user.getPassword();
        userDetails.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
