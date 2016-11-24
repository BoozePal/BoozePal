package hu.deik.boozepal.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.deik.boozepal.common.entity.User;

/**
 * Felhasználói műveleteket megvalósító adathozzáférési interfész deklarációja.
 * 
 * @version 1.0
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Felhasználó keresése megadott felhasználónév alapján.
     * 
     * @param username
     *            a keresett felhasználó neve.
     * @return a megtalált felhasználó entitás.
     */
    User findByUsername(String username);

    /**
     * Felhasználó keresése megadott e-mail cím alapján.
     * 
     * @param email
     *            a keresett felhasználó e-mail címe.
     * @return a megtalált felhasználó entitás.
     */
    User findByEmail(String email);

    /**
     * Visszaadja azokat a felhasználókat amelyek csak <b>ROLE_USER</b>
     * szerepkörrel rendelkeznek.
     * 
     * @return <b>ROLE_USER</b> szerepkörrel rendelkező felhaszálók listája.
     */
    @Query("SELECT u FROM User u join u.roles r WHERE r.roleName = 'ROLE_USER' ")
    List<User> findByRoleUser();

    /**
     * Visszaadja a kért árkategória darabszámát, azaz hogy hány ember
     * preferálja ezt a kategóriát.
     * 
     * @param category
     *            a vizsgálandó kategória.
     * @return a darabszám.
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.priceCategory = :category")
    Integer countGivenPriceCategory(@Param("category") Integer category);

    /**
     * Felhasználó aktuális koordinátájának frissítése.
     * 
     * @param latitude
     *            aktuális szélesség.
     * @param altitude
     *            aktuális magasság.
     * @param userId
     *            felhasználó azonosítója.
     */
    //@formatter:off
    @Modifying
    @Transactional
    @Query("update User set lastKnownCoordinate.latitude = :latitude,lastKnownCoordinate.altitude = :altitude where id = :userId")
    void updateUserCoordinate(
                              @Param("latitude") Double latitude,
                              @Param("altitude") Double altitude,
                              @Param("userId") Long userId);
    /**
     * Felhasználó preferált árkategóriájának frissítése.
     * 
     * @param priceCategory 
     *            az új preferált árkategória.
     * @param userId 
     *            a felhasznláló azonosítója.
     */
    @Modifying
    @Transactional
    @Query("update User set priceCategory = :priceCategory where id = :userId")
    void updatePriceCategory(
                            @Param("priceCategory") Integer priceCategory,
                            @Param("userId") Long userId);
    
    /**
     * Felhasználó deaktiválása.
     * @param userId a deaktiválandó felhasználó azonosítója.
     */
    @Modifying
    @Transactional
    @Query("update User set remove = 1 where id = :userId")
    void deactivateUser(@Param("userId") Long userId);
    
    /**
     * Felhasználó aktiválása..
     * @param userId az aktiválandó felhasználó azonosítója.
     */
    @Modifying
    @Transactional
    @Query("update User set remove = 0 where id = :userId")
    void activateUser(@Param("userId") Long userId);
    
    /**
     * Az aktuálisan online felhasználók listázása.
     * @return az online felhasználók listája.
     */
    @Query("select u from User u where u.loggedIn = true")
    List<User> findOnlineUsers();
   //@formatter:on

    /**
     * Felhasználó keresése megadott e-mail cím alapján.
     *
     * @param id
     *            a keresett felhasználó azonositója.
     * @return a megtalált felhasználó entitás.
     */
    User findById(Long id);
}
