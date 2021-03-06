package hu.deik.boozepal.service.impl.test;

import hu.deik.boozepal.arquillian.container.*;
import hu.deik.boozepal.common.contants.*;
import hu.deik.boozepal.common.entity.*;
import hu.deik.boozepal.common.exceptions.*;
import hu.deik.boozepal.common.vo.*;
import hu.deik.boozepal.core.repo.*;
import hu.deik.boozepal.rest.service.*;
import hu.deik.boozepal.rest.vo.*;
import org.hamcrest.*;
import org.jboss.arquillian.junit.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.context.support.*;

import javax.ejb.*;
import java.util.*;

@RunWith(Arquillian.class)
public class UserServiceRestIT extends ArquillianContainer {

    private static final Double RADIUS = 500.0;
    private static final Coordinate ORIGO = new Coordinate(0.0, 0.0);

    @EJB
    private UserServiceRest userService;

    private RoleRepository roleDao;

    private PubRepository pubDao;

    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CORE_TEST_XML);
        roleDao = context.getBean(RoleRepository.class);
        pubDao = context.getBean(PubRepository.class);
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
        Role userRole = Role.builder().roleName(BoozePalConstants.ROLE_USER).build();
        userRole = roleDao.save(userRole);
        System.out.println(userRole);
        // Generate users
        // The user who is looking for pals, is always in the circle.
        User iAm = User.builder().username("looking4Pals").email("looking@pals.com").password("Pals").loggedIn(true)
                .roles(Arrays.asList(userRole)).lastKnownCoordinate(ORIGO).build();
        userService.saveUser(iAm);

        // x = 5, y = 0
        User firtUserInCircle = User.builder().username("firtUserInCircle").email("firtUserInCircle").password("pass")
                .roles(Arrays.asList(userRole)).loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(5.0).longitude(0.0).build()).build();
        userService.saveUser(firtUserInCircle);

        // x = 0, y = 5
        User secodUserInCircle = User.builder().username("secodUserInCircle").email("secodUserInCircle")
                .roles(Arrays.asList(userRole)).password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).longitude(5.0).build()).build();
        userService.saveUser(secodUserInCircle);

        // x = 2, y = 2
        User thirdUserInCirlce = User.builder().username("thirdUserInCirlce").email("thirdUserInCirlce")
                .roles(Arrays.asList(userRole)).password("pass").loggedIn(true)
                .lastKnownCoordinate(Coordinate.builder().latitude(2.0).longitude(2.0).build()).build();
        userService.saveUser(thirdUserInCirlce);

        // offline but in circle
        User offlineUserButInCircle = User.builder().username("offlineUserButInCircle").email("offlineUserButInCircle")
                .roles(Arrays.asList(userRole)).password("pass").loggedIn(false)
                .lastKnownCoordinate(Coordinate.builder().latitude(0.0).longitude(3.0).build()).build();
        userService.saveUser(offlineUserButInCircle);

        // Online but not in circle
        User onlineUserButNotInCircle = User.builder().username("onlineUserButNotInCircle")
                .roles(Arrays.asList(userRole)).email("onlineUserButNotInCircle").password("pass").loggedIn(true).roles(Arrays.asList(userRole))
                .lastKnownCoordinate(Coordinate.builder().latitude(10.0).longitude(10.0).build()).build();
        userService.saveUser(onlineUserButNotInCircle);

        List<User> usersInGivenRadiusAndCoordinate = userService.getUsersInGivenRadiusAndCoordinate(
                iAm.getLastKnownCoordinate().getLatitude(), iAm.getLastKnownCoordinate().getLongitude(), RADIUS);
        System.out.println(usersInGivenRadiusAndCoordinate.size());
        System.err.println(usersInGivenRadiusAndCoordinate);
        // Assert.assertEquals(Arrays.asList(iAm,firtUserInCircle,secodUserInCircle,thirdUserInCirlce),
        // usersInGivenRadiusAndCoordinate);
        Assert.assertThat(Arrays.asList(firtUserInCircle, secodUserInCircle, thirdUserInCirlce, offlineUserButInCircle),
                Matchers.containsInAnyOrder(usersInGivenRadiusAndCoordinate.toArray()));

    }

    @Test
    public void testAccesUpdateUserDetails() {

        User savedUser = null;
        User testUser = buildTestUser();
        hu.deik.boozepal.common.entity.User remoteUser = buildTestUser();
        testUser = userService.saveUser(testUser);
        remoteUser.setId(testUser.getId());
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder().token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser).build();
        try {
            savedUser = userService.updateUserDetails(remoteUserDetailsVO);
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
        hu.deik.boozepal.common.entity.User remoteUser = buildTestUser();
        remoteUser.setId(testUser.getId() + 1);
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder().token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser).build();
        userService.updateUserDetails(remoteUserDetailsVO);
        userService.deleteUser(testUser);
    }

    @Test
    public void testUpdateOnlyUserTimeBoard() {
        Date day1 = new Date(2016, 11, 20);
        Date day2 = new Date(2016, 11, 22);
        User testUser = buildTestUser();
        testUser = userService.saveUser(testUser);
        User remoteUser = User.builder().username("tesztUser")
                .timeBoard(Arrays.asList(day1, day2)).build();
        remoteUser.setId(testUser.getId());
        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder().token("1/fFAGRNJru1FTz70BzhT3Zg")
                .user(remoteUser).build();
        try {
            testUser = userService.updateUserDetails(remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertTrue(testUser.getTimeBoard().contains(day1));
        Assert.assertTrue(testUser.getTimeBoard().contains(day2));
        userService.deleteUser(testUser);
    }

//    @Test
//    public void testUpdateUserPals() {
//        User testUser = buildTestUser ();
//
//        User testUserPal1 = buildTestUser ();
//        testUserPal1.setUsername ("user@1");
//        testUserPal1.setEmail ("user@1.com");
//
//        User testUserPal2 = buildTestUser ();
//        testUserPal2.setUsername ("user@2");
//        testUserPal2.setEmail ("user@2.com");
//
//        testUser = userService.saveUser (testUser);
//        testUserPal1 = userService.saveUser (testUserPal1);
//        testUserPal2 = userService.saveUser (testUserPal2);
//
////        RemoteUserVO remoteTestUser = RemoteUserVO.builder ().id (testUser.getId ()).build ();
//
//        User remoteTestUser = User.builder ().build ();
//
//        User remoteTestUserPal1 = User.builder ().build ();
//        remoteTestUserPal1.setId (testUserPal1.getId ());
//
//        User remoteTestUserPal2 = User.builder ().build ();
//        remoteTestUserPal2.setId (testUserPal1.getId ());
//
//        remoteTestUser.setId (testUser.getId ());
//        /remoteTestUser.setActualPals (Arrays.asList (remoteTestUserPal1, remoteTestUserPal2));
//
//        RemoteUserDetailsVO remoteUserDetailsVO = RemoteUserDetailsVO.builder ().token ("1/fFAGRNJru1FTz70BzhT3Zg")
//                .user (remoteTestUser).build ();
//        try {
//            testUser = userService.updateUserDetails (remoteUserDetailsVO);
//        } catch (UserDetailsUpdateException e) {
//            Assert.fail (e.getMessage ());
//        }
////        Assert.assertTrue(testUser.getActualPals().contains(testUserPal2));
//        userService.deleteUser (testUser);
//        userService.deleteUser (testUserPal1);
//        userService.deleteUser (testUserPal2);
//    }

    @Test
    public void testUpdateUserLocation() {
        User user = buildTestUser();
        User saveUser = userService.saveUser(user);
//        RemoteUserVO remoteUser = new RemoteUserVO();
//        remoteUser.setId(saveUser.getId());
        Double latitude = 5.0;
        Double longitude = 6.0;
        Coordinate coordinateVO = hu.deik.boozepal.common.entity.Coordinate.builder().latitude(latitude).longitude(longitude).build();
        saveUser.setLastKnownCoordinate(coordinateVO);
        User updateUserLocation = userService.updateUserLocation(saveUser);
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

    @Test
    public void testPalRequestAndAcceptRequestCombination() {
        Pub kitalacioPub = pubDao.findById(1L);
        User viktor = User.builder().username("Viktor").email("Viktor@viktor.com").password("XXX").build();
        viktor = userService.saveUser(viktor);
        User fanny = User.builder().username("Fanny").email("Fanny@fanny.com").password("XXX").build();
        fanny = userService.saveUser(fanny);

        //Viktor jelzi az igényt hogy menne inni a kitalációba Fannyval.
        Date now = new Date();
        userService.palRequest(RemotePalRequestVO.builder().date(now).pubId(kitalacioPub.getId()).userId(viktor.getId()).requestedUserId(fanny.getId()).build());
        //Fannynak megérkezik a kérés a listájába hogy Viktor iszni vele
        fanny = userService.findByEmail("Fanny@fanny.com");
        Map<Long, PalRequest> fannyPals = fanny.getActualPals();
        System.out.println(fannyPals);
        PalRequest request = fannyPals.get(viktor.getId());
        Assert.assertNotNull(request);
        Assert.assertEquals(now, request.getDate());
        Assert.assertEquals(kitalacioPub, request.getPub());
        Assert.assertFalse(request.isAccepted());

        //első lehetőség Fanny elfogadja.
        userService.acceptRequest(RemotePalAcceptVO.builder().accepted(true).userId(fanny.getId()).requestedUserId(viktor.getId()).build());
        viktor = userService.findByEmail("Viktor@viktor.com");

        Map<Long, PalRequest> viktorPals = viktor.getActualPals();
        PalRequest palRequest = viktorPals.get(fanny.getId());

        Assert.assertNotNull(palRequest);
        Assert.assertTrue(viktorPals.containsKey(fanny.getId()));
        Assert.assertEquals(now, palRequest.getDate());
        Assert.assertEquals(kitalacioPub, palRequest.getPub());
        Assert.assertTrue(palRequest.isAccepted());

    }

    @Test
    public void testPalRequestAndDenniesRequestCombination() {
        Pub kitalacioPub = pubDao.findById(1L);
        System.out.println(kitalacioPub.getId());
        User viktor = User.builder().username("Viktor1").email("Viktor1@viktor.com").password("XXX").build();
        viktor = userService.saveUser(viktor);
        User fanny = User.builder().username("Fanny1").email("Fanny1@fanny.com").password("XXX").build();
        fanny = userService.saveUser(fanny);

        //Viktor jelzi az igényt hogy menne inni a kitalációba Fannyval.
        Date now = new Date();
        userService.palRequest(RemotePalRequestVO.builder().date(now).pubId(kitalacioPub.getId()).userId(viktor.getId()).requestedUserId(fanny.getId()).build());
        //Fannynak megérkezik a kérés a listájába hogy Viktor iszni vele
        fanny = userService.findByEmail("Fanny1@fanny.com");
        Map<Long, PalRequest> fannyPals = fanny.getActualPals();
        System.out.println(fannyPals);
        PalRequest request = fannyPals.get(viktor.getId());
        Assert.assertNotNull(request);
        Assert.assertEquals(now, request.getDate());
        Assert.assertEquals(kitalacioPub, request.getPub());
        Assert.assertFalse(request.isAccepted());

        //másodk lehetőség Fanny elutasítja.
        userService.acceptRequest(RemotePalAcceptVO.builder().accepted(false).userId(fanny.getId()).requestedUserId(viktor.getId()).build());
        viktor = userService.findByEmail("Viktor1@viktor.com");
        Map<Long, PalRequest> viktorPals = viktor.getActualPals();
        fanny = userService.findByEmail("Fanny1@fanny.com");
        Map<Long, PalRequest> fannyPalsAfterDeny = fanny.getActualPals();
        Assert.assertFalse(viktorPals.containsKey(fanny.getId()));
        Assert.assertFalse(fannyPalsAfterDeny.containsKey(viktor.getId()));
    }

    private User buildTestUser() {
        return User.builder().username("tesztUser").email("looking@test.com").password("Palss").loggedIn(false)
                .lastKnownCoordinate(ORIGO).build();
    }

    private RemoteUserVO buildTestRemoteUser() {
        return RemoteUserVO.builder().name("tesztUser").city("Debrecen")
                .boozes(Arrays.asList(DrinkVO.builder().name("booze").drinkType(DrinkTypeEnum.BRANDY).build()))
                .pubs(Arrays.asList("pub")).savedDates(Arrays.asList(new Date())).searchRadius(10).priceCategory(20)
                .build();
    }
}
