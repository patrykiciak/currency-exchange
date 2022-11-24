package com.example.cryptoexchange.exchangerates.domain;

import java.math.BigDecimal;

public record ExchangeForecast(BigDecimal rate, BigDecimal amount, BigDecimal result, BigDecimal fee) {}
