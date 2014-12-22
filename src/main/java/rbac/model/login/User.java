package rbac.model.login;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
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

    @OneToMany
    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<Role> roles;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}


