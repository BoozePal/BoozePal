package hu.deik.boozepal.rest.service;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkVO;

/**
 * Az italok listázásása során használt szolgáltatás.
 *
 */
@Local
public interface DrinkServiceRest {

    /**
     * Visszaadja az összes italt.
     * 
     * @return az összes ital listája.
     */
    List<DrinkVO> getAllDrinks();

    /**
     * Visszaadja az italok típus szerint.
     * 
     * @param drinkType
     *            a keresendő ital típus.
     * @return a keresett ital típus italai.
     */
    List<DrinkVO> getAllDrinksByType(DrinkTypeEnum drinkType);

}
