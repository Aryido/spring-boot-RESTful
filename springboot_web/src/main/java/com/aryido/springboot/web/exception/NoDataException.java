package com.aryido.springboot.web.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoDataException extends RuntimeException {
    private String stockSymbol;

    public NoDataException(String stockSymbol) {
        super("No data!");
        this.stockSymbol = stockSymbol;
    }

}



