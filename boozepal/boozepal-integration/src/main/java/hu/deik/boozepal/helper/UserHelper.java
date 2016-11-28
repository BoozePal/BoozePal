package hu.deik.boozepal.helper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.Address;
import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.common.vo.DrinkVO;
import hu.deik.boozepal.core.repo.DrinkRepository;
import hu.deik.boozepal.core.repo.PubRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

/**
 * Created by Pusinszky on 2016. 11. 22..
 */
@Singleton
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userDao;

    @Autowired
    private DrinkRepository drinkDao;

    @Autowired
    private PubRepository pubDao;

    public User remoteUserVoToUserEntityByGoogleToken(final RemoteUserVO remoteUserVO, final String email)
            throws UserDetailsUpdateException {
        User user = userDao.findByEmail(email);
        if (user == null || remoteUserVO == null) {
            logger.info("User is null");
            throw new UserDetailsUpdateException("User is null");
        } else {
            logger.info("User {}", user.toString());
            return mapper(remoteUserVO, user);
        }
    }

    @Deprecated
    public User remoteUserVoToUserEntityById(final RemoteUserVO remoteUserVO) throws UserDetailsUpdateException {
        User user = userDao.findById(remoteUserVO.getId());
        if (user == null) {
            throw new UserDetailsUpdateException("User is null");
        } else {
            logger.info("User : {}", user.getUsername());
            return mapper(remoteUserVO, user);
        }
    }

    public boolean updateUserDates(final String email, final List<Date> timeBoards) throws UserDetailsUpdateException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UserDetailsUpdateException("User is null");
        } else {
            logger.info("User : {}", user.getUsername());
            user.setTimeBoard(timeBoards);
            userDao.save(user);
        }
        return true;
    }

    private User mapper(final RemoteUserVO remoteUserVO, User user) {
        if (remoteUserVO.getName() != null)
            user.setUsername(remoteUserVO.getName());
        if (remoteUserVO.getCity() != null)
            if (user.getAddress() == null)
                user.setAddress(Address.builder().town(remoteUserVO.getCity()).build());
            else {
                user.getAddress().setTown(remoteUserVO.getCity());
            }
        user.setPriceCategory(remoteUserVO.getPriceCategory());
        user.setSearchRadius(remoteUserVO.getSearchRadius());
        if (remoteUserVO.getSavedDates() != null)
            user.setTimeBoard(remoteUserVO.getSavedDates());
        if (remoteUserVO.getBoozes() != null)
            user.setFavouriteDrinks(getRemoteUserFavoritDrinks(remoteUserVO));
        if (remoteUserVO.getPubs() != null)
            user.setFavouritePub(getRemoteUserPubs(remoteUserVO));
        if (remoteUserVO.getMyPals() != null)
            user.setActualPals(getActualUsersList(remoteUserVO));
        logger.info("save update user");
        return userDao.save(user);
    }

    private List<Pub> getRemoteUserPubs(final RemoteUserVO remoteUserVO) {
        List<Pub> pubs = new LinkedList<>();
        for (String pubName : remoteUserVO.getPubs()) {
            logger.info("Pub name: " + pubName);
            // TODO A pub értékeit vagy be kell majd szettelni normálisan vagy
            // ki kell venni a null checket!
            Pub savedPub = pubDao.saveAndFlush(Pub.builder().name(pubName).openHours("24:00").priceCategory(5).build());
            pubs.add(savedPub);
        }
        return pubs;
    }

    private List<User> getActualUsersList(final RemoteUserVO remoteUserVO) {
        List<User> users = new LinkedList<>();
        for (RemoteUserVO user : remoteUserVO.getMyPals()) {
            logger.info("Pal name: " + user.getName());
            users.add(userDao.findById(user.getId()));
        }
        return users;
    }

    private List<Drink> getRemoteUserFavoritDrinks(final RemoteUserVO remoteUserVO) {
        List<Drink> drinks = new LinkedList<>();
        for (DrinkVO drinksName : remoteUserVO.getBoozes()) {
            logger.info("Drink name: " + drinksName.getName());
            Drink drink = drinkDao.findByName(drinksName.getName());
            if(drink == null)
                drink = drinkDao.saveAndFlush(Drink.builder().name(drinksName.getName()).drinkType(drinksName.getDrinkType()).build());
            drinks.add(drink);
        }
        return drinks;
    }
}
