package hu.deik.boozepal.service;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.User;

@Local
public interface UserService {

	public User findUserByName(String username);

	public List<User> findAll();

	public void save(User user);
}
