package practice.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.product.req.ProductReq;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.Matchers.hasLength;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Autowired
    private MockMvc mvc;

    static String code;

    @Test
    public void t1post() throws Exception {
        stubFor(WireMock.get("https://api.hnb.hr/tecajn/v2?valuta=EUR").willReturn(WireMock.ok(
                "[{\"broj_tecajnice\":\"84\",\"datum_primjene\":\"2022-05-01\",\"drzava\":\"EMU\",\"drzava_iso\":\"EMU\",\"sifra_valute\":\"978\",\"valuta\":\"EUR\",\"jedinica\":1,\"kupovni_tecaj\":\"2\",\"srednji_tecaj\":\"2\",\"prodajni_tecaj\":\"2\"}]"
        )));

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
    public void t2get() throws Exception {
        mvc.perform(get("/product").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void t3get() throws Exception {
        mvc.perform(get("/product/" + code).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
