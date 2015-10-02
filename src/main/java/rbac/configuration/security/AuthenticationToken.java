package rbac.configuration.security;

import rbac.model.login.Role;

import java.util.Collection;
import java.util.Set;

/**
 * Created by chandra on 11/7/14.
 */
public class AuthenticationToken {
    private String username;
    private Collection<Role> roles;
    private String token;

    public AuthenticationToken() {
        super();
    }

    public AuthenticationToken(String username, Collection<Role> roles, String token) {
        this.roles = roles;
        this.token = token;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
