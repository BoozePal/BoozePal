package hu.deik.boozepal.service;

import javax.ejb.Local;

/**
 * A rendszer felállásakor elinduló bean ami a szükséges létező adatokat
 * biztosítja a felületnek.
 *
 */
@Local
public interface StartupService {
    
    /**
     * Admin felhasználó létrehozása.
     */
    public void createAdminUser();

}
