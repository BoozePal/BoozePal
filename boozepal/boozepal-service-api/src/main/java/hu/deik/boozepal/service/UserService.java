package hu.deik.boozepal.service;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.common.vo.MapUserVO;

/**
 * Az adminisztrátori felülethez tartozó felhasználói funkciókat megvalósító
 * szolgáltatás.
 * 
 * @version 1.0
 */
@Local
public interface UserService {
    /**
     * Felhasználó keresése felhasználónév alapján.
     * 
     * @param username
     *            a keresendő felhasználó neve.
     * @return a megtalált felhasználó entitás.
     */
    public User findUserByName(String username);

    /**
     * Új adminisztrátor létrehozása a felülethez.
     * 
     * @param username
     *            az adminisztrátor bejelentkezési neve.
     * @param password
     *            a jelszava.
     * @return a mentett adminisztrátori entitás.
     * @throws RegistrationException
     *             ha már a felhasználónévvel létezik ilyen adminisztrátor.
     */
    public User createNewAdministrator(String username, String password) throws RegistrationException;

    /**
     * Visszaadja az összes felhasználó entitást.
     * 
     * @return az összes felhasználó entitás listája.
     */
    public List<User> findAll();

    /**
     * Felhasználó mentése.
     * 
     * @param user
     *            a mentendő felhasználó entitás.
     */
    public void save(User user);

    /**
     * Létrehoz és adatbázisba ment egy új felhasználót a megadott névvel és
     * e-mail címmel.
     * 
     * @param fullName
     *            a felhasználó teljes neve.
     * @param email
     *            a felhasználó e-mail címe.
     * @return a mentett felhasználó entitás.
     */
    public User createNewUser(String fullName, String email);

    /**
     * Visszaadja az éppen elérhető felhasználókat.
     * 
     * @return éppen elérhető felhasználók listája.
     */
    public List<User> getOnlineUsers();

    /**
     * Visszaadja az elérhető felhasználókat térképen megjeleníthető formában.
     * 
     * @return térképen megjeleníthető felhasználók listája.
     */
    public List<MapUserVO> getOnlineUsersToMap();
}
