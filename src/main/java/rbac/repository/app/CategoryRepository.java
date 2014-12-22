package rbac.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rbac.model.app.Category;

/**
 * Created by chandra on 10/10/14.
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
