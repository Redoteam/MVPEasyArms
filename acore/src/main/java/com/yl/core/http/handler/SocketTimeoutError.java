package com.yl.core.http.handler;

import com.yl.core.http.HttpCode;

/**
 * Created by 寻水的鱼 on 2018/11/5.
 */
public class SocketTimeoutError implements IHttpError {
    @Override
    public HttpCode getHttCode(String code) {
        return HttpCode.CODE_40007;
    }
}
