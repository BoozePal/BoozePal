package hu.deik.boozepal.dao.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.entity.DrinkType;
import hu.deik.boozepal.core.repo.DrinkRepository;
import hu.deik.boozepal.core.repo.DrinkTypeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-core-test.xml" })
@Rollback
public class DrinkRepositoryTest {

    @Autowired
    private DrinkRepository drinkDao;

    @Autowired
    private DrinkTypeRepository drinkTypeDao;

    @Test
    public void testPubDaoNotNull() {
        Assert.assertNotNull(drinkDao);
    }

    @Test
    public void testFindDrinkByType() {
//        List<Drink> createDrinks = createDrinks();
//        drinkDao.save(createDrinks);
        DrinkType beerType = drinkTypeDao.findByName("beer");
        List<Drink> beers = drinkDao.findByDrinkType(beerType);
        Assert.assertEquals(3, beers.size());

        DrinkType rumType = drinkTypeDao.findByName("rum");
        List<Drink> rums = drinkDao.findByDrinkType(rumType);
        Assert.assertEquals(3, rums.size());
        
        DrinkType vodkaType = drinkTypeDao.findByName("vodka");
        List<Drink> vodkas = drinkDao.findByDrinkType(vodkaType);
        Assert.assertEquals(1, vodkas.size());
    }
    
    @Test
    public void testGetNumberOfFavouriteDrink() {
        Integer numberOfFavouriteDrinkBorsodiSor = drinkDao.getNumberOfFavouriteDrink(drinkDao.findByName("Borsodi Sör"));
        Assert.assertEquals(Integer.valueOf(1), numberOfFavouriteDrinkBorsodiSor);
        
        Integer numberOfFavouriteDrinkHabosSor = drinkDao.getNumberOfFavouriteDrink(drinkDao.findByName("Habos sör"));
        Assert.assertEquals(Integer.valueOf(0), numberOfFavouriteDrinkHabosSor);
        
        Integer numberOfFavouriteDrinkErosRum = drinkDao.getNumberOfFavouriteDrink(drinkDao.findByName("Erős rum"));
        Assert.assertEquals(Integer.valueOf(2), numberOfFavouriteDrinkErosRum);
    }
    
    @Deprecated
    private List<Drink> createDrinks() {
        List<Drink> drinks = new ArrayList<Drink>();
        // 2 darab sörtípus létrehozása
        drinks.add(Drink.builder().name("Sör").drinkType(beerType()).build());
        drinks.add(Drink.builder().name("Habos sör").drinkType(beerType()).build());
        // 3 darab rumtípus
        drinks.add(Drink.builder().name("Erős rum").drinkType(rumType()).build());
        drinks.add(Drink.builder().name("Gyengébb rum").drinkType(rumType()).build());
        drinks.add(Drink.builder().name("Közepes rum").drinkType(rumType()).build());
        return drinks;
    }

    private DrinkType beerType() {
        DrinkType beerType = drinkTypeDao.findByName("beer");
        if (beerType == null) {
            DrinkType build = DrinkType.builder().name("beer").build();
            return drinkTypeDao.save(build);
        } else
            return beerType;
    }

    private DrinkType rumType() {
        DrinkType rumType = drinkTypeDao.findByName("rum");
        if (rumType == null) {
            DrinkType build = DrinkType.builder().name("rum").build();
            return drinkTypeDao.save(build);
        } else
            return rumType;
    }

}
