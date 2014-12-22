package rbac.repository.login;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.login.Permission;


/**
 * Created by chandra on 11/3/14.
 */
@Repository
@Qualifier("hsqlInMemory")
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}
