package com.bjpowernode.pay.web;/**
 * ClassName:AlipayController
 * Package:com.bjpowernode.pay.web
 * Description
 *
 * @date ：${Date}
 */

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.bjpowernode.pay.config.AlipayConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author：Rainyu
 * 2019/9/24
 */
@Controller
public class AlipayController {


    @RequestMapping("/api/alipay")
    public String alipay(Model model, @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                         @RequestParam(value = "total_amount", required = true) Double total_amount,
                         @RequestParam(value = "subject", required = true) String subject) throws AlipayApiException {
//获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient (AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest ();
        alipayRequest.setReturnUrl (AlipayConfig.return_url);
        alipayRequest.setNotifyUrl (AlipayConfig.notify_url);


        alipayRequest.setBizContent ("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");


        //请求
        String result = alipayClient.pageExecute (alipayRequest).getBody ();
        model.addAttribute ("toAlipay", result);
        return "payToAlipay";
    }

    @RequestMapping("/api/alipayTradeQuery")
    @ResponseBody
    public String alipayTradeQuery(Model model, @RequestParam(value = "out_trade_no", required = true) String out_trade_no) throws AlipayApiException {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient (AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest ();

        alipayRequest.setBizContent ("{\"out_trade_no\":\"" + out_trade_no + "\"}");

        //请求
        String result = alipayClient.execute (alipayRequest).getBody ();

        return result;
    }
}
