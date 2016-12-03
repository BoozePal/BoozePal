package hu.deik.boozepal.common.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boozepal_user")
@Data
@EqualsAndHashCode(callSuper = false, of = { "username", "email" })
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
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    /**
     * Felhasználó kedvenc itala.
     */
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "boozepal_user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "drink_id", referencedColumnName = "id"))
    private List<Drink> favouriteDrinks;

    /**
     * Felhasználó kedvenc sörözője.
     */
    @ManyToMany
    private List<Pub> favouritePub;

    /**
     * Preferált árkategória, 0-5 terjedő skálán.
     */
    @Column
    private int priceCategory;

    /**
     * Felhasználó lakóhelye.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    /**
     * Utolsó ismert tartózkodiási hely.
     */
    @Embedded
    private Coordinate lastKnownCoordinate;

    /**
     * Keresési sugár, km-ben megadva.
     */
    private int searchRadius;

    /**
     * Aktuális cimborák.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "boozepal_pal_requests")
    @MapKeyColumn(name = "id")
    private Map<Long, PalRequest> actualPals;

    /**
     * Bejelentkezett-e éppen.
     */
    private boolean loggedIn;

    /**
     * Felhasználó "ráérési" táblája amibe beírja mikor érhető el.
     */
    @ElementCollection
    @CollectionTable(name = "boozepal_user_timeBoards", joinColumns = @JoinColumn(name = "user_id"))
    private List<Date> timeBoard;

    private Date lastLoggedinTime;

    public Map<Long, PalRequest> getActualPals() {
        if (actualPals == null) {
            actualPals = new HashMap<>();
            return actualPals;
        } else
            return actualPals;
    }


}
