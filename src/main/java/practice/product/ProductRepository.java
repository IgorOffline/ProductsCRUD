package practice.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductDto, Integer> {

    Optional<ProductDto> findByCode(String code);
}
