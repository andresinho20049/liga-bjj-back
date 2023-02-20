package com.bjj.ligabjj.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bjj.ligabjj.models.Roles;


public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}
