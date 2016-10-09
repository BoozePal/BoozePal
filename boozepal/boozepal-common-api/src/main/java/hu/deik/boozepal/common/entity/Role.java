package hu.deik.boozepal.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Super class for Entities.
 * 
 * @author Viktor
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 64)
    private String roleName;
}
