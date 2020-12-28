package com.aryido.springboot.web.dao;

import com.aryido.springboot.web.dao.entity.Stock;
import org.springframework.data.repository.CrudRepository;

/**
 * The interface is used to access all stock's data by extending CrudRepository,
 * which is a class from springframework.data, and it has basing CRUD methods.
 *
 * The type Stock、String represent bean and Id(primary key)
 */
public interface IStockDAO extends CrudRepository<Stock, String> {

}
