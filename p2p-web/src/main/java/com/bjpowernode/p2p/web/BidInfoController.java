package com.bjpowernode.p2p.web;/**
 * ClassName:BidInfoController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.utils.Result;
import com.bjpowernode.p2p.vo.PaginationVO;
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
 * 2019/9/19
 */
@Controller
public class BidInfoController extends BaseController {
    @Autowired
    private BidInfoService bidInfoService;


    @RequestMapping("/loan/myInvest")
    public String myInvest(Model model ,HttpServletRequest request,
                           @RequestParam(value = "currentPage", defaultValue = "1") Integer currenPage) {
        //从session取出user
        User sessionUser = (User) BaseController.getSession (request, Constants.sessionUser);

        //准备参数map
        HashMap<String,Object> map = new HashMap ();
        int pageSize = 10;
        map.put ("uId", sessionUser.getId ());
        map.put ("currentPage", (currenPage-1)*pageSize);
        map.put ("pageSize", pageSize);

        PaginationVO<BidInfo> vo
                = bidInfoService.queryBidInfoListByPage (map);

        //计算总页数
        int totalPage = vo.getTotal ().intValue ()/pageSize;
        int mod = vo.getTotal ().intValue () % pageSize;
        if(mod != 0){
            totalPage+=1;
        }


        model.addAttribute ("totalPage", totalPage);
        model.addAttribute ("bidInfoTotal", vo.getTotal ());
        model.addAttribute ("bidInfoList", vo.getDataList ());
        model.addAttribute ("currentPage", currenPage);


        return "myInvest";
    }

    @RequestMapping("/loan/inverst")
    @ResponseBody
    public Result inverst(HttpServletRequest request,@RequestParam(value = "bidMoney",required = true)Double bidMoney,
                          @RequestParam(value = "loanId",required = true)Integer loanId){

        User user = (User) BaseController.getSession (request, Constants.sessionUser);
        //准备数据
        Map<String,Object> map =  new HashMap<> ();
        map.put ("loanId", loanId);
        map.put ("bidMoney", bidMoney);
        map.put ("userId", user.getId ());
        map.put ("phone", user.getPhone ());

        //投资  1.产品的可投资金额（用乐观是避免超卖现象） 2.投资记录 3.账户余额
        try {
            bidInfoService.invert(map);


        } catch (Exception e) {
            e.printStackTrace ();
            return Result.error ("投资失败");
        }


        return Result.success ();
    }
}
