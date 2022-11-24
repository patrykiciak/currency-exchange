package com.example.cryptoexchange.exchangerates.adapter.in;

import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeForecastUseCase;
import com.example.cryptoexchange.exchangerates.application.port.in.GetExchangeRatesUseCase;
import com.example.cryptoexchange.exchangerates.exception.FailedToFetchExchangeRatesException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CurrenciesController.class)
class CurrenciesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetExchangeRatesUseCase getExchangeRatesUseCase;

    private <T> T getResponseBody(MvcResult mvcResult, Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), clazz);
    }

    @Test
    void getExchangeRates_successfully() throws Exception {
        // given
        final var baseCurrency = "EUR";
        final var exchangeRates = Map.of("PLN", new BigDecimal("4.75"));
        when(getExchangeRatesUseCase.getExchangeRates(eq(baseCurrency), any())).thenReturn(exchangeRates);

        // when
        final var mvcResult = this.mockMvc.perform(get("/currencies/" + baseCurrency)).andReturn();

        // then
        final var response = getResponseBody(mvcResult, GetCurrenciesResponse.class);
        assertEquals(exchangeRates, response.rates());
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    void getExchangeRates_couldNotRetrieve() throws Exception {
        // given
        final var baseCurrency = "EUR";
        when(getExchangeRatesUseCase.getExchangeRates(eq(baseCurrency), any())).thenThrow(FailedToFetchExchangeRatesException.class);

        // when
        final var mvcResult = this.mockMvc.perform(get("/currencies/" + baseCurrency)).andReturn();

        // then
        assertEquals(503, mvcResult.getResponse().getStatus());
    }
}
