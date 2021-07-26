package nirmalya.aathithya.webmodule.user.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import nirmalya.aathithya.webmodule.user.model.User;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails{

    private User user;
 
    public CustomUserDetails(User user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        
        return authorities;
    }
 
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUserName();
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
        return user.getUserStatus();
    }
    
}