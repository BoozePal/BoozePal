package hu.deik.boozepal.service.impl.test;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.core.repo.DrinkRepository;
import hu.deik.boozepal.core.repo.DrinkTypeRepository;
import hu.deik.boozepal.service.DrinkTypeService;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.service.impl.DrinkTypeServiceImpl;
import hu.deik.boozepal.service.impl.UserServiceImpl;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;
import hu.deik.boozepal.service.statistics.impl.AbstractStatisticsService;
import hu.deik.boozepal.service.statistics.impl.DrinkStatisticsServiceImpl;

@RunWith(Arquillian.class)
public class DrinkStatisticsServiceIT extends ArquillianContainer {

    private static Class<?>[] testClasses = { UserService.class, UserServiceImpl.class, DrinkTypeService.class,
            DrinkTypeServiceImpl.class, DrinkTypeRepository.class, DrinkRepository.class, DrinkStatisticsVO.class,
            DrinkStatisticsService.class, DrinkStatisticsServiceImpl.class, AbstractStatisticsService.class };

    @Deployment
    public static Archive<WebArchive> createDeployment() {
        Archive<WebArchive> deployment = ArquillianContainer.createDeployment();
        ((ClassContainer<WebArchive>) deployment).addClasses(testClasses);
        return deployment;
    }

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
        DrinkTypeEnum theFavouriteDrinkType = drinkStatisticsService.getTheFavouriteDrinkType();
        Assert.assertEquals(DrinkTypeEnum.RUM, theFavouriteDrinkType);
    }

    @Test
    public void testGetDrinkStatistics() {
        // given

        // drinks in db
        // users in db
        // drink types in db
        DrinkStatisticsVO beerTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.BEER, 2);
        DrinkStatisticsVO rumTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.RUM, 3);
        DrinkStatisticsVO vodkaTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.VODKA, 0);
        DrinkStatisticsVO champagneTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.CHAMPAGNE, 0);
        DrinkStatisticsVO brandyTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.BRANDY, 0);
        DrinkStatisticsVO ginTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.GIN, 0);
        DrinkStatisticsVO wine = new DrinkStatisticsVO(DrinkTypeEnum.WINE, 0);
        DrinkStatisticsVO whiskey = new DrinkStatisticsVO(DrinkTypeEnum.WHISKEY, 0);
        DrinkStatisticsVO unknown = new DrinkStatisticsVO(DrinkTypeEnum.UNKNOWN, 0);

        // when
        List<DrinkStatisticsVO> drinkStatistics = drinkStatisticsService.getDrinkStatistics();

        // then
        Assert.assertThat(Arrays.asList(beerTypeStat, rumTypeStat, vodkaTypeStat, champagneTypeStat, wine, whiskey,
                unknown, ginTypeStat, brandyTypeStat), Matchers.containsInAnyOrder(drinkStatistics.toArray()));

    }

    @Test
    public void testGetDrinksTopList() {
        List<DrinkStatisticsVO> topList = drinkStatisticsService.getDrinksTopList();
        // Beer type
        DrinkStatisticsVO beerTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.BEER, 2);
        // Rum type
        DrinkStatisticsVO rumTypeStat = new DrinkStatisticsVO(DrinkTypeEnum.RUM, 3);
        // Assert in ORDER
        Assert.assertEquals(Arrays.asList(rumTypeStat, beerTypeStat), topList);
    }
}
