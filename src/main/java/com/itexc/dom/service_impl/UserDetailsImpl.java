package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private DBSession dbSession;
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private String password;



    public UserDetailsImpl(String id, String email, String password,
                           Collection<? extends GrantedAuthority> authorities, DBSession dbSession, User user) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.dbSession = dbSession;
        this.user  = user;
    }

    public static UserDetailsImpl buildFromUser(User user) {
        Collection<GrantedAuthority> authorities = getAuthorities(user);
        return new UserDetailsImpl(String.valueOf(user.getId()), user.getEmailAddress(), user.getPassword().getCredential(), authorities, null, user);

    }

    public static UserDetailsImpl buildFromUser(User user, DBSession dbSession) {
        Collection<GrantedAuthority> authorities = getAuthorities(user);
        return new UserDetailsImpl(String.valueOf(user.getId()), user.getEmailAddress(), user.getPassword().getCredential(), authorities, dbSession, user);

    }

    private static Collection<GrantedAuthority> getAuthorities(User user) {
        return user.getProfile().getPrivileges().stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public DBSession getDbSession() {
        return dbSession;
    }

    public void setDbSession(DBSession dbSession) {
        this.dbSession = dbSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
