package com.renny.simplebrowser.business.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.renny.simplebrowser.business.permission.PermissionActivity;
import com.renny.simplebrowser.globe.thread.task.IGroup;

import java.util.ArrayList;

/**
 * Created by Renny on 2018/1/4.
 */

public abstract class BaseActivity extends PermissionActivity implements View.OnClickListener,IGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam(getParams(getIntent().getExtras()));
        setContentView(getLayoutId());
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ArrayList<View> outView = new ArrayList<View>();
                getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                if (outView.size() > 0) {
                    outView.get(0).setVisibility(View.GONE);
                }
            }
        });
        bindView(savedInstanceState);
        afterViewBind(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initParam(getParams(intent));
    }

    /**
     * 从intent中初始化参数
     *
     * @param intent
     */
    protected void initParam(Bundle intent) {
    }

    protected void bindView(Bundle savedInstanceState) {

    }

    protected void afterViewBind(Bundle savedInstanceState) {

    }

    protected abstract int getLayoutId();

    @Override
    public void onClick(View v) {

    }
    @Override
    public String groupName() {
        return getLocalClassName() + this.toString();
    }

    private Bundle getParams(Intent intent) {
        Bundle params = intent.getExtras();
        if (params == null) {
            params = new Bundle();
        }
        return params;
    }

    private Bundle getParams(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
