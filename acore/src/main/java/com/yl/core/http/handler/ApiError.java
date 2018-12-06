package com.yl.core.http.handler;

import android.text.TextUtils;

import com.yl.core.http.HttpCode;

/**
 * Created by 寻水的鱼 on 2018/11/5.
 */
public class ApiError implements IHttpError {
    private final String HttpCodeHead = "CODE_";

    @Override
    public HttpCode getHttCode(String code) {
        HttpCode httpCode;
        try {
            httpCode = HttpCode.valueOf(getRealCode(code));
        } catch (Exception e) {
            httpCode = HttpCode.CODE_40008;
            httpCode.setMsg("错误码：" + code);
        }

        return httpCode;
    }

    private String getRealCode(String code) {

        if (!TextUtils.isEmpty(code)) {
            if (code.startsWith(HttpCodeHead)) {
                return code;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(HttpCodeHead).append(code);
                return sb.toString();
            }
        } else {
            return "CODE_40008";
        }
    }
}
