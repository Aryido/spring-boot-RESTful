package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.service.IStockService;
import com.aryido.springboot.web.vo.StockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/stock")
public class StockHandler {

    @Qualifier("StockServiceImpl")
    private IStockService stockService;

    @Autowired
    public StockHandler(IStockService stockService) {
        this.stockService = stockService;
    }


    @GetMapping("/")
    public Collection<StockVO> findAll() {
        return stockService.queryAll();
    }

    @GetMapping("/{stockSymbol}")
    public StockVO findBySymbol(@PathVariable("stockSymbol") String stockSymbol) {
        return stockService.queryBy(stockSymbol);
    }

    @PostMapping("/")
    public StockVO save(@RequestBody StockVO stock) {
        return stockService.addData(stock);
    }

    @PutMapping("/")
    public StockVO update(@RequestBody StockVO stock) {
        return stockService.updateData(stock);
    }

    @DeleteMapping("/{stockSymbol}")
    public StockVO deleteByStockSymbol(@PathVariable("stockSymbol") String stockSymbol) {
        return stockService.deleteDataBy(stockSymbol);
    }

}
