package hu.deik.boozepal.service;

import javax.ejb.Local;

import hu.deik.boozepal.common.contants.BoozePalConstants;

/**
 * A rendszer felállásakor elinduló bean ami a szükséges létező adatokat
 * biztosítja a felületnek.
 *
 */
@Local
public interface StartupService {

    /**
     * Az alkalmazás alap applikációs környezetének létrehozása.
     * 
     * Létrehoz egy alapértelmezett adminisztrátor felhasználót
     * ({@value BoozePalConstants#ADMIN}, a megfelelő joggal
     * ({@value BoozePalConstants#ROLE_ADMIN}, illetve létrehozza az alapvető
     * felhasználói jogot({@value BoozePalConstants#ROLE_USER}.
     * 
     */
    public void createDefaultApplicationContext();

}
