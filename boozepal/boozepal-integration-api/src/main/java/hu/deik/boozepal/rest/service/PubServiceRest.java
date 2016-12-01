package hu.deik.boozepal.rest.service;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.vo.PubVO;

import javax.ejb.Local;
import java.util.List;

/**
 * Kocsma szolgáltatás az Android kliens által használt funkciókra.
 *
 * @version 1.3
 */
@Local
public interface PubServiceRest {

    /**
     * Összes kocsma visszaadása.
     *
     * @return list of pubs
     */
    List<PubVO> getAllPubs();


}
