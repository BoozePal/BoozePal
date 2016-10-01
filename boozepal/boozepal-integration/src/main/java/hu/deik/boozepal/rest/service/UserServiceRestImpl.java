package hu.deik.boozepal.rest.service;

import javax.ejb.Stateless;

@Stateless
public class UserServiceRestImpl implements UserServiceRest {

    @Override
    public String testMethod() {
        return "testMethod()";
    }

}
