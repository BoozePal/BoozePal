package hu.deik.boozepal.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;

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
            return Response.status(Status.BAD_REQUEST).build();
        }
        try {
            userServiceRest.updateUserDetails(remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            logger.info("Felhasználó adatmódositás sikertelen volt! {}", e.getMessage());
            return Response.status(Status.BAD_REQUEST).build();
        }
        logger.info("Felhasználó adatmódositás sikeres volt!");
        return Response.status(Status.OK).build();
    }

    private RemoteUserDetailsVO getValue(String string) throws IOException, JsonParseException, JsonMappingException {
        RemoteUserDetailsVO remoteUser;
        remoteUser = mapper.readValue(string, RemoteUserDetailsVO.class);
        return remoteUser;
    }

}
