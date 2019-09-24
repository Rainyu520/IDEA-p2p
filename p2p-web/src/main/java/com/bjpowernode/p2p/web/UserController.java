package com.bjpowernode.p2p.web;/**
 * ClassName:UserController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.*;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;
import com.bjpowernode.p2p.utils.Result;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/16
 */
@Controller
public class UserController {
    private Logger logger = LogManager.getLogger (UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInfoService bidInfoService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private IncomeRecordService incomeRecordService;
    @Autowired
    private RedisService redisService;


    @RequestMapping(value = "/loan/checkPhone")
    @ResponseBody
    public Result checkPhone(@RequestParam(value = "phone", required = true) String phone) {

        //根据手机号查用户信息
        User user = userService.queryUserInfoByPhone (phone);
        if (!ObjectUtils.allNotNull (user)) {
            return Result.success ();
        }

        return Result.error ("手机号已经存在");

    }

    @RequestMapping(value = "/loan/checkCode")
    @ResponseBody
    public Result checkCode(HttpServletRequest request, String checkCode) {
        //从session中取验证码
        // String capcha = (String) request.getSession ().getAttribute (Constants.CAPTCHA);
        String capcha = (String) BaseController.getSession (request, Constants.CAPTCHA);

        if (StringUtils.equalsIgnoreCase (capcha, checkCode)) {
            return Result.success ();
        }
        return Result.error ("验证码不正确，请重新输入");

    }

    @RequestMapping(value = "/loan/register")
    @ResponseBody
    public Result register(HttpServletRequest request, String phone, String password) {

        //生成新用户
        try {
            userService.register (phone, password);
            //放入session中
            //request.getSession ().setAttribute (Constants.sessionUser, userService.queryUserInfoByPhone (phone));
            BaseController.setSession (request, Constants.sessionUser, userService.queryUserInfoByPhone (phone));
        } catch (Exception e) {
            e.printStackTrace ();
            logger.info ("手机号" + phone + ",注册失败:" + e.getMessage ());
            return Result.error ("注册失败");
        }


        return Result.success ();
    }

    @RequestMapping("/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request) {
        //从session中获取用户的信息
        User sessionUser = (User) BaseController.getSession (request, Constants.sessionUser);

        //根据用户标识获取帐户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid (sessionUser.getId ());

        return financeAccount;
    }

    @RequestMapping(value = "/loan/verfiyRealName")
    @ResponseBody
    public Result verfiyRealName(HttpServletRequest request, String realName, String idCard) {


        //访问验证名字与身份证号是否一致
        Map<String, Object> pMap = new HashMap<> ();
        pMap.put ("appkey", "7b51e28418627dbc631589e2af4229a5");
        pMap.put ("cardNo", idCard);
        pMap.put ("realName", realName);

        try {
            //获得返回值类型是json字符串
            String jsonString = HttpClientUtils.doPost ("https://way.jd.com/youhuoBeijing/test", pMap);

        /*    String jsonString="{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"乐天磊\",\n" +
                    "            \"idcard\": \"350721197702134399\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";*/


            //使用fastjson转换成json对象
            JSONObject jsonObject = JSONObject.parseObject (jsonString);
            //获得通信码
            String code = jsonObject.getString ("code");
            if (!StringUtils.equals (code, "10000")) {
                return Result.error ("通信异常，请稍候再试");
            }
            Boolean status = jsonObject.getJSONObject ("result").getJSONObject ("result").getBoolean ("isok");
            //如果是假，验证成功
            if (!status) {
                return Result.error ("姓名与身份证号码不匹配，请重试");
            }
            //修改数据库信息
            //从session中取出user
            User sessionUser = (User) BaseController.getSession (request, Constants.sessionUser);

            //新建user对象
            User u = new User ();
            u.setId (sessionUser.getId ());
            u.setName (realName);
            u.setIdCard (idCard);
            //调用服务层修改用户信息（实名认证）
            userService.modifyUserInfoById (u);
            //把完整user对象放入session中
            BaseController.setSession (request, Constants.sessionUser, userService.queryUserInfoByPhone (sessionUser.getPhone ()));


        } catch (Exception e) {
            e.printStackTrace ();
            logger.info (e.getMessage ());
            return Result.error ("用户实名认证异常");
        }


        return Result.success ();
    }

    @RequestMapping(value = "/loan/logout")
    public String logout(HttpServletRequest request) {
        request.getSession ().removeAttribute (Constants.sessionUser);
        return "redirect:/index";
    }

    @RequestMapping("/loan/loginResources")
    @ResponseBody
    public Result loginResources() {
        Result result = new Result ();
        //历史平均年化收益率
        Double historyAverageYearRate = loanInfoService.queryHistoryAverageYearRate ();
        //平台用户数
        Long userCount = userService.queryAllUserCount ();
        //累计成交额
        Double totalBidMoney = bidInfoService.queryTotalBidMoney ();

        result.put ("historyAverageRate", historyAverageYearRate);
        result.put ("UserCount", userCount);
        result.put ("countMoney", totalBidMoney);


        return result;
    }

    @RequestMapping(value = "/loan/login")
    @ResponseBody
    public Result login(HttpServletRequest request, @RequestParam(value = "phone", required = true) String phone,
                        @RequestParam(value = "password", required = true) String password,
                        @RequestParam(value = "messageCode",required = true)String messageCode) {
        //先验证短信验证码是否争取
       //从redis中取验证码
        String code = redisService.get(phone);
        if (!StringUtils.equals (code, messageCode)){
            return Result.error ("验证码错误，请重试");
        }else {
            //验证用户名密码是否正确、修改最近登录时间 返回user对象
            User user = null;
            try {
                user = userService.login (phone, password);
                //将user对象放入session中
                BaseController.setSession (request, Constants.sessionUser, user);
            } catch (Exception e) {
                e.printStackTrace ();
                return Result.error ("用户名密码错误");
            }


        }




        return Result.success ();


    }

    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model) {
        //从session中取user
        User user = (User) BaseController.getSession (request, Constants.sessionUser);

        Map map = new HashMap ();
        map.put ("uId", user.getId ());
        map.put ("currentPage", 0);
        map.put ("pageSize", 5);

        //查询可用余额
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid (user.getId ());

        model.addAttribute ("financeAccount", financeAccount);

        //最近5笔投资
        List<BidInfo> bidInfoList = bidInfoService.queryRecentBidInfoListByUid (map);
        model.addAttribute ("bidInfoList", bidInfoList);


        //最近5笔充值
        List<RechargeRecord> rechargeRecords = rechargeRecordService.queryRecentRechargeRecordByUid (map);
        model.addAttribute ("rechargeRecords", rechargeRecords);

        //最近5笔收益
        List<IncomeRecord> incomeRecords = incomeRecordService.queryRecentIncomeRecordByUid (map);
        model.addAttribute ("incomeRecords", incomeRecords);


        return "myCenter";
    }

