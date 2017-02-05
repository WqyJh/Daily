package com.wqy.daily.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wqy on 17-2-4.
 */

public abstract class ViewImpl implements IActivityView {

    protected IPresenter mIPresenter;

    protected View mRootView;

    @Override
    public View create(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(getResId(), container, false);
        mRootView = view;
        return view;
    }

    @Override
    public void bindPresenter(IPresenter presenter) {
        mIPresenter = presenter;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem item) {
        return false;
    }

    public abstract Context getContext();

    @Override
    public void created() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public int getMenuId() {
        return 0;
    }

    @Override
    public int getFragmentContainerId() {
        return 0;
    }
}
