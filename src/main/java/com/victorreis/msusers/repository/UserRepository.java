package com.victorreis.msusers.repository;

import com.victorreis.msusers.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
}
