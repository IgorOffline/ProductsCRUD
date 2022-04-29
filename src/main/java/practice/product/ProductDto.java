package practice.product;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "products")
public class ProductDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false, length = 10)
    @NotBlank(message = "Code is mandatory")
    @Length(min = 10, max = 10, message = "Code length must be 10")
    private String code;
    @Column(nullable = false, length = 512)
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Column(name = "price_hrk", nullable = false)
    @Min(value = 0, message = "Minimal priceHrk value is 0")
    private BigDecimal priceHrk;
    @Column(name = "price_eur", nullable = false)
    @Min(value = 0, message = "Minimal priceEur value is 0")
    private BigDecimal priceEur;
    @Column(nullable = false, length = 2048)
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
}
