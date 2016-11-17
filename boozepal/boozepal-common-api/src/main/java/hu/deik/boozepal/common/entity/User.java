package hu.deik.boozepal.common.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
@EqualsAndHashCode(callSuper = false, of = { "username", "fullName", "email" })
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
     * Felhasználó teljes neve.
     */
    @Column(length = 128)
    private String fullName;

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

    /**
     * Felhasználó kedvenc itala.
     */
    //TODO legyen lista
    @ManyToMany
    private List<Drink> favouriteDrinks;

    /**
     * Felhasználó kedvenc sörözője.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Pub favouritePub;

    /**
     * Preferált árkategória, 0-5 terjedő skálán.
     */
    @Column
    private Integer priceCategory;

    /**
     * Felhasználó lakóhelye.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    /**
     * Utolsó ismert tartózkodiási hely.
     */
    @Embedded
    private Coordinate lastKnownCoordinate;

    /**
     * Keresési sugár, km-ben megadva.
     */
    private Long searchRadius;

    /**
     * Aktuális cimborák.
     */
    @ManyToMany
    private List<User> actualPals;

    /**
     * Bejelentkezett-e éppen.
     */
    private boolean loggedIn;

    /**
     * Felhasználó "ráérési" táblája amibe beírja mikor érhető el.
     */
    //TODO legyen List<Date>
    @Column(length = 256)
    private String timeBoard;

}
