package org.izomp.transaction.manager.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.izomp.transaction.manager.entities.UserRole;

@Getter
@Setter
public class SignUpRequestDto {
    private String name;

    private String login;

    private String password;

    private UserRole userRole;
}
