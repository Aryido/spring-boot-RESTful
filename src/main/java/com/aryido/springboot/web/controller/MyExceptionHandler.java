package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
import com.aryido.springboot.web.exception.NoDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MyExceptionHandler is used to catch all "Exception" from spring-boot controller.
 * With the annotation "ControllerAdvice", we can handle exception.
 *
 * @author YunYang LEE
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    /**
     * It can handle NoDataException and returning message which be defined by ourself.
     *
     * @param noDataExceptionObj be defined exception object
     * @return errorMessageMap
     */
    @ExceptionHandler({NoDataException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleException_NoDataException(NoDataException noDataExceptionObj) {
        log.error("NoDataException");
        return new HashMap<>() {
            {
                put("Time", nowTimeFormatFunction());
                put("status", HttpStatus.BAD_REQUEST);
                put("message", noDataExceptionObj.getMessage());
                put("search stockSymbol", noDataExceptionObj.getStockSymbol());
            }
        };
    }

    /**
     * It can handle DataFormatException and returning message which be defined by ourself.
     *
     * @param dataFormatExceptionObj be defined exception object
     * @return errorMessageMap
     */
    @ExceptionHandler(DataFormatException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public Map<String, Object> handleException_DataFormatException(DataFormatException dataFormatExceptionObj) {
        log.error("DataFormatException");
        return new HashMap<>() {
            {
                put("Time", nowTimeFormatFunction());
                put("status", HttpStatus.METHOD_NOT_ALLOWED);
                put("message", dataFormatExceptionObj.getMessage());
                put("Failed data", dataFormatExceptionObj.getObj());
            }
        };
    }

    /**
     * It can handle ConflictException and returning message which be defined by ourself.
     *
     * @param conflictExceptionObj be defined exception object
     * @return errorMessageMap
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> handleException_ConflictException(ConflictException conflictExceptionObj) {
        log.error("ConflictException");
        return new HashMap<>() {
            {
                put("Time", nowTimeFormatFunction());
                put("status", HttpStatus.CONFLICT);
                put("message", conflictExceptionObj.getMessage());
                put("Failed data", conflictExceptionObj.getObj());
            }
        };
    }

    /**
     * To format now time
     *
     * @return SimpleDateFormat
     */
    private String nowTimeFormatFunction() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date());
    }

}
