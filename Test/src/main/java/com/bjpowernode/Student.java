package com.bjpowernode; /**
 * ClassName:Student
 * Package:PACKAGE_NAME
 * Description
 *
 * @date ：${Date}
 */

/**
 *Author：Rainyu
 *2019/9/25
 */


import org.springframework.beans.factory.BeanNameAware;



public class Student implements BeanNameAware {
    private String name;

    //无参构造方法
    public Student() {
        super();
    }

    /** 设置对象属性
     * @param name the name to set
     */
    public void setName(String name) {
        System.out.println("设置对象属性setName()..");
        this.name = name;
    }

    //Bean的初始化方法
    public void initStudent() {
        System.out.println("Student这个Bean：初始化");
    }

    //Bean的销毁方法
    public void destroyStudent() {
        System.out.println("Student这个Bean：销毁");
    }

    //Bean的使用
    public void play() {
        System.out.println("Student这个Bean：使用");
    }

    /* 重写toString
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Student [name = " + name + "]";
    }

    //调用BeanNameAware的setBeanName()
    //传递Bean的ID。
    @Override
    public void setBeanName(String name) {
        System.out.println("调用BeanNameAware的setBeanName()..." +name);
    }

}

