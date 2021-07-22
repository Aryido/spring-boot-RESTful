package com.aryido.springboot.web.service;

import com.aryido.springboot.web.dao.entity.Stock;
import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Integration test of StockServiceImpl.
 *
 * @author YunYnag Lee
 */
@SpringBootTest
public class StockServiceTest {

    @Qualifier("StockServiceImpl")
    private final IStockService stockService;

    public StockServiceTest(@Autowired IStockService stockService) {
        this.stockService = stockService;
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
        Iterable<StockVO> stockVOs = stockService.queryAll();
        Assertions.assertEquals(5, stockVOs.spliterator().getExactSizeIfKnown());
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
        StockVO expectedStockVO = new StockVO("3711", "日月光123", 80.4f, 146);

        //executed queryBy() by inputting "3711"
        StockVO stockVO = stockService.queryBy("3711");
        Assertions.assertFalse(Objects.isNull(stockVO));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), stockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), stockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), stockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), stockVO.getVolume(), "Volume failed")
        );

        //Exception test
        //executed queryBy() by inputting a bad data. Ex:"9999"
        Assertions.assertThrows(NoDataException.class, () -> stockService.queryBy("9999"), "No data exception");
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

        //Exception test
        //executed addData() by inputting a bad StockVO
        Assertions.assertThrows(
                DataFormatException.class,
                () -> stockService.updateData(new StockVO("2412111111111111", "中華電", 109.0F, 5227)),
                "Data format Exception, stocksymbol is too long");
        Assertions.assertThrows(
                DataFormatException.class,
                () -> stockService.updateData(new StockVO("0", "中華電", 109.0F, 5227)),
                "Data format Exception, stocksymbol is too short");
        Assertions.assertThrows(NoDataException.class, () -> stockService.updateData(new StockVO("1234", "1234", 109.0F, 5227)), "No data exception");
        Assertions.assertThrows(
                ConflictException.class,
                () -> stockService.updateData(new StockVO("3711", "abc", 44.4f, 200)),
                "inputting data has existed");
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

        //Exception test
        //executed updateData() by inputting a bad StockVO
        Assertions.assertThrows(
                DataFormatException.class,
                () -> stockService.addData(new StockVO("37111111111111111111111111111", "日月光", 99.9f, 999)),
                "Data format Exception, stock's symbol is too long");
        Assertions.assertThrows(
                DataFormatException.class,
                () -> stockService.addData(new StockVO("", "日月光", 99.9f, 999)),
                "Data format Exception, stock's symbol is too short");
        Assertions.assertThrows(
                ConflictException.class,
                () -> stockService.addData(new StockVO("3711", "abc", 44.4f, 200)),
                "conflict Exception, inputting stockVO' companyName has existed, but stockSymbol is different");

    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteDataBy() {
        //expected output
        StockVO expectedStockVO = new StockVO("3711", "日月光", 80.4f, 146);

        //executed deleteDataBy() by inputting "3711"
        StockVO deletedStockVO = stockService.deleteDataBy("3711");
        Assertions.assertFalse(Objects.isNull(expectedStockVO));
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedStockVO.getStockSymbol(), deletedStockVO.getStockSymbol(), "stockSymbol failed."),
                () -> Assertions.assertEquals(expectedStockVO.getCompanyName(), deletedStockVO.getCompanyName(), "CompanyName failed."),
                () -> Assertions.assertEquals(expectedStockVO.getPrice(), deletedStockVO.getPrice(), "Price failed"),
                () -> Assertions.assertEquals(expectedStockVO.getVolume(), deletedStockVO.getVolume(), "Volume failed")
        );

        Assertions.assertThrows(NoDataException.class, () -> stockService.queryBy("3711"), "Need to throws NoDataException because data has been deleted.");
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
        StockVO stockVO = StockServiceImpl.transformEntityToVO(new Stock("3711", "日月光", "80.4", "146", "2020-12-25 15:32:06"));

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
        Stock stock = Stock.builder().stockSymbol("3711").companyName("日月光").price("80.4").volume("146").createTime("2020-11-11 11:11:11").build();
        Assertions.assertEquals(false, StockServiceImpl.isDataConflict(stock, StockVO.builder().stockSymbol("3711").companyName("日月光").price(80.4F).volume(146).build()));
        Assertions.assertEquals(true, StockServiceImpl.isDataConflict(stock, StockVO.builder().stockSymbol("3711").companyName("日月光1").price(80.4F).volume(146).build()));
    }

}
