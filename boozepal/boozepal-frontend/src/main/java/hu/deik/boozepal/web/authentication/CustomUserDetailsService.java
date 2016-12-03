package hu.deik.boozepal.web.authentication;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.deik.boozepal.common.entity.Role;
import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.service.UserService;

@Service("customUserDetailsService")
@EJB(name = "ejb.UserService", beanInterface = UserService.class)
public class CustomUserDetailsService implements UserDetailsService {

	private final static String ROLE_ADMIN = "ROLE_ADMIN";

	@EJB
	UserService userService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userService.findUserByName(username);

		if (user == null || user.getRoles().isEmpty()) {
			logger.error("Nem talált felhasználó{}", username);
			throw new UsernameNotFoundException(username);
		}
		logger.info("Felhasználó belépés: {}", user.getUsername());
		for (Role e : user.getRoles()) {
			if (e.getRoleName().equals(ROLE_ADMIN) && !user.isRemove()) {
				List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
				return buildUserForAuthentication(user, authorities);
			}
		}
		throw new UsernameNotFoundException(username);
	}

	/* org.springframework.security.core.userdetails.User */
	private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user,
			List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
		for (Role userRole : userRoles) {
			result.add(new SimpleGrantedAuthority(userRole.getRoleName()));
		}
		return result;
	}
}
