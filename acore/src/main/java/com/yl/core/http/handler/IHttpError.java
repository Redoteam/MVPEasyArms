package com.yl.core.http.handler;

import com.yl.core.http.HttpCode;

/**
 * Created by 寻水的鱼 on 2018/11/5.
 */
public interface IHttpError {
    HttpCode getHttCode(String code);
}
