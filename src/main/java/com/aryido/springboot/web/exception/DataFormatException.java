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
public class DataFormatException extends RuntimeException {
    private Object obj;

    public DataFormatException(Object obj) {
        super("Data's format incorrect!");
        this.obj = obj;
    }

}
