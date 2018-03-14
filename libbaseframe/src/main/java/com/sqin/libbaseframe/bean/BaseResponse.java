package com.sqin.libbaseframe.bean;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;

public class BaseResponse extends BaseBean {

        int code;
        String msg;
        List<String> details;
        String data;



    public String getData() {
        return data;
    }

    public <T> List<T> getDataList(Class<T> clazz){
        if(isEmpty(data)) return null;
        if(data.startsWith("[") && data.endsWith("]")) {
            return JSON.parseArray(data, clazz);
        }else if(data.startsWith("{") && data.endsWith("}")){
            T obj = JSON.parseObject(data, clazz);
            return Arrays.asList(obj);
        }else{
            return null;
        }
    }

    public <T> T getData(Class<T> clazz){
        if(data.startsWith("[") && data.endsWith("]")){
            List<T> list = getDataList(clazz);
            if(list == null || list.size() == 0)
                return null;
            else
                return list.get(0);
        }else{
            return JSON.parseObject(data, clazz);
        }
    }


    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getDetails(){
        return details;
    }
    public void setDetails(List<String> details){
        this.details = details;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isEmpty(CharSequence str){
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
