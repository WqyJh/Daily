package com.wqy.daily.mvp;

import android.os.Bundle;

import com.wqy.daily.mvp.IView;

/**
 * Created by wqy on 17-2-4.
 */

public interface IPresenter {

    // invoke before setContentView,
    // handle some configuration
    void create(Bundle savedInstanceState);

    // invoke after setContentView
    // handle some loading work
    void created(Bundle savedInstanceState);

    // invoke at the end of onCreate
    // do some optional work
    void prepared(Bundle savedInstanceState);

    void start();

    void stop();

    void destroy();

    IView getIView();
}
