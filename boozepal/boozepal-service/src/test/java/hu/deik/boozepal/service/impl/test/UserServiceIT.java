package hu.deik.boozepal.service.impl.test;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.common.vo.MapUserVO;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.service.impl.UserServiceImpl;
import hu.deik.boozepal.util.MapUserConveter;

@RunWith(Arquillian.class)
public class UserServiceIT extends ArquillianContainer {

    private static Class<?>[] testClasses = { UserService.class, UserServiceImpl.class, MapUserVO.class,
            RegistrationException.class, User.class, MapUserConveter.class };

    @Deployment
    public static Archive<WebArchive> createDeployment() {
        Archive<WebArchive> deployment = ArquillianContainer.createDeployment();
        ((ClassContainer<WebArchive>) deployment).addClasses(testClasses);
        return deployment;
    }

    private static final String ADMIN_WHO_WILL_NOT_BE_SHOWN_TO_MAP = "AdminWhoWillNotBeShownToMap";
    private static final String ENCODED_PASSWORD = "EncodedPassword";
    private static final String TEST_ADMIN = "TestAdmin";
    private static final String TEST_ADMIN_RESERVED = "TestAdminReserved";

    @EJB
    private UserService userService;

    @Test
    public final void testFindUserByName() {
        // ADMIN Felhasználó létezik mikor feláll a rendszer!
        User findUserByName = userService.findUserByName(BoozePalConstants.ADMIN);
        Assert.assertNotNull(findUserByName);
        Assert.assertEquals(BoozePalConstants.ADMIN, findUserByName.getUsername());
    }

    @Test
    public final void testCreateNewAdministrator() throws RegistrationException {
        // Given userService
        // When createNewAdministrator
        userService.createNewAdministrator(TEST_ADMIN, ENCODED_PASSWORD);
        // And findItByHisUsername
        User findUserByName = userService.findUserByName(TEST_ADMIN);
        // Then
        // Egyetlen egy joga van csak
        Assert.assertEquals(1, findUserByName.getRoles().size());
        // Az is csak a ROLE_ADMIN
        Assert.assertEquals(BoozePalConstants.ROLE_ADMIN, findUserByName.getRoles().get(0).getRoleName());
    }

    @Test(expected = RegistrationException.class)
    public final void testCreateNewAdministratorShouldThrowRegistrationException() throws RegistrationException {
        // Given userService
        // When createNewAdministrator
        userService.createNewAdministrator(TEST_ADMIN_RESERVED, ENCODED_PASSWORD);
        userService.createNewAdministrator(TEST_ADMIN_RESERVED, ENCODED_PASSWORD);
        // Then exception should have been thrown!
        fail("Exception should have been thrown!");

    }

    @Test
    public final void testGetOnlineUsersToMap() throws RegistrationException {
        // given
        // one admin user logged in.
        userService.createNewAdministrator(ADMIN_WHO_WILL_NOT_BE_SHOWN_TO_MAP, ENCODED_PASSWORD);
        User findUserByName = userService.findUserByName(ADMIN_WHO_WILL_NOT_BE_SHOWN_TO_MAP);
        findUserByName.setLoggedIn(true);
        userService.save(findUserByName);

        // one online user
        User onlineUser = userService.createNewUser("onlineUser", "onlineUser");
        onlineUser.setLoggedIn(true);
        userService.save(onlineUser);

        // one offline
        User offlineUser = userService.createNewUser("offlineUser", "offlineUser");
        offlineUser.setLoggedIn(false);
        userService.save(offlineUser);

        List<MapUserVO> onlineUsersToMap = userService.getOnlineUsersToMap();
        Assert.assertEquals(1, onlineUsersToMap.size());
        Assert.assertEquals(MapUserConveter.toMapUserVO(onlineUser), onlineUsersToMap.get(0));
    }

    @Test
    public final void testGetOnlineUsers() {
        // given two users
        User onlineUser = User.builder().username("oneOnlineUser").password(ENCODED_PASSWORD)
                .email("onlineuser@boozepal.com").loggedIn(true).build();
        userService.save(onlineUser);

        User offlineUser = User.builder().username("oneOfflineUser").password(ENCODED_PASSWORD)
                .email("offlineuser@boozepal.com").loggedIn(false).build();
        userService.save(offlineUser);

        // when
        List<User> onlineUsers = userService.getOnlineUsers();

        // then
        Assert.assertEquals(1, onlineUsers.size());
        Assert.assertThat(Arrays.asList(onlineUser), Matchers.containsInAnyOrder(onlineUsers.toArray()));
    }

    @Test
    public final void testFindAll() {
        // given some users, now two + the admin user who always exists!!
        User onlineUser = User.builder().username("user1").password(ENCODED_PASSWORD).email("user1@boozepal.com")
                .loggedIn(true).build();
        userService.save(onlineUser);

        User offlineUser = User.builder().username("user2").password(ENCODED_PASSWORD).email("user2@boozepal.com")
                .loggedIn(false).build();
        userService.save(offlineUser);

        User adminUser = userService.findUserByName(BoozePalConstants.ADMIN);

        // when
        List<User> allUser = userService.findAll();
        System.out.println(allUser);

        // then
        Assert.assertEquals(2 + 1, allUser.size());
        Assert.assertThat(Arrays.asList(onlineUser, offlineUser, adminUser),
                Matchers.containsInAnyOrder(allUser.toArray()));
    }

    @After
    public void tearDown() {
        userService.deleteAllUsers();
    }

}
