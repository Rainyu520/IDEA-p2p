package com.bjpowernode.p2p.service.user;/**
 * ClassName:FinanceAccountServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *Author：Rainyu
 *2019/9/17
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer id) {
        return financeAccountMapper.selectFinanceAccountByUid(id);

    }
}
