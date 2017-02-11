package com.wqy.daily.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.wqy.daily.CommonUtils;
import com.wqy.daily.R;
import com.wqy.daily.event.BusAction;
import com.wqy.daily.event.DatasetChangedEvent;
import com.wqy.daily.event.ShowDialogEvent;
import com.wqy.daily.model.Bigday;
import com.wqy.daily.mvp.ViewImpl;
import com.wqy.daily.presenter.BigdayFragment;
import com.wqy.daily.presenter.MainActivity;
import com.wqy.daily.widget.DateTimePickerFragment;
import com.wqy.daily.widget.TagPickerFragment;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqy on 17-2-8.
 */

public class CreateBigdayView extends ViewImpl {
    public static final String TAG = CreateBigdayView.class.getSimpleName();

    @BindView(R.id.cbigday_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.cbigday_title_layout)
    TextInputLayout tilTitle;

    @BindView(R.id.cbigday_title)
    TextInputEditText etTitle;

    @BindView(R.id.cbigday_desc_layout)
    TextInputLayout tilDesc;

    @BindView(R.id.cbigday_desc)
    TextInputEditText etDesc;

    @BindView(R.id.cbigday_time)
    View vTime;

    @BindView(R.id.cbigday_time_value)
    TextView tvTime;

    @BindView(R.id.cbigday_tags)
    View vTags;

    @BindView(R.id.cbigday_tags_value)
    TextView tvTags;

    private Resources mResources;

    private boolean mEditable = true;

    private Bigday mBigday;

    private ProgressDialog mProgressDialog;


    @Override
    public int getResId() {
        return R.layout.activity_create_bigday;
    }

    @Override
    public int getMenuId() {
        return R.menu.activity_cbigday;
    }

    @Override
    public void created() {
        ButterKnife.bind(this, mRootView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mResources = getContext().getResources();
        RxBus.get().register(this);
    }

    @Override
    public void destroy() {
        RxBus.get().unregister(this);
    }

    @Override
    public void bindEvent() {
        etTitle.addTextChangedListener(new AfterTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                verifyTitle();
            }
        });

        etDesc.addTextChangedListener(new AfterTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                verifyDesc();
            }
        });
    }

    private void navigateUp() {
//        ((Activity) getContext()).getIntent().putExtra(MainActivity.CURRENT_FRAGMENT, BigdayFragment.TAG);
//        NavUtils.navigateUpFromSameTask((Activity) getContext());
        ((Activity) getContext()).finish();
    }

    @Override
    public boolean onMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp();
                return true;
            case R.id.cbigday_confirm:
                // create a new bigday
                if (mEditable) {
                    confirm();
                    return true;
                }
            default:
                return false;
        }
    }

    private void confirm() {
        Log.d(TAG, "confirm: ");
        if (verifyTitle() && verifyDesc()) {
            mBigday.setTitle(etTitle.getText().toString());
            mBigday.setDesc(etDesc.getText().toString());
            mBigday.setTags(tvTags.getText().toString());
//            mBigday.setDate();
            mProgressDialog = ProgressDialog.show(getContext(), null, getContext().getString(R.string.saving_data));
            RxBus.get().post(BusAction.SAVE_BIGDAY, mBigday);
        }
    }

    @Subscribe(tags = {@Tag(BusAction.BIGDAY_DATASET_CHANGED)})
    public void onDatasetChanged(DatasetChangedEvent<Long> event) {
        mProgressDialog.dismiss();
        navigateUp();
    }

    public void enableEdit() {
        etTitle.setEnabled(true);
        etDesc.setEnabled(true);
        vTime.setClickable(true);
        vTags.setClickable(true);
    }

    public void disableEdit() {
        etTitle.setEnabled(false);
        etDesc.setEnabled(false);
        vTime.setClickable(false);
        vTags.setClickable(false);
    }

    @Subscribe(tags = {@Tag(BusAction.CBIGDAY_EDITABLE)})
    public void setEditable(Boolean editable) {
        mEditable = editable;
        if (mEditable) {
            enableEdit();
        } else {
            disableEdit();
        }
    }

    @Produce(tags = {@Tag(BusAction.SET_CBIGDAY_TITLE)})
    public String setActivityTitle() {
        return getContext().getString(R.string.title_cbigday);
    }

    private boolean verifyTitle() {
        String title = etTitle.getText().toString();
        boolean lengthNotNull = title != null && title.length() > 0;
        if (!lengthNotNull) {
            tilTitle.setError(mResources.getString(R.string.cpunch_title_required));

            return false;
        }
        boolean lengthLessThanMax = title.length() < tilTitle.getCounterMaxLength();
        if (!lengthLessThanMax) {
            tilTitle.setError(mResources.getString(R.string.cpunch_out_of_maxlen));
            return false;
        }
        tilTitle.setError(null);
        return true;
    }

    private boolean verifyDesc() {
        boolean ok = etDesc.getText().toString().length() < tilDesc.getCounterMaxLength();
        if (!ok) {
            tilDesc.setError(mResources.getString(R.string.cpunch_out_of_maxlen));
            return false;
        }
        tilDesc.setError(null);
        return true;
    }

    private void showDialog(String tag, DialogFragment fragment) {
        mIPresenter.showDialog(tag, fragment);
    }

    @OnClick(R.id.cbigday_time)
    public void showDateTimeDialog() {
        Log.d(TAG, "showDateTimeDialog: ");
        DialogFragment fragment = new DateTimePickerFragment();
        Bundle args = new Bundle();
        args.putString(DateTimePickerFragment.ARG_EVENT_TAG,
                BusAction.CBIGDAY_TIME);
        fragment.setArguments(args);
        showDialog(DateTimePickerFragment.TAG, fragment);
    }

    @OnClick(R.id.cbigday_tags)
    public void showTagDialog() {
        Log.d(TAG, "showTagDialog: ");
        DialogFragment fragment = new TagPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TagPickerFragment.ARG_EVENT_TAG,
                BusAction.CBIGDAY_TAGS);
        fragment.setArguments(bundle);
        showDialog(TagPickerFragment.TAG, fragment);
    }

    @Subscribe(tags = {@Tag(BusAction.CBIGDAY_SET_BIGDAY)})
    public void setBigday(Bigday bigday) {
        Log.d(TAG, "setBigday: ");
        mBigday = bigday;
        etTitle.setText(mBigday.getTitle());
        etDesc.setText(mBigday.getDesc());
        tvTime.setText(CommonUtils.getDateTimeString(getContext().getResources(),
                mBigday.getDate()));
        tvTags.setText(mBigday.getTags());
    }

    @Subscribe(tags = {@Tag(BusAction.CBIGDAY_TIME)})
    public void setTime(Date event) {
        tvTime.setText(CommonUtils.getDateTimeString(mResources, event));
        mBigday.setDate(event);
    }

    @Subscribe(tags = {@Tag(BusAction.CBIGDAY_TAGS)})
    public void setTags(String[] tags) {
        tvTags.setText(CommonUtils.getTagString(mResources, tags));
    }

    private abstract class AfterTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }
}
