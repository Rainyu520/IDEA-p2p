package com.bjpowernode.p2p.service.loan;/**
 * ClassName:BidInfoService
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.vo.BidUesr;
import com.bjpowernode.p2p.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/15
 */

public interface BidInfoService {
    /**
     * 查询平台累计投资金额
     * @return
     */
    Double queryTotalBidMoney();



    /**
     * 展示投资记录
     * @param map  map中有当前页，查询记录数，loanId
     * @return  记录列表
     */

    List<BidInfo> queryRecentBidInfoListByLoanId(Map<String, Object> map);

    List<BidInfo> queryRecentBidInfoListByUid(Map map);

    /**
     * 分页查询投资信息
     * @param map
     * @return
     */
    PaginationVO<BidInfo> queryBidInfoListByPage(HashMap<String, Object> map);

    /**
     * 投资
     * @param map
     */

    void invert(Map<String, Object> map);

    /**
     * 获取投资排行榜
     * @return
     */

    List<BidUesr> getBidInfoTop();
}
