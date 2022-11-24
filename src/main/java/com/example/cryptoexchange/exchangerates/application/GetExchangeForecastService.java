package com.example.cryptoexchange.exchangerates.application;

import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeForecastUseCase;
import com.example.cryptoexchange.exchangerates.application.port.out.GetExchangeRatesPort;
import com.example.cryptoexchange.exchangerates.domain.ExchangeForecast;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

class GetExchangeForecastService implements GetExchangeForecastUseCase {
    private final GetExchangeRatesPort getExchangeRatesPort;

    GetExchangeForecastService(GetExchangeRatesPort getExchangeRatesPort) {
        this.getExchangeRatesPort = getExchangeRatesPort;
    }

    @Override
    public Map<String, ExchangeForecast> getForecast(GetForecastCommand command)
            throws FailedToFetchExchangeRatesException {
        final var exchangeRates =
                getExchangeRatesPort.getExchangeRates(command.baseCurrency(), command.targetCurrencies());
        return exchangeRates.entrySet().parallelStream().map(entry -> {
            final var exchangeRate = entry.getValue();
            final var exchangeResult = command.amount().multiply(exchangeRate);
            final var fee = command.amount().multiply(new BigDecimal("0.01"));
            final var forecast = new ExchangeForecast(exchangeRate, command.amount(), exchangeResult, fee);
            return Map.entry(entry.getKey(), forecast);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
