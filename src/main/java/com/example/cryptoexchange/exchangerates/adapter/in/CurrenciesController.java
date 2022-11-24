package com.example.cryptoexchange.exchangerates.adapter.in;

import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeForecastUseCase;
import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeRatesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
class CurrenciesController {

    private final GetExchangeRatesUseCase getExchangeRatesUseCase;

    private final GetExchangeForecastUseCase getExchangeForecastUseCase;

    CurrenciesController(GetExchangeRatesUseCase getExchangeRatesUseCase,
            GetExchangeForecastUseCase getExchangeForecastUseCase) {
        this.getExchangeRatesUseCase = getExchangeRatesUseCase;
        this.getExchangeForecastUseCase = getExchangeForecastUseCase;
    }

    @GetMapping("/currencies/{currency}")
    ResponseEntity<GetCurrenciesResponse> getExchangeRates(@PathVariable("currency") String baseCurrency,
            @RequestParam(value = "filter", required = false, defaultValue = "") String[] filter) {
        try {
            final var exchangeRates = getExchangeRatesUseCase.getExchangeRates(baseCurrency, filter);
            final var response = new GetCurrenciesResponse(baseCurrency, exchangeRates);
            return ResponseEntity.ok(response);
        } catch (FailedToFetchExchangeRatesException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/currencies/exchange")
    ResponseEntity<Map<String, Object>> forecastExchange(@RequestBody PostForecastRequest request) {
        try {
            final var exchangeForecastMap =
                    getExchangeForecastUseCase.getForecast(request.amount(), request.from(), request.to());
            final Map<String, Object> response = new HashMap<>(exchangeForecastMap);
            response.put("from", request.from());
            return ResponseEntity.ok(response);
        } catch (FailedToFetchExchangeRatesException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
