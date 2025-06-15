package com.subash.fund.management.security;

import com.subash.fund.management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} used by Spring Security
 * to load user-specific data during authentication.
 * <p>
 * This service fetches a user from the database and wraps it in {@link CustomUserDetails}
 * to be used by the Spring Security context.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Constructs the service with the required {@link UserRepository} dependency.
     *
     * @param userRepository repository for accessing user data
     */
    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user by username from the database.
     * <p>
     * Throws {@link UsernameNotFoundException} if the user does not exist.
     *
     * @param username the username to look up
     * @return the user details required by Spring Security
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
