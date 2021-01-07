package com.aryido.springboot.web.vo;

import lombok.*;

/**
 * The stockVo value object.
 *
 * @author YunYang Lee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class StockVO {
    private String stockSymbol;
    private String companyName;
    private float price;
    private int volume;

}
