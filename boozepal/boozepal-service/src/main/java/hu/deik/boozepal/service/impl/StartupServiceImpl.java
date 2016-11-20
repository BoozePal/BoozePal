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
import static hu.deik.boozepal.common.contants.BoozePalConstants.*;

/**
 * A rendszer felállásakor elinduló bean ami a szükséges létező adatokat
 * biztosítja a felületnek.
 *
 */
@Startup
@Singleton
// ====================================================
// Kivételesen ide szükséges, tranzakciós hibákba ütküzönk ha a Container (azaz
// az default) beállítást használjuk, máshol nem használjuk itt sem nagyon
// elegáns.
@TransactionManagement(TransactionManagementType.BEAN)
// ====================================================
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class StartupServiceImpl implements StartupService {

    /* password = admin */
    /**
     * Adminisztrátor BCryptel kódolt jelszava.
     */
    private static final String PASSWORD = "$2a$04$e9R7K4j5IYZRxOUB4.yVweDsguwUCdiqkd5kzJ0uO/0F7CQxJdjZ.";

    /**
     * Az osztály loggere.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupServiceImpl.class);

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
     * Admin szerpekör.
     */
    private Role adminRole;

    /**
     * Felhasználói szerpekör.
     */
    private Role userRole;

    /**
     * Felhasználó entitás amelyet betöltünk majd.
     */
    private User adminUser;

    /**
     * Init metódus mely elsőként fut le a szolgáltatás elindulása után.
     */
    @PostConstruct
    public void init() {
        LOGGER.info("StartupService#init()");
        createDefaultApplicationContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDefaultApplicationContext() {
        try {
            checkIfDefaultAdministratorExists();
            checkIfDefaultUserRoleExists();
        } catch (Exception e) {
            LOGGER.error("Failed to deploy application.", e);
        }
    }

    private void createAdminUser() {
        LOGGER.info("Admin profil létrehozása.");
        adminRole = roleDao.save(new Role(ROLE_ADMIN));
        LOGGER.debug("Create admin role : {}", adminRole.toString());
        adminUser = userDao.save(User.builder().username(ADMIN).password(PASSWORD).email(ADMINEMAIL).remove(false)
                .roles(Arrays.asList(adminRole)).build());
        LOGGER.debug("Create user : {} ", adminUser.toString());
    }

    private void checkIfDefaultUserRoleExists() {
        userRole = roleDao.findByRoleName(ROLE_USER);
        if (isUserRoleNull()) {
            userRole = roleDao.save(new Role(ROLE_USER));
            LOGGER.debug("Create user role : {}", userRole.toString());
        }
    }

    private void checkIfDefaultAdministratorExists() {
        loadAdmin();
        if (isAdminRoleNull() && isAdminUserNull()) {
            createAdminUser();
        } else {
            adminUser.setPassword(PASSWORD);
            userDao.save(adminUser);
        }
    }

    private boolean isUserRoleNull() {
        return userRole == null;
    }

    private void loadAdmin() {
        adminRole = roleDao.findByRoleName(ROLE_ADMIN);
        adminUser = userDao.findByUsername(ADMIN);
    }

    private boolean isAdminUserNull() {
        return adminUser == null;
    }

    private boolean isAdminRoleNull() {
        return adminRole == null;
    }

}
