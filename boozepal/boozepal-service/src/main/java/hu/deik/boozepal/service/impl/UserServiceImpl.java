package hu.deik.boozepal.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.Coordinate;
import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.common.vo.MapUserVO;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.util.MapUserConveter;

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
     * Origo koordináta.
     */
    private static final Coordinate ORIGO = Coordinate.builder().latitude(0.0).altitude(0.0).build();
    
    /**
     * Osztály loggere.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        logger.info("Creating new administrator with username: {}", username);
        checkIfUsernameExixst(username);
        Role adminRole = roleDao.findByRoleName(BoozePalConstants.ROLE_ADMIN);
        User user = User.builder().username(username).password(password).roles(Arrays.asList(adminRole)).build();
        return userDao.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        logger.info("Getting all users.");
        return userDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) {
        logger.info("{} user entity.", isUserIdNotNull(user) ? "Update" : "Save");
        userDao.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getOnlineUsers() {
        logger.info("Getting all online users list.");
        return userDao.findOnlineUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MapUserVO> getOnlineUsersToMap() {
        List<User> findOnlineUsers = userDao.findOnlineUsers();
        logger.debug("Online Users: {}", findOnlineUsers);
        List<MapUserVO> mapUsersList = findOnlineUsers.stream().filter(this::hasRoleUser)
                .map(this::createMapUserVOFromUser).collect(Collectors.toList());
        return mapUsersList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createNewUser(String fullName, String email) {
        User newUser = User.builder().email(email).fullName(fullName).lastKnownCoordinate(ORIGO)
                .password(BoozePalConstants.ANDROID_USER_DOES_NOT_NEED_PASSWORD).roles(Arrays.asList(getRoleUser()))
                .build();
        logger.debug("New User:{}", newUser);
        return userDao.save(newUser);
    }

    private boolean isUserIdNotNull(User user) {
        return user.getId() != null;
    }

    private void checkIfUsernameExixst(String username) throws RegistrationException {
        User findByUsername = userDao.findByUsername(username);
        if (findByUsername != null) {
            throw new RegistrationException("Username has been already exists.");
        }
    }

    private MapUserVO createMapUserVOFromUser(User user) {
        MapUserVO mapUserVO = MapUserConveter.toMapUserVO(user);
        logger.debug("MapUserVO:{}", mapUserVO);
        return mapUserVO;
    }

    private boolean hasRoleUser(User user) {
        return user.getRoles().contains(getRoleUser());
    }

    private Role getRoleUser() {
        return roleDao.findByRoleName(BoozePalConstants.ROLE_USER);
    }

}
