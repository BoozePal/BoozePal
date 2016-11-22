package hu.deik.boozepal.rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;

/**
 * A külső felhasználók által használt szolgáltatások végpontja.
 *
 * @version 1.0
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
     * @param remoteUser a felhasználó Google token-e.
     * @return a beléptett felhasználót reprezentáló entitás.
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User loginUser(RemoteTokenVO remoteUser) {
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

    /**
     * Külső felhasználó adatmódositására a rendszerbe.
     *
     * @param remoteUser a felhasználó Google token-e.
     * @return ha sikerült az adatmódositás OK, ha nem akkor a hiba oka.
     */
    @Path("/updateDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserInformation(RemoteUserDetailsVO remoteUser) {
        logger.info("Update user information");
        try {
            User savedUser = userServiceRest.updateUserDetails(remoteUser);
        } catch (UserDetailsUpdateException e) {
            logger.info(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

}
