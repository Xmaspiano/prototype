package com.helper.system.model;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/28 - 下午2:25
 * Created by IntelliJ IDEA.
 */
@Data
public class ResponseData {
    boolean status;
    int code;
    String message;
    Object data;

}
