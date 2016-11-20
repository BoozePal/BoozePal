package hu.deik.boozepal.service;

import hu.deik.boozepal.common.entity.DrinkType;

/**
 * 
 *
 */
public interface DrinkTypeService {
    
    /**
     * 
     * @param name
     * @return
     */
    DrinkType findByName(String name);

}
