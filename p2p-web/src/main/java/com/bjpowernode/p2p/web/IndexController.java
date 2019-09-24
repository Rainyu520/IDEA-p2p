package com.bjpowernode.p2p.web;/**
 * ClassName:inedxController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */


import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * Author：Rainyu
 * 2019/9/14
 */
@Controller
public class IndexController {
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidInfoService bidInfoService;


    @RequestMapping("index")
    public String index(Model model) {
        //创建一个固定的线程池
        /*ExecutorService executorService = Executors.newFixedThreadPool (100);

        for (int i = 0; i < 10000; i++) {


            executorService.submit (new Runnable () {
                @Override
                public void run() {

                    //动力金融网历史评价年化收益率
                    Double historyYearAverageRate = loanInfoService.queryHistoryAverageYearRate ();
                    model.addAttribute (Constants.HISTORY_AVERAGE_RATE, historyYearAverageRate);
                }
            });

        }*/
        //动力金融网历史评价年化收益率
        Double historyYearAverageRate = loanInfoService.queryHistoryAverageYearRate ();
        model.addAttribute (Constants.HISTORY_AVERAGE_RATE, historyYearAverageRate);


        //平台用户数
        Long userCount = userService.queryAllUserCount();
        model.addAttribute (Constants.ALL_USER_COUNT, userCount);

        //累计成交额
        Double totalBidMoney   = bidInfoService.queryTotalBidMoney();
        model.addAttribute (Constants.TOTAL_BID_MONEY, totalBidMoney);

        Map<String,Object> map =  new HashMap<> ();
        map.put ("productType", Constants.LOAN_PRODUCTTYPE_X);
        map.put ("currentPage", 0);
        map.put ("pageSize", 1);

        //新手宝
        List<LoanInfo> xLoanInfos = loanInfoService.queryLoanInfoListByProductType(map);
        model.addAttribute ("xLoanProductList", xLoanInfos);
        //优选产品
        map.put ("productType", Constants.LOAN_PRODUCTTYPE_Y);
        map.put ("pageSize", 4);
        List<LoanInfo> yLoanInfos  =loanInfoService.queryLoanInfoListByProductType (map);
        model.addAttribute ("yLoanProductList", yLoanInfos);
        //散标
        map.put ("productType", Constants.LOAN_PRODUCTTYPE_S);
        map.put ("pageSize", 8);
        List<LoanInfo> sLoanInfos  =loanInfoService.queryLoanInfoListByProductType (map);
        model.addAttribute ("sLoanProductList", sLoanInfos);
        return "index";
    }
}
