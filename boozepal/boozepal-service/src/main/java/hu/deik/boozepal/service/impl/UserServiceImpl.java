package hu.deik.boozepal.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import hu.deik.boozepal.service.UserService;

@Stateless
@Local(UserService.class)
public class UserServiceImpl implements UserService {

}
