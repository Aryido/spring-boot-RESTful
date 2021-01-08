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
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service("StockServiceImpl")
public class StockServiceImpl implements IStockService {

    private final IStockDAO stockDAO;

    @Autowired
    public StockServiceImpl(IStockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    @Override
    public Iterable<StockVO> queryAll() {
        Iterable<Stock> stocks = stockDAO.findAll();
        Stream<Stock> stream = StreamSupport.stream(stocks.spliterator(), false);
        return stream
                .map(StockServiceImpl::transformEntityToVO)
                .collect(Collectors.toList());
    }

    @Override
    public StockVO queryBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        return optionalStock
                .map(StockServiceImpl::transformEntityToVO)
                .orElseThrow(() -> new NoDataException(stockSymbol));
    }

    @Override
    public StockVO addData(StockVO stockVO) {
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

    @Override
    public StockVO updateData(StockVO stockVO) {
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }

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
        return stockVO;
    }

    @Override
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

    public static Boolean isDataConflict(String dataBaseCompanyName, String inputCompanyName) {
        return (!dataBaseCompanyName.equals(inputCompanyName));
    }

    public static StockVO transformEntityToVO(Stock stock) {
        StockVO stockVO = new StockVO();
        BeanUtils.copyProperties(stock, stockVO);
        stockVO.setPrice(Float.parseFloat(stock.getPrice()));
        stockVO.setVolume(Integer.parseInt(stock.getVolume()));
        return stockVO;
    }

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
