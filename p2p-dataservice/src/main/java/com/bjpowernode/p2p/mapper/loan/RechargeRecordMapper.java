package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

import java.util.List;
import java.util.Map;

public interface RechargeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int insert(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int insertSelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    RechargeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int updateByPrimaryKeySelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int updateByPrimaryKey(RechargeRecord record);

    List<RechargeRecord> selectRechargeRecordByUid(Map map);

    Long selectRechargeRecordTotalByUid(Map<String, Object> map);

    /**
     * 根据订单编号修改订单状态
     * @param recharge
     * @return
     */
    int updateRechargeRecordByRechargeNo(RechargeRecord recharge);

    /**
     * 根据订单状态查询订单
     * @param i
     */

    List<RechargeRecord> selectRechargeRecordListByrechargeStatus(int i);

    /**
     * 根据充值订单号查询订单
     * @param rechargeNo
     * @return
     */
    RechargeRecord selectRechargeRecordByRechargeNo(String rechargeNo);
}