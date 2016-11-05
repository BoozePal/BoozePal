package hu.deik.boozepal.service.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.UserService;

@Stateless(mappedName = "UserService")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Local(UserService.class)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userDao;

	@Override
	public User findUserByName(String username) {
		User user = userDao.findByUsername(username);
		return user;
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void save(User user) {
		userDao.save(user);
	}

}
