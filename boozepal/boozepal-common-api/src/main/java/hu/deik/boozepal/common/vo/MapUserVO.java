package hu.deik.boozepal.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A térképen megjelenő felhasználókat reprezentáló érték osztály.
 * 
 * @author Nandi
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class MapUserVO {

    /**
     * Felhasználó teljes neve.
     */
    private String username;

    /**
     * Szélesség.
     */
    private Double latitude;

    /**
     * Hosszúsági.
     */
    private Double longitude;
}
