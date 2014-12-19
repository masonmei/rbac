package rbac.repository.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.login.User;


/**
 * Created by chandra on 11/3/14.
 */

//@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);

    public User findById(Integer id);

    public User findByEmail(String email);
}
