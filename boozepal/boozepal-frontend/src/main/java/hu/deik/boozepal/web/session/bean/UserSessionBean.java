package hu.deik.boozepal.web.session.bean;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@SessionScoped
@ManagedBean(name = "userSessionBean")
public class UserSessionBean implements Serializable {

    private static final long serialVersionUID = -1660866024225185114L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB
    private UserService userService;

    private User sessionUser;

    @PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        if (sessionUser == null) {
            Principal principal = req.getUserPrincipal();
            if (principal != null && !principal.getName().isEmpty()) {
                try {
                    sessionUser = userService.findUserByName(principal.getName());
                    sessionUser.setLoggedIn(true);
                    sessionUser.setLastLoggedinTime(new Date());
                    userService.save(sessionUser);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("Kijelentkezés: {}", sessionUser.getUsername());
        sessionUser.setLoggedIn(false);
        userService.save(sessionUser);
    }
}
