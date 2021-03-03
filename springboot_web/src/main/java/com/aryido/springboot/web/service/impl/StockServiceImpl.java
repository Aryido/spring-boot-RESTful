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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
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
<<<<<<< HEAD
    public Collection<StockVO> queryAll() {
        HashMap<String, StockVO> map = new HashMap<>() {
            {
                for (Stock stock : stockDAO.findAll()) {
                    StockVO stockVO = transformEntityToVO(stock);
                    put(stock.getStockSymbol(), stockVO);
                }
            }
        };
        return map.values();
=======
    public Iterable<StockVO> queryAll() {
        Iterable<Stock> stocks = stockDAO.findAll();
        Stream<Stock> stream = StreamSupport.stream(stocks.spliterator(), false);
        return stream
                .map(StockServiceImpl::transformEntityToVO)
                .collect(Collectors.toList());
>>>>>>> branchExercise
    }

    /**
     * To find a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol stock's id.
     * @return StockVO
     */
    @Override
    @Cacheable(value = "queryByStockSymbol")
    public StockVO queryBy(String stockSymbol) {
        System.out.println("from H2");
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
<<<<<<< HEAD
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockSymbol);
        } else {
            return transformEntityToVO(optionalStock.get());
        }
    }

    /**
     * To create a data in database, and we need to check data's format and existing of the data.
     *
     * @param stockVO data to be added
     * @return StockVO
     */
=======
        return optionalStock
                .map(StockServiceImpl::transformEntityToVO)
                .orElseThrow(() -> new NoDataException(stockSymbol));
    }

>>>>>>> branchExercise
    @Override
    public StockVO addData(StockVO stockVO) {
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }
<<<<<<< HEAD

        if (stockDAO.findById(stockVO.getStockSymbol()).isPresent()) {
            throw new ConflictException(stockVO);
        }

        try {
            Stock stock = transformVOtoEntity(stockVO);
            stockDAO.save(stock);
        } catch (Exception e) {
            throw new RuntimeException("add error");
        }
=======
        stockDAO
                .findById(stockVO.getStockSymbol())
                .ifPresent(stock -> {
                    throw new ConflictException(stockVO);
                });

        stockDAO.save(transformVOtoEntity(stockVO));
>>>>>>> branchExercise
        return stockVO;
    }

    /**
     * To update a data in database, and we need to check data's format and existing of the data .
     *
     * @param stockVO data to be updated
     * @return StockVO
     */
    @Override
    public StockVO updateData(StockVO stockVO) {
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }
<<<<<<< HEAD
        Optional<Stock> optionalStock = stockDAO.findById(stockVO.getStockSymbol());
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockVO.getStockSymbol());
        }
        if (isDataConflict(optionalStock.get(), stockVO)) {
            throw new ConflictException(stockVO);
        }

        try {
            Stock stock = transformVOtoEntity(stockVO);
            stockDAO.save(stock);
        } catch (Exception e) {
            throw new RuntimeException("update error");
        }
=======

        Optional<Stock> optionalStock = stockDAO.findById(stockVO.getStockSymbol());
        optionalStock
                .ifPresentOrElse(
                        stock -> {
                            if (isDataConflict(stock.getCompanyName(), stockVO.getCompanyName())) {
                                throw new ConflictException(stockVO);
                            }
                        },
                        () -> {
                            throw new NoDataException(stockVO.getStockSymbol());
                        });
        stockDAO.save(transformVOtoEntity(stockVO));
>>>>>>> branchExercise
        return stockVO;
    }

    /**
     * To delete a data in database by primary key(stock's symbol).
     *
     * @param stockSymbol to be deleted data's id
     * @return StockVO
     */
    @Override
    public StockVO deleteDataBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
<<<<<<< HEAD
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockSymbol);
        } else {
            Stock stock = optionalStock.get();
            try {
                stockDAO.deleteById(stock.getStockSymbol());
            } catch (Exception e) {
                throw new RuntimeException("delete error");
            }
            return transformEntityToVO(stock);
        }
    }

    /**
     * To check Data's format
     *
     * @return Boolean
     */
    public static Boolean isDataFormatIncorrect(StockVO stockObj) {
=======
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
>>>>>>> branchExercise
        String stockSymbol = stockObj.getStockSymbol();
        return (stockSymbol.length() != 4);
    }

<<<<<<< HEAD
    /**
     * To check whether Data is Conflict or not
     *
     * @return Boolean
     */
    public static Boolean isDataConflict(Stock stock, StockVO stockVO) {
        return !stock.getCompanyName().equals(stockVO.getCompanyName());
=======
    public static Boolean isDataConflict(String dataBaseCompanyName, String inputCompanyName) {
        return (!dataBaseCompanyName.equals(inputCompanyName));
>>>>>>> branchExercise
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
