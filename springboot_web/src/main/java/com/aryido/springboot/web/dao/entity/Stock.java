package com.aryido.springboot.web.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="STOCK")
public class Stock {

    @Id
    @Column(name="STOCK_SYMBOL", length = 4)
    private String stockSymbol;

    @Column(name="COMPANY_NAME", length = 10)
    private String companyName;

    @Column(name="PRICE")
    private String price;

    @Column(name = "VOLUME")
    private String volume;

    @Column(name = "CREATE_TIME")
    private String createTime;

}
