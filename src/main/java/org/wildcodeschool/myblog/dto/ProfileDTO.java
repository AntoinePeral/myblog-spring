package org.wildcodeschool.myblog.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ProfileDTO {
    private String email;
    private GrantedAuthority[] roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GrantedAuthority[] getRoles() {
        return roles;
    }

    public void setRoles(GrantedAuthority[] roles) {
        this.roles = roles;
    }
}
