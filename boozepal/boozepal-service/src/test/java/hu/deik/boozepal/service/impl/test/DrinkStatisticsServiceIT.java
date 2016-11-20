package hu.deik.boozepal.service.impl.test;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.service.DrinkTypeService;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;

@RunWith(Arquillian.class)
public class DrinkStatisticsServiceIT extends ArquillianContainer {

    private static final String VODKA = "vodka";
    private static final String RUM = "rum";
    private static final String BEER = "beer";

    @EJB
    private UserService userService;

    @EJB
    private DrinkTypeService drinkTypeService;

    @EJB
    private DrinkStatisticsService drinkStatisticsService;

    @Test
    public void testGetTheFavouriteDrinkType() {
        DrinkType theFavouriteDrinkType = drinkStatisticsService.getTheFavouriteDrinkType();
        Assert.assertEquals(drinkTypeService.findByName(RUM), theFavouriteDrinkType);
    }

    @Test
    public void testGetDrinkStatistics() {
        // given
        // drinks in db
        // users in db
        // drink types in db
        // Beer type
        DrinkStatisticsVO beerTypeStat = new DrinkStatisticsVO(drinkTypeService.findByName(BEER), 2);
        // Rum type
        DrinkStatisticsVO rumTypeStat = new DrinkStatisticsVO(drinkTypeService.findByName(RUM), 3);
        // vodka type
        DrinkStatisticsVO vodkaTypeStat = new DrinkStatisticsVO(drinkTypeService.findByName(VODKA), 0);

        // when
        List<DrinkStatisticsVO> drinkStatistics = drinkStatisticsService.getDrinkStatistics();
        
        // then
        Assert.assertThat(Arrays.asList(beerTypeStat, rumTypeStat, vodkaTypeStat),
                Matchers.containsInAnyOrder(drinkStatistics.toArray()));

    }
}
