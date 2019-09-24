package com.bjpowernode.p2p.service.user;/**
 * ClassName:FinanceAccountService
 * Package:com.bjpowernode.p2p.service.user
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 *Author：Rainyu
 *2019/9/17
 */

public interface FinanceAccountService {
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
