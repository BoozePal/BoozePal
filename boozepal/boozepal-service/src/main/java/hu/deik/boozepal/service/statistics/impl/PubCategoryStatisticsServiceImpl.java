package hu.deik.boozepal.service.statistics.impl;

import hu.deik.boozepal.common.entity.*;
import hu.deik.boozepal.common.vo.*;
import hu.deik.boozepal.core.repo.*;
import hu.deik.boozepal.service.statistics.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.ejb.interceptor.*;

import javax.ejb.*;
import javax.interceptor.*;
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
        Map<Pub, Integer> allPabs = new HashMap<> ();
        List<PubCategoryVO> listOfPubs = new ArrayList<> ();
        for (User user : allUsers) {
            for (Pub pub : user.getFavouritePub ()) {
                if (allPabs.get (pub) == null)
                    allPabs.put (pub, 0);
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
