package hu.deik.boozepal.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Italokat típusokat reprenzetáló entitás.
 * 
 * @version 1.0
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_drinktype")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class DrinkType extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * Ital típusa.
     */
    @Column(nullable = false, length = 128)
    private String name;

}
