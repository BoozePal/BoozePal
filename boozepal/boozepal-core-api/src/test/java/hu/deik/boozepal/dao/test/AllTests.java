package hu.deik.boozepal.dao.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DrinkRepositoryTest.class, PubRepositoryTest.class, RoleRepositoryTest.class, UserRepositoryTest.class,
        AddressRepositoryTest.class })
public class AllTests {

}
