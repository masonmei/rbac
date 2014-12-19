package rbac.repository.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.login.Permission;


/**
 * Created by chandra on 11/3/14.
 */
//@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}
