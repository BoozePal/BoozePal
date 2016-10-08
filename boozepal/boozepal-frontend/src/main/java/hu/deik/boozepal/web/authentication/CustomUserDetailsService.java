package hu.deik.boozepal.web.authentication;

import javax.ejb.EJB;

import org.springframework.stereotype.Service;

import hu.deik.boozepal.service.UserService;

@Service("customUserDetailsService")
@EJB(name = "ejb.UserService", beanInterface = UserService.class)
public class CustomUserDetailsService/* implements UserDetailsService*/ {

    private final static String ADMIN_ROLE = "ROLE_ADMIN";

    @EJB
    UserService userService;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userService.findUserByName(username);
//
//        if (user == null || user.getRoles().isEmpty()) {
//            throw new UsernameNotFoundException(username);
//        }
//        for (Role e : user.getRoles()) {
//            if (e.getRoleName().equals(ADMIN_ROLE) && !user.isRemove()) {
//                user.setRemove(true);
//                List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
//                return buildUserForAuthentication(user, authorities);
//            }
//        }
//        throw new UsernameNotFoundException(username);
//    }

    /* org.springframework.security.core.userdetails.User */
//    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user,
//            List<GrantedAuthority> authorities) {
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
//                true, true, true, authorities);
//    }
//
//    private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
//        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
//        for (Role userRole : userRoles) {
//            result.add(new SimpleGrantedAuthority(userRole.getRoleName()));
//        }
//        return result;
//    }
}
