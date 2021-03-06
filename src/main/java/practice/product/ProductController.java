package practice.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.code.Code;
import practice.hnb.ExchangeRate;
import practice.hnb.HnbService;
import practice.product.req.ProductEditReq;
import practice.product.req.ProductReq;
import practice.util.Money;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private HnbService hnbService;

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
    public ResponseEntity<ProductDto> post(@Valid @RequestBody ProductReq product) {
        log.info("POST /product, product: {}", product);
        var productDto = new ProductDto();
        productDto.setCode(Code.randomCode());
        productDto.setName(product.getName());
        productDto.setPriceHrk(product.getPriceHrk());

        try {
            setPriceEuro(productDto);
        } catch (Exception ex) {
            log.error("hnbService.exchangeRateEuro error: {}", ex.getMessage());
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }

        productDto.setDescription(product.getDescription());
        productDto.setAvailable(product.getAvailable());

        productDto = repository.save(productDto);
        return new ResponseEntity(productDto, HttpStatus.CREATED);
    }

    private void setPriceEuro(ProductDto productDto) throws IOException {
        ExchangeRate exchangeRateEuro = hnbService.exchangeRateEuro();
        var priceEuro = Money.priceKunaToEuro(productDto.getPriceHrk(), exchangeRateEuro.getSellingRateAsMoney());
        log.info("priceEuro= {}", priceEuro);
        productDto.setPriceEur(priceEuro);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ProductDto> put(@PathVariable String code, @Valid @RequestBody ProductEditReq product) {
        log.info("PUT /product/{}, product: {}", code, product);
        Optional<ProductDto> productDto = repository.findByCode(code);
        if (productDto.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (product.getName() != null) {
            productDto.get().setName(product.getName());
        }
        if (product.getPriceHrk() != null) {
            int comparePriceHrk = productDto.get().getPriceHrk().compareTo(product.getPriceHrk());
            if (comparePriceHrk != 0) {
                productDto.get().setPriceHrk(product.getPriceHrk());

                try {
                    setPriceEuro(productDto.get());
                } catch (Exception ex) {
                    log.error("hnbService.exchangeRateEuro error: {}", ex.getMessage());
                    return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
                }
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
