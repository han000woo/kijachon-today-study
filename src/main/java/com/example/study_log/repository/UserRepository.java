package com.example.study_log.repository;

import com.example.study_log.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findById(Long id);

    Optional<User> findByRefreshToken(String refreshToken);
}
