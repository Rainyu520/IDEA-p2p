package com.bjpowernode.p2p.web;/**
 * ClassName:LoanInfoController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.vo.BidUesr;
import com.bjpowernode.p2p.vo.PaginationVO;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/16
 */
@Controller
public class LoanInfoController {
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInfoService bidInfoService;
    @Autowired
    private FinanceAccountService financeAccountService;


    @RequestMapping(value = "/loan/loan")
    public String loan(Model model, @RequestParam(value = "ptype", required = false) Integer ptype,
                       @RequestParam(value = "currentPage", defaultValue = "1") Integer currenPage
    ) {
        //分页查询
        Map<String, Object> map = new HashMap<> ();
        if (ObjectUtils.allNotNull (ptype)) {
            map.put ("productType", ptype);
        }
        //每页显示的条款
        int pageSize = 9;
        map.put ("pageSize", pageSize);
        //页码
        map.put ("currentPage", (currenPage - 1) * pageSize);

        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoListByPage (map);
        //计算总页数
        int totalPage = paginationVO.getTotal ().intValue () / pageSize;
        int mod = paginationVO.getTotal ().intValue () % pageSize;
        if (mod != 0) {
            totalPage += 1;
        }
        model.addAttribute ("totalRows", paginationVO.getTotal ());
        model.addAttribute ("currentPage", currenPage);
        model.addAttribute ("totalPage", totalPage);
        model.addAttribute ("loanInfoList", paginationVO.getDataList ());

        //判断产品类型是否为空
        if (ObjectUtils.allNotNull (ptype)) {
            model.addAttribute ("ptype", ptype);
        }



        //获取投资排行榜
        List<BidUesr> bidUserList =bidInfoService.getBidInfoTop();
        model.addAttribute ("topUserList", bidUserList);



        return "loan";
    }

    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(HttpServletRequest request, Model model, @RequestParam(value = "id", required = true) Integer id) {
        //根据id查出产品
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById (id);
        if (ObjectUtils.allNotNull (loanInfo)) {
            model.addAttribute ("loanInfo", loanInfo);
        }

        //准备map
        Map<String, Object> map = new HashMap<> ();
        map.put ("currentPage", 0);
        map.put ("pageSize", 10);
        map.put ("loanId", id);

        //查最近投资记录前10条
        List<BidInfo> bidInfoList = bidInfoService.queryRecentBidInfoListByLoanId (map);
        model.addAttribute ("bidInfoList", bidInfoList);


        //从session中取user对象
        User user = (User) BaseController.getSession (request, Constants.sessionUser);
        if (ObjectUtils.allNotNull (user)){

            //查当前登录用户可用余额
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid (user.getId ());
            model.addAttribute ("financeAccount", financeAccount);
        }


        return "loanInfo";
    }


}
