package hu.deik.boozepal.common.vo;

import hu.deik.boozepal.common.entity.Pub;
import lombok.*;

/**
 * Ital árkategóriát reprezentáló érték osztály.
 */
@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
public class PubCategoryVO {

    /**
     * A kocsma neve.
     */
    private String pubName;

    /**
     * A Like-olások száma.
     */
    private int totalLike;
}
