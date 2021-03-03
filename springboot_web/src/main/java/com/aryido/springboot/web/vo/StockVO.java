package com.aryido.springboot.web.vo;

import lombok.*;

import java.io.Serializable;


/**
 * The stockVo value object.
 *
 * @author YunYang Lee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockVO implements Serializable {
    private String stockSymbol;
    private String companyName;
    private float price;
    private int volume;
}
