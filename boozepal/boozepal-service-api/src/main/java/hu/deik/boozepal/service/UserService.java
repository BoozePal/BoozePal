package hu.deik.boozepal.service;

import java.util.List;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;

/**
 * Az adminisztrátori felülethez tartozó felhasználói funkciókat megvalósító
 * szolgáltatás.
 * 
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
     */
    public User createNewAdministrator(String username, String password) throws RegistrationException;

	public List<User> findAll();

	public void save(User user);
}
