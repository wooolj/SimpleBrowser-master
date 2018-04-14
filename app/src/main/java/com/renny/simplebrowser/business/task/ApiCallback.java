package com.renny.simplebrowser.business.task;

import com.renny.simplebrowser.business.base.ILoading;
import com.renny.simplebrowser.business.http.constants.ResultCode;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.globe.exception.NetworkNotAvailableException;
import com.renny.simplebrowser.globe.http.bean.Result;
import com.renny.simplebrowser.globe.http.reponse.IResult;

import java.net.SocketException;


/**
 * @author Created by yh on 2016/4/28
 */
public class ApiCallback<T> extends SimpleCallback<IResult<T>> implements IApiCallback<T> {


    protected ApiCallback() {
    }

    public ApiCallback(ILoading iLoading) {
        this.iLoading = iLoading;
    }


    @Override
    public final void onException(Throwable t) {
        if (t instanceof NetworkNotAvailableException) {
            onFailure(Result.fail(ResultCode.LOCAL_NET_EXCEPTION, "网络未开启"));
            return;
        } else if (t instanceof SocketException) {
            onFailure(Result.fail(ResultCode.LOCAL_EXCEPTION, "网络异常"));
            return;
        }
        Logs.defaults.e("ApiCallback 操作失败");
//        onFailure(Result.fail(ResultCode.LOCAL_EXCEPTION, "操作失败"));
    }

    @Override
    public final void onComplete(IResult<T> result) {
        if (result != null) {
            if (result.success()) {
                onSuccess(result);
            } else {
                onFailure(result);
            }
        }
    }

    public void onSuccess(IResult<T> result) {
    }

    @Override
    public void onAfterCall() {
        super.onAfterCall();
    }

    public void onFailure(IResult result) {
        switch (result.code()) {
            case ResultCode.LOCAL_NET_NOT_AVAILABLE:
            case ResultCode.LOCAL_NET_EXCEPTION:
            case ResultCode.ERROR_SERVER_DATA_ERROR:
            case ResultCode.LOCAL_EXCEPTION:
                ToastHelper.makeToast("网络异常");
                break;
        }
        Logs.common.e("onFailure" + result.code() + "--" + result.msg());
    }


}
