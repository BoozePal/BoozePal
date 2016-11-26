package hu.deik.boozepal.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;

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

    private ObjectMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new ObjectMapper();
    }

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
     * @param string
     *            a felhasználó Google token-e.
     * @return ha sikerült az adatmódositás OK, ha nem akkor a hiba oka.
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Path("/updateDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserInformation(String string) {
        logger.info("Felhasználó adatok módositása");
        RemoteUserDetailsVO remoteUserDetailsVO;

        try {
            remoteUserDetailsVO = getValue(string);
        } catch (IOException e) {
            return Response.serverError().build();
        }
        try {
            userServiceRest.updateUserDetails(remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            logger.info("Felhasználó adatmódositás sikertelen volt! {}", e.getMessage());
            return Response.serverError().build();
        }
        logger.info("Felhasználó adatmódositás sikeres volt!");
        return Response.status(Status.OK).build();
    }

    private RemoteUserDetailsVO getValue(String string) throws IOException, JsonParseException, JsonMappingException {
        RemoteUserDetailsVO remoteUser;
        remoteUser = mapper.readValue(string, RemoteUserDetailsVO.class);
        return remoteUser;
    }

    /**
     * Külső felhasználó kiléptetése a rendszerből.
     *
     * @param remoteUser
     *            a felhasználó Google token-e.
     * @return sikeres kiléptetésnél HTTP 200. ha nem sikeres akkor HTTP 500.
     */
    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(RemoteTokenVO remoteUser) {
        logger.info("Felhasználó kiléptetése.");
        try {
            userServiceRest.logoutUserLogically(remoteUser);
            logger.info("Felhasználó kiléptetése.");
        } catch (AuthenticationException e) {
            return Response.serverError().build();
        }
        return Response.status(Status.OK).build();
    }

    /**
     * Cimborák visszaadása az endpointon keresztül.
     * 
     * @param string
     * @return
     */
    @Path("/findPals")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findPals(String string) {
        logger.info("Cimborák keresése a közelben.");

        RemoteUserDetailsVO remoteUserDetailsVO;

        try {
            remoteUserDetailsVO = getValue(string);
        } catch (IOException e) {
            logger.error("Hiba a JSON parsolása során, BAD_REQUEST küldése.");
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        }
        RemoteUserVO user = remoteUserDetailsVO.getUser();

        List<User> usersInGivenRadiusAndCoordinate = userServiceRest.getUsersInGivenRadiusAndCoordinate(
                user.getLastKnownCoordinate().getLatitude(), user.getLastKnownCoordinate().getLongitude(),
                (double) user.getSearchRadius());

        return Response.ok().entity(usersInGivenRadiusAndCoordinate).build();
    }

}
