package com.aryido.springboot.web.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataFormatException extends RuntimeException {
    private Object obj;

    public DataFormatException(Object obj) {
        super("Data's format incorrect!");
        this.obj = obj;
    }

}
