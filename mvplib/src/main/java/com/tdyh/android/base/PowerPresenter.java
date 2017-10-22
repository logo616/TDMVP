package com.tdyh.android.base;

import java.util.ArrayList;
import java.util.List;

/**注入多个Presenter，一个页面多个Presenter的情况
 * Created by Administrator on 2017/10/17 0017.
 */

public class PowerPresenter <T extends BaseView> extends BasePresenter<T> {

    private List<Presenter> presenters = new ArrayList<>();
    @SafeVarargs
    public final <Q extends Presenter<T>> void requestPresenter(Q... cls){
        for (Q cl : cls) {
            presenters.add(cl);
        }
    }

    @Override
    public void attachView(T view) {
        super.attachView(view);
        for (Presenter presenter : presenters) {
            presenter.attachView(view);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        for (Presenter presenter : presenters) {
            presenter.detachView();
        }
    }


}