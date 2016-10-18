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
			}
		} catch (Exception e) {
			logger.error("Failed to deploy application.", e);
		}

	}

	/* A jelszó admin */
	public void createAdminUser() {
		userRole = roleDao.save(new Role("ROLE_ADMIN"));
		logger.info("Create admin role : " + userRole.toString());
		user = userDao.save(User.builder().username("admin")
				.password("$2a$04$sIC5RDGG8CESaA7JGmn4huaC2av5olRYp3D7Cyaxq3/JNhMSzqC1O").email("teszt@teszt.com")
				.remove(false).roles(Arrays.asList(userRole)).build());
		logger.info("Create user : " + user.toString());
	}
}
