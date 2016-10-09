package hu.deik.boozepal.dao.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class UserRepositoryTest {

    private static final String TEST_EMAIL_COM = "Test@email.com";
    private static final String TEST_PASSWORD = "TestPassword";
    private static final String TEST_USER = "TestUser";

    @Autowired
    private UserRepository userDao;

    @Test
    public void testUserDaoNotNull() {
        Assert.assertNotNull(userDao);
    }

    @Test
    public void testSaveNewUser() {
        User user = createUser();
        User savedUser = userDao.save(user);
        Assert.assertNotNull(savedUser.getId());
        userDao.delete(savedUser.getId());
    }

    @Test
    public void testFindSavedUserByUserName() {
        User user = createUser();
        User savedUser = userDao.save(user);
        User foundUser = userDao.findByUsername(TEST_USER);
        Assert.assertEquals(TEST_USER, foundUser.getUsername());
        userDao.delete(savedUser.getId());
    }

    @Test
    public void testFindSavedUserByEmail() {
        User user = createUser();
        User savedUser = userDao.save(user);
        User foundUser = userDao.findByEmail(TEST_EMAIL_COM);
        Assert.assertEquals(TEST_EMAIL_COM, foundUser.getEmail());
        userDao.delete(savedUser.getId());
    }

    private User createUser() {
        return User.builder().username(TEST_USER).email(TEST_EMAIL_COM).password(TEST_PASSWORD).remove(false).build();
    }

}
