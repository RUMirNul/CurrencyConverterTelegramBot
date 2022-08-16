package ru.svistunovaleksei.tg.currencyconverter.dto;

import lombok.Data;

@Data
public class ConversionParametersDto {
    private String from;
    private String to;
    private String amount;

}
