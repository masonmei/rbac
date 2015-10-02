package rbac.repository.login;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.login.Role;
import rbac.model.login.User;

/**
 * Created by chandra on 11/3/14.
 */
@Repository
@Qualifier("hsqlInMemory")
public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Role findByName(String name);
}
