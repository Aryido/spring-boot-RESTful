package com.aryido.springboot.web.service.impl;

import com.aryido.springboot.web.dao.IStockDAO;
import com.aryido.springboot.web.dao.entity.Stock;
import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
import com.aryido.springboot.web.exception.NoDataException;
import com.aryido.springboot.web.service.IStockService;
import com.aryido.springboot.web.vo.StockVO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * To solve main business logic.
 *
 * @author YunYang Lee
 */
@Service("StockServiceImpl")
public class StockServiceImpl implements IStockService {

    private final IStockDAO stockDAO;


    @Autowired
    public StockServiceImpl(IStockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    /**
     * To find all data in database.
     *
     * @return Collection
     */
    @Override
    @Cacheable(value = "queryAll")//cacheManager = "RedisCacheManager"  cacheAutoConfiguration
    public Iterable<StockVO> queryAll() {
        System.out.println("from H2");
        Iterable<Stock> stocks = stockDAO.findAll();
        return StreamSupport.stream(stocks.spliterator(), false)
                .map(StockServiceImpl::transformEntityToVO)
                .collect(Collectors.toList());
    }

    /**
     * To find a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol stock's id.
     * @return StockVO
     */
    @Override
    @Cacheable(value = "queryBy")
    public StockVO queryBy(String stockSymbol) {
        System.out.println("from H2");
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        return optionalStock
                .map(StockServiceImpl::transformEntityToVO)
                .orElseThrow(() -> new NoDataException(stockSymbol));
    }


    @Override
    @CacheEvict(value = "queryAll", allEntries = true)
    public StockVO addData(StockVO stockVO) {
        System.out.println("from H2");
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }
        if (stockDAO.findById(stockVO.getStockSymbol()).isPresent()) {
            throw new ConflictException(stockVO);
        }
        stockDAO.save(transformVOtoEntity(stockVO));
        return stockVO;
    }

    /**
     * To update a data in database, and we need to check data's format and existing of the data .
     *
     * @param stockVO data to be updated
     * @return StockVO
     */
    @Override
    @CachePut(value = "queryBy", key = "#stockVO.stockSymbol")
    @CacheEvict(value = "queryAll", allEntries = true)
    public StockVO updateData(StockVO stockVO) {
        System.out.println("from H2");
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }

        Optional<Stock> optionalStock = stockDAO.findById(stockVO.getStockSymbol());

        if (optionalStock.isPresent()) {
            if (isDataConflict(optionalStock.get(), stockVO)) {
                throw new ConflictException(stockVO);
            }
        } else {
            throw new NoDataException(stockVO.getStockSymbol());
        }

        stockDAO.save(transformVOtoEntity(stockVO));
        return stockVO;
    }

    /**
     * To delete a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol to be deleted data's id
     * @return StockVO
     */
    @Override
    @CacheEvict(value = {"queryBy", "queryAll"}, key = "#stockSymbol", allEntries = true)
    public StockVO deleteDataBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        optionalStock.ifPresent(stock -> stockDAO.deleteById(stock.getStockSymbol()));

        return optionalStock.map(StockServiceImpl::transformEntityToVO)
                .orElseThrow(() -> {
                    throw new NoDataException(stockSymbol);
                });
    }

    public static boolean isDataFormatIncorrect(StockVO stockObj) {
        String stockSymbol = stockObj.getStockSymbol();
        return (stockSymbol.length() != 4);
    }


    /**
     * To check whether Data is Conflict or not
     *
     * @return Boolean
     */
    public static Boolean isDataConflict(Stock stock, StockVO stockVO) {
        return !stock.getCompanyName().equals(stockVO.getCompanyName());
    }

    /**
     * To transform Entity to VO
     *
     * @return StockVO
     */
    public static StockVO transformEntityToVO(Stock stock) {
        return StockVO.builder()
                .stockSymbol(stock.getStockSymbol())
                .companyName(stock.getCompanyName())
                .price(Float.parseFloat(stock.getPrice()))
                .volume(Integer.parseInt(stock.getVolume()))
                .build();
    }

    /**
     * To transform VO to Entity
     *
     * @return Entity
     */
    public static Stock transformVOtoEntity(StockVO stockVO) {
        Date nowTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = ft.format(nowTime);
        return Stock.builder()
                .stockSymbol(stockVO.getStockSymbol())
                .companyName(stockVO.getCompanyName())
                .price(String.valueOf(stockVO.getPrice()))
                .volume(String.valueOf(stockVO.getVolume()))
                .createTime(time)
                .build();
    }

}
