package com.projectx.module.user.repository;


import com.projectx.module.user.entity.UserServiceMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserServiceMapRepository extends JpaRepository<UserServiceMap,Long> {

    List<UserServiceMap> findByUserId(Long userId);
}