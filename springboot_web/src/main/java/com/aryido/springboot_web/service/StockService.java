package com.aryido.springboot_web.service;

import com.aryido.springboot_web.stockEntity.Stock;

import java.util.Collection;

public interface StockService {

	public Collection<Stock> queryAll();

	public Stock queryBy(String str);

	public Stock addData(Stock stockObj);

	public Stock updateData(Stock stockObj);

	public Stock deleteDataBy(String str);

}
