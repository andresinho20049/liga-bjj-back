package com.bjj.ligabjj.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bjj.ligabjj.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional(readOnly = true)
	Optional<User> findById(Long id);
    
	@Transactional(readOnly = true)
	Optional<User> findByEmailAndActive(String email, Boolean active);
	
	@Transactional(readOnly = true)
	Optional<User> findByEmail(String email);
	
	@Transactional(readOnly = true)
	Boolean existsByEmail(String email);
	
	@Transactional(readOnly = true)
	Page<User> findByActive(Boolean active, Pageable pageable);
}
