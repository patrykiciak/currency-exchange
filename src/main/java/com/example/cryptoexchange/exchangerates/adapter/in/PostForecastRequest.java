package com.example.cryptoexchange.exchangerates.adapter.in;

import java.math.BigDecimal;

record PostForecastRequest(String from, String[] to, BigDecimal amount) {}
