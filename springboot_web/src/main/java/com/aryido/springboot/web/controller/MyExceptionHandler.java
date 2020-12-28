package com.aryido.springboot.web.controller;

import com.aryido.springboot.web.exception.ConflictException;
import com.aryido.springboot.web.exception.DataFormatException;
import com.aryido.springboot.web.exception.NoDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler({NoDataException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleException_NoDataException(Exception e, NoDataException inputObj) {
        Map<String, Object> map = new HashMap<>();
        String timeStr = nowTimeFormatFunction();
        map.put("Time", timeStr);
        map.put("status", HttpStatus.BAD_REQUEST);
        map.put("message", e.getMessage());
        map.put("search stockSymbol", inputObj.getStockSymbol());
        return map;
    }

    @ExceptionHandler(DataFormatException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public Map<String, Object> handleException_DataFormatException(Exception e, DataFormatException inputObj) {
        Map<String, Object> map = new HashMap<>();
        String timeStr = nowTimeFormatFunction();
        map.put("Time", timeStr);
        map.put("status", HttpStatus.METHOD_NOT_ALLOWED);
        map.put("message", e.getMessage());
        map.put("Failed data", inputObj.getObj());
        return map;
    }


    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> handleException_ConflictException(Exception e, ConflictException inputObj) {
        Map<String, Object> map = new HashMap<>();
        String timeStr = nowTimeFormatFunction();
        map.put("Time", timeStr);
        map.put("status", HttpStatus.CONFLICT);
        map.put("message", e.getMessage());
        map.put("Failed data", inputObj.getObj());
        return map;
    }


    private String nowTimeFormatFunction() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date nowTime = new Date();
        String timeStr = sdFormat.format(nowTime);
        return timeStr;
    }

}
