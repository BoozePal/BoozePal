package hu.deik.boozepal.dao.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.core.repo.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class RoleRepositoryTest {

    private static final String ROLENAME = "ROLE_USER";

    @Autowired
    private RoleRepository roleDao;

    private Role role;

    @Test
    public void testRoleDaoNotNull() {
        Assert.assertNotNull(roleDao);
    }

    @Test
    public void testSaveNewRole() {
        role = createRole();
        Role savedRole = roleDao.save(role);
        Assert.assertNotNull(savedRole.getId());
    }

    @Test
    public void testFindSavedUserByUserName() {
        role = createRole();
        roleDao.save(role);
        Role role = roleDao.findByRoleName(ROLENAME);
        Assert.assertEquals(ROLENAME, role.getRoleName());
    }

    private Role createRole() {
        return Role
                .builder()
                .roleName(ROLENAME)
                .build();
    }
    
    @After
    public void tearDown() {
        if (role != null && role.getId() != null)
            roleDao.delete(role);
    }
}
