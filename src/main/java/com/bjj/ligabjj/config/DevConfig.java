package com.bjj.ligabjj.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bjj.ligabjj.exceptions.ProjectException;
import com.bjj.ligabjj.models.Roles;
import com.bjj.ligabjj.models.User;
import com.bjj.ligabjj.models.enums.Belt;
import com.bjj.ligabjj.service.RolesService;
import com.bjj.ligabjj.service.UserService;
import com.bjj.ligabjj.utils.Constants;

@Configuration
@Profile({ "dev" })
@ComponentScan({ "com.bjj.ligabjj.controller" })
public class DevConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private RolesService rolesService;

	@Bean
	public void startDatabase() {
		this.initialUser();
	}

	private void initialUser() {

		String[] rolesName = { Constants.ROLE_ADMIN, Constants.ROLE_VIEW_USER, Constants.ROLE_CREATE_USER,
				Constants.ROLE_UPDATE_USER, Constants.ROLE_UPDATE_ROLES_USER, Constants.ROLE_DELETE_USER,
				Constants.ROLE_DISABLE_USER };
		
		Roles roles = null;
		for (String roleName : rolesName) {
			try {
				rolesService.findByName(roleName);
			} catch (ProjectException e) {
				roles = new Roles(roleName);
				rolesService.save(roles);
			}
		}

		roles = rolesService.findByName(Constants.ROLE_ADMIN);
		for (Belt belt: Belt.values()) {
			String beltName = belt.name().toLowerCase();
			
			String mail = String.format("%s@email.com", beltName);
			String password = String.format("%s@1234", beltName);
			
			try {
				userService.findByEmail(mail, true);
			} catch (ProjectException e) {
				User user = new User(beltName, mail, password, belt, Arrays.asList(roles));
				userService.save(user);
			}
		}

	}
}
