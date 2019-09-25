package com.bjpowernode.p2p.web;/**
 * ClassName:RechargeRecordController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.config.AlipayConfig;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.p2p.utils.DateUtil;
import com.bjpowernode.p2p.vo.PaginationVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/19
 */
@Controller
public class RechargeRecordController {
    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private RedisService redisService;


    @RequestMapping("/loan/myRecharge")
    public String myRecharge(Model model, HttpServletRequest request,
                             @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage) {

        //从session取值
        User sessionUser = (User) BaseController.getSession (request, Constants.sessionUser);

        //准备参数
        int pageSize = 10;
        Map<String, Object> map = new HashMap<> ();
        map.put ("currentPage", (currentPage - 1) * pageSize);
        map.put ("pageSize", pageSize);
        map.put ("uId", sessionUser.getId ());

        PaginationVO<RechargeRecord> vo = rechargeRecordService.queryRechargeRecordByPage (map);
        //计算总页数
        int totalPage = vo.getTotal ().intValue () / pageSize;
        int mod = vo.getTotal ().intValue () % pageSize;
        if (mod != 0) {
            mod += 1;
        }

        model.addAttribute ("totalPage", totalPage);
        model.addAttribute ("rechargeRecordList", vo.getDataList ());
        model.addAttribute ("currentPage", currentPage);
        model.addAttribute ("rechargeRecordTotal", vo.getTotal ());

        return "myRecharge";

    }

    @RequestMapping("/loan/alipayRecharge")
    public String alipayRecharge(HttpServletRequest request, Model model,
                                 @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {
        //从session取user
        User user = (User) BaseController.getSession (request, Constants.sessionUser);


        //生成充值记录

        //准备订单编号  时间戳 + 随机数字
        String rechargeNo = DateUtil.getDate () + redisService.getRandomNum ();
        RechargeRecord rechargeRecord = new RechargeRecord ();
        rechargeRecord.setRechargeMoney (rechargeMoney);
        rechargeRecord.setRechargeNo (rechargeNo);
        rechargeRecord.setRechargeStatus ("0");//0表示充值中
        rechargeRecord.setRechargeTime (new Date ());
        rechargeRecord.setUid (user.getId ());
        rechargeRecord.setRechargeDesc ("支付宝充值");
        try {
            rechargeRecordService.addRechargeRecord (rechargeRecord);
        } catch (Exception e) {
            e.printStackTrace ();
            model.addAttribute ("trade_msg", "充值失败");
            return "toRechargeBack";
        }
        //调用pay工程支付请求接口

        model.addAttribute ("out_trade_no", rechargeNo);
        model.addAttribute ("total_amount", rechargeMoney);
        model.addAttribute ("subject", "TEST");


        return "payToPayProject";
    }

    @RequestMapping("/loan/alipayBack")
    public String alipayBack(Model model, HttpServletRequest request, String out_trade_no) throws Exception {
        System.out.println ("alipayBack");


        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String> ();
        Map<String, String[]> requestParams = request.getParameterMap ();
        for (Iterator<String> iter = requestParams.keySet ().iterator (); iter.hasNext (); ) {
            String name = (String) iter.next ();
            String[] values = (String[]) requestParams.get (name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String (valueStr.getBytes ("ISO-8859-1"), "utf-8");
            params.put (name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1 (params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //调用pay工程的服务订单查询接口
            Map<String, Object> map = new HashMap<> ();
            map.put ("out_trade_no", out_trade_no);
            String resultString = HttpClientUtils.doPost ("http://localhost:8083/pay/api/alipayTradeQuery", map);



            //解析json
            JSONObject resultJson = JSONObject.parseObject (resultString);
            JSONObject alipay_trade_page_pay_response = resultJson.getJSONObject ("alipay_trade_query_response");
            //返回通信code
            String code = alipay_trade_page_pay_response.getString ("code");

            if (!StringUtils.equals (code, "10000")) {
                model.addAttribute ("trade_msg", "充值失败");
                return "toRechargeBack";
            }

            String trade_status = alipay_trade_page_pay_response.getString ("trade_status");

            /*
             * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）
             * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
             * TRADE_SUCCESS（交易支付成功）
             * TRADE_FINISHED（交易结束，不可退款）*/

            String rechargeNo = rechargeNo = alipay_trade_page_pay_response.getString ("out_trade_no");
            String rechargeMoney = alipay_trade_page_pay_response.getString ("total_amount");
            if (StringUtils.equals (trade_status, "TRADE_CLOSED")) {
                //交易失败
                //修改充值状态为2，
                RechargeRecord recharge = new RechargeRecord ();

                recharge.setRechargeNo (rechargeNo);
                recharge.setRechargeStatus ("2");
                try {
                    rechargeRecordService.modifyRechargeRecordByRechargeNo (recharge);
                } catch (Exception e) {
                    e.printStackTrace ();
                    model.addAttribute ("trade_msg", "充值失败");
                    return "toRechargeBack";
                }


            } else if (StringUtils.equals (trade_status, "TRADE_SUCCESS")) {
                //交易成功
                //修改充值订单状态为1 充值成功 ; 给用户账户添加金额【uid,rechargeMoney,rechargeNo】
                User user = (User) BaseController.getSession (request, Constants.sessionUser);

                Map<String, Object> parammap = new HashMap<> ();
                parammap.put ("uId", user.getId ());
                parammap.put ("rechargeMoney", rechargeMoney);
                parammap.put ("rechargeNo", rechargeNo);

                try {
                    //判断当前订单状态是否为0
                    RechargeRecord rechargeRecord = rechargeRecordService.queryRechargeByRechargeNo(out_trade_no);
                    if ("0".equals (rechargeRecord.getRechargeStatus ())){
                        rechargeRecordService.recharge (parammap);
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                    model.addAttribute ("trade_msg", "充值失败");
                    return "toRechargeBack";
                }

            }


        } else {
            model.addAttribute ("trade_msg", "签名失败");
            return "toRechargeBack";
        }


        return "redirect:/loan/myCenter";
    }

    @RequestMapping("/loan/wxpayRecharge")
    public String wxpayRecharge(HttpServletRequest request,
                                @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        System.out.println ("wxpay" + rechargeMoney);


        return "";
    }
}
