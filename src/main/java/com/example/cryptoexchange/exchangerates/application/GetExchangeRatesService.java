package com.example.cryptoexchange.exchangerates.application;

import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeRatesUseCase;
import com.example.cryptoexchange.exchangerates.application.port.out.GetExchangeRatesPort;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;

import java.math.BigDecimal;
import java.util.Map;

class GetExchangeRatesService implements GetExchangeRatesUseCase {

    private final GetExchangeRatesPort getExchangeRatesPort;

    GetExchangeRatesService(GetExchangeRatesPort getExchangeRatesPort) {
        this.getExchangeRatesPort = getExchangeRatesPort;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency, String[] targetCurrencies) throws
            FailedToFetchExchangeRatesException {
        return getExchangeRatesPort.getExchangeRates(baseCurrency, targetCurrencies);
    }
}
