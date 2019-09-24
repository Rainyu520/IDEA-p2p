package com.bjpowernode.p2p.service.loan;/**
 * ClassName:RedisServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *Author：Rainyu
 *2019/9/20
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void put(String phoen, String randomCode) {
            redisTemplate.opsForValue ().set (phoen, randomCode, 5, TimeUnit.MINUTES);
    }

    @Override
    public String get(String phone) {
        return (String) redisTemplate.opsForValue ().get (phone);
    }

    @Override
    public String getRandomNum() {
        return  redisTemplate.opsForValue ().increment (Constants.RECHARGENO,1).toString ();
    }
}
