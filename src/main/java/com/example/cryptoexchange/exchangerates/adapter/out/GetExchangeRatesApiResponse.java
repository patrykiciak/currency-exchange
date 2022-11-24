package com.example.cryptoexchange.exchangerates.adapter.out;

import java.math.BigDecimal;
import java.util.Map;

record GetExchangeRatesApiResponse(
        Motd motd,
        boolean success,
        String base,
        String date,
        Map<String, BigDecimal> rates
) {}

record Motd(String msg, String url) {}
