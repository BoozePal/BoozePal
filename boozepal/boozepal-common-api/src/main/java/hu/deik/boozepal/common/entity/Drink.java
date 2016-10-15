package hu.deik.boozepal.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Italokat reprezentáló entitás.
 * 
 * @version 1.0
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_drink")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Drink extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * Ital megnevezése.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Ital típusa.
     */
    @OneToOne(cascade = CascadeType.MERGE)
    private DrinkType drinkType;

}
