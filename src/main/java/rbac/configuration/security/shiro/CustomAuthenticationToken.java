package rbac.configuration.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by chandra on 11/6/14.
 */
public class CustomAuthenticationToken implements AuthenticationToken {

    private String username;

    private char[] password;

    //private Set<Role> roles;

    private int gid;

    public CustomAuthenticationToken(){
        super();
    }

    public CustomAuthenticationToken(String username, char[] password) {
        this.username = username;
        this.password = password;
        //this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(setRoles(Set<Role> roles));
//
//        return sb.toString();
//    }
}
