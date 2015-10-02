package rbac.model.login;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by chandra on 11/3/14.
 */
@Entity
@Table(name="rb_permission")
//@org.springframework.beans.factory.annotation.Qualifier("hsqlInMemory")
public class Permission implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "INTEGER", unique = true)
    private int id;

    @Column(name = "name", nullable=false)
    private String name;

    @ManyToOne
    @JsonBackReference
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
