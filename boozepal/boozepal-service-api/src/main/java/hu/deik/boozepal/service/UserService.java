package hu.deik.boozepal.service;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.UserVO;

@Local
public interface UserService {

	public UserVO findUserByName(String username);
}
