package com.example.cryptoexchange.exchangerates.adapter.in;

import java.math.BigDecimal;
import java.util.Map;

record GetCurrenciesResponse(String source, Map<String, BigDecimal> rates) {}
