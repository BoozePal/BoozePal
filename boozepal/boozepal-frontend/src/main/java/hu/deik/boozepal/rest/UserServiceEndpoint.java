package hu.deik.boozepal.rest;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemotePalAcceptVO;
import hu.deik.boozepal.rest.vo.RemotePalRequestVO;
import hu.deik.boozepal.rest.vo.RemoteTimeTableVO;
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
public class UserServiceEndpoint extends AbstractEndpoint implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger (this.getClass ());

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
        logger.info ("Felhasználó beléptetése.");
        User user = null;
        try {
            user = userServiceRest.createOrLoginUser (remoteUser);
            logger.info ("Felhasználó beléptetése sikeres!");
        } catch (AuthenticationException e) {
            logger.error (e.getMessage (), e);
        }
        return user;
    }

    /**
     * Külső felhasználó adatmódositására a rendszerbe.
     *
     * @param string a felhasználó Google token-e.
     * @return ha sikerült az adatmódositás OK, ha nem akkor a hiba oka.
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Path("/updateDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON + ENCODING)
    @Consumes(MediaType.APPLICATION_JSON + ENCODING)
    public Response updateUserInformation(String string) {
        logger.info ("Felhasználó adatok módositása");
        RemoteUserDetailsVO remoteUserDetailsVO;

        try {
            remoteUserDetailsVO = new GsonBuilder ().create ().fromJson (string, RemoteUserDetailsVO.class);
        } catch (JsonSyntaxException e) {
            logger.error ("Hiba a cimborák keresése közben.", e);
            return Response.serverError ().entity (e.getMessage ()).build ();
        }
        try {
            userServiceRest.updateUserDetails (remoteUserDetailsVO);
        } catch (UserDetailsUpdateException e) {
            logger.info ("Felhasználó adatmódositás sikertelen volt! {}", e.getMessage ());
            return Response.serverError ().build ();
        }
        logger.info ("Felhasználó adatmódositás sikeres volt!");
        return Response.status (Status.OK).build ();
    }

    /**
     * Külső felhasználó kiléptetése a rendszerből.
     *
     * @param remoteUser a felhasználó Google token-e.
     * @return sikeres kiléptetésnél HTTP 200. ha nem sikeres akkor HTTP 500.
     */
    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(RemoteTokenVO remoteUser) {
        logger.info ("Felhasználó kiléptetése.");
        try {
            userServiceRest.logoutUserLogically (remoteUser);
            logger.info ("Felhasználó kiléptetése.");
        } catch (AuthenticationException e) {
            return Response.serverError ().build ();
        }
        return Response.status (Status.OK).build ();
    }

    /**
     * Felhasználó ráérési napok frissitése.
     *
     * @param string a felhasználó Google token-e és a ráérési napok listája.
     * @return sikeres frissités után HTTP 200.
     */
    @Path("/timetable")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response timeTableRefresher(String string) {
        logger.info ("Felhasználó ráérési időpontok fogadása: {}", new GsonBuilder ().create ().toJson (string));
        try {
            RemoteTimeTableVO v = new GsonBuilder ().create ().fromJson (string, RemoteTimeTableVO.class);
            userServiceRest.updateUserDates (v);
        } catch (AuthenticationException e) {
            return Response.status (Status.INTERNAL_SERVER_ERROR).entity ("Sikertelen bejelentkezés" + e.getMessage ())
                    .build ();
        } catch (UserDetailsUpdateException e) {
            return Response.status (Status.INTERNAL_SERVER_ERROR).entity ("Sikertelen ráérési idő frissités").build ();
        } catch (RuntimeException e) {
            logger.info ("RuntimeException:", e);
            return Response.status (Status.NOT_ACCEPTABLE).entity ("Tokent és List<Date> várunk").build ();
        }
        return Response.status (Status.OK).build ();
    }

    /**
     * Cimborák visszaadása az endpointon keresztül.
     *
     * @param string
     * @return a válasz.
     */
    @Path("/findPals")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findPals(String string) {
        logger.info ("Cimborák keresése a közelben.");

        User user;
        try {
            user = new GsonBuilder ().create ().fromJson (string, User.class);
        } catch (JsonSyntaxException e) {
            logger.error ("Hiba a cimborák keresése közben.", e);
            return Response.serverError ().entity (e.getMessage ()).build ();
        }
        userServiceRest.updateUserLocation (user);
        List<User> usersInGivenRadiusAndCoordinate = userServiceRest.getUsersInGivenRadiusAndCoordinate (
                user.getLastKnownCoordinate ().getLatitude (), user.getLastKnownCoordinate ().getLongitude (),
                (double) user.getSearchRadius ());

        return Response.ok ().entity (usersInGivenRadiusAndCoordinate).build ();
    }

    /**
     * Felhasználó entitás lekérése token alapján.
     *
     * @param tokenVo a tokent tartalmazó VO.
     * @return a felhasználó entitás.
     */
    @Path("/getUser/{token}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserByToken(@PathParam("token") String token) {
        logger.info ("Felhasználó keresése token alapján.");
        User user;
        try {
            user = userServiceRest.getUserByToken (token);
        } catch (AuthenticationException | GeneralSecurityException | IOException e) {
            logger.error ("Hiba a felhasználó visszakérésekor.", e);
            return Response.serverError ().entity (e.getMessage ()).build ();
        }
        return Response.ok ().entity (user).build ();
    }

    /**
     * Cimbora kérés fogadása a kliens felől.
     *
     * @param vo a kapott információk.
     * @return a válasz.
     */
    @Path("/sendrequest")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendRequest(String string) {
        logger.info ("Felhasználó felkérése cimboraként.");
        RemotePalRequestVO vo = new GsonBuilder ().create ().fromJson (string, RemotePalRequestVO.class);
        try {
            userServiceRest.palRequest (vo);
        } catch (RuntimeException e) {
            logger.error ("Hiba a cimbora hozzáadása közben.", e);
            return Response.serverError ().entity (e.getMessage ()).build ();
        }
        return Response.ok ().build ();
    }

    /**
     * Cimbora kérés fogadása a kliens felől.
     *
     * @param vo a kapott információk.
     * @return a válasz.
     */
    @Path("/acceptrequest")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acceptRequest(String string) {
        logger.info ("Felhasználó elfogadása cimboraként.");
        RemotePalAcceptVO vo = new GsonBuilder ().create ().fromJson (string, RemotePalAcceptVO.class);
        try {
            userServiceRest.acceptRequest (vo);
        } catch (RuntimeException e) {
            logger.error ("Hiba a cimbora elfogadása/elutsítása közben.", e);
            return Response.serverError ().entity (e.getMessage ()).build ();
        }
        return Response.ok ().build ();
    }


}
