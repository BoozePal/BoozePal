package hu.deik.boozepal.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Fogadókat, sörözőket reprezentáló entitás.
 * 
 * @version 1.0
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_pub")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Pub extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * Város.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Söröző címe.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    /**
     * Nyitvatartási idő, szövegesen a hét minden napján.
     */
    @Column(nullable = false, length = 256)
    private String openHours;

    /**
     * Árkategória, 0-5 terjedő skálán.
     */
    @Column(nullable = false)
    private Integer priceCategory;

}
