package com.bjpowernode.p2p.service.loan;/**
 * ClassName:RechargeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.vo.PaginationVO;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/19
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    @Override
    public List<RechargeRecord> queryRecentRechargeRecordByUid(Map map) {
        return rechargeRecordMapper.selectRechargeRecordByUid (map);
    }

    @Override
    public PaginationVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> map) {
        List<RechargeRecord> rechargeRecords = rechargeRecordMapper.selectRechargeRecordByUid (map);
        Long total = rechargeRecordMapper.selectRechargeRecordTotalByUid (map);
        PaginationVO<RechargeRecord> vo = new PaginationVO<> ();
        vo.setTotal (total);
        vo.setDataList (rechargeRecords);
        return vo;
    }

    @Override
    public void addRechargeRecord(RechargeRecord rechargeRecord) {
        rechargeRecordMapper.insertSelective (rechargeRecord);
    }

    @Override
    public void modifyRechargeRecordByRechargeNo(RechargeRecord recharge) throws Exception {
        int updateCount = rechargeRecordMapper.updateRechargeRecordByRechargeNo (recharge);
        if (updateCount <= 0) {
            throw new Exception ("修改订单状态失败");
        }
    }

    @Override
    public void recharge(Map<String, Object> parammap) {
        //修改订单状态为1 成功
        RechargeRecord rechargeRecord = new RechargeRecord ();
        rechargeRecord.setRechargeNo ((String) parammap.get ("rechargeNo"));
        rechargeRecord.setRechargeStatus ("1");
        int updateCount = rechargeRecordMapper.updateRechargeRecordByRechargeNo (rechargeRecord);
        if (updateCount <= 0) {
            throw new RuntimeException ("修改订单状态失败");
        }
        //给用户充值
        int rechargeCount = financeAccountMapper.updateRechargeByUid (parammap);
        if (rechargeCount <= 0) {
            throw new RuntimeException ("账户余额修改失败");
        }


    }

    @Override
    public void dealRechargeRecord() {
        //查出所有订单状态为0充值中的订单
        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectRechargeRecordListByrechargeStatus (0);
        for (RechargeRecord rechargeRecord : rechargeRecordList) {

            Map<String, Object> map = new HashMap<> ();
            map.put ("out_trade_no", rechargeRecord.getRechargeNo ());
            try {
                String resultString = HttpClientUtils.doPost ("http://localhost:8083/pay/api/alipayTradeQuery", map);
                //解析json
                JSONObject resultJson = JSONObject.parseObject (resultString);
                JSONObject alipay_trade_page_pay_response = resultJson.getJSONObject ("alipay_trade_query_response");
                //返回通信code
                String code = alipay_trade_page_pay_response.getString ("code");

                if (!StringUtils.equals (code, "10000")) {
                    throw new Exception ("通信异常");
                }

                String trade_status = alipay_trade_page_pay_response.getString ("trade_status");
                String rechargeNo = rechargeNo = alipay_trade_page_pay_response.getString ("out_trade_no");
                String rechargeMoney = alipay_trade_page_pay_response.getString ("total_amount");

                RechargeRecord rechargeRecordDetail = rechargeRecordMapper.selectRechargeRecordByRechargeNo(rechargeRecord.getRechargeNo ());

                if (StringUtils.equals (trade_status, "TRADE_CLOSED")) {
                    //交易失败
                    //修改充值状态为2，
                    RechargeRecord recharge = new RechargeRecord ();
                    recharge.setRechargeNo (rechargeNo);
                    recharge.setRechargeStatus ("2");
                    int updateCont = rechargeRecordMapper.updateRechargeRecordByRechargeNo (recharge);
                    if (updateCont <= 0) {
                        throw new RuntimeException ("修改充值状态失败");
                    }

                } else if (StringUtils.equals (trade_status, "TRADE_SUCCESS") && "0".equals (rechargeRecordDetail.getRechargeStatus ())) {

                    //交易成功
                    //修改充值订单状态为1 充值成功 ; 给用户账户添加金额【uid,rechargeMoney,rechargeNo】


                    RechargeRecord rechargeDetail = new RechargeRecord ();
                    rechargeDetail.setRechargeNo (rechargeNo);
                    rechargeDetail.setRechargeStatus ("1");
                    int updateCont = rechargeRecordMapper.updateRechargeRecordByRechargeNo (rechargeDetail);
                    if (updateCont <= 0) {
                        throw new RuntimeException ("修改充值状态失败");
                    }
                    //修改账户余额
                    Map<String, Object> paramMap = new HashMap<> ();
                    paramMap.put ("rechargeMoney", rechargeRecord.getRechargeMoney ());
                    paramMap.put ("uId", rechargeRecord.getUid ());

                    financeAccountMapper.updateRechargeByUid (paramMap);


                }

            } catch (Exception e) {
                e.printStackTrace ();
            }


        }


    }


    @Override
    public RechargeRecord queryRechargeByRechargeNo(String out_trade_no) {
      return   rechargeRecordMapper.selectRechargeRecordByRechargeNo (out_trade_no);
    }
}
