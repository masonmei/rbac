package rbac.configuration.security;

import rbac.model.login.Role;

import java.util.Set;

/**
 * Created by chandra on 11/7/14.
 */
public class AuthenticationToken {
    private String username;
    private Set<Role> roles;
    private String token;

    public AuthenticationToken() {
        super();
    }

    public AuthenticationToken(String username, Set<Role> roles, String token) {
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
