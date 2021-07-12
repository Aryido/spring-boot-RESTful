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
public class ConflictException extends RuntimeException {
    private Object obj;

    public ConflictException(Object obj) {
        super("Data conflict!");
        this.obj = obj;
    }

}
