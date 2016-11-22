package hu.deik.boozepal.service.impl.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.Lists;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;
import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.Coordinate;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.rest.service.UserServiceRest;

@RunWith(Arquillian.class)
public class UserServiceRestIT extends ArquillianContainer {
    private static final Double RADIUS = 5.0;
    private static final Coordinate ORIGO = new Coordinate(0.0, 0.0);

    @EJB
    private UserServiceRest userService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreateOrLoginUser() {
        // TODO - keresni egy olyan apit ami érvényes tokent generál hogy
        // tesztelni tudjunk.
    }

    @Test
    public void testLogoutUserLogically() {
        // TODO - keresni egy olyan apit ami érvényes tokent generál hogy
        // tesztelni tudjunk.
    }

    @Test
    public void testGetUsersInGivenRadiusAndCoordinate() {
        // Generate users
        // The user who is looking for pals, is always in the circle.
        User iAm = User.builder().username("looking4Pals").email("looking@pals.com").password("Pals").loggedIn(true)
                .lastKnownCoordinate(ORIGO).build();
        userService.saveUser(iAm);

        // x = 5, y = 0
        User firtUserInCircle = User.builder().username("firtUserInCircle").email("firtUserInCircle").password("pass")
                .loggedIn(true).lastKnownCoordinate(Coordinate.builder().latitude(5.0).altitude(0.0).build()).build();
        userService.saveUser(firtUserInCircle);

        // x = 0, y = 5
        User secodUserInCircle = User.builder().username("secodUserInCircle").email("secodUserInCircle")
                .password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).altitude(5.0).build()).build();
        userService.saveUser(secodUserInCircle);

        // x = 2, y = 2
        User thirdUserInCirlce = User.builder().username("thirdUserInCirlce").email("thirdUserInCirlce")
                .password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(2.0).altitude(2.0).build()).build();
        userService.saveUser(thirdUserInCirlce);

        // offline but in circle
        User offlineUserButInCircle = User.builder().username("offlineUserButInCircle").email("offlineUserButInCircle")
                .password("pass").loggedIn(false)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).altitude(3.0).build()).build();
        userService.saveUser(offlineUserButInCircle);

        // Online but not in circle
        User onlineUserButNotInCircle = User.builder().username("onlineUserButNotInCircle")
                .email("onlineUserButNotInCircle").password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(10.0).altitude(10.0).build()).build();
        userService.saveUser(onlineUserButNotInCircle);

        List<User> usersInGivenRadiusAndCoordinate = userService.getUsersInGivenRadiusAndCoordinate(
                iAm.getLastKnownCoordinate().getLatitude(), iAm.getLastKnownCoordinate().getAltitude(), RADIUS);
        System.out.println(usersInGivenRadiusAndCoordinate.size());
        // Assert.assertEquals(Arrays.asList(iAm,firtUserInCircle,secodUserInCircle,thirdUserInCirlce),
        // usersInGivenRadiusAndCoordinate);
        Assert.assertThat(Arrays.asList(firtUserInCircle, secodUserInCircle, thirdUserInCirlce, iAm),
                Matchers.containsInAnyOrder(usersInGivenRadiusAndCoordinate.toArray()));

    }

    @Test
    public void testAccesUpdateUserDetails() {
        User savedUser = null;
        User testUser = buildTestUser();
        RemoteUserVO remoteUser = buildTestRemoteUser();
        userService.saveUser(testUser);
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder()
                .token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser)
                .build();
        try {
            savedUser = userService.updateUserDetails(
                    remoteUserDetailsVO);
            System.out.println("Mentett felhasznalo : " + savedUser.toString());
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(remoteUser.getSearchRadius(), savedUser.getSearchRadius());
        Assert.assertEquals(remoteUser.getPriceCategory(), savedUser.getPriceCategory());
        userService.deleteUser(savedUser);
    }

    @Test(expected = UserDetailsUpdateException.class)
    public void testDeniedUpdateUserDetails() throws UserDetailsUpdateException {
        User testUser = buildTestUser();
        RemoteUserVO remoteUser = buildTestRemoteUser();
        remoteUser.setName("undefinedUser");
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder()
                .token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser)
                .build();
        testUser = userService.saveUser(testUser);
        userService.updateUserDetails(
                remoteUserDetailsVO);
        userService.deleteUser(testUser);
    }

    @Test
    public void testUpdateOnlyUserTimeBoard() {
        User testUser = buildTestUser();
        Date day1 = new Date(2016, 11, 20);
        Date day2 = new Date(2016, 11, 22);
        RemoteUserVO remoteUser = RemoteUserVO.builder()
                .name("tesztUser")
                .savedDates(Arrays.asList(day1, day2))
                .build();
        testUser = userService.saveUser(testUser);
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder()
                .token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser)
                .build();
        try {
            testUser = userService.updateUserDetails(
                    remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertTrue(testUser.getTimeBoard().contains(day1));
        Assert.assertTrue(testUser.getTimeBoard().contains(day2));
        userService.deleteUser(testUser);
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
                .name("tesztUser")
                .city("Debrecen")
                .boozes(Arrays.asList("booze"))
                .pubs(Arrays.asList("pub"))
                .savedDates(Arrays.asList(new Date()))
                .searchRadius(10)
                .priceCategory(20)
                .build();
    }
}
