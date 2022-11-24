package com.example.cryptoexchange.exchangerates.application;

import com.example.cryptoexchange.exchangerates.exception.InvalidAmountException;

import java.math.BigDecimal;

public final class GetForecastCommand {

    private final BigDecimal amount;
    private final String baseCurrency;
    private final String[] targetCurrencies;

    private GetForecastCommand(BigDecimal amount, String baseCurrency, String[] targetCurrencies) {
        this.amount = amount;
        this.baseCurrency = baseCurrency;
        this.targetCurrencies = targetCurrencies;
    }

    public static GetForecastCommand create(BigDecimal amount, String baseCurrency, String[] targetCurrencies) throws InvalidAmountException {
        final var command = new GetForecastCommand(amount, baseCurrency, targetCurrencies);
        command.validate();
        return command;
    }

    private void validate() throws InvalidAmountException {
        if (this.amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidAmountException();
        }
    }

    public BigDecimal amount() {
        return amount;
    }

    public String baseCurrency() {
        return baseCurrency;
    }

    public String[] targetCurrencies() {
        return targetCurrencies;
    }
}
