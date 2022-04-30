package practice.product;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.http.ContentType;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import practice.hnb.HnbService;
import practice.product.req.ProductReq;
import practice.stringly.Path;
import practice.stringly.StatusCode;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    static ProductReq productReq = new ProductReq();

    @Test
    @Order(1)
    void test1() {
        stubFor(get(urlMatching(HnbService.HNB_API_URL + ".*")).willReturn(ok().withBody("[{\"broj_tecajnice\":\"123\"}]")));

        productReq.setName(UUID.randomUUID().toString());
        productReq.setPriceHrk(BigDecimal.valueOf(23.0));
        productReq.setDescription(UUID.randomUUID().toString());
        productReq.setAvailable(true);
        ProductDto productDto = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(productReq)
                .when().post(Path.product()).then().statusCode(StatusCode.ok())
                .extract().response().body().as(ProductDto.class);
        assertNotNull(productDto);
        assertNotNull(productDto.getCode());
        assertEquals(10, productDto.getCode().length());
    }
}
