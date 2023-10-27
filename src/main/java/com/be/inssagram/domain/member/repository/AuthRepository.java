package com.be.inssagram.domain.member.repository;

import com.be.inssagram.domain.member.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmailAndCode (String email, String code);
}
