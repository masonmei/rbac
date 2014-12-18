package rbac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rbac.model.Category;
import rbac.model.Product;
import rbac.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandra on 10/10/14.
 */

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(Product product) {
        product.setName(product.getName());
        product.setCategory(product.getCategory());
        productRepository.save(product);
    }

    public void update(Integer id, Product product){
        Product productOld = productRepository.findOne(id);
        if(productOld == null){
            throw new IllegalStateException("No product data found");
        }
        product.setId(productOld.getId());
        productRepository.save(productOld);
    }

    public void delete(int id) {
        if (id == 0) {
            return;
        }
        productRepository.delete(id);
    }

    public Product findById(int id) {
        if (id == 0) {
            return null;
        }
        return productRepository.findOne(id);
    }

    public Page<Product> findAll(Pageable pageable) {
        if(pageable == null){
            pageable = new PageRequest(0, 20);
        }
        return productRepository.findAll(pageable);
    }

    public List findAllBranchWithCategory(){
       List productCategory = new ArrayList();
       List<Product> product = productRepository.findAll();

        for(Product productNew : product){
            Category category = productNew.getCategory();
            productCategory = (List) category;
        }
        System.out.println(productCategory);
        return productCategory;
    }
}
