package rbac.configuration.security.shiro;


import rbac.model.Role;
import rbac.model.User;

import java.util.Set;

/**
 * Created by chandra on 11/6/14.
 */
public class CustomPrincipal {

    private User user;
    private Set<Role> roles;

    public CustomPrincipal(User user, Set<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
