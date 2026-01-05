package com.example.invc_proj.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserPrincipal implements UserDetails {


    private final Integer Id;
    private final String Username;
    private final String Password;
    private final String EmailId;
    private final Collection<? extends GrantedAuthority> authorities;

//    public UserPrincipal (User User) {this.User = User; }

    public UserPrincipal(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        this.Id = user.getId();
        this.Username = user.getUsername();
        this.Password = user.getPassword();
        this.EmailId = user.getEmailId();

        // Safely extract role authority (defensive against null role)
        String roleName = "USER"; // fallback default
        if (user.getRole() != null && user.getRole().getRoleName() != null) {
            roleName = "ROLE_" + user.getRole().getRoleName().toUpperCase(); // normalize
        }

        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(roleName)
        );
    }

   /* @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("user"));
    }*/

   /* @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + User.getRole().getRoleName()));
    }
    */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // immutable, pre-computed
    }


    @Override
    public String getPassword() {
       // System.out.println("get password");
        return Password;
    }

    @Override
    public String getUsername() {
        //System.out.println("get username");
        return Username;
    }

    public Integer getUserId()
    {
        return Id;
    }

    public String getEmailId()
    {
        return EmailId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
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
