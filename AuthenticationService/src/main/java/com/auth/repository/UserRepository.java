package com.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
