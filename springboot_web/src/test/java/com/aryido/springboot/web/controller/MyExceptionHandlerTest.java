package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
import com.aryido.springboot.web.exception.NoDataException;
import com.aryido.springboot.web.vo.StockVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Unit test of StockExceptionHandler.
 *
 * @author YunYnag Lee
 */
@AutoConfigureMockMvc
@WebMvcTest(StockHandler.class)
public class MyExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StockHandler stockHandler;

    @BeforeEach
    void setup() {
        System.out.println("Starting test....");
    }

    @AfterEach
    void afterEachMethod() {
        System.out.println("Ending test...");
    }

    @Test
    void testNoDataException() throws Exception {

        //Mockito
        Mockito.when(stockHandler.findBySymbol("1234")).thenThrow(NoDataException.class);

        //executed url="/stock/{id}", method=GET
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stock/1234");
        MvcResult responseData = mockMvc.perform(requestBuilder).andReturn();

        //Assertion
        Assertions.assertEquals(400, responseData.getResponse().getStatus());
    }

    @Test
    void testDataFormatException() throws Exception {

        //Mockito
        Mockito.when(stockHandler.save(Mockito.any(StockVO.class))).thenThrow(DataFormatException.class);

        //executed url="/stock/", method=POST
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/stock/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new StockVO("12345", "中華電", 109.0F, 5227)));
        MvcResult responseData = mockMvc.perform(requestBuilder).andReturn();

        //Assertion
        Assertions.assertEquals(405, responseData.getResponse().getStatus());
    }

    @Test
    void testConflictException() throws Exception {

        //Mockito
        Mockito.when(stockHandler.update(Mockito.any(StockVO.class))).thenThrow(ConflictException.class);

        //executed url="/stock/", method=POST
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/stock/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new StockVO("2330", "中華電", 109.0F, 5227)));
        MvcResult responseData = mockMvc.perform(requestBuilder).andReturn();

        //Assertion
        Assertions.assertEquals(409, responseData.getResponse().getStatus());
    }

}
