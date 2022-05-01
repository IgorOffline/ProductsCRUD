package practice.product;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import practice.hnb.ExchangeRate;
import practice.hnb.HnbService;
import practice.product.req.ProductReq;
import practice.stringly.Path;
import practice.stringly.StatusCode;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    @MockBean
    private HnbService hnbService;

    static ProductReq productReq = new ProductReq();

    static int count = 1;

    @Test
    @Order(1)
    void test1() throws IOException {
        var exchangeRate = new ExchangeRate();
        exchangeRate.setNumber("84");
        exchangeRate.setApplicationDate("2022-05-01");
        exchangeRate.setCountry("EMU");
        exchangeRate.setCountryIso("EMU");
        exchangeRate.setCurrencyCode("978");
        exchangeRate.setCurrency("EUR");
        exchangeRate.setUnit(1);
        exchangeRate.setBuyingRate("2");
        exchangeRate.setMidRate("2");
        exchangeRate.setSellingRate("2");

        BDDMockito.given(hnbService.exchangeRateEuro()).willReturn(exchangeRate);

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

    @Test
    @Order(3)
    void test3() {
        ++count;
        assertEquals(3, count);
    }

    @Test
    @Order(2)
    void test2() {
        ++count;
        assertEquals(2, count);
    }
}
