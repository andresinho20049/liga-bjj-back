package com.bjj.ligabjj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjj.ligabjj.exceptions.ProjectException;
import com.bjj.ligabjj.models.Roles;
import com.bjj.ligabjj.repository.RoleRepository;
import com.bjj.ligabjj.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void save(Roles roles) {
		roleRepository.save(roles);
	}

	@Override
	public List<Roles> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void update(Roles roles) {
		findById(roles.getId());
		roleRepository.save(roles);
	}

	@Override
	public void delete(Roles roles) {
		findById(roles.getId());
		roleRepository.delete(roles);
	}

	@Override
	public Roles findById(Long id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new ProjectException(String.format("Role id: %s not found", id)));
	}

	@Override
	public Roles findByName(String nameRole) {
		return roleRepository.findByName(nameRole)
				.orElseThrow(() -> new ProjectException(String.format("Role %s not found", nameRole)));
	}

}
