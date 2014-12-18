package rbac.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by chandra on 11/3/14.
 */

public class Role {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    public Role(){
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
