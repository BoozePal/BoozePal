package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ManagedBean
@ViewScoped
@Named("userListMBean")
public class UserListMBean extends BoozePalAbstractMBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@EJB
	private UserService userService;

	private List<User> users;

	private boolean activeUser;

	@PostConstruct
	public void init() {
		setActiveUser(false);
		users = allUsersList();
		logger.info("=== init === {}", getUsers().size());
	}

	public void switchUsers() {
		if (activeUser) {
			users = getUsers().stream().filter(e -> e.isLoggedIn() == true).collect(Collectors.toList());
		} else {
			users = allUsersList();
		}
		logger.info("== activeUser == {}", getUsers().size());
	}

	private List<User> allUsersList() {
		return userService.findAll();
	}

	public void addMessage() {
		String message = activeUser ? "Aktív felhasználó" : "Összes felhasználó";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public boolean isActiveUser() {
		return activeUser;
	}

	public void setActiveUser(boolean activeUser) {
		this.activeUser = activeUser;
	}

}