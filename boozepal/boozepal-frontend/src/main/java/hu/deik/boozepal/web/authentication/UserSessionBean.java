package hu.deik.boozepal.web.authentication;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import hu.deik.boozepal.common.entity.UserVO;
import hu.deik.boozepal.service.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SessionScoped
@ManagedBean(name = "userSessionBean")
public class UserSessionBean {

	@EJB
	private UserService userService;

	private UserVO sessionUser;

	@PostConstruct
	public void init() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		if (sessionUser == null) {
			Principal principal = req.getUserPrincipal();
			if (principal != null && !principal.getName().isEmpty()) {
				try {
					sessionUser = userService.findUserByName(principal.getName());
				} catch (UsernameNotFoundException e) {
					e.getMessage();
				}
			}
		}
	}
}
