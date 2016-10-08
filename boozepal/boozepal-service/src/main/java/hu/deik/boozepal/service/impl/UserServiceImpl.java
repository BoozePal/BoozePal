package hu.deik.boozepal.service.impl;

import java.util.Arrays;

import javax.ejb.Local;
import javax.ejb.Stateless;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;

@Stateless
@Local(UserService.class)
public class UserServiceImpl implements UserService {

	/* TODO: FIX Implementálni !!! */
	@Override
	public User findUserByName(String username) {
		return testUser(username);
	}

	private User testUser(String username) {
		Role role = new Role("ROLE_ADMIN");
		/* A jelszó "alma" BCrypt */
		User retUser = new User(username, "$2a$04$sIC5RDGG8CESaA7JGmn4huaC2av5olRYp3D7Cyaxq3/JNhMSzqC1O",
				"teszt@teszt.com", false, Arrays.asList(role));
		return retUser;
	}

}
