package hu.deik.boozepal.common.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GPS koordinátát reprezenáltó osztály.
 * 
 * @author Nandi
 *
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinate {

    /**
     * Szélesség.
     */
    private Double latitude;

    /**
     * Magasság.
     */
    private Double longitude;
}
