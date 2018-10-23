package com.helper.system.controller;

import com.helper.system.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/28 - 下午2:20
 * Created by IntelliJ IDEA.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常处理，比如：404,500
     * * @param req
     * * @param resp
     * * @param e
     * * @return
     * * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData defaultErrorHandler(Exception e) throws Exception {
        logger.error("GlobalExceptionHandler -- error："+e.getMessage(), e);
        ResponseData r = new ResponseData();
        r.setMessage(e.toString());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            r.setCode(404);
        } else {
            r.setCode(500);
        }
        r.setData(e.getStackTrace());
        r.setStatus(false);
        return r;
    }
}
