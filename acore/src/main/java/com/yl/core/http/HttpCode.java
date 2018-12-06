package com.yl.core.http;

import com.yl.core.http.exception.ApiException;
import com.yl.core.http.exception.TokenInvalidException;
import com.yl.core.http.handler.ApiError;
import com.yl.core.http.handler.ConnectError;
import com.yl.core.http.handler.IHttpError;
import com.yl.core.http.handler.NetError;
import com.yl.core.http.handler.SocketTimeoutError;
import com.yl.core.http.handler.TokenInvalidError;
import com.yl.core.http.handler.UnKnownHostError;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;

/**
 * @author Yang Shihao
 */
public enum HttpCode {
    CODE_0("0", "操所成功"),
    CODE_10001("10001", "登录成功"),
    CODE_10002("10002", "操作成功,自动分配失败,请手动分配"),
    CODE_10003("10003", "签到成功"),
    CODE_20000("20000", "登录失败"),
    CODE_20001("20001", "操作失败"),
    CODE_20002("20002", "手机号已被注册"),
    CODE_20003("20003", "用户名或者密码错误"),
    CODE_20004("20004", "验证码错误"),
    CODE_20005("20005", "TOKEN错误或者已失效"),
    CODE_20006("20006", "参数错误"),
    CODE_20007("20007", "没有操作权限"),
    CODE_20008("20008", "注册失败"),
    CODE_20009("20009", "未知错误"),
    CODE_20010("20010", "您已投过票,请勿重复投票"),
    CODE_20011("20011", "上传数量超过限制"),
    CODE_20012("20012", "当前用户没有绑定区域或者绑定未审核通过"),
    CODE_20013("20013", "未查询到相关数据"),
    CODE_20014("20014", "数据重复"),
    CODE_20015("20015", "数据未发生任何改变"),
    CODE_20016("20016", "操作校验未通过"),
    CODE_20017("20017", "用户名或者密码不能为空"),
    CODE_20018("20018", "会话已过期或者无效"),
    CODE_20019("20019", "该用户已绑定区域,如果要重新绑定,请管理员解除绑定"),
    CODE_20020("20020", "该账户已被锁定,无法登陆,管理员解锁后方可再次登录"),
    CODE_20021("20021", "该账户无手机端权限,管理员授权后方可登录"),
    CODE_20022("20022", "该账户已和其他手机绑定,请使用绑定的手机进行登录"),
    CODE_20023("20023", "该事件已处理完毕,不能再处理"),
    CODE_20024("20024", "该事件已是最高级别,不能再上报"),
    CODE_20025("20025", "请上传焦点图,再操作"),
    CODE_20026("20026", "密码错误次数太多,账户已被锁定"),
    CODE_20027("20027", "请先选择区域,再操作"),
    CODE_20028("20028", "事件已完结或者已经在处理中,无法编辑"),
    CODE_20029("20029", "密码错误"),
    CODE_20030("20030", "该手机帐号绑定手机错误"),
    CODE_20031("20031", "不能重复请假"),
    CODE_20032("20032", "上午重复打卡"),
    CODE_20033("20033", "数据错误"),
    CODE_20034("20034", "缺少身份证号"),
    CODE_20035("20035", "请先保存人口基础信息"),
    CODE_20036("20036", "请先保存人口附属信息"),
    CODE_20037("20037", "重复添加,该身份证ID已存在"),
    CODE_20038("20038", "不能重复点赞"),
    CODE_20039("20039", "家长信息需要进一步完善"),
    CODE_20040("20040", "同步信息到网易云失败"),
    CODE_20041("20041", "用户尚未注册"),
    CODE_20042("20042", "该条件下未找到对应学生,请核对信息"),
    CODE_20043("20043", "密码错误"),
    CODE_20044("20044", "身份证号已注册!"),
    CODE_20045("20045", "不能使用初始密码,请更换密码"),
    CODE_20046("20046", "不能投票"),
    CODE_20047("20047", "摄像头未开启"),
    CODE_30001("30001", "网络异常,请稍后重试"),
    CODE_30002("30002", "网络请求失败"),
    CODE_30003("30003", "Json解析异常"),
    CODE_30004("30004", "没有更多"),
    CODE_40001("40001", "获取监控播放地址失败"),
    CODE_40002("40002", "参数非法"),
    CODE_40003("40003", "服务未开通"),
    CODE_40004("40004", "未知异常"),
    CODE_40005("40005", "服务器连接异常"),
    CODE_40006("40006", "未知Host"),
    CODE_40007("40007", "服务器连接超时"),
    CODE_40008("40008", "未知错误码");

    static Map<String, String> mMap = new HashMap<>();
    static Map<String, IHttpError> httpCodeMap = new HashMap<>();

    private String mCode;
    private String mMsg;

    HttpCode(String code, String msg) {
        mCode = code;
        mMsg = msg;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public static String getMsg(String code) {
        String msg = mMap.get(code);
        return msg == null ? "错误码" + code : msg;
    }

    static {
        for (HttpCode httpCode : HttpCode.values()) {
            mMap.put(httpCode.getCode(), httpCode.getMsg());
        }
    }

    static {
        NetError netError = new NetError();
        httpCodeMap.put(Objects.requireNonNull(TokenInvalidException.class.getCanonicalName()), new TokenInvalidError());
        httpCodeMap.put(Objects.requireNonNull(HttpException.class.getCanonicalName()), netError);
        httpCodeMap.put(Objects.requireNonNull(CompositeException.class.getCanonicalName()), netError);
        httpCodeMap.put(Objects.requireNonNull(ConnectException.class.getCanonicalName()), new ConnectError());
        httpCodeMap.put(Objects.requireNonNull(UnknownHostException.class.getCanonicalName()), new UnKnownHostError());
        httpCodeMap.put(Objects.requireNonNull(SocketTimeoutException.class.getCanonicalName()), new SocketTimeoutError());
        httpCodeMap.put(Objects.requireNonNull(ApiException.class.getCanonicalName()), new ApiError());
        httpCodeMap.put(Objects.requireNonNull(SocketTimeoutException.class.getCanonicalName()), new SocketTimeoutError());
    }
}