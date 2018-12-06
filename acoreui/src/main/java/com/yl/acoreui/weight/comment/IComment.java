package com.yl.acoreui.weight.comment;

/**
 * Created by 寻水的鱼 on 2018/11/28.
 */
public interface IComment<U extends IUser> {
    U getToReplyUser();

    U getUser();

    String getContent();
}
