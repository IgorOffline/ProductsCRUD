package practice.product.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductReq {

    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "PriceHrk is mandatory")
    @Min(value = 0, message = "Minimal priceHrk value is 0")
    private BigDecimal priceHrk;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotNull(message = "Available is mandatory")
    private Boolean available;
}
