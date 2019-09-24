package com.bjpowernode.p2p.vo;/**
 * ClassName:PaginationVO
 * Package:com.bjpowernode.p2p.vo
 * Description
 *
 * @date ：${Date}
 */

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *Author：Rainyu
 *2019/9/16
 */
@Data
public class PaginationVO<T> implements Serializable {
    //总条数
    private Long total;
    //显示的数据
   private List<T> dataList;
}
