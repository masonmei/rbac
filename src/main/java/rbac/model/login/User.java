package rbac.model.login;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.action.internal.CollectionRecreateAction;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by chandra on 11/3/14.
 */

@Entity
@Table(name="rb_user")
//@org.springframework.beans.factory.annotation.Qualifier("hsqlInMemory")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INTEGER", unique = true)
    private int id;

    @Column(name = "username", nullable=false)
    private String username;

//    @Column(name = "email", nullable=false)
//    private String email;

//    @Column(name = "password", nullable=false)
//    private String password;

    @OneToMany(mappedBy="user")
    @JsonManagedReference
    private List<Role> roles;

    public User(){
        roles = new ArrayList<Role>();
    }

//    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
//    private Set<Permission> permissions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


    public Collection<Role> getRoles() {
        return roles;
    }

//    public void setRoles(Role roles) {
//        if (!getRoles().contains(roles)) {
//            getRoles().add(roles);
//            if (roles.getUser() != null) {
//                roles.getUser().getRoles().remove(roles);
//            }
//            roles.setUser(this);
//        }
//    }


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}


