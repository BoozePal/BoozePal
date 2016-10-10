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
 * Cím-eket reprezentáló entitás.
 * 
 * @version 1.0
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_address")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Address extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * Város.
     */
    @Column(length = 128)
    private String town;

    /**
     * Utca
     */
    @Column(length = 128)
    private String street;

}
