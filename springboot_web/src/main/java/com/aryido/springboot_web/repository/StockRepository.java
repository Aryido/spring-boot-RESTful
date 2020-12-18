package com.aryido.springboot_web.repository;

import com.aryido.springboot_web.stockEntity.Stock;

import java.util.Collection;


public interface StockRepository {

	public Collection<Stock> findAll();

	public Stock findBySymbol( String stockSymbol );

	public void saveOrUpdate( Stock stock );

	public void deleteBySymbol( String stockSymbol );

}
