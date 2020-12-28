package com.aryido.springboot.web.service;

import com.aryido.springboot.web.dao.IStockDAO;
import com.aryido.springboot.web.dao.entity.Stock;
import com.aryido.springboot.web.vo.StockVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
public class StockServiceTest {

    @MockBean
    private IStockDAO stockDAO;

    @Autowired
    private IStockService stockService;


    @Test
    public void testQueryAll() {
        Mockito.when(stockDAO.findAll()).thenReturn(generatedStockRepository());
        Collection<StockVO> stockVOs = stockService.queryAll();

        for (StockVO stockVO : stockVOs) {
            Assertions.assertAll(
                    () -> Assertions.assertEquals("3711", stockVO.getStockSymbol(), "stockSymbol failed."),
                    () -> Assertions.assertEquals("日月光", stockVO.getCompanyName(), "CompanyName failed."),
                    () -> Assertions.assertEquals(80.4, stockVO.getPrice(), "Price failed"),
                    () -> Assertions.assertEquals(146, stockVO.getVolume(), "Volume failed")
            );
        }
    }

    public void testQueryBy() {
        Iterable<Stock> stocks = generatedStockRepository();

        Mockito.when(stockDAO.findAll()).thenReturn(generatedStockRepository());
    }


    private Iterable<Stock> generatedStockRepository() {
        ArrayList<Stock> list = new ArrayList<Stock>();
        list.add(new Stock("3711", "日月光", "80.4", "146", "2020/12/25 15:32:06"));
        return list;
    }

}
