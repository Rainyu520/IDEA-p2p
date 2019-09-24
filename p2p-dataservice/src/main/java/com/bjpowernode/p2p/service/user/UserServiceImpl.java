package com.bjpowernode.p2p.service.user;/**
 * ClassName:UserServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Author：Rainyu
 * 2019/9/14
 */
@Service("userServiceImpl")
public class
UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapperv;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Long queryAllUserCount() {
        //将redisTemplate中key的序列化方式修改为String
        redisTemplate.setKeySerializer (new StringRedisSerializer ());


        //获取处理redis数据类型的操作对象
        BoundValueOperations<Object, Object> boundValueOperations = redisTemplate.boundValueOps (Constants.ALL_USER_COUNT);
        //cogredis中获取
        Long allUserCount = (Long) boundValueOperations.get ();
        if (!ObjectUtils.allNotNull (allUserCount)) {
            synchronized (this) {

                allUserCount = (Long) boundValueOperations.get ();
                if (!ObjectUtils.allNotNull (allUserCount)) {
                    //从数据库取
                    allUserCount = userMapperv.selectAllUserCount ();
                    //放入redis
                    boundValueOperations.set (allUserCount, 15, TimeUnit.MILLISECONDS);
                }
            }
        }

        return allUserCount;
    }

    @Override
    public User queryUserInfoByPhone(String phone) {
        return userMapperv.selectUserInfoByPhone (phone);
    }

    @Override
    public void register(String phone, String password) throws Exception {
        //插入新用户
        User user = new User ();
        user.setPhone (phone);
        user.setLoginPassword (password);
        user.setLastLoginTime (new Date ());
        user.setAddTime (new Date ());

        userMapperv.insertSelective (user);

        //添加一个账户
        // user  = userMapperv.selectUserInfoByPhone (phone);
        FinanceAccount financeAccount = new FinanceAccount ();
        financeAccount.setUid (user.getId ());
        financeAccount.setAvailableMoney (888.0);

        int insetCount = financeAccountMapper.insertSelective (financeAccount);
        if (insetCount <= 0) {
            throw new Exception ("手机号" + phone + "新建账户异常");
        }


    }

    @Override
    public void modifyUserInfoById(User u) {
        userMapperv.updateByPrimaryKeySelective (u);
    }

    @Override
    public User login(String phone, String password) {
        HashMap map = new HashMap ();
        map.put ("phone", phone);
        map.put ("password", password);
        User user = userMapperv.SelectUserInfoByPhoneAndLoginPassWord (map);
        if (ObjectUtils.allNotNull (user)) {
            //更新最近登录时间

            User updataUser = new User ();
            updataUser.setId (user.getId ());
            updataUser.setLastLoginTime (new Date ());

            int updateCount = userMapperv.updateByPrimaryKeySelective (updataUser);
            if (updateCount <= 0) {
                throw new RuntimeException ("更新登录时间失败");
            }
        } else {
            throw new RuntimeException ("账户密码错误");
        }
        return user;

    }
}
