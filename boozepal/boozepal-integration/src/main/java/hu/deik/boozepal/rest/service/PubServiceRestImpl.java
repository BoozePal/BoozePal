package hu.deik.boozepal.rest.service;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.vo.PubVO;
import hu.deik.boozepal.core.repo.PubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.LinkedList;
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
    public List<PubVO> getAllPubs() {
        List<Pub> pubs = pubDao.findAll ();
        List<PubVO> remotePubs = mapper (pubs);
        return remotePubs;
    }

    private List<PubVO> mapper(List<Pub> pubs) {
        List<PubVO> remotePubs = new LinkedList<> ();
        for (Pub pub : pubs) {
            remotePubs.add (PubVO.builder ().id (pub.getId ()).name (pub.getName ()).town (pub.getAddress ().getTown ()).build ());
        }
        return remotePubs;
    }
}
