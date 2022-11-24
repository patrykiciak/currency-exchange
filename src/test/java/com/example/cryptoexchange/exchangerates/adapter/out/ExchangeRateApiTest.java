package com.example.cryptoexchange.exchangerates.adapter.out;

import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ExchangeRateApiTest {

    private ExchangeRateApi exchangeRateApi;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        exchangeRateApi = new ExchangeRateApi(restTemplate);
    }

    @Test
    void getExchangeRates_successfully() throws FailedToFetchExchangeRatesException {
        // given
        final var baseCurrency = "EUR";
        final var plnExchangeRate = new BigDecimal("4.75");
        final var exchangeRates = Map.of("PLN", plnExchangeRate);
        final var receivedResponse =
                new GetExchangeRatesApiResponse(null, true, baseCurrency, "", exchangeRates);

        when(restTemplate.getForObject(any(URI.class), eq(GetExchangeRatesApiResponse.class)))
                .thenReturn(receivedResponse);

        // when
        final var response = exchangeRateApi.getExchangeRates(baseCurrency, new String[]{"PLN"});

        // then
        assertEquals(1, response.size());
        assertEquals(0, response.get("PLN").compareTo(plnExchangeRate));
    }

    @Test
    void getExchangeRates_noMatch() throws FailedToFetchExchangeRatesException {
        // given
        final var baseCurrency = "EUR";
        final var plnExchangeRate = new BigDecimal("4.75");
        final var exchangeRates = Map.of("PLN", plnExchangeRate);
        final var receivedResponse =
                new GetExchangeRatesApiResponse(null, true, baseCurrency, "", exchangeRates);

        when(restTemplate.getForObject(any(URI.class), eq(GetExchangeRatesApiResponse.class)))
                .thenReturn(receivedResponse);

        // when
        final var response = exchangeRateApi.getExchangeRates(baseCurrency, new String[]{"USD"});

        // then
        assertEquals(0, response.size());
    }
}
