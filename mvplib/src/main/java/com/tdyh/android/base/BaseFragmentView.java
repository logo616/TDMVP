package com.tdyh.android.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public interface BaseFragmentView extends BaseView {

     Context getViewContext();
     void startFragment(Fragment tofragment);
     void startFragment(Fragment tofragment, String tag);
     BaseFragment getFragment();
     Bundle getBundle();

}
