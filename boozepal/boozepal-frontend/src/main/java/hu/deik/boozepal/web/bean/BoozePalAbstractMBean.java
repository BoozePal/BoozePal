package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract ősosztály a közös beanek behúzására.
 *
 */
public abstract class BoozePalAbstractMBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String UNKNOWN = "unknown";
    private static final Logger logger = LoggerFactory.getLogger(BoozePalAbstractMBean.class);

    /**
     * i18n-t kezelő szolgáltatás.
     */
    @ManagedProperty("#{out}")
    protected ResourceBundle bundle;

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * i18n kezelő szolgáltatás érték olvasására képes metódus.
     * 
     * @param key
     *            a tulajdonság neve.
     * @return a tulajdonság értéke.
     */
    protected String getKeyFromProperty(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.error("Requested key is missing, {}", key);
            return bundle.getString(UNKNOWN);
        }

    }

}
