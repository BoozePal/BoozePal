package hu.deik.boozepal.dao.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.Coordinate;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class UserRepositoryTest {

    private static final String TEST_EMAIL_COM = "Test@email.com";
    private static final String TEST_PASSWORD = "TestPassword";
    private static final String TEST_USER = "TestUser";

    private User user;

    @Autowired
    private UserRepository userDao;

    @Test
    public void testUserDaoNotNull() {
        Assert.assertNotNull(userDao);
    }

    @Test
    public void testSaveNewUser() {
        user = createUser();
        User savedUser = userDao.save(user);
        Assert.assertNotNull(savedUser.getId());
    }

    @Test
    public void testFindSavedUserByUserName() {
        user = createUser();
        userDao.save(user);
        User foundUser = userDao.findByUsername(TEST_USER);
        Assert.assertEquals(TEST_USER, foundUser.getUsername());
    }

    @Test
    public void testFindSavedUserByEmail() {
        user = createUser();
        userDao.save(user);
        User foundUser = userDao.findByEmail(TEST_EMAIL_COM);
        Assert.assertEquals(TEST_EMAIL_COM, foundUser.getEmail());
    }

    @Test
    public void testUpdateUserCoordinate() {
        user = createUser();
        User savedUser = userDao.save(user);
        double latitude = 11.0;
        double altitude = 12.0;
        userDao.updateUserCoordinate(latitude, altitude, savedUser.getId());
        User findOne = userDao.findOne(savedUser.getId());
        Assert.assertEquals(latitude, findOne.getLastKnownCoordinate().getLatitude(), 0.0);
        Assert.assertEquals(altitude, findOne.getLastKnownCoordinate().getAltitude(), 0.0);
        // userDao.delete(savedUser.getId());
    }

    @Test
    public void testUpdatePriceCategory() {
        user = createUser();
        User savedUser = userDao.save(user);
        int priceCategory = 4;
        userDao.updatePriceCategory(priceCategory, savedUser.getId());
        User findOne = userDao.findOne(savedUser.getId());
        Assert.assertEquals(priceCategory, findOne.getPriceCategory(), 0.0);
        // userDao.delete(savedUser.getId());
    }

    @Test
    public void testActivateUser() {
        user = createUser();
        User savedUser = userDao.save(user);
        userDao.activateUser(savedUser.getId());
        User findOne = userDao.findOne(savedUser.getId());
        Assert.assertFalse(findOne.isRemove());
    }

    @Test
    public void testDeActivateUser() {
        user = createUser();
        User savedUser = userDao.save(user);
        userDao.deactivateUser(savedUser.getId());
        User findOne = userDao.findOne(savedUser.getId());
        Assert.assertTrue(findOne.isRemove());
    }

    @After
    public void tearDown() {
        if (user != null && user.getId() != null)
            userDao.delete(user);
    }

    //@formatter:off
    private User createUser() {
        return User.builder()
                .username(TEST_USER)
                .email(TEST_EMAIL_COM)
                .password(TEST_PASSWORD)
                .remove(false)
                .lastKnownCoordinate(new Coordinate(10.0, 10.0))
                .priceCategory(3)
                .build();
    }
   //@formatter:on

}
