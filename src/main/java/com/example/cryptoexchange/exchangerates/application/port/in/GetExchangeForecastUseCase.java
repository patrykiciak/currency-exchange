package com.example.cryptoexchange.exchangerates.application.port.in;

import com.example.cryptoexchange.exchangerates.domain.ExchangeForecast;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;

import java.math.BigDecimal;
import java.util.Map;

public interface GetExchangeForecastUseCase {
    Map<String, ExchangeForecast> getForecast(BigDecimal amount, String baseCurrency, String[] targetCurrencies) throws
            FailedToFetchExchangeRatesException;
}
