package org.izomp.transaction.manager.security;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.entities.User;
import org.izomp.transaction.manager.repository.UserRepository;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(UUID id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        return UserPrincipal.create(user);
    }
}

