package hu.deik.boozepal.common.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Felhasználót reprezentáló entitás.
 * 
 * @version 1.0
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_user")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = { "password" })
@Builder
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * Felhasználónév.
     */
    @Column(unique = true, length = 64)
    private String username;

    /**
     * Jelszó BCryptel titkosítva.
     */
    @Column(nullable = false, length = 64)
    private String password;

    /**
     * Felhasználó e-mail címe.
     */
    @Column(unique = true, length = 64)
    private String email;

    /**
     * Logikai törlés.
     */
    @Column(nullable = false)
    private boolean remove;

    /**
     * Felhasználó jogai.
     */
    @ManyToMany
    private List<Role> roles;
    
    

}
