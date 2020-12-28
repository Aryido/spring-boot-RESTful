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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service("StockServiceImpl")
public class StockServiceImpl implements IStockService {

    private final IStockDAO stockDAO;

    @Autowired
    public StockServiceImpl(IStockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    @Override
    public Collection<StockVO> queryAll() {
        Iterable<Stock> stocks = stockDAO.findAll();
        HashMap<String, StockVO> map = new HashMap<>();
        for (Stock stock : stocks) {
            StockVO stockVO = transformEntityToVO(stock);
            map.put(stock.getStockSymbol(), stockVO);
        }
        return map.values();
    }

    @Override
    public StockVO queryBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockSymbol);
        } else {
            Stock stock = optionalStock.get();
            StockVO stockVO = transformEntityToVO(stock);
            return stockVO;
        }
    }

    @Override
    public StockVO addData(StockVO stockVO) {
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        } else {
            Stock stock = transformVOtoEntity(stockVO);
            stockDAO.save(stock);
            System.out.println(stock);
            return stockVO;
        }
    }

    @Override
    public StockVO updateData(StockVO stockVO) {
        if (isDataFormatIncorrect(stockVO)) {
            throw new DataFormatException(stockVO);
        }

        Optional<Stock> optionalStock = stockDAO.findById(stockVO.getStockSymbol());
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockVO.getStockSymbol());
        } else {
            Stock oldStock = optionalStock.get();
            String dataBaseCompanyName=oldStock.getCompanyName();
            String inputCompanyName = stockVO.getCompanyName();
            if(isDataConflict(dataBaseCompanyName, inputCompanyName)){
                throw  new ConflictException(stockVO);
            }else{
                Stock newStock = transformVOtoEntity(stockVO);
                stockDAO.save(newStock);
                return stockVO;
            }
        }
    }

    @Override
    public StockVO deleteDataBy(String stockSymbol) {
        Optional<Stock> optionalStock = stockDAO.findById(stockSymbol);
        if (optionalStock.isEmpty()) {
            throw new NoDataException(stockSymbol);
        } else {
            Stock stock = optionalStock.get();
            stockDAO.deleteById(stock.getStockSymbol());
            StockVO stockVO = transformEntityToVO(stock);
            return stockVO;
        }
    }

    private Boolean isDataFormatIncorrect(StockVO stockObj) {
        String stockSymbol = stockObj.getStockSymbol();
        return (stockSymbol.length() != 4) ? true : false;
    }

    private Boolean isDataConflict(String dataBaseCompanyName, String inputCompanyName) {
        return (dataBaseCompanyName.equals(inputCompanyName)) ? false : true;
    }

    private StockVO transformEntityToVO(Stock stock) {
        StockVO stockVO = new StockVO();
        BeanUtils.copyProperties(stock, stockVO);
        stockVO.setPrice(Float.parseFloat(stock.getPrice()));
        stockVO.setVolume(Integer.parseInt(stock.getVolume()));
        return stockVO;
    }

    private Stock transformVOtoEntity(StockVO stockVO) {
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
