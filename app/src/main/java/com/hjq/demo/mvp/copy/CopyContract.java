package com.hjq.demo.mvp.copy;

import com.hjq.demo.mvp.IMvpView;

import java.util.List;

/**
 *    author : Android
 *    github : https://github.com/
 *    time   : 2018/11/17
 *    desc   : 可进行拷贝的契约类
 */
public final class CopyContract {

    public interface View extends IMvpView {

        void loginError(String msg);

        void loginSuccess(List<String> data);
    }

    public interface Presenter {
        void login(String account, String password);
    }
}