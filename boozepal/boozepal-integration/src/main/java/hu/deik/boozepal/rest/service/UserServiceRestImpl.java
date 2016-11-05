package hu.deik.boozepal.rest.service;

import static hu.deik.boozepal.common.contants.BoozePalConstants.ROLE_USER;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.rest.vo.PayloadUserVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

@Stateless
@Local(UserServiceRest.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserServiceRestImpl implements UserServiceRest {

    private static final String HTTPS_ACCOUNTS_GOOGLE_COM = "https://accounts.google.com";
    private static final String ANDROID_USER_DOES_NOT_NEED_PASSWORD = "AndroidUserDoesNotNeedPassword";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     */
    @Autowired
    private UserRepository userDao;

    /**
     * 
     */
    @Autowired
    private RoleRepository roleDao;

    /**
     * 
     */
    private Role userRole;

    /**
     * 
     */
    private GoogleIdTokenVerifier verifier;

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
    public User createOrLoginUser(RemoteUserVO remoteUser) throws AuthenticationException {
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
    public void logoutUserLogically(RemoteUserVO remoteUser) throws AuthenticationException {
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
    public List<User> getUsersInGivenRadiusAndCoordinate(Double lattitude, Double altitude, Double radius) {
        List<User> onlineUsers = userDao.findOnlineUsers();
        List<User> usersInRadius = onlineUsers.stream()
                .filter(p -> isInRadius(lattitude, altitude, radius, p))
                .collect(Collectors.toList());
        return usersInRadius;
    }

    private boolean isInRadius(Double lattitude, Double altitude, Double radius, User p) {
        return distanceBetweenPoints(lattitude, altitude, p) <= radius;
    }

    private double distanceBetweenPoints(Double lattitude, Double altitude, User p) {
        return Math.sqrt(toSquare((p.getLastKnownCoordinate().getLatitude() - lattitude))
                + toSquare((p.getLastKnownCoordinate().getAltitude() - altitude)));
    }

    private User createNewUser(Payload payload) {
        User newUser = User.builder().email(payload.getEmail()).fullName((String) payload.get("name"))
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

}
