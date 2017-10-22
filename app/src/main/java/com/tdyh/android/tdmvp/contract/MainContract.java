package com.tdyh.android.tdmvp.contract;

import com.tdyh.android.base.BaseView;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public interface MainContract {
    interface Model {
    }

    interface View extends BaseView {
        void loginCallback(boolean isSuccess);
    }

    interface Presenter {
        void login(String userName,String pwd);
    }
}
