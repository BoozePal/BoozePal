package hu.deik.boozepal.service.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.StartupService;

//@Startup
@Singleton
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class StartupServiceImpl implements StartupService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userDao;

    @PostConstruct
    public void init() {
        if (logger != null)
            logger.info("StartupService#init()");
        createAdminUser();
    }

    @Override
    public void createAdminUser() {
        // if (logger != null)
        // logger.info("Creating admin user");
        // UserVO user = new UserVO();
        // user.setUsername("admin");
        // user.setEmail("admin@admin.com");
        // userDao.save(user);

    }

}
