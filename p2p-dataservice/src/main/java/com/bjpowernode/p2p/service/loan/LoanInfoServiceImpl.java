package com.bjpowernode.p2p.service.loan;/**
 * ClassName:LoanInfoServiceImpl
 * Package:com.bjpowernode.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.vo.PaginationVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author：Rainyu
 * 2019/9/14
 */
@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryHistoryAverageYearRate() {
        //先从redis中查询，有就返回，没有就从数据库查询，放到redis中

        /*Double historyAverageRate = (Double) redisTemplate.opsForValue ().get (Constants.HISTORY_AVERAGE_RATE);
        if (!ObjectUtils.allNotNull (historyAverageRate)) {
            historyAverageRate  = loanInfoMapper.selectHistoryAverageRate();
            redisTemplate.opsForValue ().set (Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.MINUTES);
            System.out.println ("从数据库中取");
        }else {
            System.out.println ("从redis取");
        }*/
        Double historyAverageRate = (Double) redisTemplate.opsForValue ().get (Constants.HISTORY_AVERAGE_RATE);



        //双重检测 静态同步代码锁

        if (!ObjectUtils.allNotNull (historyAverageRate)) {
            synchronized (this) {
                //再次从redis中取
                historyAverageRate = (Double) redisTemplate.opsForValue ().get (Constants.HISTORY_AVERAGE_RATE);
                if (!ObjectUtils.allNotNull (historyAverageRate)) {
                    historyAverageRate = loanInfoMapper.selectHistoryAverageRate ();
                    redisTemplate.opsForValue ().set (Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.MINUTES);
                    System.out.println ("从数据库中取");
                } else {
                    System.out.println ("从redis中取");
                }
            }

        } else {
            System.out.println ("从redis中取");
        }


        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> map) {
        return loanInfoMapper.selectLoanInfoListByProductType (map);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoListByPage(Map<String, Object> map) {
        //查询总条数
        Long total = loanInfoMapper.selectLoanInfoTotalByProductType (map);
        //根据产品名称查询产品信息
        final List<LoanInfo> loanInfos = loanInfoMapper.selectLoanInfoListByProductType (map);
        List<LoanInfo> loanInfoList = loanInfos;
        PaginationVO<LoanInfo> vo = new PaginationVO<> ();
        vo.setDataList (loanInfoList);
        vo.setTotal (total);
        return vo;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey (id);

    }

}
