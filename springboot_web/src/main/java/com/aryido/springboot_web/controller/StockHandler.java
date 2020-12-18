package com.aryido.springboot_web.controller;

import com.aryido.springboot_web.repository.StockRepository;
import com.aryido.springboot_web.service.StockService;
import com.aryido.springboot_web.stockEntity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/stock")
public class StockHandler {

    @Autowired
    private StockService stockService;

    @GetMapping("/")
    public Collection<Stock> findAll() {
        return stockService.queryAll();
    }

    @GetMapping("/{stockSymbol}")
    public Stock findBySymbol(@PathVariable("stockSymbol") String stockSymbol) {
        return stockService.queryBy(stockSymbol);
    }

    @PostMapping("/")
    public void save(@RequestBody Stock stock) {
        stockService.addData(stock);
    }

    @PutMapping("/")
    public void update(@RequestBody Stock stock) {
        stockService.updateData(stock);
    }

    @DeleteMapping("/{stockSymbol}")
    public void deleteByStockSymbol(@PathVariable("stockSymbol") String stockSymbol) {
        stockService.deleteDataBy(stockSymbol);
    }

}
