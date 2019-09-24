package com.bjpowernode.p2p.service.user;/**
 * ClassName:UserService
 * Package:com.bjpowernode.p2p.service.user
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.model.user.User;

/**
 *Author：Rainyu
 *2019/9/14
 */

public interface UserService {
    /**
     * 获取平台总用户数
     * @return
     */
    Long queryAllUserCount();

    User queryUserInfoByPhone(String phone);

    void register(String phone, String password) throws Exception;

    void modifyUserInfoById(User u);

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    User login(String phone, String password);
}
