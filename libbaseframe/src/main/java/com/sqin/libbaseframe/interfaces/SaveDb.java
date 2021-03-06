package com.sqin.libbaseframe.interfaces;
import com.sqin.libbaseframe.bean.BaseBean;

public interface SaveDb<T extends BaseBean> {
    void saveDb(T t);

    void saveDb(Iterable<T> objects);
}
