package hu.deik.boozepal.common.vo;

import hu.deik.boozepal.common.entity.DrinkType;
import lombok.Value;

/**
 * A webes felületen való ital statisztikák megjelenítéséhez használt érték
 * osztály.
 * 
 * Segítségével a felületen a statisztikákhoz tudunk adni adatokat, a
 * {@code DrinkStatisticsVO#drinkType} az ital típust adja meg míg a
 * {@code DrinkStatisticsVO#total} azt a darabszámot amelyek az emberek által
 * került kiszámolásra, azaz hogy hányan szeretik az efféle ital típust.
 *
 */
@Value
public class DrinkStatisticsVO {
    /**
     * A vizsgált ital típus.
     */
    private final DrinkType drinkType;
    /**
     * A vizsált ital típus emberek által preferált darabszáma.
     */
    private final Integer total;
}
