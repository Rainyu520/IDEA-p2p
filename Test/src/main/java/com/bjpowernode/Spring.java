package com.bjpowernode; /**
 * ClassName:Spring
 * Package:PACKAGE_NAME
 * Description
 *
 * @date ：${Date}
 */

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *Author：Rainyu
 *2019/9/25
 */



public class Spring {
    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext ("applicationContext.xml");
        Student student = (Student) context.getBean("student");
        //Bean的使用
        student.play();
        System.out.println(student);
        //关闭容器
        ((AbstractApplicationContext) context).close();
    }
    @Test
    public void test2(){
        double i = 10000d;
        Double j = new Double (10000d);
        System.out.println(i == j);
        System.out.println(j.equals(i));
    }
}
