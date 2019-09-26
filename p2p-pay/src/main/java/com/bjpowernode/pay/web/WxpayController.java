package com.bjpowernode.pay.web;/**
 * ClassName:WxpayController
 * Package:com.bjpowernode.pay.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.http.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/26
 */
@Controller
public class WxpayController {



    @RequestMapping("/api/wxpay")
    @ResponseBody
    public Object wxpay(@RequestParam(value = "body",required = true)String body,
                        @RequestParam(value = "out_trade_no",required = true)String out_trade_no,
                        @RequestParam(value = "total_fee",required = true)String total_fee) throws Exception {
        //准备请求参数
        Map<String,String> requestDataMap =  new HashMap<> ();
        requestDataMap.put ("appid","wx8a3fcf509313fd74");
        requestDataMap.put ("mch_id","1361137902");
        requestDataMap.put ("nonce_str",WXPayUtil.generateNonceStr ());
        requestDataMap.put ("body",body);
        requestDataMap.put ("out_trade_no",out_trade_no);

        BigDecimal bigDecimal = new BigDecimal (total_fee);
        bigDecimal.multiply (new BigDecimal (100));
        int rechargeMoney = bigDecimal.intValue ();
        requestDataMap.put ("total_fee",String.valueOf (rechargeMoney));
        requestDataMap.put ("spbill_create_ip","127.0.0.1");
        //TODO
        requestDataMap.put ("notify_url","http://localhost:8083/p2p/loan/wxpayNotify");
        requestDataMap.put ("trade_type","NATIVE");
        requestDataMap.put ("product_id",out_trade_no);

        requestDataMap.put ("sign",WXPayUtil.generateSignature (requestDataMap, "367151c5fd0d50f1e34a68a802d6bbca"));
        Map<String,String> responseDateMap = null;

        try {
            //将map转为xml
            String requestDataXml = WXPayUtil.mapToXml (requestDataMap);
            //调用wxpay统一下单接口接口
            String responseDataXml = HttpClientUtils.doPostByXml ("https://api.mch.weixin.qq.com/pay/unifiedorder", requestDataXml);
            //将xml转map
            responseDateMap = WXPayUtil.xmlToMap (responseDataXml);
        } catch (Exception e) {
            e.printStackTrace ();
        }


        return responseDateMap;
    }

}
