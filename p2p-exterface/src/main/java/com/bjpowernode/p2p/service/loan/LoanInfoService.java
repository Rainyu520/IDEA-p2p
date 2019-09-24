package com.bjpowernode.p2p.service.loan;/**
 * ClassName:LoanInfoService
 * Package:com.bjpowernode.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/14
 */

public interface LoanInfoService {
    /**
     * 查询历史平均年化收益率
     * @return
     */
    Double queryHistoryAverageYearRate();

    /**
     * 根据产品类型查出最近此类型所有产品
     * @param map
     * @return
     */

    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> map);

    /**
     * 分页查询出产品
     * @param map
     * @return
     */

    PaginationVO<LoanInfo> queryLoanInfoListByPage(Map<String, Object> map);

    /**
     * 根据id产出产品信息
     * @param id
     */

    LoanInfo queryLoanInfoById(Integer id);


}
