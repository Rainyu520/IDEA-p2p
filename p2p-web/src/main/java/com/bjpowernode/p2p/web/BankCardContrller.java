package com.bjpowernode.p2p.web;/**
 * ClassName:BankCardContrller
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.p2p.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/26
 */
@Controller
public class BankCardContrller {
    @Autowired
    private RedisService redisService;

    @RequestMapping("/loan/bankCard")
    @ResponseBody
    public Result bankCardCheck(@RequestParam(value = "phone",required = true)String phone,
                                @RequestParam(value = "realName",required = true)String realName,
                                @RequestParam(value = "bankCardNo",required = true)String bankCardNo,
                                @RequestParam(value = "idCard",required = true)String idCard,
                                @RequestParam(value = "messageCode",required = true)String messageCode) throws Exception {
        System.out.println ("realName");
        //从redis中取验证码
        String phoneCode = redisService.get(phone);
        if (!StringUtils.equals (phoneCode, messageCode)) {
            return Result.error ("验证码错误，请重试");
        }else {
            //准备验证银行卡的参数
            Map<String,Object> paramMap = new HashMap<> ();
            paramMap.put ("appkey", "7b51e28418627dbc631589e2af4229a5");
            paramMap.put ("accName", realName);
            paramMap.put ("cardPhone", phone);
            paramMap.put ("certificateNo", idCard);
            paramMap.put ("cardNo", bankCardNo);
          /*  String jsonString = HttpClientUtils.doPost ("https://way.jd.com/YOUYU365/keyelement", paramMap);*/
       String jsonString = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"serialNo\": \"5590601f953b512ff9695bc58ad49269\",\n" +
                    "        \"respCode\": \"000000\",\n" +
                    "        \"respMsg\": \"验证通过\",\n" +
                    "        \"comfrom\": \"jd_query\",\n" +
                    "        \"success\": \"true\"\n" +
                    "    }\n" +
                    "}";
            System.out.println (jsonString);
            JSONObject jsonObject = JSONObject.parseObject (jsonString);
            String code = jsonObject.getString ("code");
            if(!StringUtils.equals (code, "10000")){
                return Result.error ("通信异常");
            }
            JSONObject resultJson = jsonObject.getJSONObject ("result");
            String success = resultJson.getString ("success");
            if (!StringUtils.equals (success, "true")){
                return Result.error ("验证失败");
            }

            return Result.success ();

        }



    }

    @RequestMapping("/loan/msgCheck")
    @ResponseBody
    public Result msgCheck(@RequestParam(value = "phone", required = true) String phone) throws Exception {
        //准备参数
        System.out.println (phone);
        String randomCode = BaseController.getRandom (4);
        String content = "【创信】你的验证码是：" + randomCode + "，3分钟内有效";
        Map<String, Object> paramMap = new HashMap<> ();
        paramMap.put ("appkey", "7b51e28418627dbc631589e2af4229a5");
        paramMap.put ("mobile", phone);
        paramMap.put ("content", content);
        /*  String jsonString  = HttpClientUtils.doPost ("https://way.jd.com/chuangxin/dxjk", paramMap);*/
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"ReturnStatus\": \"Success\",\n" +
                "        \"Message\": \"ok\",\n" +
                "        \"RemainPoint\": 420842,\n" +
                "        \"TaskID\": 18424321,\n" +
                "        \"SuccessCounts\": 1\n" +
                "    }\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject (jsonString);
        String code = jsonObject.getString ("code");
        if (!StringUtils.equals (code, "10000")) {
            return Result.error ("通信错误,请稍候重试");
        }
        JSONObject resultJson = jsonObject.getJSONObject ("result");
        String resultMessage = resultJson.getString ("Message");
        if (!StringUtils.equals (resultMessage, "ok")) {
            return Result.error ("短信验证失败");
        }

        //将生成的验证码存到redis中
        redisService.put (phone, randomCode);


        return Result.success ("messageCode", randomCode);

    }
}
