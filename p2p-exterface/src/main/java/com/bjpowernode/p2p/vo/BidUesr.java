package com.bjpowernode.p2p.vo;/**
 * ClassName:BidUesr
 * Package:com.bjpowernode.p2p.vo
 * Description
 *
 * @date ：${Date}
 */

import java.io.Serializable;

/**
 *Author：Rainyu
 *2019/9/21
 */

public class BidUesr implements Serializable {
    private String phone;
    private Double bidMoney;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }
}
