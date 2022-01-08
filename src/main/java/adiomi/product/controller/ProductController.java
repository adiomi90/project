package adiomi.product.controller;


import adiomi.product.entity.Product;
import adiomi.product.entity.ProductDto;
import adiomi.product.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {


    private ProductRepository productRepository;


    @Autowired
    public ProductController(ProductRepository shopRepository) {
        this.productRepository = shopRepository;

    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        var findAllProduct = productRepository.findAll();
        try {
            if (findAllProduct.isEmpty() || findAllProduct.size() == 0)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(findAllProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") long id) {
        var product = productRepository.findById(id);
        if (product.isPresent())
            return new ResponseEntity<>(product.get(), HttpStatus.OK);

        var errorResponse = "product with id " + id + " isn't available";
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<? extends Object> addProduct(@RequestBody Product product) {

        var newProduct = new Product();
        var productDto = new ProductDto();


        try {
            newProduct.setName(product.getName());
            newProduct.setQuantity(product.getQuantity());
            newProduct.setPrice(product.getPrice());

            if (newProduct.getPrice() < 0 || newProduct.getQuantity() < 0)
                return new ResponseEntity<>(newProduct, HttpStatus.BAD_REQUEST);


            var result = productRepository.save(newProduct);
            productDto.setId(result.getId());

            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends Object> updateItem(@PathVariable long id, @RequestBody Product product) {
        var productDto = new ProductDto();
        try {
            var productInCatalog = productRepository.getById(id);
            productInCatalog.setName(product.getName());
            productInCatalog.setQuantity(product.getQuantity());
            productInCatalog.setPrice(product.getPrice());

            var result = productRepository.save(productInCatalog);
            productDto.setId(result.getId());

            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteItem(@PathVariable(value = "id") long id) {
        var productDto = new ProductDto();
        try {
            var findProduct = productRepository.findById(id);
            if (!(findProduct.isPresent()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            productDto.setId(findProduct.get().getId());
            productRepository.deleteById(findProduct.get().getId());
            return new ResponseEntity<>(productDto, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
