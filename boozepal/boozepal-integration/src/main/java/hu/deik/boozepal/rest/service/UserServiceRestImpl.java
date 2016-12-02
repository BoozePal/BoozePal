package hu.deik.boozepal.rest.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import hu.deik.boozepal.common.entity.PalRequest;
import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.core.repo.PubRepository;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.helper.UserHelper;
import hu.deik.boozepal.rest.vo.*;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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
    private PubRepository pubDao;

    /**
     * Szerepköröket elérő adathozzáférési osztály.
     */
    @Autowired
    private RoleRepository roleDao;

    @EJB
    private UserHelper userHelper;

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
    public List<User> getUsersInGivenRadiusAndCoordinate(Double latitude, Double longitude, Double radius) {
        logger.info("Felhasználók keresése: {} {} koordinátától {} km-es körsugárban.", latitude, longitude, radius);
        List<User> onlineUsers = userDao.findByRoleUser();
        List<User> usersInRadius = onlineUsers.stream()
                .filter(p -> isInRadius(latitude, longitude, radius, p) && filterRequestor(latitude, longitude, p))
                .collect(Collectors.toList());
        return usersInRadius;
    }

    private boolean filterRequestor(Double latitude, Double longitude, User p) {
        if (p.getLastKnownCoordinate().getLatitude().equals(latitude)
                && p.getLastKnownCoordinate().getLongitude().equals(longitude)) {
            logger.info("Kérő kiszűrve.");
            return false;
        } else
            return true;
    }

    private boolean isInRadius(Double latitude, Double longitude, Double radius, User p) {
        if (isNullCoordinate(p))
            return false;
        return distanceBetweenPoints(latitude, longitude, p) <= radius / 100;
    }

    private boolean isNullCoordinate(User p) {
        return p.getLastKnownCoordinate() == null || p.getLastKnownCoordinate().getLatitude() == null
                || p.getLastKnownCoordinate().getLatitude() == null;
    }

    private double distanceBetweenPoints(Double latitude, Double longitude, User p) {
        return Math.sqrt(toSquare((p.getLastKnownCoordinate().getLatitude() - latitude))
                + toSquare((p.getLastKnownCoordinate().getLongitude() - longitude)));
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
        PayloadUserVO payloadUserVO;
        GoogleIdToken idToken = verifier.verify(token);
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
     * Kapott ráérési idők frissitése egy felhasználónál.
     */
    @Override
    public void updateUserDates(RemoteTimeTableVO remoteTimeTableVO)
            throws UserDetailsUpdateException, AuthenticationException {
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(remoteTimeTableVO.getToken());
        } catch (Exception e) {
            logger.info("Rossz token! {}", e);
            throw new AuthenticationException("Rossz token!");
        }
        boolean ret = userHelper.updateUserDates(idToken.getPayload().getEmail(), remoteTimeTableVO.getTimeTableList());
    }

    /**
     * Kapot token adataiból kikeressük az adott felhasználót.
     */
    @Override
    public User updateUserDetails(RemoteUserDetailsVO remoteUser) throws UserDetailsUpdateException {
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(remoteUser.getToken());
        } catch (Exception e) {
            logger.info("Bad token!");
            return userHelper.remoteUserVoToUserEntityById(remoteUser.getUser());
            // throw new UserDetailsUpdateException("Bad token!");
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            return userHelper.remoteUserVoToUserEntityByGoogleToken(remoteUser.getUser(), payload.getEmail());
        } else {
            return userHelper.remoteUserVoToUserEntityById(remoteUser.getUser());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUserLocation(User remoteUser) {
        logger.info("{} felhasználó aktuális pozíciójának módosítása.", remoteUser.getUsername());
        logger.info("Új koordináta: {}", remoteUser.getLastKnownCoordinate());
        Long userId = remoteUser.getId();
        userDao.updateUserCoordinate(remoteUser.getLastKnownCoordinate().getLatitude(), remoteUser.getLastKnownCoordinate().getLongitude(), userId);
        return userDao.findById(userId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByToken(String token) throws AuthenticationException, GeneralSecurityException, IOException {
        PayloadUserVO userByGoogleToken = getUserByGoogleToken(token);
        return userByGoogleToken.getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void palRequest(RemotePalRequestVO vo) {
        logger.info(
                "{} azonosítóval rendelkező felhasználó beszúrása, {} azonosítójú felhasználóhoz, {} nappal egybekötve.",
                vo.getRequestedUserId(), vo.getUserId(), vo.getDate());
        User user = userDao.findById(vo.getUserId());
        User requestedUser = userDao.findById(vo.getRequestedUserId());
        Pub pub = pubDao.findById(vo.getPubId());
        logger.info("Pub: {}", pub);
        if (user != null && requestedUser != null && pub != null) {
            logger.info("PalRequest kérés elvégezhető, egyik mező sem NULL");
            logger.info("Kocsma neve:{}", pub.getName());
            logger.info("Időpont:{}", vo.getDate());
            requestedUser.getActualPals().put(user.getId(),
                    PalRequest.builder().date(vo.getDate()).pub(pub).accepted(false).requesterUser(user).build());
//            userDao.save(requestedUser);
        } else {
            logger.info("PalRequest kérés NEM végezhető el, egyik mező NULL");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptRequest(RemotePalAcceptVO vo) {
        //user megjelölte requetedUsert hogy szeretne vele iszni.
        //óvatosan mert itt fordítva vannak a szerepek jelenleg.
        User user = userDao.findById(vo.getUserId());
        User requestedUser = userDao.findById(vo.getRequestedUserId());
        //ha requestedUser elfogadta
        if (vo.isAccepted()) {
            PalRequest palRequest = user.getActualPals().get(requestedUser.getId());
            palRequest.setAccepted(true);
            //akkor user listájába is berakjuk a requestedusert mint cimbora
            if (user != null && requestedUser != null) {
                requestedUser.getActualPals().put(user.getId(), PalRequest.builder().date(palRequest.getDate()).pub(palRequest.getPub()).requesterUser(user).accepted(true).build());
//                userDao.save(requestedUser);
            }
        } else {
            //ha nem akkor pedig kidobjuk
            user.getActualPals().remove(requestedUser.getId());
//            userDao.save(user);
        }
    }

}
