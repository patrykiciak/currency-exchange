package com.example.cryptoexchange.exchangerates.application.port.out;

import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;

import java.math.BigDecimal;
import java.util.Map;

public interface GetExchangeRatesPort {
    Map<String, BigDecimal> getExchangeRates(String baseCurrency, String[] targetCurrencies) throws
            FailedToFetchExchangeRatesException;
}
