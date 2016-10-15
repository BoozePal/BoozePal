package hu.deik.boozepal.dao.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.Address;
import hu.deik.boozepal.core.repo.AddressRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class AddressRepositoryTest {

    private static final String DOBERDÓ = "Doberdó";
    private static final String DEBRECEN = "Debrecen";

    @Autowired
    private AddressRepository addressDao;

    private Address address;

    @Test
    public void testRoleDaoNotNull() {
        Assert.assertNotNull(addressDao);
    }

    @Test
    public void testFindSavedAddressByTown() {
        address = createAddress();
        addressDao.save(address);
        List<Address> address = addressDao.findByTown(DEBRECEN);
        Assert.assertEquals(1, address.size());
        Assert.assertEquals(DEBRECEN, address.get(0).getTown());
    }
    
    @Test
    public void testFindSavedAddressByStreet() {
        address = createAddress();
        addressDao.save(address);
        List<Address> address = addressDao.findByStreet(DOBERDÓ);
        Assert.assertEquals(1, address.size());
        Assert.assertEquals(DOBERDÓ, address.get(0).getStreet());
    }

    private Address createAddress() {
        return Address.builder().town(DEBRECEN).street(DOBERDÓ).build();
    }

    @After
    public void tearDown() {
        if (address != null && address.getId() != null)
            addressDao.delete(address);
    }
}