    @RequestMapping("/loan/messageCheck")
    @ResponseBody
    public Result meaageCheck(HttpServletRequest request,
                              @RequestParam(value = "phone", required = true) String phone) {

        //准备参数
        String randomCode = BaseController.getRandom(4);
        String content = "【凯信通】您的验证码是："+randomCode;

        Map<String,Object> map = new HashMap<> ();
        map.put ("appkey", "7b51e28418627dbc631589e2af4229a5");
        map.put ("mobile", phone);
        map.put ("content", content);




        try {
         //   String jsonString = HttpClientUtils.doPost ("https://way.jd.com/kaixintong/kaixintong", map);

            String jsonString="{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 0,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1188684</remainpoint>\\n <taskID>106476496</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                    "}";



            //解析json
            JSONObject jsonObject  = JSONObject.parseObject (jsonString);
            String code  = jsonObject.getString ("code");
            if (!StringUtils.equals (code, "10000")){
                return Result.error ("通信错误,请稍候重试");
            }
            String resultXml = jsonObject.getString ("result");

            //解析xml
            Document document = DocumentHelper.parseText (resultXml);
            Node node = document.selectSingleNode ("//returnstatus");
            String text = node.getText ();
            if (!StringUtils.equals (text, "Success")){
                return Result.error ("短信平台发送失败");
            }
            //将生成的验证码存到redis中
            redisService.put(phone,randomCode);




        } catch (Exception e) {
            e.printStackTrace ();
            return Result.error ("异常超时，请稍候再试");
        }


        return Result.success ("messageCode",randomCode);
    }

}
