package com.aryido.springboot_web.stockEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stock {
	private String stockSymbol;
	private String companyName;
	private float price;
	private long volume;
}
