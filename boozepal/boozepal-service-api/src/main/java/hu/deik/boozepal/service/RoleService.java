package hu.deik.boozepal.service;

import javax.ejb.Local;

import hu.deik.boozepal.common.entity.Role;

/**
 * Szerepköröket kezelő szolgáltatás.
 *
 */
@Local
public interface RoleService {

    /**
     * Szerepkör keresése név alapján.
     * 
     * @param roleName
     *            a szerepkör neve.
     * @return a megtalált szerepkör.
     */
    Role findByRoleName(String roleName);

}
