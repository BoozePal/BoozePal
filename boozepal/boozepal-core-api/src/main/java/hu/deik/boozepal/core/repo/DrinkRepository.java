package hu.deik.boozepal.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.entity.DrinkType;

/**
 * Italokat kezelőt adathozzáférési szolgáltatás.
 * 
 * @version 1.0
 *
 */
@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    /**
     * Ital keresése név alapján.
     * 
     * @param name
     *            a keresendő ital neve.
     * @return a megtalált ital.
     */
    Drink findByName(String name);

    /**
     * Ital keresése típus alapján.
     * 
     * @param drinkType
     *            ital típusa.
     * @return a keresett típussal rendelkező italok listája.
     */
    List<Drink> findByDrinkType(DrinkType drinkType);

}
