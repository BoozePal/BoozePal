package hu.deik.boozepal.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.core.repo.RoleRepository;
import hu.deik.boozepal.service.RoleService;

/**
 * Szerepköröket kezelő szolgáltatás megvalósítása.
 *
 */
@Stateless
@Local(RoleService.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RoleServiceImpl implements RoleService {

    /**
     * Szerepköröket kezelő adathozzáférési osztály.
     */
    @Autowired
    private RoleRepository roleDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Role findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }

}
