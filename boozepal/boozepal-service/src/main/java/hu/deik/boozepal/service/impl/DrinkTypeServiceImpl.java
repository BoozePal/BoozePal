package hu.deik.boozepal.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.core.repo.DrinkTypeRepository;
import hu.deik.boozepal.service.DrinkTypeService;

/**
 * 
 *
 */
@Stateless
@Local(DrinkTypeService.class)
@Interceptors({ SpringBeanAutowiringInterceptor.class })
public class DrinkTypeServiceImpl implements DrinkTypeService {

    /**
     * 
     */
    @Autowired
    private DrinkTypeRepository drinkTypeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public DrinkType findByName(String name) {
        return drinkTypeRepository.findByName(name);
    }

}
