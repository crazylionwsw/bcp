package com.fuze.bcp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 2017/5/19.
 * 返回接口的定义类
 */
public class ResultBean<T> implements Serializable {

    public final static int SUCCEED = 0;

    public final static int FAILED = 9;


    /**
     * 返回结果编码
     */
    public int c = SUCCEED;

    /**
     * 返回结果消息
     */
    public String m = null;

    /**
     * 返回结果的数据对象
     */
    public T d = null;

    /**
     * 返回复杂消息
     */
    public List<String> l = new ArrayList<String>();

    public ResultBean(){
    }

    public boolean failed(){
        return SUCCEED != c;
    }

    public int getC() {
        return c;
    }

    public ResultBean setC(int c) {
        this.c = c;
        return this;
    }

    public List<String> getL() {
        return l;
    }

    public ResultBean setL(List<String> l) {
        this.l = l;
        return this;
    }

    public ResultBean addL(String str) {
        this.l.add(str);
        return this;
    }

    public String getM() {
        return m;
    }

    public ResultBean setM(String m) {
        this.m = m;
        return this;
    }

    public T getD() {
        return d;
    }

    public ResultBean setD(T d) {
        this.d = d;
        return this;
    }

    public final static ResultBean getSucceed(){
        return new ResultBean().setM("操作成功！");
    }

    public final static ResultBean getFailed(){
        return new ResultBean().setC(FAILED);
    }


    public final boolean isSucceed(){
        return this.getC() == SUCCEED;
    }
}
