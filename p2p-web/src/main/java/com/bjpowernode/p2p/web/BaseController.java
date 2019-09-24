package com.bjpowernode.p2p.web;/**
 * ClassName:BaseController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.user.User;

import javax.servlet.http.HttpServletRequest;

/**
 *Author：Rainyu
 *2019/9/17
 */

public class BaseController {
    public static Object getSession(HttpServletRequest request, Object key) {
        return request.getSession ().getAttribute ((String) key);
    }

    public static void setSession(HttpServletRequest request, String name, Object object) {
        request.getSession ().setAttribute (name, object);
    }

    public static String getRandom(int n) {
        StringBuffer stringBuffer = new StringBuffer ();
        for (int i=0;i<n;i++){
           stringBuffer.append (Math.round (Math.random ()*9));
        }
        return stringBuffer.toString ();

    }


}
