package com.example.cryptoexchange.exchangerates.application.port.in;

import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;

import java.math.BigDecimal;
import java.util.Map;

public interface GetExchangeRatesUseCase {

    /**
     * @param targetCurrencies: When null, provide exchange rates for all retrieved currencies.
     *                          Return empty collection when no match was found for specified target currencies
     */
    Map<String, BigDecimal> getExchangeRates(String baseCurrency, String[] targetCurrencies)
            throws FailedToFetchExchangeRatesException;
}
