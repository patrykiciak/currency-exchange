package com.example.cryptoexchange.exchangerates.application;

import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeForecastUseCase;
import com.example.cryptoexchange.exchangerates.application.port.out.GetExchangeRatesPort;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

class GetExchangeForecastServiceTest {

    private GetExchangeForecastUseCase getExchangeForecastUseCase;

    private GetExchangeRatesPort getExchangeRatesPort;

    @BeforeEach
    void setUp() {
        getExchangeRatesPort = Mockito.mock(GetExchangeRatesPort.class);
        getExchangeForecastUseCase = new GetExchangeForecastService(getExchangeRatesPort);
    }

    @Test
    void getForecast_doesCorrectCalculations() throws FailedToFetchExchangeRatesException {
        // given
        final var baseCurrency = "EUR";
        final var exchangeRates = Map.of("PLN", new BigDecimal("4.75"));
        final var targetCurrencies = exchangeRates.keySet().toArray(new String[exchangeRates.size()]);
        when(getExchangeRatesPort.getExchangeRates(baseCurrency, targetCurrencies)).thenReturn(exchangeRates);

        // when
        final var forecastMap =
                getExchangeForecastUseCase.getForecast(new BigDecimal(100), baseCurrency, targetCurrencies);

        // then
        assertEquals(0, new BigDecimal(100).compareTo(forecastMap.get("PLN").amount()));
        assertEquals(0, new BigDecimal(1).compareTo(forecastMap.get("PLN").fee()));
        assertEquals(0, new BigDecimal("4.75").compareTo(forecastMap.get("PLN").rate()));
        assertEquals(0, new BigDecimal(475).compareTo(forecastMap.get("PLN").result()));
    }

    @Test()
    void getForecast_cannotRetrieveExchangeRates() throws FailedToFetchExchangeRatesException {
        // given
        final var baseCurrency = "EUR";
        final var exchangeRates = Map.of("PLN", new BigDecimal("4.75"));
        final var targetCurrencies = exchangeRates.keySet().toArray(new String[exchangeRates.size()]);
        when(getExchangeRatesPort.getExchangeRates(baseCurrency, targetCurrencies)).thenThrow(
                FailedToFetchExchangeRatesException.class);

        // when
        try {
            getExchangeForecastUseCase.getForecast(new BigDecimal(100), baseCurrency, targetCurrencies);

            // then
            fail(); // we expect the exception, so fail if it wasn't thrown
        } catch (FailedToFetchExchangeRatesException ignored) {
        }
    }
}
