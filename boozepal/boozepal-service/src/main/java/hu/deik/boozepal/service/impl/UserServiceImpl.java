package hu.deik.boozepal.service.impl;

import java.util.Arrays;

import javax.ejb.Local;
import javax.ejb.Stateless;

import hu.deik.boozepal.common.entity.RoleVO;
import hu.deik.boozepal.common.entity.UserVO;
import hu.deik.boozepal.service.UserService;

@Stateless
@Local(UserService.class)
public class UserServiceImpl implements UserService {

	/* TODO: FIX Implementálni !!! */
	@Override
	public UserVO findUserByName(String username) {
		return testUser(username);
	}

	private UserVO testUser(String username) {
		RoleVO role = new RoleVO("ROLE_ADMIN");
		/* A jelszó "alma" BCrypt */
		UserVO retUser = new UserVO(username, "$2a$04$sIC5RDGG8CESaA7JGmn4huaC2av5olRYp3D7Cyaxq3/JNhMSzqC1O",
				"teszt@teszt.com", false, Arrays.asList(role));
		return retUser;
	}

}
