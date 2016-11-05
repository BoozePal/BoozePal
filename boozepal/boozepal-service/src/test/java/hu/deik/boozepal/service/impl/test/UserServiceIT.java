package hu.deik.boozepal.service.impl.test;

import static org.junit.Assert.fail;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.service.UserService;

@RunWith(Arquillian.class)
public class UserServiceIT extends ArquillianContainer {

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

}
