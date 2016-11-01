package hu.deik.boozepal.service.impl;

import java.util.Arrays;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.UserService;

/**
 * Adminisztrátori felülethez tartozó felhasználói tevékenységeket ellátó
 * szolgáltatás.
 * 
 * 
 * @version 1.0
 */
@Stateless(mappedName = "UserService")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Local(UserService.class)
public class UserServiceImpl implements UserService {

    /**
     * Felhasználókat elérő adathozzáférési osztály.
     */
    @Autowired
    private UserRepository userDao;
    /**
     * Szerepköröket elérő adathozzáférési osztály.
     */
    @Autowired
    private RoleRepository roleDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public User findUserByName(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createNewAdministrator(String username, String password) throws RegistrationException {
        checkIfUsernameExixst(username);
        Role adminRole = roleDao.findByRoleName(BoozePalConstants.ROLE_ADMIN);
        User user = User.builder().username(username).password(password).roles(Arrays.asList(adminRole)).build();
        return userDao.save(user);
    }

    private void checkIfUsernameExixst(String username) throws RegistrationException {
        User findByUsername = userDao.findByUsername(username);
        if (findByUsername != null) {
            throw new RegistrationException("Username has been already exists.");
        }
    }

}
