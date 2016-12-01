package hu.deik.boozepal.service.statistics.impl;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.vo.PubCategoryVO;
import hu.deik.boozepal.core.repo.PubRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.statistics.PubCategoryStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.*;

/**
 * Kocsmákról készülő statisztikákat leíró szolgáltatás megvalósítása.
 */
@Stateless
@Local(PubCategoryStatisticsService.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PubCategoryStatisticsServiceImpl implements PubCategoryStatisticsService {

    /**
     * Felhasználókat elérő adathozzáférési osztály.
     */
    @Autowired
    private UserRepository userDao;

    @Override
    public List<PubCategoryVO> getAllPubsCategoryStatistics() {
        List<User> allUsers = userDao.findByRoleUser ();
        Map<Pub, Integer> allPabs = new TreeMap<> ();
        List<PubCategoryVO> listOfPubs = new ArrayList<> ();
        for (User user : allUsers) {
            for (Pub pub : user.getFavouritePub ()) {
                allPabs.putIfAbsent (pub, 0);
                allPabs.put (pub, allPabs.get (pub) + 1);
            }
        }
        for (Pub p : allPabs.keySet ()) {
            allPabs.get (p);
            listOfPubs.add (PubCategoryVO.builder ()
                    .pubName (p.getName ())
                    .totalLike (allPabs.get (p))
                    .build ());
        }
        return listOfPubs;
    }
}
