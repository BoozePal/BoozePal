package hu.deik.boozepal.rest.service;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.core.repo.PubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * A távoli felhasználók igényeit megvalósító szolgáltatás.
 */
@Stateless
@Local(PubServiceRest.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PubServiceRestImpl implements PubServiceRest {

    /**
     * Kocsmákat elérő adathozzáférési osztály.
     */
    @Autowired
    private PubRepository pubDao;


    @Override
    public List<Pub> getAllPubs() {
        return pubDao.findAll();
    }
}
