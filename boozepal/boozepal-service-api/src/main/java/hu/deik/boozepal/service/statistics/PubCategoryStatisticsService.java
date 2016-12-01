package hu.deik.boozepal.service.statistics;

import hu.deik.boozepal.common.vo.PubCategoryVO;

import javax.ejb.Local;
import java.util.List;

/**
 * Kocsmákról készülő statisztikákat leíró interfész.
 */
@Local
public interface PubCategoryStatisticsService {

    /**
     * Visszaadja az összes kocsma nevét és a hozzá tartozó like-ok számát.
     *
     * @return Kocsmák és értékelésük.
     */
    List<PubCategoryVO> getAllPubsCategoryStatistics();

}
