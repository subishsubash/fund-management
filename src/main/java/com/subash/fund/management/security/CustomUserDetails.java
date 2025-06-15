package com.subash.fund.management.security;

import com.subash.fund.management.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of {@link UserDetails} that wraps the {@link User} entity.
 * <p>
 * This is used by Spring Security for authentication and authorization.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Constructs a {@code CustomUserDetails} instance using the provided {@link User} entity.
     *
     * @param user the user entity to wrap
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user. In this case, it's derived from the user's role.
     *
     * @return a list containing the user's authority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return the password hash
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
