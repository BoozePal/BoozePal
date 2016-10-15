package hu.deik.boozepal.dao.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.core.repo.PubRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class PubRepositoryTest {

    private static final String PUB_NAME = "Kiskocsma";

    @Autowired
    private PubRepository pubDao;

    private Pub pub;

    @Test
    public void testPubDaoNotNull() {
        Assert.assertNotNull(pubDao);
    }

    @Test
    public void testSaveNewPub() {
        pub = createPub();
        Pub savedPub = pubDao.save(pub);
        Assert.assertNotNull(savedPub.getId());
    }

    @Test
    public void testFindSavedPubByName() {
        pub = createPub();
        pubDao.save(pub);
        Pub pub = pubDao.findByName(PUB_NAME);
        Assert.assertEquals(PUB_NAME, pub.getName());
    }

    private Pub createPub() {
        return Pub.builder().name(PUB_NAME).openHours("Minden nap 8-24").priceCategory(3).build();
    }

    @After
    public void tearDown() {
        if (pub != null && pub.getId() != null)
            pubDao.delete(pub);
    }
}
