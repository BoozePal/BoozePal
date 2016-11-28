package hu.deik.boozepal.service.impl.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkVO;
import hu.deik.boozepal.rest.service.DrinkServiceRest;

@RunWith(Arquillian.class)
public class DrinkServiceRestIT extends ArquillianContainer {

    @EJB
    private DrinkServiceRest drinkService;

//    @Test
    public void testGetAllDrinks() {
        //given
        //Drinks in the db
        /*
         * insert into boozepal_drink(id,name,drinkType) values(1,'Borsodi','BEER');
            insert into boozepal_drink(id,name,drinkType) values(2,'Sör','BEER');
            insert into boozepal_drink(id,name,drinkType) values(3,'Habos sör','BEER');
            insert into boozepal_drink(id,name,drinkType) values(4,'Royal vodka','VODKA');
            insert into boozepal_drink(id,name,drinkType) values(5,'Gyengébb rum','RUM');
            insert into boozepal_drink(id,name,drinkType) values(6,'Közepes rum','RUM');
            insert into boozepal_drink(id,name,drinkType) values(7,'Eros rum','RUM');
         * */
        //when
        List<DrinkVO> allDrinks = drinkService.getAllDrinks();
        //then
        DrinkVO build = DrinkVO.builder().id(1L).name("Borsodi").drinkType(DrinkTypeEnum.BEER).build();
        DrinkVO build2 = DrinkVO.builder().id(2L).name("Sör").drinkType(DrinkTypeEnum.BEER).build();
        DrinkVO build3 = DrinkVO.builder().id(3L).name("Habor sör").drinkType(DrinkTypeEnum.BEER).build();
        DrinkVO build4 = DrinkVO.builder().id(4L).name("Royal Vodka").drinkType(DrinkTypeEnum.VODKA).build();
        DrinkVO build5 = DrinkVO.builder().id(5L).name("Gyengébb rum").drinkType(DrinkTypeEnum.RUM).build();
        DrinkVO build6 = DrinkVO.builder().id(6L).name("Közepes rum").drinkType(DrinkTypeEnum.RUM).build();
        DrinkVO build7 = DrinkVO.builder().id(7L).name("Eros rum").drinkType(DrinkTypeEnum.RUM).build();
        Assert.assertThat(Arrays.asList(build,build2,build3,build4,build5,build6,build7), Matchers.containsInAnyOrder(allDrinks.toArray()));
    }
    
//    @Test
    public void testGetAllDrinksByType() {
        //given
        //Drinks in the db
        /*
         * insert into boozepal_drink(id,name,drinkType) values(1,'Borsodi','BEER');
            insert into boozepal_drink(id,name,drinkType) values(2,'Sör','BEER');
            insert into boozepal_drink(id,name,drinkType) values(3,'Habos sör','BEER');
            insert into boozepal_drink(id,name,drinkType) values(4,'Royal vodka','VODKA');
            insert into boozepal_drink(id,name,drinkType) values(5,'Gyengébb rum','RUM');
            insert into boozepal_drink(id,name,drinkType) values(6,'Közepes rum','RUM');
            insert into boozepal_drink(id,name,drinkType) values(7,'Eros rum','RUM');
         * */
        //when
        List<DrinkVO> allDrinks = drinkService.getAllDrinksByType(DrinkTypeEnum.VODKA);
        //then
        DrinkVO royal = DrinkVO.builder().id(4L).name("Royal Vodka").drinkType(DrinkTypeEnum.VODKA).build();
        Assert.assertThat(Arrays.asList(royal), Matchers.containsInAnyOrder(allDrinks.toArray()));
    }

}
