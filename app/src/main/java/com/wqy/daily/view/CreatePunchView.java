package com.wqy.daily.view;

import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.annotation.Tag;
import com.wqy.daily.R;
import com.wqy.daily.event.BusAction;
import com.wqy.daily.mvp.ViewImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqy on 17-2-7.
 */

public class CreatePunchView extends ViewImpl {

    @BindView(R.id.cpunch_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.cpunch_title)
    TextView tvTitle;

    @BindView(R.id.cpunch_desc)
    TextView tvDesc;

    @BindView(R.id.cpunch_aim)
    View vAim;

    @BindView(R.id.cpunch_aim_value)
    TextView tvAim;

    @BindView(R.id.cpunch_time)
    View vTime;

    @BindView(R.id.cpunch_time_value)
    TextView tvTime;

    @BindView(R.id.cpunch_duration)
    View vDuration;

    @BindView(R.id.cpunch_duration_value)
    TextView tvDuration;

    @BindView(R.id.cpunch_remind)
    View vRemind;

    @BindView(R.id.cpunch_remind_value)
    TextView tvRemind;

    @BindView(R.id.cpunch_priority)
    View vPriority;

    @BindView(R.id.cpunch_priority_value)
    TextView tvPriority;

    @Override
    public int getResId() {
        return R.layout.activity_create_punch;
    }

    @Override
    public int getMenuId() {
        return R.menu.activity_cpunch;
    }

    @Override
    public void created() {
        ButterKnife.bind(this, mRootView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RxBus.get().register(this);
    }

    @Override
    public void destroy() {
        RxBus.get().unregister(this);
    }

    @Override
    public boolean onMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask((Activity) getContext());
                return true;
            case R.id.cpunch_confirm:
                // create a new punch event
                return true;
            default:
                return false;
        }
    }

    @Produce(tags = {@Tag(BusAction.SET_CPUNCH_TITLE)})
    public String setActivityTitle() {
        return getContext().getString(R.string.title_cpunch);
    }
}
