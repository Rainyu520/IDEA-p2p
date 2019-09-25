package com.bjpowernode.p2p.service.loan;/**
 * ClassName:RechargeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/19
 */

public interface RechargeRecordService {
    /**
     * 根据用户id查询充值记录
     * @param map
     * @return
     */
    List<RechargeRecord> queryRecentRechargeRecordByUid(Map map);


    PaginationVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> map);

    /**
     * 添加充值记录
     * @param rechargeRecord
     */
    void addRechargeRecord(RechargeRecord rechargeRecord);

    /**
     * 根据充值订单号修改订单状态
     * @param recharge
     */

    void modifyRechargeRecordByRechargeNo(RechargeRecord recharge) throws Exception;

    /**
     * 充值功能
     * @param parammap
     */
    void recharge(Map<String, Object> parammap);

    /**
     * 定时器处理掉单
     */

    void dealRechargeRecord();

    /**
     * 根据充值订单号查找充值订单
     * @param out_trade_no
     * @return
     */

    RechargeRecord queryRechargeByRechargeNo(String out_trade_no);
}
