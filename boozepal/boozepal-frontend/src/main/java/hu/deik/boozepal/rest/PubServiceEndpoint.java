package hu.deik.boozepal.rest;

import hu.deik.boozepal.common.entity.Pub;
import hu.deik.boozepal.rest.service.PubServiceRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
