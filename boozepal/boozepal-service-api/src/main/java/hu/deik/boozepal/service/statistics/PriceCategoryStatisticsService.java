package hu.deik.boozepal.service.statistics;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.vo.PriceCategoryVO;

/**
 * Árkategóriákról készülő statisztikákat leíró interfész.
 *
 */
@Local
public interface PriceCategoryStatisticsService {

    /**
     * Visszaadja a kategóriánkénti szummázott összegeket.
     * 
     * @return kategóriánként szummázott összegek.
     */
    List<PriceCategoryVO> getPriceCategoryStatistics();

    /**
     * Visszaadja a legnagyobb értékkel rendelkező árkategória értékét.
     * 
     * @return a legnagyobb értékkel rendelkező árkategória értéke.
     */
    Integer getTheBiggestCategoryTotal();
}
