package com.dyhl.hongyun.dangjian.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        finishCreateView(savedInstanceState);
    }
    public abstract void finishCreateView(Bundle state);
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscriber(tag = "MainActivity.onPageChange")
    void onPageChange(int pos) {

        Log.d(getClass().getSimpleName(), "onPageChange pos=" + pos);
    }

}
