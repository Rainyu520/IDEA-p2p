package com.bjpowernode.p2p.service.loan;/**
 * ClassName:IncomeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.utils.DateUtil;
import com.bjpowernode.p2p.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/19
 */
@Service("incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService {
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private FinanceAccountMapper  financeAccountMapper;
    @Override
    public List<IncomeRecord> queryRecentIncomeRecordByUid(Map map) {
        return incomeRecordMapper.selectIncomeRecordByUid (map);


    }

    @Override
    public PaginationVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> map) {
        List<IncomeRecord> incomeRecords = incomeRecordMapper.selectIncomeRecordByUid (map);
        Long total = incomeRecordMapper.selectIncomeRecordsTotalByUid (map);
        PaginationVO<IncomeRecord> vo = new PaginationVO<> ();
        vo.setDataList (incomeRecords);
        vo.setTotal (total);
        return vo;
    }

    @Override
    public void incomePlan() {
        //返回已满标的产品
        List<LoanInfo> loanInfoList = loanInfoMapper.queryLoanListByProductType (1);

        //循环遍历满标产品
        for (LoanInfo loanInfo : loanInfoList) {
            //查出该产品所有投资记录
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoListByLoanId (loanInfo.getId ());
            //遍历投资记录
            for (BidInfo bidInfo : bidInfoList) {

                //收益金钱
                Double incomeMoney = null;
                //收益时间
                Date incomeDate = null;

                //如果产品类型是0 就是新手宝 按日计算
                if (loanInfo.getProductStatus () == 0) {
                    //收益=(投资金额*rate/100/365)
                    incomeMoney = bidInfo.getBidMoney () * (loanInfo.getRate () / 100 / 365);
                    incomeDate = DateUtil.getDateByAddDay (bidInfo.getBidTime (), loanInfo.getCycle ());

                } else {
                    //如果产品类型是1,是其他类型，按月计算
                    //收益=(投资金额*rate/100/365)*30
                    incomeMoney = bidInfo.getBidMoney () * (loanInfo.getRate () / 100 / 365) * 30;
                    incomeDate = DateUtil.getDateByAddMonth (bidInfo.getBidTime (), loanInfo.getCycle ());
                }
                incomeMoney = Math.round (incomeMoney*Math.pow (10, 2))/Math.pow (10,2);
                //生成收益计划
                IncomeRecord incomeRecord = new IncomeRecord ();
                incomeRecord.setIncomeDate (incomeDate);
                incomeRecord.setIncomeMoney (incomeMoney);
                incomeRecord.setBidId (bidInfo.getId ());
                incomeRecord.setLoanId (loanInfo.getId ());
                incomeRecord.setUid (bidInfo.getUid ());
                incomeRecord.setBidMoney (bidInfo.getBidMoney ());
                incomeRecord.setIncomeStatus (0);//收益状态为0是未返还，1是已返还
                incomeRecordMapper.insertSelective (incomeRecord);


            }
            //更新当前产品状态为2
            LoanInfo updateLoanInfo = new LoanInfo ();
            updateLoanInfo.setId (loanInfo.getId ());
            updateLoanInfo.setProductStatus (2);//产品状态为2表示已满标并生成收益计划
            loanInfoMapper.updateByPrimaryKeySelective (updateLoanInfo);

        }


    }

    @Override
    public void incomeReBack() {
        //查出收益状态为0,并且收益时间等于当前日期
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByIncomeStatus(0);
        //遍历为返回的收益
        for (IncomeRecord incomeRecord : incomeRecordList) {
            //修改用户余额
            Map<String,Object>  map = new HashMap<> ();
            map.put ("uId",incomeRecord.getUid ());
            map.put ("bidMoney", incomeRecord.getBidMoney ());
            map.put ("incomeMoney", incomeRecord.getIncomeMoney ());

            financeAccountMapper.updateAddMoneyByUid (map);
            //修改收益状态为1
            IncomeRecord updateIncome = new IncomeRecord ();
            updateIncome.setId (incomeRecord.getId ());
            updateIncome.setIncomeStatus (1);
            incomeRecordMapper.updateByPrimaryKeySelective (updateIncome);
        }


    }
}
