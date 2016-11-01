package hu.deik.boozepal.web.session.bean;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.tool.FacesMessageTool;

@ManagedBean(name = "adminRegistrationBean")
@ViewScoped
public class AdminRegistrationBean implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EJB
    private UserService userService;

    @ManagedProperty("#{out}")
    private ResourceBundle bundle;

    private BCryptPasswordEncoder bcrypt;

    private String userName;
    private String password;

    @PostConstruct
    public void init() {
        bcrypt = new BCryptPasswordEncoder();
    }

    public void createNewAdminAccount() {
        logger.info("Creating new admin user.");
        try {
            createAccount();
        } catch (RegistrationException e) {
            logger.error("Error while trying to create new admin user.");
            logger.error(e.getMessage());
            FacesMessageTool.createErrorMessage(bundle.getString("usernameIsExists"));
        }
    }

    private void createAccount() throws RegistrationException {
        userService.createNewAdministrator(userName, bcrypt.encode(password));
        FacesMessageTool.createInfoMessage(bundle.getString("registrationSuccess"));
        logger.info("New admin user created.");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

}
