package hu.deik.boozepal.service.impl;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.StartupService;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class StartupServiceImpl implements StartupService {

	/* password = admin */
	private static final String PASSWORD = "$2a$04$e9R7K4j5IYZRxOUB4.yVweDsguwUCdiqkd5kzJ0uO/0F7CQxJdjZ.";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userDao;

	@Autowired
	private RoleRepository roleDao;

	private Role userRole;
	private User user;

	@PostConstruct
	public void init() {
		logger.info("StartupService#init()");
		try {
			userRole = roleDao.findByRoleName("ROLE_ADMIN");
			user = userDao.findByUsername("admin");
			if (userRole == null && user == null) {
				createAdminUser();
			} else {
				user.setPassword(PASSWORD);
				userDao.save(user);
			}
		} catch (Exception e) {
			logger.error("Failed to deploy application.", e);
		}

	}

	/* A jelszó admin */
	public void createAdminUser() {
		userRole = roleDao.save(new Role("ROLE_ADMIN"));
		logger.debug("Create admin role : " + userRole.toString());
		user = userDao.save(User.builder().username("admin").password(PASSWORD).email("admin@admin.com").remove(false)
				.roles(Arrays.asList(userRole)).build());
		logger.debug("Create user : " + user.toString());
	}
}
