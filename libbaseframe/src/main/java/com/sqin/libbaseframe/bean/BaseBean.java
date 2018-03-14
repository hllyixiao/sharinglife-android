package com.sqin.libbaseframe.bean;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 作者：sqin
 * 日期：2016/3/1.
 */
public abstract class BaseBean implements Serializable{
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
