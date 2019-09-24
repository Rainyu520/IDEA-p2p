package com.bjpowernode.p2p.web;/**
 * ClassName:RechargeRecordController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.p2p.utils.DateUtil;
import com.bjpowernode.p2p.vo.PaginationVO;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
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
    public String alipayRecharge(HttpServletRequest request,Model model,
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


        return "";
    }

    @RequestMapping("/loan/wxpayRecharge")
    public String wxpayRecharge(HttpServletRequest request,
                                @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        System.out.println ("wxpay" + rechargeMoney);


        return "";
    }
}
