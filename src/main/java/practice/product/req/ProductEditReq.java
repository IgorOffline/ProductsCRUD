package practice.product.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductEditReq {

    private String name;
    @Min(value = 0, message = "Minimal priceHrk value is 0")
    private BigDecimal priceHrk;
    private String description;
    private Boolean available;
}
