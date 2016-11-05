package hu.deik.boozepal.service.impl.test;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.rest.service.UserServiceRest;

@RunWith(Arquillian.class)
public class UserServiceRestIT extends ArquillianContainer {
    private static final String ENCODED_PASSWORD = "EncodedPassword";
    private static final String TEST_ADMIN = "TestAdmin";
    private static final String TEST_ADMIN_RESERVED = "TestAdminReserved";

    @EJB
    private UserServiceRest userService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreateOrLoginUser() {
        // TODO implement
    }

    @Test
    public void testLogoutUserLogically() {
        // TODO implement
    }

    @Test
    public void testGetUsersInGivenRadiusAndCoordinate() {
        
    }

}
