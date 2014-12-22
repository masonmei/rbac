package rbac.repository.login;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.login.Role;

/**
 * Created by chandra on 11/3/14.
 */
@Repository
@Qualifier("hsqlInMemory")
public interface RoleRepository extends JpaRepository<Role, Integer> {


}
