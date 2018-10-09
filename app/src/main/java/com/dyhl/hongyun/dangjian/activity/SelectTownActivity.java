package com.dyhl.hongyun.dangjian.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dyhl.hongyun.R;
import com.dyhl.hongyun.dangjian.util.Constant;
import com.dyhl.hongyun.dangjian.view.AppWebView;


/**
 * 网上e支部
 */
public class SelectTownActivity extends Activity implements View.OnClickListener {
    public static final String REQUEST_ID = "id";
    private AppWebView webView;
    private Handler handler = new Handler();
//    private PullToRefreshLayout pull;

    private void assignViews() {
//        pull = (PullToRefreshLayout) findViewById(R.id.pull);
        webView = (AppWebView) findViewById(R.id.webView);
//        bun = (Button) findViewById(R.id.bun);
//        bun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String id = getIntent().getStringExtra(REQUEST_ID);
//                if (null != id) {
//                    Log.d("reg", "id:" + id);
//                    webView.loadUrl("javascript:showAndroid('" + id + "')");
//                }
//            }
//        });
    }


    private void initViews() {

//        pull.setPtrHandler(this);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT < 18) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }

//            @Override
//            public void onPageFinished(WebView view, String url) {
//                pull.refreshComplete();
//            }
        });
//        pull.post(new Runnable() {
//            @Override
//            public void run() {
//                pull.autoRefresh();
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttown);
        assignViews();
        initViews();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        String url=Constant.DOMAIN + "/app/wsezb/index.html";
      webView.loadUrl(url);
    //    webView.loadUrl(Constant.DOMAIN + "/app/fpdt/index.html");

        if (webView != null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    String id = getIntent().getStringExtra(REQUEST_ID);

                    if (null != id) {
                        webView.loadUrl("javascript:showAndroid('" + id + "')");
                    }
                }
            });
        }

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void Back() {
                finish();
            }
        }, "ba");



        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void xinwen(String id) {
                if (null!=id){
                    Intent intent = new Intent(SelectTownActivity.this, NewsdetailActivity.class);
                    intent.putExtra(NewsdetailActivity.REQUEST_ID, id);
                    startActivity(intent);
                }else {
                    startActivityForResult(new Intent(SelectTownActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
                }
            }
        }, "Android");

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void opinio(String id) {
                Intent intent = new Intent(SelectTownActivity.this, OpinionActivity.class);
                intent.putExtra(OpinionActivity.REQUEST_NAME, "社情民意");
                intent.putExtra(OpinionActivity.REQUEST_TYPE, "1");
                intent.putExtra(OpinionActivity.REQUEST_ID, id);
                intent.putExtra("apiType", "QZ");
                startActivity(intent);
            }
        }, "op");

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void part(String id) {
                Intent intent = new Intent(SelectTownActivity.this, OpinionActivity.class);
                intent.putExtra(OpinionActivity.REQUEST_NAME, "约见党代表");
                intent.putExtra(OpinionActivity.REQUEST_TYPE, "2");
                intent.putExtra(OpinionActivity.REQUEST_ID, id);
                intent.putExtra("apiType", "QZ");
                startActivity(intent);
            }
        }, "pa");
    }

//    @Override
//    public void finish() {
//        if (webView != null) {
//            webView.loadUrl("about:blank");
//        }
//        super.finish();
//    }



    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

/*
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
        return webView.getScrollY() == 0 && PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
    }
*/

//    @Override
//    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

////        //结束后调用
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pull.refreshComplete();
//            }
//        }, 1000);
//    }

//    @Override
//    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
//        return webView.getScrollY() == 0 && PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
//    }
//
//    @Override
//    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//
//        //结束后调用
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pull.refreshComplete();
//            }
//        }, 1000);
//
//
//    }

}
