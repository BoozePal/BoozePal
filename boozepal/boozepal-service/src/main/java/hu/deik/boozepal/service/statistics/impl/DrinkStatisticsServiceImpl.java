package hu.deik.boozepal.service.statistics.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.core.repo.DrinkTypeRepository;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;

/**
 * Az ital statiszikákat elkészítő szolgáltatás implementációja.
 *
 */
@Stateless
@Local(DrinkStatisticsService.class)
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class DrinkStatisticsServiceImpl extends AbstractStatisticsService<Drink> implements DrinkStatisticsService {

    /**
     * Osztály loggere.
     */
    private static Logger logger = LoggerFactory.getLogger(DrinkStatisticsServiceImpl.class);

    /**
     * Ital típusokat kezelő adathozzáférési osztály.
     */
    @Autowired
    private DrinkTypeRepository drinkTypDao;

    /**
     * Felhasználókat elérő adathozzáférési osztály.
     */
    @Autowired
    private UserRepository userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public DrinkTypeEnum getTheFavouriteDrinkType() {
        logger.info("Getting favourite DrinkType");
        Map<DrinkTypeEnum, Integer> sortByValue = collectDrinksInOrderOfTotal();
        Iterator<Entry<DrinkTypeEnum, Integer>> iterator = sortByValue.entrySet().iterator();
        if (iterator.hasNext()) {
            Entry<DrinkTypeEnum, Integer> next = iterator.next();
            return next.getKey();
        } else {
            return DrinkTypeEnum.UNKNOWN;
        }
    }

    private Map<DrinkTypeEnum, Integer> collectDrinksInOrderOfTotal() {
        List<User> users = userDao.findByRoleUser();
        Map<DrinkTypeEnum, Integer> drinkTypeMap = new HashMap<>();
        collectDrinksType(users, drinkTypeMap);
        Map<DrinkTypeEnum, Integer> sortByValue = sortByValue(drinkTypeMap);
        return sortByValue;
    }

    private void collectDrinksType(List<User> users, Map<DrinkTypeEnum, Integer> drinkTypeMap) {
        for (User user : users) {
            List<Drink> favouriteDrinks = user.getFavouriteDrinks();
            for (Drink drink : favouriteDrinks) {
                DrinkTypeEnum drinkType = drink.getDrinkType();
                incrementTypeNumberInMap(drinkTypeMap, drinkType);
            }
        }
    }

    private void incrementTypeNumberInMap(Map<DrinkTypeEnum, Integer> drinkTypeMap, DrinkTypeEnum drinkType) {
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
        List<DrinkTypeEnum> allDrinkTypes = Arrays.asList(DrinkTypeEnum.values());
        List<DrinkStatisticsVO> result = allDrinkTypes.stream().map(this::mapToDrinkStatisticsVO)
                .collect(Collectors.toList());
        return result;
    }

    private DrinkStatisticsVO mapToDrinkStatisticsVO(DrinkTypeEnum drinkType) {
        Integer numberOfDrinkType = drinkTypDao.getNumberOfDrinkType(drinkType);
        logger.info("Map {} to VO with {} number of drink types", drinkType, numberOfDrinkType);
        return new DrinkStatisticsVO(drinkType, numberOfDrinkType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DrinkStatisticsVO> getDrinksTopList() {
        List<DrinkStatisticsVO> result = new ArrayList<DrinkStatisticsVO>();
        Map<DrinkTypeEnum, Integer> collectDrinksInOrderOfTotal = collectDrinksInOrderOfTotal();
        Iterator<Entry<DrinkTypeEnum, Integer>> iterator = collectDrinksInOrderOfTotal.entrySet().iterator();
        fillResultList(result, iterator);
        return result;
    }

    private void fillResultList(List<DrinkStatisticsVO> result, Iterator<Entry<DrinkTypeEnum, Integer>> iterator) {
        while (iterator.hasNext()) {
            Entry<DrinkTypeEnum, Integer> nextDrink = iterator.next();
            result.add(new DrinkStatisticsVO(nextDrink.getKey(), nextDrink.getValue()));
        }
    }

}
