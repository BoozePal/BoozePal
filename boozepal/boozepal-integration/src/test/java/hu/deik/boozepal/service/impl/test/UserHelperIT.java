package hu.deik.boozepal.service.impl.test;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.Coordinate;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.helper.UserHelper;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteUserVO;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(Arquillian.class)
public class UserHelperIT extends ArquillianContainer {

    private static final Coordinate ORIGO = new Coordinate(0.0, 0.0);

    @EJB
    private UserHelper userHelper;

    @EJB
    private UserServiceRest userService;

    User user = null;

    @Before
    public void setUp() throws Exception {
        user = buildTestUser();
        userService.saveUser(user);
    }

    @After
    public void tearDown() throws Exception {
        User user = buildTestUser();
        user = userService.findByEmail(user.getEmail());
        userService.deleteUser(user);
    }

    @Test
    public void testUpdateUserDates() {
        Date day1 = new Date(2016, 11, 27);
        Date day2 = new Date(2016, 11, 26);
        List<Date> dates = Arrays.asList(day1, day2);
        try {
            userHelper.updateUserDates(user.getEmail(), dates);
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        User savedUser = userService.findByEmail(user.getEmail());

        Assert.assertTrue(savedUser.getTimeBoard().contains(day1));
        Assert.assertTrue(savedUser.getTimeBoard().contains(day2));
    }

    @Test(expected = UserDetailsUpdateException.class)
    public void testDeniedUpdateUserDates() throws UserDetailsUpdateException {
        userHelper.updateUserDates("exceptionEmail@email.com", Arrays.asList(new Date()));
    }

    @Test
    public void testRemoteUserVoToUserEntityByGoogleToken() {
        RemoteUserVO remoteUser = buildTestRemoteUser();
        try {
            user = userHelper.remoteUserVoToUserEntityByGoogleToken(remoteUser, user.getEmail());
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertTrue(user.getAddress().getTown().equals(remoteUser.getCity()));
        Assert.assertEquals(user.getUsername(), remoteUser.getName());
    }

    @Test(expected = UserDetailsUpdateException.class)
    public void testExceptionRemoteUserVoToUserEntityByGoogleToken() throws UserDetailsUpdateException {
        RemoteUserVO remoteUser = buildTestRemoteUser();
        userHelper.remoteUserVoToUserEntityByGoogleToken(remoteUser, "exceptionEmail@email.com");
    }

    private User buildTestUser() {
        return User.builder()
                .username("tesztUser")
                .email("looking@test.com")
                .password("Palss")
                .loggedIn(false)
                .lastKnownCoordinate(ORIGO)
                .build();
    }

    private RemoteUserVO buildTestRemoteUser() {
        return RemoteUserVO.builder()
                .name("remoteTestUser")
                .city("Debrecen")
                .boozes(Arrays.asList("booze"))
                .pubs(Arrays.asList("pub"))
                .savedDates(Arrays.asList(new Date()))
                .searchRadius(10)
                .priceCategory(20)
                .build();
    }
}
