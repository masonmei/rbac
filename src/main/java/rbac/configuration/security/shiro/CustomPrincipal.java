package rbac.configuration.security.shiro;


import org.hibernate.type.CustomType;
import org.springframework.stereotype.Component;
import rbac.model.login.Role;
import rbac.model.login.User;

import java.util.Set;

/**
 * Created by chandra on 11/6/14.
 */

@Component
public class CustomPrincipal {

    private User user;
    private Set<Role> roles;

    public CustomPrincipal(){
        super();
    }

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
