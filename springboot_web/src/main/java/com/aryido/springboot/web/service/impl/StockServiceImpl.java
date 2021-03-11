package com.aryido.springboot.web.service.impl;

import com.aryido.springboot.web.dao.IStockDAO;
import com.aryido.springboot.web.dao.entity.Stock;
import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
import com.aryido.springboot.web.exception.NoDataException;
import com.aryido.springboot.web.service.IStockService;
import com.aryido.springboot.web.vo.StockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    @Cacheable(value = "queryAll")  //cacheManager = "RedisCacheManager"  cacheAutoConfiguration
    public Iterable<StockVO> queryAll() {
        System.out.println("from H2");
        Iterable<Stock> stocks = stockDAO.findAll();
        Stream<Stock> stream = StreamSupport.stream(stocks.spliterator(), false);
        return stream
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
        stockDAO
                .findById(stockVO.getStockSymbol())
                .ifPresent(stock -> {
                    throw new ConflictException(stockVO);
                });

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
    public StockVO updateData(StockVO stockVO) {
        System.out.println("from H2");
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }
        Optional<Stock> optionalStock = stockDAO.findById(stockVO.getStockSymbol());
        optionalStock
                .ifPresentOrElse(
                        stock -> {
                            if (isDataConflict(stock, stockVO)) {
                                throw new ConflictException(stockVO);
                            }
                        },
                        () -> {
                            throw new NoDataException(stockVO.getStockSymbol());
                        });
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
    @CacheEvict(value = "queryBy", key = "#stockSymbol")
    public StockVO deleteDataBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        return optionalStock
                .map(stock -> {
                    stockDAO.deleteById(stock.getStockSymbol());
                    return stock;
                })
                .map(StockServiceImpl::transformEntityToVO)
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
        StockVO stockVO = new StockVO();
        BeanUtils.copyProperties(stock, stockVO);
        stockVO.setPrice(Float.parseFloat(stock.getPrice()));
        stockVO.setVolume(Integer.parseInt(stock.getVolume()));
        return stockVO;
    }

    /**
     * To transform VO to Entity
     *
     * @return Entity
     */
    public static Stock transformVOtoEntity(StockVO stockVO) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(stockVO, stock);
        String price = String.valueOf(stockVO.getPrice());
        stock.setPrice(price);
        String volume = String.valueOf(stockVO.getVolume());
        stock.setVolume(volume);
        Date nowTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = ft.format(nowTime);
        stock.setCreateTime(time);
        return stock;
    }

}
