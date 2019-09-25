package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int insert(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int insertSelective(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    FinanceAccount selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int updateByPrimaryKeySelective(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Thu Sep 12 12:56:01 CST 2019
     */
    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户id查询账户信息
     * @param id
     * @return
     */
    FinanceAccount selectFinanceAccountByUid(Integer id);

    /**
     * 修改账户余额 ：投资
     * @param map
     * @return
     */

    int updateByUid(Map<String, Object> map);

    /**
     * 修改账户余额：收益
     * @param map
     */
    void updateAddMoneyByUid(Map<String, Object> map);

    /**
     * 修改账户余额 ：充值
     * @param parammap
     * @return
     */
    int updateRechargeByUid(Map<String, Object> parammap);
}