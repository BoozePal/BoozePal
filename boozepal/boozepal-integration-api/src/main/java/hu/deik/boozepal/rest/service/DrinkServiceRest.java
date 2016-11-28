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
     * 
     * @return
     */
    List<DrinkVO> getAllDrinks();

    /**
     * 
     * @param drinkType
     * @return
     */
    List<DrinkVO> getAllDrinksByType(DrinkTypeEnum drinkType);

}
