package hu.deik.boozepal.rest.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkVO;
import hu.deik.boozepal.core.repo.DrinkRepository;
import hu.deik.boozepal.helper.DrinkConverter;

/**
 * Az italok listázásása során használt szolgáltatás megvalósítása.
 *
 */
@Stateless
@Local(DrinkServiceRest.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DrinkServiceRestImpl implements DrinkServiceRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrinkServiceRestImpl.class);

    /**
     * Az italokat kezelő adathozzáférési objektum.
     */
    @Autowired
    private DrinkRepository drinkDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DrinkVO> getAllDrinks() {
        LOGGER.info("Összes ital lekérése.");
        List<Drink> drinks = drinkDao.getAll();
        LOGGER.debug("Lista mérete:{}",drinks.size());
        return DrinkConverter.toVO(drinks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DrinkVO> getAllDrinksByType(DrinkTypeEnum drinkType) {
        LOGGER.info("{} típusú italok lekérése.", drinkType.getValue());
        List<Drink> drinks = drinkDao.findByDrinkType(drinkType);
        LOGGER.debug("Lista mérete:{}",drinks.size());
        return DrinkConverter.toVO(drinks);
    }

}
