package hu.deik.boozepal.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.deik.boozepal.common.entity.Pub;

/**
 * Sörözőket kezelő adathozzáférési szolgáltatás.
 * 
 * @version 1.0
 *
 */
@Repository
public interface PubRepository extends JpaRepository<Pub, Long> {

    /**
     * Söröző keresése név alapján.
     * 
     * @param name
     *            söröző neve.
     * @return megtalált söröző entitás.
     */
    Pub findByName(String name);
    
    Pub findById(Long id);

}
