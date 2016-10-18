package hu.deik.boozepal.service;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.User;

@Local
public interface UserService {

	public User findUserByName(String username);

}
