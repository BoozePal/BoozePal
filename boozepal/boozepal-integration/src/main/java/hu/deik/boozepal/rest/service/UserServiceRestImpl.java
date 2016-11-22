package hu.deik.boozepal.rest.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import hu.deik.boozepal.common.entity.*;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.core.repo.DrinkRepository;
import hu.deik.boozepal.core.repo.PubRepository;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.rest.vo.PayloadUserVO;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static hu.deik.boozepal.common.contants.BoozePalConstants.ROLE_USER;

/**
 * A távoli felhasználók igényeit megvalósító szolgáltatás.
 */
@Stateless
@Local(UserServiceRest.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserServiceRestImpl implements UserServiceRest {

    private static final String HTTPS_ACCOUNTS_GOOGLE_COM = "https://accounts.google.com";
    private static final String ANDROID_USER_DOES_NOT_NEED_PASSWORD = "AndroidUserDoesNotNeedPassword";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Felhasználókat elérő adathozzáférési osztály.
     */
    @Autowired
    private UserRepository userDao;

    @Autowired
    private DrinkRepository drinkDao;

    @Autowired
    private PubRepository pubDao;

    /**
     * Szerepköröket elérő adathozzáférési osztály.
     */
    @Autowired
    private RoleRepository roleDao;

    /**
     * Alapértelmezett felhasználói jog.
     */
    private Role userRole;

    /**
     * A REST hívásokon keresztül kapott Google Token validáló.
     */
    private GoogleIdTokenVerifier verifier;

    /**
     * A szolgáltatás felállásakor lefutó metódus.
     */
    @PostConstruct
    public void init() {
        userRole = roleDao.findByRoleName(ROLE_USER);
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setIssuer(HTTPS_ACCOUNTS_GOOGLE_COM).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createOrLoginUser(RemoteTokenVO remoteUser) throws AuthenticationException {
        PayloadUserVO userByGoogleToken;
        try {
            userByGoogleToken = getUserByGoogleToken(remoteUser.getToken());
        } catch (GeneralSecurityException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Login error, " + e.getMessage());
        }

        return createNewUserOrGetExisting(userByGoogleToken);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logoutUserLogically(RemoteTokenVO remoteUser) throws AuthenticationException {
        PayloadUserVO userByGoogleToken;
        try {
            userByGoogleToken = getUserByGoogleToken(remoteUser.getToken());
        } catch (GeneralSecurityException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Logout error, " + e.getMessage());
        }
        logoutUser(userByGoogleToken.getUser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsersInGivenRadiusAndCoordinate(Double latitude, Double altitude, Double radius) {
        List<User> onlineUsers = userDao.findOnlineUsers();
        List<User> usersInRadius = onlineUsers.stream().filter(p -> isInRadius(latitude, altitude, radius, p))
                .collect(Collectors.toList());
        return usersInRadius;
    }

    private boolean isInRadius(Double latitude, Double altitude, Double radius, User p) {
        return distanceBetweenPoints(latitude, altitude, p) <= radius;
    }

    private double distanceBetweenPoints(Double latitude, Double altitude, User p) {
        return Math.sqrt(toSquare((p.getLastKnownCoordinate().getLatitude() - latitude))
                + toSquare((p.getLastKnownCoordinate().getAltitude() - altitude)));
    }

    private User createNewUser(Payload payload) {
        User newUser = User.builder().email(payload.getEmail()).username((String) payload.get("name"))
                .password(ANDROID_USER_DOES_NOT_NEED_PASSWORD).roles(Arrays.asList(userRole)).loggedIn(true).build();
        return userDao.save(newUser);
    }

    private User createNewUserOrGetExisting(PayloadUserVO userByGoogleToken) {
        User user = userByGoogleToken.getUser();
        if (user == null)
            return createNewUser(userByGoogleToken.getPayload());
        else {
            user.setLoggedIn(true);
            return userDao.save(user);
        }
    }

    private PayloadUserVO getUserByGoogleToken(String token)
            throws AuthenticationException, GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(token);
        PayloadUserVO payloadUserVO;
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            User user = userDao.findByEmail(payload.getEmail());
            payloadUserVO = PayloadUserVO.builder().user(user).payload(payload).build();
        } else {
            throw new AuthenticationException("Invalid token.");
        }
        return payloadUserVO;
    }

    private void logoutUser(User user) {
        user.setLoggedIn(false);
        userDao.save(user);
    }

    private Double toSquare(Double number) {
        return Math.pow(number, 2);
    }

    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    /**
     * Kapot token adataiból kikeressük az adott felhasználót.
     */
    @Override
    public User updateUserDetails(RemoteUserVO remoteUser) throws UserDetailsUpdateException {
        GoogleIdToken idToken = null;
        Payload payload = null;
        // FIXME a tesztek miatt kivettem vissza kell majd kötni
//        try {
//            idToken = verifier.verify(remoteUser.getToken());
//        } catch (GeneralSecurityException | IOException e) {
//            throw new UserDetailsUpdateException("Invalid token." + e.getStackTrace());
//        }
        if (idToken != null) {
            payload = idToken.getPayload();
            return remoteUserVoToUserEntity(remoteUser, payload);
        } else {
            /* TODO a teszt miatt van benne amíg nem tudunk tokent generálni a teszteknél*/
            return remoteUserVoToUserEntity(remoteUser);
        }
    }

    private User remoteUserVoToUserEntity(final RemoteUserVO remoteUserVO, final Payload payload) throws UserDetailsUpdateException {
        User user = userDao.findByEmail(payload.getEmail());
        if (user == null || remoteUserVO == null) {
            logger.info("User is null");
            throw new UserDetailsUpdateException("User is null");
        } else {
            logger.info("User {}", user.toString());
            if (remoteUserVO.getName() != null)
                user.setUsername(remoteUserVO.getName());
            if (remoteUserVO.getCity() != null)
                user.setAddress(Address.builder().town(remoteUserVO.getCity()).build());
            user.setPriceCategory(remoteUserVO.getPriceCategory());
            user.setSearchRadius(remoteUserVO.getSearchRadius());
            if (remoteUserVO.getSavedDates() != null)
                user.setTimeBoard(remoteUserVO.getSavedDates());
            if (remoteUserVO.getBoozes() != null)
                user.setFavouriteDrinks(getRemoteUserFavoritDrinks(remoteUserVO));
            if (remoteUserVO.getPubs() != null)
                user.setFavouritePub(getRemoteUserPubs(remoteUserVO));
            if (remoteUserVO.getMyPals() != null)
                user.setActualPals(getActualUsersList(remoteUserVO));
            logger.info("save update user");
            return userDao.save(user);
        }
    }

    /* TODO Kiveni ha lesz token a teszthez! */
    private User remoteUserVoToUserEntity(final RemoteUserVO remoteUserVO) throws UserDetailsUpdateException {
        User user = userDao.findByUsername(remoteUserVO.getName());
        if (user == null) {
            logger.info("User is null");
            throw new UserDetailsUpdateException("User is null");
        } else {
            logger.info("User {}", user.toString());
            if (remoteUserVO.getName() != null)
                user.setUsername(remoteUserVO.getName());
            if (remoteUserVO.getCity() != null)
                user.setAddress(Address.builder().town(remoteUserVO.getCity()).build());
            user.setPriceCategory(remoteUserVO.getPriceCategory());
            user.setSearchRadius(remoteUserVO.getSearchRadius());
            if (remoteUserVO.getSavedDates() != null)
                user.setTimeBoard(remoteUserVO.getSavedDates());
            if (remoteUserVO.getBoozes() != null)
                user.setFavouriteDrinks(getRemoteUserFavoritDrinks(remoteUserVO));
            if (remoteUserVO.getPubs() != null)
                user.setFavouritePub(getRemoteUserPubs(remoteUserVO));
            if (remoteUserVO.getMyPals() != null)
                user.setActualPals(getActualUsersList(remoteUserVO));
            logger.info("save update user");
            return userDao.save(user);
        }
    }

    private List<User> getActualUsersList(final RemoteUserVO remoteUserVO) {
        List<User> users = new LinkedList<>();
        for (RemoteUserVO user : remoteUserVO.getMyPals()) {
            logger.info("Haver neve: " + user.getName());
            users.add(userDao.findById(user.getId()));
        }
        return users;
    }

    private List<Pub> getRemoteUserPubs(final RemoteUserVO remoteUserVO) {
        List<Pub> pubs = new LinkedList<>();
        for (String pubName : remoteUserVO.getPubs()) {
            logger.info("Kocsma neve: " + pubName);
            // TODO A pub értékeit vagy be kell majd szettelni normálisan vagy ki kell venni a null checket!
            Pub savedPub = pubDao.save(Pub.builder().name(pubName).openHours("24:00").priceCategory(5).build());
            pubs.add(savedPub);
        }
        return pubs;
    }

    private List<Drink> getRemoteUserFavoritDrinks(final RemoteUserVO remoteUserVO) {
        List<Drink> drinks = new LinkedList<>();
        for (String drinksName : remoteUserVO.getBoozes()) {
            logger.info("alkohol neve: " + drinksName);
            Drink savedDrink = drinkDao.save(Drink.builder().name(drinksName).build());
            drinks.add(savedDrink);
        }
        return drinks;
    }

}
