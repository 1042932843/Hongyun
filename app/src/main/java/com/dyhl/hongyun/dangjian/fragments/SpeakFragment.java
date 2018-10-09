package com.dyhl.hongyun.dangjian.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awen.camera.util.ToastUtil;
import com.dyhl.hongyun.dangjian.activity.LoginActivity;
import com.dyhl.hongyun.dangjian.activity.MaillistActivity;
import com.dyhl.hongyun.dangjian.activity.NewsdetailActivity;
import com.dyhl.hongyun.dangjian.activity.RedFlagActivity;
import com.dyhl.hongyun.dangjian.activity.StudioActivity;
import com.dyhl.hongyun.dangjian.activity.VolunteerActivity;
import com.dyhl.hongyun.dangjian.util.Constant;
import com.dyhl.hongyun.dangjian.view.CustomWebView;
import com.dyhl.hongyun.searchhistory.view.SearchActivity;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.dyhl.hongyun.R;
import com.dyhl.hongyun.dangjian.App;
import com.dyhl.hongyun.dangjian.activity.MainActivity;
import com.dyhl.hongyun.dangjian.activity.PublicationtypeActivity;
import com.dyhl.hongyun.dangjian.model.ApiMsg;
import com.dyhl.hongyun.dangjian.model.User;
import com.dyhl.hongyun.dangjian.util.HttpUtil;
import com.dyhl.hongyun.dangjian.util.LogUtil;
import com.dyhl.hongyun.dangjian.wx.GlideImageLoader;
import com.dyhl.hongyun.dangjian.wx.ImagePickerAdapter;
import com.dyhl.hongyun.dangjian.wx.SelectDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class SpeakFragment extends BaseFragment{

    @Bind(R.id.web)
    CustomWebView mWebView;
    @Bind(R.id.home_content)
    LinearLayout home_content;

    @Subscriber(tag = "spe")
    public void onEvent(String event) {
        if ("re".equals(event)) {
            mWebView.setVisibility(View.INVISIBLE);
            home_content.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.dwgk)
    public void dwgk(){
        home_content.setVisibility(View.INVISIBLE);
        mWebView.loadUrl("javascript:showAndroid('" + 2 + "')");
        mWebView.setVisibility(View.VISIBLE);

    }
    @OnClick(R.id.ryq)
    public void ryq(){
        home_content.setVisibility(View.INVISIBLE);
        mWebView.loadUrl("javascript:showAndroid('" + 0 + "')");
        mWebView.setVisibility(View.VISIBLE);

    }
    @OnClick(R.id.jyxx)
    public void jyxx(){
        mWebView.loadUrl("javascript:showAndroid('" + 1+ "')");
        home_content.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ddbgzs)
    public void ddbgzs(){
        Intent intent = new Intent(getActivity(), StudioActivity.class);
        startActivity(intent);
    }


    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_speak, container, false);
            EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //最好放到 Application oncreate执行
    }
    private static final String APP_CACAHE_DIRNAME = "/data/data/com.zeller.fastlibrary/cache/webviewCache";
    @Override
    public void finishCreateView(Bundle state) {
        mWebView.loadUrl(Constant.DOMAIN+"/app/djzj/index.html");
//        mWebView.loadUrl("file:///android_asset/index.html");
        //设置支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置渲染效果优先级，高
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //设置缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        String cacheDirPath = APP_CACAHE_DIRNAME;
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置 应用 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 DOM 存储功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 数据库 存储功能
        mWebView.getSettings().setDatabaseEnabled(true);
        //开启 应用缓存 功能
        mWebView.getSettings().setAppCacheEnabled(true);
        // 新闻详情
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void xinwen(String id) {
                Intent intent = new Intent(getActivity(), NewsdetailActivity.class);
                intent.putExtra(NewsdetailActivity.REQUEST_ID, id);
                startActivity(intent);

            }
        }, "Android");

        //用户联系人列表
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void maillist() {
                User user = App.me().getUser();
                if (null != user) {
                    Intent intent = new Intent(getActivity(), MaillistActivity.class);
                    intent.putExtra(MaillistActivity.REQUEST_UUID, user.getUuid());
                    startActivity(intent);
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.REQUEST_CODE);
                }
            }
        }, "And");


        //搜索
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void search() {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        }, "sea");


    }


    public static SpeakFragment newInstance(String arg1) {
        SpeakFragment speakFragment = new SpeakFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", arg1);
        speakFragment.setArguments(bundle);
        return speakFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
    }


}