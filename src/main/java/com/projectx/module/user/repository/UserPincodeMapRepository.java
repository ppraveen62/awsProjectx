package com.projectx.module.user.repository;

import com.projectx.module.user.entity.UserPincodeMap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserPincodeMapRepository extends JpaRepository<UserPincodeMap,Long> {
    List<UserPincodeMap> findByUserId(Long userId);
}
