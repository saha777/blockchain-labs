package org.izomp.transaction.manager.auth.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String login;

    private String password;
}
