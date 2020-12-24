package org.izomp.transaction.manager.auth.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.izomp.transaction.manager.auth.model.LoginRequestDto;
import org.izomp.transaction.manager.auth.model.SignUpRequestDto;
import org.izomp.transaction.manager.auth.service.AuthService;
import org.izomp.transaction.manager.entities.User;
import org.izomp.transaction.manager.repository.UserRepository;
import org.izomp.transaction.manager.security.TokenProvider;
import org.izomp.transaction.manager.security.model.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public String login(LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }

    @Override
    @Transactional
    public String register(SignUpRequestDto signUpRequestDto) {

        if (userRepository.getUserByLogin(signUpRequestDto.getLogin()).isPresent()) {
            throw new RuntimeException("LOGIN_ALREADY_IN_USE");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setLogin(signUpRequestDto.getLogin());
        user.setName(signUpRequestDto.getName());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setUserRole(signUpRequestDto.getUserRole());
        user = userRepository.save(user);

        return tokenProvider.createToken(UserPrincipal.create(user));
    }
}
