package com.aryido.springboot.web.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Handling exception which is defined by ourself.
 *
 * @author YunYang Lee
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NoDataException extends RuntimeException {
    private String stockSymbol;

    public NoDataException(String stockSymbol) {
        super("No data!");
        this.stockSymbol = stockSymbol;
    }

}



