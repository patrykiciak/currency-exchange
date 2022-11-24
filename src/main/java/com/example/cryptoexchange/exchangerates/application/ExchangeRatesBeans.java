package com.example.cryptoexchange.exchangerates.application;

import com.example.cryptoexchange.exchangerates.adapter.out.ExchangeRateApi;
import com.example.cryptoexchange.exchangerates.application.port.out.GetExchangeRatesPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class ExchangeRatesBeans {
    private final RestTemplate restTemplate;

    ExchangeRatesBeans(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    GetExchangeRatesPort getExchangeRatesPort() {
        return new ExchangeRateApi(restTemplate);
    }

    @Bean
    GetExchangeRatesService getExchangeRatesService() {
        return new GetExchangeRatesService(getExchangeRatesPort());
    }

    @Bean
    GetExchangeForecastService getExchangeForecastService() {
        return new GetExchangeForecastService(getExchangeRatesPort());
    }
}
