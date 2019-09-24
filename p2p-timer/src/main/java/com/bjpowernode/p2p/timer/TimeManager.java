package com.bjpowernode.p2p.timer;/**
 * ClassName:TimeManager
 * Package:com.bjpowernode.p2p.timer
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *Author：Rainyu
 *2019/9/21
 */
@Component
public class TimeManager {
    @Autowired
    private IncomeRecordService incomeRecordService;
    private Logger logger = LogManager.getLogger (TimeManager.class);

  //  @Scheduled(cron = "0/5 * * * * ?")
    private void test(){
        logger.info ("日志开始");
        incomeRecordService.incomePlan();
        logger.info ("日志结束");

    }
    @Scheduled(cron = "0/5 * * * * ?")
    private void generateIncomeBack(){

        logger.info ("返还收益开始");
        try {
            incomeRecordService.incomeReBack();
        } catch (Exception e) {
            e.printStackTrace ();
            logger.info (e.getMessage ());
        }
        logger.info ("返还收益结束");
    }


}
