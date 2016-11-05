package hu.deik.boozepal.rest.service;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

/**
 * Felhasználó szolgáltatás az Android kliens által használt funkciókra.
 * 
 * 
 * @version 1.0
 *
 */
@Local
public interface UserServiceRest {

    /**
     * Távoli felhasználó beléptetése vagy ha még nem létezik akkor új
     * felhasználó létrehozása.
     * 
     * @param remoteUser
     *            távoli felhasználó.
     * @return az újonnan létrehozott felhasználó vagy egy már meglévő.
     * @throws AuthenticationException
     *             ha nem sikerült a token validálása.
     */
    public User createOrLoginUser(RemoteUserVO remoteUser) throws AuthenticationException;

    /**
     * Felhasználó logikai kiléptetése a rendszerből.
     * 
     * @param userId
     *            felhasználó azonosítója.
     * @throws AuthenticationException
     */
    public void logoutUserLogically(RemoteUserVO remoteUser) throws AuthenticationException;
    
    /**
     * 
     * @param lattitude
     * @param altitude
     * @param radius
     * @return
     */
    public List<User> getUsersInGivenRadiusAndCoordinate(Double lattitude, Double altitude, Double radius);

}
