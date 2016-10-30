package hu.deik.boozepal.rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

/**
 * A külső felhasználók által használt szolgáltatások végpontja.
 * 
 * @version 1.0
 *
 */
@Path("/user")
@RequestScoped
public class UserServiceEndpoint implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB
    private UserServiceRest userServiceRest;

    /**
     * Külső felhasználó beléptetése a rendszerbe.
     * 
     * @param remoteUser
     *            a felhasználó Google token-e.
     * @return a beléptett felhasználót reprezentáló entitás.
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User loginUser(RemoteUserVO remoteUser) {
        logger.info("Felhasználó beléptetése.");
        User user = null;
        try {
            user = userServiceRest.createOrLoginUser(remoteUser);
            logger.info("Felhasználó beléptetése sikeres!");
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
        }
        return user;
    }

}
