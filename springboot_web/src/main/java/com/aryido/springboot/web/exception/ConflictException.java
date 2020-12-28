package com.aryido.springboot.web.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConflictException extends RuntimeException {
    private Object obj;

    public ConflictException(Object obj) {
        super("Data conflict!");
        this.obj = obj;
    }
}
