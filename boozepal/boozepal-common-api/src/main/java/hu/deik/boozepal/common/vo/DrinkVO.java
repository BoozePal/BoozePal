package hu.deik.boozepal.common.vo;

import java.io.Serializable;

import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = { "name", "drinkType" })
@ToString(callSuper = false, of = { "name", "drinkType" })
public class DrinkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Az ital azonosítója.
     */
    private Long id;

    /**
     * Ital megnevezése.
     */
    private String name;

    /**
     * Ital típusa.
     */
    private DrinkTypeEnum drinkType;
}
