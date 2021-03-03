package com.aryido.springboot.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StockVO implements Serializable {
	private String stockSymbol;
	private String companyName;
	private float price;
	private int volume;


}
