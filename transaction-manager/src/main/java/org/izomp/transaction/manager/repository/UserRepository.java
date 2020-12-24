package org.izomp.transaction.manager.repository;

import org.izomp.transaction.manager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByLogin(String login);
    Optional<User> getUserById(UUID userId);
}
