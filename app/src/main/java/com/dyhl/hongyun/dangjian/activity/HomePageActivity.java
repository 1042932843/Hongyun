/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.dyhl.hongyun.dangjian.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.squareup.picasso.Picasso;
import com.dyhl.hongyun.R;
import com.dyhl.hongyun.dangjian.App;
import com.dyhl.hongyun.dangjian.listener.PgyUpdateManagerListener;
import com.dyhl.hongyun.dangjian.util.LogUtil;

import java.util.LinkedList;
import java.util.List;


/**
 * 启动页
 */
public class HomePageActivity extends BaseActivity implements View.OnClickListener {
    //region 已启动过或重新启动时则不再显示启动页面
    // 例:小米打开相机后被activity结束,返回App时为重启
    private boolean started;
    private boolean restarted;
    private long onBackPressedTimeMillis; // 按下返回键的时间戳
    private TextView travel, hui_pu_jin_rong, djcyltv, pests, flood_information, notice_of_program, social_service, huimin_subsidies, dwjyz,sjd;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<Activity> activityList = new LinkedList<Activity>();
    private ImageView img;
    private static final int PERMISSION_REQUEST_CODE = 1; //权限请求码





    private void initViews() {
        View[] views = {travel, hui_pu_jin_rong, djcyltv, pests, flood_information, notice_of_program, social_service, huimin_subsidies, dwjyz,sjd};
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    private void assignViews() {
        img = (ImageView) findViewById(R.id.img);
        String im = "http://61.143.38.10:9035/app/images/banner9.png";
        Picasso.with(App.me()).load(im).fit().placeholder(R.drawable.banner).into(img);
        travel = (TextView) findViewById(R.id.travel);
        hui_pu_jin_rong = (TextView) findViewById(R.id.hui_pu_jin_rong);
        djcyltv = (TextView) findViewById(R.id.djcyltv);
        flood_information = (TextView) findViewById(R.id.flood_information);
        notice_of_program = (TextView) findViewById(R.id.notice_of_program);
        social_service = (TextView) findViewById(R.id.social_service);
        huimin_subsidies = (TextView) findViewById(R.id.huimin_subsidies);
        pests = (TextView) findViewById(R.id.pests);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否模拟器打开app软件
        if (AntiEmulator.checkPipes() == true && AntiEmulator.checkQEmuDriverFile() == true
                && AntiEmulator.CheckPhoneNumber(this) == true
                && AntiEmulator.CheckDeviceIDS(this) == true && AntiEmulator.CheckImsiIDS(this) == true
                && AntiEmulator.CheckEmulatorBuild() == true) {
            exit();
        }
        started = savedInstanceState != null && savedInstanceState.getBoolean("started", false); // 是否启动过
        restarted = savedInstanceState != null && savedInstanceState.getBoolean("restarted", false); // 是否为重启
        if (!started) { // 如未启动过
            started = true; // 设置为已启动
//            startActivity(new Intent(this, StartActivity.class)); // 显示启动页
            startActivity(new Intent(this, StartActivity.class)); // 显示启动页
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        setContentView(R.layout.activity_homepage);
        assignViews();
        initViews();

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkPermission(HomePageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                获取权限后的操作。读取文件
        try {
            PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
        } catch (Exception e) {
            PgyCrashManager.reportCaughtException(this, e);
            LogUtil.e(MainActivity.class, "检查更新失败", e);
            App.me().toast("检查更新失败");
        }
//            } else {
//                请求权限
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        PERMISSION_REQUEST_CODE);
//            }
//        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //得到了授权
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    try {
                        PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
                    } catch (Exception e) {
                        PgyCrashManager.reportCaughtException(this, e);
                        LogUtil.e(MainActivity.class, "检查更新失败", e);
                        App.me().toast("检查更新失败");
                    }
                } else {
                    //未授权
                    Toast.makeText(this, "您已禁止自动更新", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://www.pgyer.com/TMOP");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    //结束整个应用程序
    public void exit() {
        //遍历 链表，依次杀掉各个Activity
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        //杀掉，这个应用程序的进程，释放 内存
        int id = android.os.Process.myPid();
        if (id != 0) {
            android.os.Process.killProcess(id);
        }
    }

    @Override
    protected void onDestroy() {
        PgyUpdateManager.unregister(); // 注销蒲公英更新监听
//        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //党建播报
            case R.id.travel:
                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
                break;
            //公告信息
            case R.id.hui_pu_jin_rong:
                Intent intent1 = new Intent(HomePageActivity.this, MainActivity.class);
                intent1.putExtra("switchPage", "switchPage(4)");
                startActivity(intent1);
                break;
            //党建产业链
            case R.id.djcyltv:
                Intent intent2 = new Intent(HomePageActivity.this, MainActivity.class);
                intent2.putExtra("switchPage2", "switchPage(0)");
                intent2.putExtra("mark", "party");
                startActivity(intent2);
                break;
            //两学一做
            case R.id.pests:
                Intent intent3 = new Intent(HomePageActivity.this, MainActivity.class);
                intent3.putExtra("switchPage", "switchPage(2)");
                startActivity(intent3);
                break;
            //网上e支部
            case R.id.flood_information:
                Intent intent4 = new Intent(HomePageActivity.this, MainActivity.class);
                intent4.putExtra("switchPage", "switchPage(1)");
                startActivity(intent4);
                break;
            //党建促扶贫
            case R.id.notice_of_program:
                Intent intent5 = new Intent(HomePageActivity.this, PovertyAlleviationActivity.class);
//                intent5.putExtra("switchPage", "switchPage(3)");
                startActivity(intent5);
                break;
            //流动党员之窗
            case R.id.social_service:
                Intent intent6 = new Intent(HomePageActivity.this, MainActivity.class);
                intent6.putExtra("switchPage2", "switchPage(1)");
                intent6.putExtra("mark", "party");
                startActivity(intent6);
                break;
            //党务助手
            case R.id.huimin_subsidies:
                Intent intent7 = new Intent(HomePageActivity.this, MainActivity.class);
                intent7.putExtra("switchPage2", "switchPage(3)");
                intent7.putExtra("mark", "party");
                startActivity(intent7);
                break;
            //学习十九大
            case R.id.sjd:
                Intent intent8 = new Intent(HomePageActivity.this, MainActivity.class);
                intent8.putExtra("switchPage2", "switchPage(2)");
                intent8.putExtra("mark", "party");
                startActivity(intent8);
                break;
        }
    }

    @Override
    public void onBackPressed() { // 连续按下两次返回键才退出App
        long currentTimeMillis = System.currentTimeMillis();
        if (onBackPressedTimeMillis != 0 && currentTimeMillis - onBackPressedTimeMillis < 3000) {
            super.onBackPressed();
        } else {
            App.me().toast("再按一次返回键退出");
//            boolean oo1 = AntiEmulator.CheckEmulatorBuild();
//            String s1 = String.valueOf(oo1);
//            App.me().toast("是Build："+ s1 );

        }
        App.me().NoPrompt();
        onBackPressedTimeMillis = currentTimeMillis;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



}
