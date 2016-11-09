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
 * Felhasználóni jogokat reprezentáló entitás.
 * 
 * @version 1.0
 */
@Entity
@Table(name = "boozepal_role")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = { "roleName" })
@Builder
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Jog neve.
     */
    @Column(nullable = false, length = 64)
    private String roleName;
}
