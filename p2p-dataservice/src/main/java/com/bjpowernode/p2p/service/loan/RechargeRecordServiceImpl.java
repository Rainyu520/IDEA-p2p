package com.bjpowernode.p2p.service.loan;/**
 * ClassName:RechargeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.vo.PaginationVO;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/19
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;


    @Override
    public List<RechargeRecord> queryRecentRechargeRecordByUid(Map map) {
        return  rechargeRecordMapper.selectRechargeRecordByUid(map);
    }

    @Override
    public PaginationVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> map) {
        List<RechargeRecord> rechargeRecords = rechargeRecordMapper.selectRechargeRecordByUid (map);
        Long total  = rechargeRecordMapper.selectRechargeRecordTotalByUid(map);
        PaginationVO<RechargeRecord> vo =new PaginationVO<> ();
        vo.setTotal (total);
        vo.setDataList (rechargeRecords);
        return vo;
    }

    @Override
    public void addRechargeRecord(RechargeRecord rechargeRecord) {
        rechargeRecordMapper.insertSelective (rechargeRecord);
    }
}
