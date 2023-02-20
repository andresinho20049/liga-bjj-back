package com.bjj.ligabjj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bjj.ligabjj.exceptions.ProjectException;
import com.bjj.ligabjj.models.User;
import com.bjj.ligabjj.repository.UserRepository;
import com.bjj.ligabjj.service.UserService;
import com.bjj.ligabjj.utils.Pagination;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void save(User user) {

		if (user == null)
			throw new ProjectException("User is null");

		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new ProjectException("E-mail is required");

		if (userRepository.existsByEmail(user.getEmail()))
			throw new ProjectException("Email is already registered");

		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		
		user.setActive(true);
		user.setUpdatePassword(true);

		userRepository.save(user);

	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void update(User user) {
		User updated = this.findById(user.getId());

		if (user.getName() != null && !user.getName().isEmpty())
			updated.setName(user.getName());

		if (user.getPassword() != null && !user.getPassword().isEmpty())
			updated.setPassword(passwordEncoder.encode(user.getPassword()));

		if (user.getRoles() != null && !user.getRoles().isEmpty())
			updated.setRoles(user.getRoles());

		userRepository.save(updated);

	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void disabled(Long id) {
		User user = this.findById(id);
		
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ProjectException(String.format("User %s not found", id)));
	}

	@Override
	public User findByEmail(String email, Boolean active) {

		if (active != null)
			return userRepository.findByEmailAndActive(email, active)
					.orElseThrow(() -> new ProjectException(String.format("Email %s not found on active users", email)));

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ProjectException(String.format("E-mail %s not found", email)));
	}

	@Override
	public Page<User> findByPage(Boolean active, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);

		if (active != null)
			return userRepository.findByActive(active, pageRequest);

		return userRepository.findAll(pageRequest);
	}

}
