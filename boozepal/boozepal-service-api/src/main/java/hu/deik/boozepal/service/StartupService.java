package hu.deik.boozepal.service;

import javax.ejb.Local;

@Local
public interface StartupService {

    public void createAdminUser();

}
