package com.tdyh.android.tdmvp.tasks;

import android.support.annotation.NonNull;

import com.tdyh.android.base.usecase.UseCase;
import com.tdyh.android.tdmvp.model.DataCallback;
import com.tdyh.android.tdmvp.model.IUserModel;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public class LoginCase extends UseCase<LoginCase.RequestValues,LoginCase.ResponseValue>{

    IUserModel model;
    public LoginCase(IUserModel model){
        this.model=model;
    }

    @Override
    public void executeUseCase(RequestValues requestValues) {
        this.model.login(requestValues.getUserName(), requestValues.getPassword(), new DataCallback() {
            @Override
            public void onSuccess() {
                getUseCaseCallback().onSuccess(new ResponseValue());
            }

            @Override
            public void onFail(String errorCode, String errorMsg) {
                getUseCaseCallback().onError(errorCode,errorMsg);
            }
        });

    }


    public static final class  RequestValues implements UseCase.RequestValues{
        private final String userName;
        private final String password;

        public RequestValues(@NonNull String userName, @NonNull String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

    }



    public static final class  ResponseValue implements UseCase.ResponseValue{

    }

}
