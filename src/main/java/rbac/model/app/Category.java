package rbac.model.app;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by chandra on 10/10/14.
 */

@Entity
@Table(name="category")
public class Category implements Serializable{

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name", nullable=false)
    private String name;

    //@OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@Column(nullable = true)
    //@JsonManagedReference
    //private Set<Product> productSet;

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
}
