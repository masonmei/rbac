package rbac.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rbac.model.app.Product;
import rbac.model.login.Role;
import rbac.model.login.User;
import rbac.repository.login.RoleRepository;
import rbac.repository.login.UserRepository;
import rbac.service.ProductService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chandra on 10/24/14.
 */
//@RequiresRoles(value = {"ROLE_MANAGER", "ROLE_USER"}, logical = Logical.OR)
//@RequiresRoles("ROLE_USER")
@RestController
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * CRUD Product
     */
    @RequiresRoles(value = {"ROLE_MANAGER", "ROLE_USER"}, logical = Logical.OR)
    //@RequiresPermissions("READ")
    //@RequiresRoles("ROLE_USER")
    @RequestMapping(value="/product", method= RequestMethod.GET)
    @ResponseBody
    public List<Product> findAllProduct(
            Pageable pageable,
            HttpServletResponse response) {
        List<Product> hasil = productService.findAll(pageable).getContent();

        return hasil;
    }
    @RequiresRoles("users")
    @RequestMapping(value="/product", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product){
        productService.save(product);
    }

    @RequiresRoles("users")
    @RequestMapping(value="/product/{id}", method=RequestMethod.GET)
    public Product productFindById(@PathVariable Integer id){
        return productService.findById(id);
    }

    @RequiresRoles("users")
    @RequestMapping(value="/product/{id}", method=RequestMethod.PUT)
    public void updateProduct(@PathVariable Integer id, @RequestBody Product product){
        Product productOld = productService.findById(id);
        if(productOld == null){
            throw new IllegalStateException("No product Found");
        }
        product.setId(productOld.getId());
        productService.save(productOld);
    }

    @RequiresRoles("users")
    @RequestMapping(value="/product/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Integer id){
        productService.delete(id);
    }

}
