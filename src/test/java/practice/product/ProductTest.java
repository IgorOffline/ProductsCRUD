package practice.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.hnb.ExchangeRate;
import practice.hnb.HnbService;
import practice.product.req.ProductReq;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HnbService hnbService;

    static String code;

    @Test
    @Order(1)
    public void post1() throws Exception {
        mockExchangeRate();

        var productReq = new ProductReq();
        productReq.setName(UUID.randomUUID().toString());
        productReq.setPriceHrk(BigDecimal.valueOf(23.0));
        productReq.setDescription(UUID.randomUUID().toString());
        productReq.setAvailable(true);

        var objectMapper = new ObjectMapper();
        var productReqJson = objectMapper.writeValueAsString(productReq);

        var result = mvc.perform(post("/product").contentType(MediaType.APPLICATION_JSON)
                .content(productReqJson))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.code", hasLength(10)))
                .andExpect(jsonPath("$.priceEur", is(11.5)))
                .andReturn();

        code = JsonPath.read(result.getResponse().getContentAsString(), "$.code");
    }

    private void mockExchangeRate() throws IOException {
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
    }

    @Test
    @Order(2)
    public void get2() throws Exception {
        mvc.perform(get("/product/" + code).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void put3() throws Exception {
        mockExchangeRate();

        var productReq = new ProductReq();
        productReq.setPriceHrk(BigDecimal.valueOf(50.0));

        var objectMapper = new ObjectMapper();
        var productReqJson = objectMapper.writeValueAsString(productReq);

        mvc.perform(put("/product/" + code).contentType(MediaType.APPLICATION_JSON)
                        .content(productReqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceHrk", is(50.0)))
                .andExpect(jsonPath("$.priceEur", is(25.0)));
    }

    @Test
    @Order(4)
    public void delete4() throws Exception {
        mvc.perform(delete("/product/" + code)).andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void getAll() throws Exception {
        var result = mvc.perform(get("/product").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        var json = result.getResponse().getContentAsString();

        var objectMapper = new ObjectMapper();

        List<ProductDto> products = objectMapper.readValue(json, new TypeReference<>(){});

        long count = products.stream().filter(p -> p.getCode().equals(code)).count();

        assertEquals(0, count);
    }
}
