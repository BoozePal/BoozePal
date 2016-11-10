package hu.deik.boozepal.service.impl.test;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.contants.BoozePalConstants;
import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.RoleService;
import hu.deik.boozepal.service.UserService;

@RunWith(Arquillian.class)
public class StartupServiceIT extends ArquillianContainer {

    @Before
    public void setUp() throws Exception {
    }

    @EJB
    private UserService userService;

    @EJB
    private RoleService roleService;

    @Test
    public void testCreateDefaultApplicationContext() {
        // given
        // the system

        // when
        // it boots up

        // then
        // Admin user should exists.
        User admin = userService.findUserByName(BoozePalConstants.ADMIN);
        Assert.assertNotNull(admin);

        // admin role should exists
        Role adminRole = roleService.findByRoleName(BoozePalConstants.ROLE_ADMIN);
        Assert.assertNotNull(adminRole);

        // user role should exists
        Role userRole = roleService.findByRoleName(BoozePalConstants.ROLE_USER);
        Assert.assertNotNull(userRole);
    }

}
