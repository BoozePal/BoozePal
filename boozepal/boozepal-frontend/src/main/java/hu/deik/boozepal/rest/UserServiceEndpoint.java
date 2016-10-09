package hu.deik.boozepal.rest;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import hu.deik.boozepal.rest.service.UserServiceRest;

@Path("/user")
@RequestScoped
public class UserServiceEndpoint implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EJB
    private UserServiceRest userServiceRest;

    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String userInfo(@PathParam("userId") String userId) {
        // return "OK:" + userId;
        return userServiceRest.testMethod();
    }

}
