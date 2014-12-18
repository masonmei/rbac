package rbac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rbac.model.Category;
import rbac.repository.CategoryRepository;

/**
 * Created by chandra on 10/10/14.
 */

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void update(Integer id, Category category){
        Category categoryOld = categoryRepository.findOne(id);
        if(categoryOld == null){
            throw new IllegalStateException("No category found");
        }
        category.setId(categoryOld.getId());
        categoryRepository.save(categoryOld);
    }

    public void delete(int id) {
        if (id == 0) {
            return;
        }
        categoryRepository.delete(id);
    }

    public Category findById(int id) {
        if (id == 0) {
            return null;
        }
        return categoryRepository.findOne(id);
    }

    public Page<Category> findAll(Pageable pageable) {
        if(pageable == null){
            pageable = new PageRequest(0, 20);
        }
        return categoryRepository.findAll(pageable);
    }
}
