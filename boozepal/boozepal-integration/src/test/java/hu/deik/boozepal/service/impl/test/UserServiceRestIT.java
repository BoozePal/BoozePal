package hu.deik.boozepal.service.impl.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.Coordinate;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.CoordinateVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

@RunWith(Arquillian.class)
public class UserServiceRestIT extends ArquillianContainer {

    private static final Double RADIUS = 500.0;
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
                .loggedIn(true).lastKnownCoordinate(Coordinate.builder().latitude(5.0).longitude(0.0).build()).build();
        userService.saveUser(firtUserInCircle);

        // x = 0, y = 5
        User secodUserInCircle = User.builder().username("secodUserInCircle").email("secodUserInCircle")
                .password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).longitude(5.0).build()).build();
        userService.saveUser(secodUserInCircle);

        // x = 2, y = 2
        User thirdUserInCirlce = User.builder().username("thirdUserInCirlce").email("thirdUserInCirlce")
                .password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(2.0).longitude(2.0).build()).build();
        userService.saveUser(thirdUserInCirlce);

        // offline but in circle
        User offlineUserButInCircle = User.builder().username("offlineUserButInCircle").email("offlineUserButInCircle")
                .password("pass").loggedIn(false)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).longitude(3.0).build()).build();
        userService.saveUser(offlineUserButInCircle);

        // Online but not in circle
        User onlineUserButNotInCircle = User.builder().username("onlineUserButNotInCircle")
                .email("onlineUserButNotInCircle").password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(10.0).longitude(10.0).build()).build();
        userService.saveUser(onlineUserButNotInCircle);

        List<User> usersInGivenRadiusAndCoordinate = userService.getUsersInGivenRadiusAndCoordinate(
                iAm.getLastKnownCoordinate().getLatitude(), iAm.getLastKnownCoordinate().getLongitude(), RADIUS);
//        System.out.println(usersInGivenRadiusAndCoordinate.size());
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
        testUser = userService.saveUser(testUser);
        remoteUser.setId(testUser.getId());
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
        testUser = userService.saveUser(testUser);
        RemoteUserVO remoteUser = buildTestRemoteUser();
        remoteUser.setId(testUser.getId() + 1);
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder()
                .token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser)
                .build();
        userService.updateUserDetails(
                remoteUserDetailsVO);
        userService.deleteUser(testUser);
    }

    @Test
    public void testUpdateOnlyUserTimeBoard() {
        Date day1 = new Date(2016, 11, 20);
        Date day2 = new Date(2016, 11, 22);
        User testUser = buildTestUser();
        testUser = userService.saveUser(testUser);
        RemoteUserVO remoteUser = RemoteUserVO.builder()
                .name("tesztUser")
                .id(testUser.getId())
                .savedDates(Arrays.asList(day1, day2))
                .build();
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

    @Test
    public void testUpdateUserPals() {
        User testUser = buildTestUser();

        User testUserPal1 = buildTestUser();
        testUserPal1.setUsername("user@1");
        testUserPal1.setEmail("user@1.com");

        User testUserPal2 = buildTestUser();
        testUserPal2.setUsername("user@2");
        testUserPal2.setEmail("user@2.com");

        testUser = userService.saveUser(testUser);
        testUserPal1 = userService.saveUser(testUserPal1);
        testUserPal2 = userService.saveUser(testUserPal2);


        RemoteUserVO remoteTestUser = RemoteUserVO.builder()
                .id(testUser.getId())
                .build();

        RemoteUserVO remoteTestUserPal1 = RemoteUserVO.builder()
                .id(testUserPal1.getId())
                .build();

        RemoteUserVO remoteTestUserPal2 = RemoteUserVO.builder()
                .id(testUserPal2.getId())
                .build();

        remoteTestUser.setId(testUser.getId());
        remoteTestUser.setMyPals(Arrays.asList(remoteTestUserPal1, remoteTestUserPal2));

        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder()
                .token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteTestUser)
                .build();
        try {
            testUser = userService.updateUserDetails(
                    remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertTrue(testUser.getActualPals().contains(testUserPal2));
        userService.deleteUser(testUser);
        userService.deleteUser(testUserPal1);
        userService.deleteUser(testUserPal2);
    }

    @Test
    public void testUpdateUserLocation() {
        User user = buildTestUser();
        User saveUser = userService.saveUser(user);
        RemoteUserVO remoteUser = new RemoteUserVO();
        remoteUser.setId(saveUser.getId());
        Double latitude = 5.0;
        Double longitude = 6.0;
        CoordinateVO coordinateVO = CoordinateVO.builder().latitude(latitude).longitude(longitude).build();
        remoteUser.setLastKnownCoordinate(coordinateVO);
        User updateUserLocation = userService.updateUserLocation(remoteUser);
        Assert.assertEquals(latitude, updateUserLocation.getLastKnownCoordinate().getLatitude());
        Assert.assertEquals(longitude, updateUserLocation.getLastKnownCoordinate().getLongitude());
        userService.deleteUser(saveUser);
    }

    @Test
    public void testFindByEmail() {
        User user = buildTestUser();
        User saveUser = userService.saveUser(user);
        user = userService.findByEmail(user.getEmail());
        Assert.assertEquals(user, saveUser);
        userService.deleteUser(saveUser);
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
