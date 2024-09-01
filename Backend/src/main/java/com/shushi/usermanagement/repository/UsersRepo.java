package com.shushi.usermanagement.repository;

import com.shushi.usermanagement.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<OurUsers,Integer> {
    Optional<OurUsers> findByEmail(String email);
}
