package com.bgcg.queryexecute.repository;

import com.bgcg.queryexecute.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsersRepository extends JpaRepository<Users,Integer> {

    Users findByEmail(String email);
//    Optional<Users> findByEmail(String email);
}
