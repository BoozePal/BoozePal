package hu.deik.boozepal.service.impl.test;

import static org.junit.Assert.fail;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.common.vo.MapUserVO;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.util.MapUserConveter;

@RunWith(Arquillian.class)
public class UserServiceIT extends ArquillianContainer {

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

}
