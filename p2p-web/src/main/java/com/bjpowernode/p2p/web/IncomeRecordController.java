package com.bjpowernode.p2p.web;/**
 * ClassName:IncomeRecordController
 * Package:com.bjpowernode.p2p.web
 * Description
 *
 * @date ：${Date}
 */

import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import com.bjpowernode.p2p.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *Author：Rainyu
 *2019/9/19
 */
@Controller
public class IncomeRecordController extends BaseController{
    @Autowired
    private IncomeRecordService incomeRecordService;

    @RequestMapping("/loan/myIncome")
    public String myRecharge(Model model, HttpServletRequest request,
                             @RequestParam(value = "currentPage",defaultValue = "1")Integer currentPage){
        //从session取值
        User sessionUser = (User) BaseController.getSession (request, Constants.sessionUser);

        //准备参数
        int pageSize = 10;
        Map<String,Object> map = new HashMap<> ();
        map.put ("currentPage", (currentPage-1)*pageSize);
        map.put ("pageSize", pageSize);
        map.put ("uId", sessionUser.getId ());

        PaginationVO<IncomeRecord> vo = incomeRecordService.queryIncomeRecordByPage(map);
        //计算总页数
        int totalPage = vo.getTotal ().intValue ()/pageSize;
        int mod = vo.getTotal ().intValue () % pageSize;
        if (mod != 0){
            mod+=1;
        }

        model.addAttribute ("totalPage", totalPage);
        model.addAttribute ("incomeRecordList", vo.getDataList ());
        model.addAttribute ("currentPage", currentPage);
        model.addAttribute ("incomeRecordTotal", vo.getTotal ());


        return "myIncome";


    }
}
