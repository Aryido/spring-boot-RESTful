package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.service.IStockService;
import com.aryido.springboot.web.vo.StockVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

@AutoConfigureMockMvc
@WebMvcTest(StockHandler.class)
public class StockHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IStockService stockService;

    @BeforeEach
    void setup() {
        System.out.println("Starting test....");
    }

    @AfterEach
    void afterEachMethod() {
        System.out.println("Ending test...");
    }

    @Test
    void testFindAll() throws Exception {
        //expected output
        HashMap<String, StockVO> map = new HashMap<>();
        map.put("3711", new StockVO("3711", "日月光", 80.4f, 146));
        map.put("0056", new StockVO("0056", "高股息", 29.66f, 580));
        map.put("2330", new StockVO("2330", "台積電", 509.0f, 932));
        map.put("3045", new StockVO("3045", "台灣大", 99.4f, 56));
        map.put("0050", new StockVO("0050", "台灣50", 118.55f, 50));
        String expectedJsonResponse = objectMapper.writeValueAsString(map.values());

        //input
        //this function do not need any input

        //Mockito
        Mockito.when(stockService.queryAll()).thenReturn(map.values());

        //executed url="/stock/", method=GET
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stock/");
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //Assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string(expectedJsonResponse));

    }

    @Test
    void testFindBySymbol() throws Exception {
        //expected output
        String expectedJsonResponse = objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 5227));

        //Mockito
        Mockito.when(stockService.queryBy("2412")).thenReturn(new StockVO("2412", "中華電", 109.0F, 5227));

        //executed url="/stock/{id}", method=GET
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stock/2412");
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //Assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string(expectedJsonResponse));

    }

    @Test
    void testSave() throws Exception {
        //expected output
        String expectedJsonResponse = objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 5227));

        //Mockito
        Mockito.when(stockService.addData(new StockVO("2412", "中華電", 109.0F, 5227))).thenReturn(new StockVO("2412", "中華電", 109.0F, 5227));

        //executed url="/stock/", method=POST
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/stock/").contentType("application/json").content(objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 5227)));
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //Assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string(expectedJsonResponse));
    }

    @Test
    void testUpdate() throws Exception {
        //expected output
        String expectedJsonResponse = objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 99999));

        //Mockito
        Mockito.when(stockService.updateData(new StockVO("2412", "中華電", 109.0F, 99999))).thenReturn(new StockVO("2412", "中華電", 109.0F, 99999));

        //executed url="/stock/{id}", method=PUT
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/stock/").contentType("application/json").content(objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 99999)));
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //Assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string(expectedJsonResponse));

    }

    @Test
    void deleteByStockSymbol() throws Exception{
        //expected output
        String expectedJsonResponse = objectMapper.writeValueAsString(new StockVO("2412", "中華電", 109.0F, 99999));

        //Mockito
        Mockito.when(stockService.deleteDataBy("2412")).thenReturn(new StockVO("2412", "中華電", 109.0F, 99999));

        //executed url="/stock/{id}",, method=DELETE
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/stock/2412");
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //Assertion
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string(expectedJsonResponse));


    }


}
