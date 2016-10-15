package hu.deik.boozepal.core.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.deik.boozepal.common.entity.Role;

/**
 * Felhasználó szerepköröket kezelő adathozzáférési szolgáltatás.
 * 
 * @version 1.0
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Szerepkör keresése név alapján.
     * @param roleName a keresendő szerepkör neve.
     * @return a megtalált szerepkör.
     */
    Role findByRoleName(String roleName);

}
