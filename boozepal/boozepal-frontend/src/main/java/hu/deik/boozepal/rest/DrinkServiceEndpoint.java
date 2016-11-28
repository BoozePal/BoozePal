package hu.deik.boozepal.rest;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.entity.DrinkTypeEnum;
import hu.deik.boozepal.common.vo.DrinkVO;
import hu.deik.boozepal.rest.service.DrinkServiceRest;

/**
 * A külső felhasználók által használt italok lekérésére szolgáló végpont.
 *
 * @version 1.0
 */
@Path("/drinks")
@RequestScoped
public class DrinkServiceEndpoint extends AbstractEndpoint implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     */
    @EJB
    private DrinkServiceRest drinkService;

    /**
     * Visszaadja az összes italt.
     * 
     * @return az összes ital.
     */
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON + ENCODING)
    public List<DrinkVO> getAllDrinks() {
        logger.info("Az összes ital lekérdezése.");
        List<DrinkVO> allDrinks = drinkService.getAllDrinks();
        logger.debug(allDrinks.toString());
        return allDrinks;
    }

    /**
     * Visszaadja az összes italt típus alapján.
     * 
     * @param drinkType
     *            az ital típusa.
     * @return a megadott típussal megegyező italok.
     */
    @Path("/{type}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DrinkVO> getAllDrinkByType(@PathParam("type") DrinkTypeEnum drinkType) {
        logger.info("Italok lekérdezése típus alapján.");
        List<DrinkVO> allDrinksByType = drinkService.getAllDrinksByType(drinkType);
        logger.debug(allDrinksByType.toString());
        return allDrinksByType;
    }
}
