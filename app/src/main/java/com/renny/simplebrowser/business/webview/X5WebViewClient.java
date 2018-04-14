package com.renny.simplebrowser.business.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.renny.simplebrowser.business.log.Logs;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Renny on 2018/1/8.
 */

public class X5WebViewClient extends WebViewClient {

    private Context mContext;

    protected X5WebViewClient(Context context) {
        mContext = context;
    }

/*
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = super.shouldInterceptRequest(view, url);
        try {
            response = new WebResourceResponse("text/javascript", "UTF-8", mContext.getAssets().open("hidden.js"));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ard", "response错误1");
        }
        return response;
    }
*/

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        Logs.h5.d("x5WebView--onPageFinished");
    }

    /**
     * 防止加载网页时调起系统浏览器
     */
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if ((url.startsWith("http://") || url.startsWith("https://"))) {
            if (hitTestResult == null) {//hitTestResult==null解决重定向问题
                view.loadUrl(url); //使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                return true;
            }
            return false;
        } else {
            return jumpScheme(url);
        }
    }

    private boolean jumpScheme(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("referer", url);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Logs.common.d("您所打开的第三方App未安装！");
            return false;
        }

        return true;
    }
}
