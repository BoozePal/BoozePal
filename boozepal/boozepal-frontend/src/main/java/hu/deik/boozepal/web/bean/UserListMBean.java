package hu.deik.boozepal.web.bean;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@ManagedBean
@Named("userListMBean")
@ViewScoped
public class UserListMBean extends BoozePalAbstractMBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB
    private UserService userService;

    private List<User> users;

    private List<User> allUsers;

    private boolean activeUser;

    private User selectedUser;

    @PostConstruct
    public void init() {
        setActiveUser(true);
        allUsers = allUsersList();
        users = getAllUsers().stream().filter(e -> e.isLoggedIn()).collect(Collectors.toList());
    }

    public void switchUsers() {
        if (activeUser) {
            users = getAllUsers().stream().filter(e -> e.isLoggedIn()).collect(Collectors.toList());
        } else {
            users = getAllUsers();
        }
        logger.info("activeUser ==: {}", getUsers().size());
    }

    private List<User> allUsersList() {
        return userService.findAll();
    }

    public void addMessage() {
        String message = activeUser ? "Aktív felhasználó" : "Összes felhasználó";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public void onRowSelect(SelectEvent event) {
        logger.info("asl {}", selectedUser.toString());
        FacesMessage msg = new FacesMessage("User Selected", ((User) event.getObject()).getUsername());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("User Unselected", ((User) event.getObject()).getUsername());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}