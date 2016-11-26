package hu.deik.boozepal.service.statistics.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.vo.PriceCategoryVO;
import hu.deik.boozepal.common.vo.PriceCategroy;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.statistics.PriceCategoryStatisticsService;

/**
 * Árkategóriákról készülő statisztikákat leíró szolgáltatás megvalósítása.
 *
 */
@Stateless
@Local(PriceCategoryStatisticsService.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PriceCategoryStatisticsServiceImpl implements PriceCategoryStatisticsService {

    /**
     * Felhasználókat elérő adathozzáférési osztály.
     */
    @Autowired
    private UserRepository userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PriceCategoryVO> getPriceCategoryStatistics() {
        List<PriceCategoryVO> categories = new ArrayList<>();
        fillResultList(categories);
        return categories;
    }

    private void fillResultList(List<PriceCategoryVO> categories) {
        for (PriceCategroy priceCategroy : PriceCategroy.values()) {
            Integer total = userDao.countGivenPriceCategory(priceCategroy.getValue());
            categories.add(PriceCategoryVO.builder().category(priceCategroy).total(total).build());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTheBiggestCategoryTotal() {
        List<PriceCategoryVO> categories = new ArrayList<>();
        fillResultList(categories);
        return categories.stream().max((c1, c2) -> Integer.compare(c1.getTotal(), c2.getTotal())).get().getTotal();
    }

}
