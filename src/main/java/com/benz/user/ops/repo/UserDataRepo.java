package com.benz.user.ops.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benz.user.ops.entity.UserEntity;

@Repository
public interface UserDataRepo extends JpaRepository<UserEntity, Integer> {

	UserEntity findByUserEmail(String email);
}
