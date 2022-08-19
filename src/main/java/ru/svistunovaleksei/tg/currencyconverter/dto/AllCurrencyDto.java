package ru.svistunovaleksei.tg.currencyconverter.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AllCurrencyDto {
    private Map<String, String> currencies;
    private String status;

}
