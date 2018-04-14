package com.renny.simplebrowser.business.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.renny.simplebrowser.business.log.Logs;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Renny on 2018/1/8.
 *
 */

public class X5WebView extends WebView {

    int touchX = 0, touchY = 0;
    private onSelectItemListener mOnSelectItemListener;

    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnSelectItemListener(onSelectItemListener onSelectItemListener) {
        mOnSelectItemListener = onSelectItemListener;
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        Logs.h5.d(url);

    }

    private void init() {
        WebSettings setting = getSettings();
        setting.setGeolocationEnabled(true);
        setting.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用（下面这两个方法合用）
        setting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        setting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        setting.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        setting.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        setting.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        setting.setCacheMode(WebSettings.LOAD_DEFAULT); //（默认）根据cache-control决定是否从网络上取数据。
        setting.setAllowFileAccess(true); //设置可以访问文件
        setting.setJavaScriptCanOpenWindowsAutomatically(false); //不支持通过JS打开新窗口
        setting.setLoadsImagesAutomatically(true); //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");//设置编码格式

        setting.setAppCacheMaxSize(1024 * 1024 * 10);
        setting.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        setting.setDatabaseEnabled(true);   //开启 database storage API 功能
        setting.setAppCacheEnabled(true);//开启 Application Caches 功能
        setting.setTextSize(WebSettings.TextSize.NORMAL);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);//设置  Application Caches 缓存目录
        setting.setSavePassword(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //android5.0以后webview默认不在保存cookie所以会导致第三方登录状态无法保存
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true);
        }
        // setting.setSupportMultipleWindows(true);
        setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                WebView.HitTestResult result = getHitTestResult();

                if (null == result)
                    return false;
                int type = result.getType();
                String extra = result.getExtra();
                Logs.h5.d("extra--" + extra);
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                       break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        if (mOnSelectItemListener != null && extra != null && URLUtil.isValidUrl(extra)) {
                            mOnSelectItemListener.onLinkSelected(touchX, touchY, result.getType(), extra);
                        }
                        return true;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        if (mOnSelectItemListener != null && extra != null && URLUtil.isValidUrl(extra)) {
                            mOnSelectItemListener.onImgSelected(touchX, touchY, result.getType(), extra);
                        }
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;

                }
                return false;

            }

        });
    }

    @Override
    public void destroy() {
        ViewGroup parent = ((ViewGroup) getParent());
        if (parent != null) {
            parent.removeAllViews();
        }
        super.destroy();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        touchX = (int) event.getX();
        touchY = (int) event.getY();
        return super.onInterceptTouchEvent(event);
    }

    public interface onSelectItemListener {
        void onImgSelected(int x, int y, int type, String extra);

        void onLinkSelected(int x, int y, int type, String extra);
    }
}
