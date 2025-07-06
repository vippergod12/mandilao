package ws.prj.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service("Auth")
public class AuthService {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserDetails getUserDetails() {
        return (UserDetails) this.getAuthentication().getPrincipal();
    }

    public String getUsername() {
        return this.getAuthentication().getName();
    }

    public List<String> getAuthorities() {
        return this.getAuthentication().getAuthorities().stream()
                .map(auth -> auth.getAuthority().substring(5)).toList();
    }

    public boolean hasAnyRole(String... role) {
        var grantedRoles = this.getAuthorities();
        return Stream.of(role).anyMatch(ro -> grantedRoles.contains(ro));
    }

    public void authenticate(String username, String password, String... roles) {
        UserDetails user = User.withUsername(username).password(password).roles(roles).build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
