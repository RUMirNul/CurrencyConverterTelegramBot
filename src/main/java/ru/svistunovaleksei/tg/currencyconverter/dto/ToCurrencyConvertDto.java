package ru.svistunovaleksei.tg.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ToCurrencyConvertDto {
    @JsonProperty("currency_name")
    private String currencyName;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("rate_for_amount")
    private String rateForAmount;
}
