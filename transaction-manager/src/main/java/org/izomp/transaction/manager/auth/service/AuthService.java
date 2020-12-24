package org.izomp.transaction.manager.auth.service;

import org.izomp.transaction.manager.auth.model.LoginRequestDto;
import org.izomp.transaction.manager.auth.model.SignUpRequestDto;

public interface AuthService {

    String login(LoginRequestDto loginRequest);

    String register(SignUpRequestDto signUpRequestDto);

}
