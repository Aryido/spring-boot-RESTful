package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.service.IStockService;
import com.aryido.springboot.web.vo.StockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableCaching
@RequestMapping(value = "/stock", produces = {"application/json; charset=UTF-8"})
@Slf4j
public class StockHandler {

    //http://localhost:8080/swagger-ui.html

    @Qualifier("StockServiceImpl")
    private final IStockService stockService;

    @Autowired
    public StockHandler(IStockService stockService) {
        this.stockService = stockService;
    }


    /**
     * To find all data in database.
     *
     * @return Collection
     */
    @GetMapping("/")
    public Iterable<StockVO> findAll() {
        log.info("start Handler find all");
        return stockService.queryAll();
    }

    /**
     * To find a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol PathVariable
     * @return StockVO
     */
    @GetMapping("/{stockSymbol}")
    public StockVO findBySymbol(@PathVariable("stockSymbol") String stockSymbol) {
        log.info("start Handler found by id");
        return stockService.queryBy(stockSymbol);
    }

    /**
     * To create a data in database.
     *
     * @param stockVO RequestBody
     * @return StockVO
     */
    @PostMapping("/")
    public StockVO save(@RequestBody StockVO stockVO) {
        log.info("start Handler saved stock");
        return stockService.addData(stockVO);
    }

    /**
     * To update a data in database.
     *
     * @param stockVO RequestBody
     * @return StockVO
     */
    @PutMapping("/")
    public StockVO update(@RequestBody StockVO stockVO) {
        log.info("start Handler updated stock");
        return stockService.updateData(stockVO);
    }

    /**
     * To delete a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol PathVariable
     * @return StockVO
     */
    @DeleteMapping("/{stockSymbol}")
    public StockVO deleteByStockSymbol(@PathVariable("stockSymbol") String stockSymbol) {
        log.info("start Handler deleted stock");
        return stockService.deleteDataBy(stockSymbol);
    }

}
