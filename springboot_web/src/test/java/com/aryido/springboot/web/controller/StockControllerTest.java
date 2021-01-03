package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.dao.IStockDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
public class StockControllerTest {

    private StockHandler stockHandler;

    public StockControllerTest(@Autowired StockHandler stockHandler) {
        this.stockHandler = stockHandler;
    }

    @BeforeEach
    void setup() {
        System.out.println("Starting test....");
    }

    @AfterEach
    void afterEachMethod() {
        System.out.println("Ending test...");
    }





}
