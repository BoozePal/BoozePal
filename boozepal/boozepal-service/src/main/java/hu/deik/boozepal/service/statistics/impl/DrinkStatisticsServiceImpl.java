package hu.deik.boozepal.service.statistics.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.core.repo.DrinkTypeRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;

/**
 * 
 *
 */
@Stateless
@Local(DrinkStatisticsService.class)
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class DrinkStatisticsServiceImpl extends AbstractStatisticsService<Drink> implements DrinkStatisticsService {

    /**
     * 
     */
    private static Logger logger = LoggerFactory.getLogger(DrinkStatisticsServiceImpl.class);

    /**
     * 
     */
    @Autowired
    private DrinkTypeRepository drinkTypDao;

    /**
     * 
     */
    @Autowired
    private UserRepository userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public DrinkType getTheFavouriteDrinkType() {
        logger.info("Getting favourite DrinkType");
        List<User> users = userDao.findByRoleUser();
        Map<DrinkType, Integer> drinkTypeMap = new HashMap<>();
        collectDrinksType(users, drinkTypeMap);

        Map<DrinkType, Integer> sortByValue = sortByValue(drinkTypeMap);
        Iterator<Entry<DrinkType, Integer>> iterator = sortByValue.entrySet().iterator();
        if (iterator.hasNext()) {
            Entry<DrinkType, Integer> next = iterator.next();
            return next.getKey();
        } else {
            return null;
        }
    }

    private void collectDrinksType(List<User> users, Map<DrinkType, Integer> drinkTypeMap) {
        for (User user : users) {
            List<Drink> favouriteDrinks = user.getFavouriteDrinks();
            for (Drink drink : favouriteDrinks) {
                DrinkType drinkType = drink.getDrinkType();
                incrementTypeNumberInMap(drinkTypeMap, drinkType);
            }
        }
    }

    private void incrementTypeNumberInMap(Map<DrinkType, Integer> drinkTypeMap, DrinkType drinkType) {
        Integer value = drinkTypeMap.get(drinkType);
        Integer pieces = value == null ? 0 : value;
        drinkTypeMap.put(drinkType, pieces + 1);
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DrinkStatisticsVO> getDrinkStatistics() {
        logger.info("Getting drink statistics.");
        List<DrinkType> allDrinkTypes = drinkTypDao.findAll();
        List<DrinkStatisticsVO> result = allDrinkTypes.stream().map(this::mapToDrinkStatisticsVO)
                .collect(Collectors.toList());
        return result;
    }

    private DrinkStatisticsVO mapToDrinkStatisticsVO(DrinkType drinkType) {
        Integer numberOfDrinkType = drinkTypDao.getNumberOfDrinkType(drinkType);
        logger.info("Map {} to VO with {} number of drink types", drinkType, numberOfDrinkType);
        return new DrinkStatisticsVO(drinkType, numberOfDrinkType);
    }

}
