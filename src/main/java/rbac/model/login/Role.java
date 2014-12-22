package rbac.model.login;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by chandra on 11/3/14.
 */
@Entity
@Table(name="rb_role")
//@org.springframework.beans.factory.annotation.Qualifier("hsqlInMemory")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INTEGER", unique = true)
    private int id;

    @Column(name = "name", nullable=false)
    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;

    @OneToMany
    private Set<Permission> permission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Set<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }

    public Set<Permission> getPermission() {
        return permission;
    }

    public void setPermission(Set<Permission> permission) {
        this.permission = permission;
    }
}
