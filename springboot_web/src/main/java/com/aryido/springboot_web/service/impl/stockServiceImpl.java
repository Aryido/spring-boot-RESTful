package com.aryido.springboot_web.service.impl;

import com.aryido.springboot_web.repository.StockRepository;
import com.aryido.springboot_web.service.StockService;
import com.aryido.springboot_web.stockEntity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class stockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public Collection<Stock> queryAll() {
        return stockRepository.findAll();
    }

    @Override
    public Stock queryBy(String stockSymbol) {
        return stockRepository.findBySymbol(stockSymbol);
    }

    @Override
    public Stock addData(Stock stockObj) {
        stockRepository.saveOrUpdate(stockObj);
        Stock theStock = stockRepository.findBySymbol(stockObj.getStockSymbol());
        return theStock;
    }

    @Override
    public Stock updateData(Stock stockObj) {
        stockRepository.saveOrUpdate(stockObj);
        Stock theStock = stockRepository.findBySymbol(stockObj.getStockSymbol());
        return theStock;
    }

    @Override
    public Stock deleteDataBy(String stockSymbol) {
        Stock theStock = stockRepository.findBySymbol(stockSymbol);
        stockRepository.deleteBySymbol(stockSymbol);
        return theStock;
    }
}
