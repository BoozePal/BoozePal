package hu.deik.boozepal.service.statistics;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;

/**
 * 
 *
 */
@Local
public interface DrinkStatisticsService {

    DrinkType getTheFavouriteDrinkType();

    List<DrinkStatisticsVO> getDrinkStatistics();

}
