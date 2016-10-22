package hu.deik.boozepal.web.session.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@ManagedBean
@ViewScoped
@Named("userListMBean")
public class UserListMBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@EJB
	private UserService userService;

	private List<User> users;

	@PostConstruct
	public void init() {
		logger.info("HELLO {}", UserListMBean.class);
		users = allUsersList();
	}

	private List<User> allUsersList() {
		return userService.findAll();
	}
}