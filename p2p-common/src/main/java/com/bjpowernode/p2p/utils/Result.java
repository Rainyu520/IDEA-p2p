package com.bjpowernode.p2p.utils;/**
 * ClassName:Result
 * Package:com.bjpowernode.p2p.utils
 * Description
 *
 * @date ：${Date}
 */

import java.util.HashMap;

/**
 *Author：Rainyu
 *2019/9/16
 */

public class Result extends HashMap<Object,Object> {

    public static Result success(){
        Result result = new Result ();
        result.put ("statuscode", 10000);
        return result;

    }
    public static Result success(String key,String msg){
        Result result = new Result ();
        result.put ("statuscode", 10000);
        result.put (key, msg);
        return result;

    }

    public static Result error(String msg){
        Result result = new Result ();
        result.put ("statuscode", 99999);
        result.put ("msg", msg);
        return result;
    }

}
