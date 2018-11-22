package com.library.base;

import com.simplelibrary.http.IBaseEntity;

import java.util.List;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public class BaseEntity implements IBaseEntity {
    public int code;
    public String msg;

    @Override
    public int code() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        return code == 1;
    }

    @Override
    public String message() {
        return msg;
    }

    @Override
    public List<?> getList() {
        return null;
    }
}
