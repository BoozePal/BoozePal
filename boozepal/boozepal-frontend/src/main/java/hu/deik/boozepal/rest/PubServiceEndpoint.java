package hu.deik.boozepal.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.AuthenticationException;
import hu.deik.boozepal.common.exceptions.UserDetailsUpdateException;
import hu.deik.boozepal.rest.service.PubServiceRest;
import hu.deik.boozepal.rest.service.UserServiceRest;
import hu.deik.boozepal.rest.vo.RemoteTimeTableVO;
import hu.deik.boozepal.rest.vo.RemoteTokenVO;
import hu.deik.boozepal.rest.vo.RemoteUserDetailsVO;
import hu.deik.boozepal.rest.vo.RemoteUserVO;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * A külső felhasználók által használt szolgáltatások végpontja.
 *
 * @version 1.3
 */
@Path("/allPubs")
@RequestScoped
public class PubServiceEndpoint implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB
    private PubServiceRest pubServiceRest;

    /**
     * Összes kocsma lekérésére való endpoint
     *
     * @return a kocsmák listája.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPubs() {
        logger.info("kocsmák lekérdezése.");
        try {
            List<Pub> pubs = pubServiceRest.getAllPubs();
            return Response.ok().entity(pubs).build();
        } catch (RuntimeException e) {
            logger.info(e.getMessage(), e);
            return Response.status(Status.CONFLICT).build();
        }
    }
}
