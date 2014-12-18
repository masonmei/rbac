package rbac.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by chandra on 10/10/14.
 */

@Entity
@Table(name="product")
public class Product implements Serializable{

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name", nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
