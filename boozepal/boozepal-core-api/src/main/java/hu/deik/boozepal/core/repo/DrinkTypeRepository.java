package hu.deik.boozepal.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Visszaadja a keresett ital számát, azaz hogy a keresendő ital hányszor
     * jelenik meg az emberek kedvenc italai között.
     * 
     * @param drink
     *            a keresendő ital.
     * @return a pontos szám.
     */
    @Query(value = "SELECT COUNT(u) FROM User u join u.favouriteDrinks d where d.drinkType = :drinkType")
    Integer getNumberOfDrinkType(@Param("drinkType") DrinkType drinkType);

}
