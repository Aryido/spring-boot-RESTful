package com.aryido.springboot_web.repository.impl;

import com.aryido.springboot_web.repository.StockRepository;
import com.aryido.springboot_web.stockEntity.Stock;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private static Map<String, Stock> stockMap;

    static {
        //ConcurrentHashMap確保執行續安全
        stockMap = new ConcurrentHashMap<>();
        stockMap.put("2330", new Stock("2330", "台積電", (float) 509.0, 932));
        stockMap.put("3045", new Stock("3045", "台灣大", (float) 99.40, 45));
        stockMap.put("0050", new Stock("0050", "台灣50", (float) 118.55, 50));
        stockMap.put("0056", new Stock("0056", "高股息", (float) 29.66, 580));
    }

    @Override
    public Collection<Stock> findAll() {
        return stockMap.values();
    }

    @Override
    public Stock findBySymbol(String stockSymbol) {
        return stockMap.get(stockSymbol);
    }

    @Override
    public void saveOrUpdate(Stock stock) {
        stockMap.put(stock.getStockSymbol(), stock);
    }

    @Override
    public void deleteBySymbol(String stockSymbol) {
        stockMap.remove(stockSymbol);
    }

}
