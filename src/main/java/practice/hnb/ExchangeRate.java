package practice.hnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {

    @JsonProperty("broj_tecajnice")
    private String number;
    @JsonProperty("datum_primjene")
    private String applicationDate;
    @JsonProperty("drzava")
    private String country;
    @JsonProperty("drzava_iso")
    private String countryIso;
    @JsonProperty("sifra_valute")
    private String currencyCode;
    @JsonProperty("valuta")
    private String currency;
    @JsonProperty("jedinica")
    private Integer unit;
    @JsonProperty("kupovni_tecaj")
    private String buyingRate;
    @JsonProperty("srednji_tecaj")
    private String midRate;
    @JsonProperty("prodajni_tecaj")
    private String sellingRate;

    public BigDecimal getSellingRateAsMoney() {
        return new BigDecimal(sellingRate.replace(",", "."));
    }
}
