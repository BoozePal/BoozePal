package hu.deik.boozepal.service.statistics;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;

/**
 * Az italok statisztikáját kezelő szolgáltatás.
 * 
 * Segítségével különböző információkat nyerhetünk a felhasználók által
 * kedvencek titulált italokkal kapcsolatban.
 *
 */
@Local
public interface DrinkStatisticsService {

    /**
     * Visszaadja a felhasználók által legjobban kedvelt ital típust.
     * 
     * @return a legkedveltebb ital típus.
     */
    DrinkTypeEnum getTheFavouriteDrinkType();

    /**
     * Ital statisztikák diagramon való megjelenítéshez.
     * 
     * @return italok diagaramon megjeleníthető formában.
     */
    List<DrinkStatisticsVO> getDrinkStatistics();

    /**
     * Italok toplistáját adja vissza.
     * 
     * @return italok rendezve a darabszám alapján.
     */
    List<DrinkStatisticsVO> getDrinksTopList();

}
