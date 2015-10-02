package rbac.model.login;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name="rb_role")
//@org.springframework.beans.factory.annotation.Qualifier("hsqlInMemory")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INTEGER", unique = true)
    private int id;

    @Column(name = "name", nullable=false)
    private String name;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy="role")
    @JsonManagedReference
    private List<Permission> permission;

    public Role(){
        permission = new ArrayList<Permission>();
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Permission> getPermission() {
        return permission;
    }

//    public void setPermission(Permission permission) {
//        if (!getPermission().contains(permission)) {
//            getPermission().add(permission);
//            if (permission.getRole() != null) {
//                permission.getRole().getPermission().remove(permission);
//            }
//            permission.setRole(this);
//        }
//    }


    public void setPermission(List<Permission> permission) {
        System.out.println("PERMISSION "+ permission);
        //for(Permission permission1 : permission) {
            this.permission = permission;
        //}
    }
}
