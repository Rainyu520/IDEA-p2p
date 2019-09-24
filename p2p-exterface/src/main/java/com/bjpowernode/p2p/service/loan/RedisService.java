package com.bjpowernode.p2p.service.loan;/**
 * ClassName:RedisService
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

/**
 *Author：Rainyu
 *2019/9/20
 */

public interface RedisService {
    void put(String messagecode, String randomCode);

    String get(String messagecode);

    String getRandomNum();
}
