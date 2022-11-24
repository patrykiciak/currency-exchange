package com.example.cryptoexchange.exchangerates.adapter.out;

import com.example.cryptoexchange.exchangerates.application.port.out.GetExchangeRatesPort;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

// TODO: It might be desired to implement caching to reduce network traffic and latency
public class ExchangeRateApi implements GetExchangeRatesPort {
    private final RestTemplate restTemplate;

    public ExchangeRateApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency, String[] targetCurrencies)
            throws FailedToFetchExchangeRatesException {
        final var response = requestExchangeRates(baseCurrency, targetCurrencies);
        final var receivedExchangeRates = response.rates();

        if (receivedExchangeRates == null) {
            throw new FailedToFetchExchangeRatesException();
        }

        final var anyCurrencyMatch = Arrays.stream(targetCurrencies).anyMatch(receivedExchangeRates::containsKey);

        if(!anyCurrencyMatch) {
            return Map.of();
        }

        return receivedExchangeRates;
    }

    private GetExchangeRatesApiResponse requestExchangeRates(String baseCurrency, String[] targetCurrencies)
            throws FailedToFetchExchangeRatesException {
        // TODO: Consider externalizing the URL
        final var urlTemplate = UriComponentsBuilder.fromHttpUrl("https://api.exchangerate.host/latest")
                .queryParam("base", baseCurrency)
                .queryParam("symbols", String.join(",", targetCurrencies))
                .queryParam("source", "crypto")
                .encode()
                .build();

        try {
            return restTemplate.getForObject(urlTemplate.toUri(), GetExchangeRatesApiResponse.class);
        } catch (RestClientException suppressed) {
            final var exception = new FailedToFetchExchangeRatesException();
            exception.addSuppressed(suppressed);
            throw exception;
        }
    }
}
