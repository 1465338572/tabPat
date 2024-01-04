package com.example.tabpat.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * 返回结果实体类
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
public class Result {
    private Integer code; //状态码
    private String message; //返回信息
    private Object data; //返回数据

    private Result(){

    }

    public Result(Integer code, String message){
        super();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object data){
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Integer code, String message){
        return new Result(code,message);
    }

    public static Result success(Integer code, String message, Object data){
        return new Result(code,message,data);
    }

    public static Result failure(Integer code, String message){
        return new Result(code,message);
    }

    public static Result failure(Integer code, String message, Object data){
        return new Result(code,message,data);
    }
}
