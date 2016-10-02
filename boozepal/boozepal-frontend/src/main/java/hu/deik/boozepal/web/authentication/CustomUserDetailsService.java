package hu.deik.boozepal.web.authentication;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.deik.boozepal.common.entity.RoleVO;
import hu.deik.boozepal.common.entity.UserVO;
import hu.deik.boozepal.service.UserService;

@Service("customUserDetailsService")
@EJB(name = "ejb.UserService", beanInterface = UserService.class)
public class CustomUserDetailsService implements UserDetailsService {

	private final static String ADMIN_ROLE = "ROLE_ADMIN";

	@EJB
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserVO user = userService.findUserByName(username);

		if (user == null || user.getRoles().isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
		for (RoleVO e : user.getRoles()) {
			if (e.getRoleName().equals(ADMIN_ROLE) && !user.isRemove()) {
				user.setRemove(true);
				List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
				return buildUserForAuthentication(user, authorities);
			}
		}
		throw new UsernameNotFoundException(username);
	}

	/* org.springframework.security.core.userdetails.User */
	private User buildUserForAuthentication(UserVO user, List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(List<RoleVO> userRoles) {
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
		for (RoleVO userRole : userRoles) {
			result.add(new SimpleGrantedAuthority(userRole.getRoleName()));
		}
		return result;
	}
}
