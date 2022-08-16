package ru.svistunovaleksei.tg.currencyconverter.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FromToCurrencyDto {
    private Map<String, ToCurrencyConvertDto> rates;
    private String status;
}
