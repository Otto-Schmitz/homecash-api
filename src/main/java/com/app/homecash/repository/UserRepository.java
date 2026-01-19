package com.app.homecash.repository;

import com.app.homecash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByEmailAndActiveTrue(String email);

    Optional<User> findByCpfAndActiveTrue(String cpf);
}

