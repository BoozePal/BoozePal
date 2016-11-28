package hu.deik.boozepal.helper;

import java.util.List;
import java.util.stream.Collectors;

import hu.deik.boozepal.common.entity.Drink;
import hu.deik.boozepal.common.vo.DrinkVO;

public class DrinkConverter {

    public static DrinkVO toVO(Drink drink) {
        if (drink != null)
            return DrinkVO.builder().id(drink.getId()).drinkType(drink.getDrinkType()).name(drink.getName()).build();
        else
            return null;
    }

    public static List<DrinkVO> toVO(List<Drink> drinks) {
        return drinks.stream().map(DrinkConverter::toVO).collect(Collectors.toList());
    }

}
