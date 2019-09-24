package com.bjpowernode.p2p.service.loan;/**
 * ClassName:BidInfoServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.vo.BidUesr;
import com.bjpowernode.p2p.vo.PaginationVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Author：Rainyu
 * 2019/9/15
 */
@Service("bidInfoService")
public class BidInfoServiceImpl implements BidInfoService {
    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    /**
     * 查询平台累计交易总额
     *
     * @return
     */
    @Override
    public Double queryTotalBidMoney() {
        //首先获取处理redis数据类型的操作对象
        BoundValueOperations<Object, Object> boundValueOperations = redisTemplate.boundValueOps (Constants.TOTAL_BID_MONEY);


        //获取该操作对象对应的值
        Double totalBidMoney = (Double) boundValueOperations.get ();

        //判断是否存在
        if (!ObjectUtils.allNotNull (totalBidMoney)) {

            //设置同步代码快
            synchronized (this) {

                //再次从redis中获取值
                totalBidMoney = (Double) boundValueOperations.get ();

                if (!ObjectUtils.allNotNull (totalBidMoney)) {

                    //从数据库中取出累计交易额
                    totalBidMoney = bidInfoMapper.selectTotalBidMoney ();

                    //将值放入redis中
                    boundValueOperations.set (totalBidMoney, 15, TimeUnit.MILLISECONDS);
                }
            }
        }
        return totalBidMoney;


    }

    @Override
    public List<BidInfo> queryRecentBidInfoListByLoanId(Map<String, Object> map) {
        return bidInfoMapper.selectRecentBidInfoListByLoanId (map);
    }

    @Override
    public List<BidInfo> queryRecentBidInfoListByUid(Map map) {
        return bidInfoMapper.selectRecentBidInfoListByUid (map);
    }

    @Override
    public PaginationVO<BidInfo> queryBidInfoListByPage(HashMap<String, Object> map) {
        //查询投资信息
        List<BidInfo> bidInfoList = bidInfoMapper.selectRecentBidInfoListByUid (map);
        //查询投资总条数
        Long total = bidInfoMapper.selectTotalBidInfoByUid (map);

        PaginationVO<BidInfo> vo = new PaginationVO<> ();
        vo.setDataList (bidInfoList);
        vo.setTotal (total);
        return vo;
    }

    @Override
    public void invert(Map<String, Object> map) {

        Integer loanId = (Integer) map.get ("loanId");
        Integer userId = (Integer) map.get ("userId");
        Double bidMoney = (Double) map.get ("bidMoney");
        String phone = (String) map.get ("phone");


        //取出产品信息，获得版本号
        LoanInfo loanInfo = loanInfoMapper.selectLoanInfoListById (loanId);
        int version = loanInfo.getVersion ();

        // 1.产品的可投资金额（用乐观是避免超卖现象）
        //准备数据
      /*map.put ("loanId", loanId);
        map.put ("bidMoney", bidMoney);
        map.put ("userId", user.getId ());*/
        map.put ("version", version);
        int updateLoanInfoCount = loanInfoMapper.updateLeftMoneyByLoanId (map);
        if (updateLoanInfoCount <= 0) {
            throw new RuntimeException ("可投资金额修改失败");
        }
        //  2.添加投资记录
        BidInfo bidInfo = new BidInfo ();
        bidInfo.setUid (userId);
        bidInfo.setLoanId (loanId);
        bidInfo.setBidMoney (bidMoney);
        bidInfo.setBidTime (new Date ());
        bidInfo.setBidStatus (1);

        int bidinfoCount = bidInfoMapper.insertSelective (bidInfo);
        if (bidinfoCount <= 0) {
            throw new RuntimeException ("添加投资记录失败");
        }

        // 3.修改账户余额
        int updatefinaAccoutnCount = financeAccountMapper.updateByUid (map);
        if (updatefinaAccoutnCount <= 0) {
            throw new RuntimeException ("修改余额失败");
        }
        //如果产品满标，需要修改产品状态，并且添加满标时间
        //再次取出产品
        LoanInfo loanInfo1 = loanInfoMapper.selectLoanInfoListById (loanId);
        if (loanInfo1.getLeftProductMoney () == 0) {

            //需要修改产品状态，并且添加满标时间
            LoanInfo updateLoanInfo = new LoanInfo ();
            updateLoanInfo.setId (loanId);
            updateLoanInfo.setProductStatus (1);
            updateLoanInfo.setProductFullTime (new Date ());
            int updateLoanInfoCount2 = loanInfoMapper.updateByPrimaryKeySelective (updateLoanInfo);
            if (updateLoanInfoCount2 <= 0) {
                throw new RuntimeException ("修改产品状态失败");
            }
        }


        //将投资金额累加放入redis,用于排行榜
        redisTemplate.opsForZSet ().incrementScore (Constants.BID_TOP, phone, bidMoney);


    }

    @Override
    public List<BidUesr> getBidInfoTop() {
        List<BidUesr> list = new ArrayList<> ();
        BidUesr bidUesr;
        //从redis取出排行榜
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet ().reverseRangeWithScores (Constants.BID_TOP, 0, 5);
        //遍历
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator ();
        while (iterator.hasNext ()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next ();
            String phone = (String) next.getValue ();
            Double bidMoney = next.getScore ();
            bidUesr = new BidUesr ();
            bidUesr.setBidMoney (bidMoney);
            bidUesr.setPhone (phone);
            list.add (bidUesr);


        }
        return list;
    }
}
