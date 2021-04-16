package com.aryido.springboot.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class dfasfd {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test1() {
        logger.warn("trace");
    }
}
