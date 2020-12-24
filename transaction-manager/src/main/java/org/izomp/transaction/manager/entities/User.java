package org.izomp.transaction.manager.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String name;
    private String login;
    private String password;
    private UserRole userRole;
}
