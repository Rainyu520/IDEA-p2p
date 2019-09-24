package com.bjpowernode.p2p.service.loan;/**
 * ClassName:IncomeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/19
 */

public interface IncomeRecordService {
    /**
     * 根据用户id查询最近收入记录
     * @param map
     * @return
     */
    List<IncomeRecord> queryRecentIncomeRecordByUid(Map map);

    /**
     * 分页查询收入记录
     * @param map
     * @return
     */
    PaginationVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> map);

    /**
     * 生成收益计划
     */
    void incomePlan();

    /**
     * 返还收益
     */
    void incomeReBack();
}
