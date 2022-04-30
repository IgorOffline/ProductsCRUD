package practice.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.code.Code;
import practice.product.req.ProductEditReq;
import practice.product.req.ProductReq;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public List<ProductDto> get() {
        log.info("GET /product");
        return repository.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductDto> getOne(@PathVariable String code) {
        log.info("GET /product/{}", code);
        Optional<ProductDto> productDto = repository.findByCode(code);
        if (productDto.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(productDto.get(), HttpStatus.OK);
    }

    @PostMapping
    public ProductDto post(@Valid @RequestBody ProductReq product) {
        log.info("POST /product, product: {}", product);
        var productDto = new ProductDto();
        productDto.setCode(Code.randomCode());
        productDto.setName(product.getName());
        productDto.setPriceHrk(product.getPriceHrk());
        productDto.setPriceEur(BigDecimal.valueOf(123.0));
        productDto.setDescription(product.getDescription());
        productDto.setAvailable(product.getAvailable());

        productDto = repository.save(productDto);
        return productDto;
    }

    @PutMapping("/{code}")
    public ResponseEntity<ProductDto> put(@PathVariable String code, @Valid @RequestBody ProductEditReq product) {
        log.info("PUT /product/{}, product: {}", code, product);
        Optional<ProductDto> productDto = repository.findByCode(code);
        if (productDto.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (product.getPriceHrk() != null) {
            int comparePriceHrk = productDto.get().getPriceHrk().compareTo(product.getPriceHrk());
            if (comparePriceHrk != 0) {
                productDto.get().setPriceHrk(product.getPriceHrk());
                productDto.get().setPriceEur(BigDecimal.valueOf(123.0));
            }
        }
        if (product.getDescription() != null) {
            productDto.get().setDescription(product.getDescription());
        }
        if (product.getAvailable() != null) {
            productDto.get().setAvailable(product.getAvailable());
        }

        var savedProductDto = repository.save(productDto.get());
        return new ResponseEntity(savedProductDto, HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable String code) {
        log.info("DELETE /product/{}", code);
        Optional<ProductDto> productDto = repository.findByCode(code);
        if (productDto.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        repository.delete(productDto.get());
        return ResponseEntity.ok(null);
    }
}
