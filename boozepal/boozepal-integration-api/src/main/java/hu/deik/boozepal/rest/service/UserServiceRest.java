package hu.deik.boozepal.rest.service;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.vo.RemoteTimeTableVO;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

import javax.ejb.Local;
import java.util.List;

/**
 * Felhasználó szolgáltatás az Android kliens által használt funkciókra.
 *
 * @version 1.0
 */
@Local
public interface UserServiceRest {

    /**
     * Felhasználó mentése.
     *
     * @param user
     * @return
     */
    public User saveUser(User user);

    /**
     * Távoli felhasználó beléptetése vagy ha még nem létezik akkor új
     * felhasználó létrehozása.
     *
     * @param remoteUser távoli felhasználó.
     * @return az újonnan létrehozott felhasználó vagy egy már meglévő.
     * @throws AuthenticationException ha nem sikerült a token validálása.
     */
    public User createOrLoginUser(RemoteTokenVO remoteUser) throws AuthenticationException;

    /**
     * Felhasználó logikai kiléptetése a rendszerből.
     *
     * @param userId felhasználó azonosítója.
     * @throws AuthenticationException
     */
    public void logoutUserLogically(RemoteTokenVO userId) throws AuthenticationException;

    /**
     * Visszaadja azon felhasználók listáját akik egy bizonyos sugarú körben
     * helyezkednek el a megadott koordinátákhoz képest.
     *
     * @param latitude szélesség.
     * @param altitude magasság.
     * @param radius   megadott körsugár.
     * @return a megadott körsugárban elérhető felhasználók.
     */
    public List<User> getUsersInGivenRadiusAndCoordinate(Double latitude, Double altitude, Double radius);

    /**
     * Távoli felhasználó adatmódositás
     *
     * @param remoteUser távoli felhasználó.
     * @throws UserDetailsUpdateException ha nem sikerült a felhasználót frissiteni.
     */
    public User updateUserDetails(RemoteUserDetailsVO remoteUser) throws UserDetailsUpdateException;

    public void deleteUser(User user);

    /**
     * Távoli felhasználó ráérési napok frissitése
     *
     * @param remoteTimeTableVO távoli felhasználó ráérési napajai és a google token.
     * @throws UserDetailsUpdateException ha nem sikerült a felhasználót frissiteni.
     */
    public void updateUserDates(RemoteTimeTableVO remoteTimeTableVO) throws AuthenticationException, UserDetailsUpdateException;

    /**
     * Felhasználó aktuális helyzetének frissítése.
     *
     * @param remoteUser a frissítendő felhasználó.
     */
    public User updateUserLocation(RemoteUserVO remoteUser);
}
