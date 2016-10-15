package hu.deik.boozepal.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.deik.boozepal.common.entity.DrinkType;

/**
 * Ital típusokat kezelőt adathozzáférési szolgáltatás.
 * 
 * @version 1.0
 *
 */
@Repository
public interface DrinkTypeRepository extends JpaRepository<DrinkType, Long> {

    /**
     * Ital típus keresése név alapján.
     * 
     * @param name
     *            a keresendő ital típus neve.
     * @return a megtalált ital típus entitás.
     */
    DrinkType findByName(String name);

}
