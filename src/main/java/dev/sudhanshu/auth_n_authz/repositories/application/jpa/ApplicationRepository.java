package dev.sudhanshu.auth_n_authz.repositories.application.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sudhanshu.auth_n_authz.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByApplicationName(String applicationName);
}
