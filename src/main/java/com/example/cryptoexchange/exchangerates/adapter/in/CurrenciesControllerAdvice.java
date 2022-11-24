package com.example.cryptoexchange.exchangerates.adapter.in;

import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import com.example.cryptoexchange.exchangerates.exception.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class CurrenciesControllerAdvice {

    @ExceptionHandler(InvalidAmountException.class)
    ResponseEntity<String> handleException(InvalidAmountException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Exchanged amount of money must be greater than zero");
    }

    @ExceptionHandler(FailedToFetchExchangeRatesException.class)
    ResponseEntity<String> handleException(FailedToFetchExchangeRatesException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Could not retrieve currency exchange data");
    }
}
