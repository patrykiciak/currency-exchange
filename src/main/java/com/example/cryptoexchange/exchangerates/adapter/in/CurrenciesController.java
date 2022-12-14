package com.example.cryptoexchange.exchangerates.adapter.in;

import com.example.cryptoexchange.exchangerates.application.GetForecastCommand;
import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeForecastUseCase;
import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeRatesUseCase;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import com.example.cryptoexchange.exchangerates.exception.InvalidAmountException;
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
            @RequestParam(value = "filter", required = false, defaultValue = "") String[] filter)
            throws FailedToFetchExchangeRatesException {
        final var exchangeRates = getExchangeRatesUseCase.getExchangeRates(baseCurrency, filter);
        final var response = new GetCurrenciesResponse(baseCurrency, exchangeRates);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/currencies/exchange")
    ResponseEntity<Map<String, Object>> forecastExchange(@RequestBody PostForecastRequest request)
            throws InvalidAmountException, FailedToFetchExchangeRatesException {
        final var exchangeForecastMap = getExchangeForecastUseCase.getForecast(
                GetForecastCommand.create(request.amount(), request.from(), request.to()));
        final Map<String, Object> response = new HashMap<>(exchangeForecastMap);
        response.put("from", request.from());
        return ResponseEntity.ok(response);
    }
}
