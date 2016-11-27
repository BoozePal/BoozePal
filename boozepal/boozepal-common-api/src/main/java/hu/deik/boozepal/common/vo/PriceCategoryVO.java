package hu.deik.boozepal.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ital árkategóriát reprezentáló érték osztály. 
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PriceCategoryVO {

    /**
     * Az árkategória értéke.
     */
    private PriceCategroy category;

    /**
     * Az árkategória szummázott száma.
     */
    private Integer total;
}
