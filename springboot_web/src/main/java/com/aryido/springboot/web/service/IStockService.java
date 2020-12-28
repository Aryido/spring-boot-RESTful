package com.aryido.springboot.web.service;


import com.aryido.springboot.web.vo.StockVO;

import java.util.Collection;

/**
 * The service use CRUD to operate data from database.
 *
 * @author YunYang LEE
 */
public interface IStockService {

    /**
     * Get all stocks from database.
     *
     * @return collection of stock
     */
    Collection<StockVO> queryAll();

    /**
     * Get stock by stock's Symbol from database.
     *
     * @param stockSymbol
     * @return the stock object
     */
    StockVO queryBy(String stockSymbol);

    /**
     * Create a stock
     *
     * @param stockObj
     * @return the stock object
     */
    StockVO addData(StockVO stockObj);

    /**
     * Update a stock
     *
     * @param stockObj
     * @return the stock object
     */
    StockVO updateData(StockVO stockObj);

    /**
     * Delete a stock by  stock's symbol
     *
     * @param stockSymbol
     * @return the stock object
     */
    StockVO deleteDataBy(String stockSymbol);


}
