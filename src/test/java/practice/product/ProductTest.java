package practice.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.product.req.ProductReq;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.hasLength;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    @Autowired
    private MockMvc mvc;

    static String code;

    @Test
    @Order(1)
    public void post1() throws Exception {
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
                .andExpect(jsonPath("$.code", hasLength(10))).andReturn();

        code = JsonPath.read(result.getResponse().getContentAsString(), "$.code");
    }

    @Test
    @Order(2)
    public void get2() throws Exception {
        mvc.perform(get("/product").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void get3() throws Exception {
        mvc.perform(get("/product/" + code).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
