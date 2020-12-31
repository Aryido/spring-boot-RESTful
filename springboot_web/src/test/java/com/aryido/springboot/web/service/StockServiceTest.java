package com.aryido.springboot.web.service;

import com.aryido.springboot.web.dao.IStockDAO;
import com.aryido.springboot.web.dao.entity.Stock;
import com.aryido.springboot.web.exception.NoDataException;
import com.aryido.springboot.web.service.impl.StockServiceImpl;
import com.aryido.springboot.web.vo.StockVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


@SpringBootTest
public class StockServiceTest {

    private IStockDAO stockDAO;

    @Qualifier("StockServiceImpl")
    private IStockService stockService;

    public StockServiceTest(@Autowired IStockService stockService, @Autowired IStockDAO stockDAO) {
        this.stockService = stockService;
        this.stockDAO = stockDAO;
    }

    @BeforeEach
    void setup() {
        System.out.println("Starting test....");
    }

    @AfterEach
    void afterEachMethod() {
        System.out.println("Ending test...");
    }

    @Test
    void testQueryAll() {
        //expected output
        HashMap<String, StockVO> map = new HashMap<>();
        map.put("3711", new StockVO("3711", "日月光", 80.4f, 146));
        map.put("0056", new StockVO("0056", "高股息", 29.66f, 580));
        map.put("2330", new StockVO("2330", "台積電", 509.0f, 932));
        map.put("3045", new StockVO("3045", "台灣大", 99.4f, 56));
        map.put("0050", new StockVO("0050", "台灣50", 118.55f, 50));

        //executed queryAll()
        Collection<StockVO> stockVOs = stockService.queryAll();

        Assertions.assertEquals(5, stockVOs.size());
        stockVOs.forEach(stockVO -> {
                    StockVO expectedStockVO = map.get(stockVO.getStockSymbol());
                    Assertions.assertAll(
                            () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                            () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                            () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                            () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
                    );
                }
        );
    }

    @Test
    void testQueryBy() {
        //expected output
        StockVO expectedStockVO = new StockVO("3711", "日月光", 80.4f, 146);
        //executed queryBy() by inputting "3711"
        StockVO stockVO = stockService.queryBy("3711");

        Assertions.assertFalse(Objects.isNull(stockVO));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testAddData() {
        //expected output
        StockVO expectedStockVO = new StockVO("2412", "中華電", 109.0F, 5227);
        //executed addData() by inputting StockVO
        StockVO stockVO = stockService.addData(new StockVO("2412", "中華電", 109.0F, 5227));

        Assertions.assertFalse(Objects.isNull(stockVO));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateData() {
        //expected output
        StockVO expectedStockVO = new StockVO("3711", "日月光", 99.9f, 999);
        //executed updateData() by inputting StockVO
        StockVO stockVO = stockService.updateData(new StockVO("3711", "日月光", 99.9f, 999));

        Assertions.assertFalse(Objects.isNull(stockVO));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteDataBy() {
        StockVO expectedHasBeenDeletedstockVO = stockService.queryBy("3711");
        Assertions.assertFalse(Objects.isNull(expectedHasBeenDeletedstockVO));

        //executed deleteDataBy() by inputting "3711"
        StockVO deletedStockVO = stockService.deleteDataBy("3711");
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedHasBeenDeletedstockVO.getStockSymbol(), deletedStockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedHasBeenDeletedstockVO.getCompanyName(), deletedStockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedHasBeenDeletedstockVO.getPrice(), deletedStockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedHasBeenDeletedstockVO.getVolume(), deletedStockVO.getVolume(), "Volume failed")
        );

        Assertions.assertThrows(NoDataException.class, () -> {
            stockService.queryBy("3711");
        }, "Need to throws NoDataException");
    }

    @Test
    void testTransformVOtoEntity() {
        //expected output
        Date nowTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = ft.format(nowTime);
        Stock expectedStock = new Stock("3711", "日月光", "80.4", "146", time);

        //executed testTransformVOtoEntity() by inputting StockVO
        Stock stock = StockServiceImpl.transformVOtoEntity(new StockVO("3711", "日月光", 80.4F, 146));

        //start assertions
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStock.getStockSymbol(), stock.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStock.getCompanyName(), stock.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStock.getPrice(), stock.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStock.getVolume(), stock.getVolume(), "Volume failed")
        );
    }

    @Test
    void testTransformEntityToVO() {
        //expected output
        StockVO expectedStockVO = new StockVO("3711", "日月光", 80.4F, 146);

        //executed testTransformVOtoEntity() by inputting Stock
        StockVO stockVO = StockServiceImpl.transformEntityToVO(new Stock("3711", "日月光", "80.4", "146", "2020/12/25 15:32:06"));

        //start assertions
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
        );
    }

    @Test
    void testIsDataFormatIncorrect() {

        StockVO stockVOCorrect = new StockVO("3711", "日月光", 80.4F, 146);
        StockVO stockVOIncorrect = new StockVO("37111111111111", "日月光", 80.4F, 146);
        Boolean incorrect = StockServiceImpl.isDataFormatIncorrect(stockVOCorrect);
        Boolean correct = StockServiceImpl.isDataFormatIncorrect(stockVOIncorrect);

        //start assertions
        Assertions.assertEquals(false, incorrect);
        Assertions.assertEquals(true, correct);
    }

    @Test
    void testIsDataConflict() {
        Assertions.assertEquals(true, StockServiceImpl.isDataConflict("Apple", "Guava"));
        Assertions.assertEquals(false, StockServiceImpl.isDataConflict("Apple", "Apple"));
    }


}
