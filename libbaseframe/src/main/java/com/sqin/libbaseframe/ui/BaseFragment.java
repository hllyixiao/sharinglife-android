package com.sqin.libbaseframe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.sqin.libbaseframe.utils.CheatSheet;
import com.sqin.libbaseframe.utils.CircleTransform;
import com.sqin.libbaseframe.utils.Log;
import com.sqin.libbaseframe.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 作者：sqin
 * 日期：2016/3/1.
 */
public class BaseFragment extends Fragment implements CheatSheet.AnswerSheet{
    protected final String TAG = this.getClass().getSimpleName();

    public static final int RESULT_OK = Activity.RESULT_OK;

    /**
     * 获得返回结果后刷新自身
     */
    public static final int REQUEST_REFRESH = newRequestCode();


    public static int newRequestCode(){
        return BaseActivity.newRequestCode();
    }
    /**
     * 图片显示/下载
     */
    public RequestManager imager;

    protected LayoutInflater mInflater;

    public Handler mHandler;

    protected BaseFragment mFragment;

    protected CircleTransform circleTransform;

    protected Activity act;

    protected SPUtils sp;
    protected Toolbar mToolbar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFragment = this;
        act = getActivity();
        super.onCreate(savedInstanceState);
        sp = new SPUtils(act);
        imager = Glide.with(this);
        mInflater = getLayoutInflater(savedInstanceState);
        mHandler = new Handler();
        circleTransform = new CircleTransform(act);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 使用rx提供的方法在主线程执行任务
     * @param task
     */
    protected void runOnUiThread(Action0 task){
        AndroidSchedulers.mainThread().createWorker().schedule(task);
    }

    protected void runOnUiThread(Action0 task, long delay){
        AndroidSchedulers.mainThread().createWorker().schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Toast显示消息
     * @param v
     */
    public void toast(View v){
        if(v == null || v.getParent() != null) return;
        Toast toast = new Toast(mFragment.getActivity());
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast显示消息
     * @param msg
     */
    public void toast(final String msg){
        if(isEmpty(msg))return;
        runOnUiThread(new Action0() {
            public void call() {
                Toast.makeText(mFragment.getActivity(), msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, msg);
            }
        });
    }

    public void startActivity(Class<? extends Activity> clazz){
        super.startActivity(new Intent(act, clazz));
    }

    public void startActivity(Class<? extends Activity> actClass, Object... extras) {
        startActivityForResult(actClass, -1, extras);
    }

    public void startActivityForResult(Class<? extends Activity> actClass, int requestCode, Object... extras){
        Intent intent = new Intent(act, actClass);
        CheatSheet cheatSheet = new CheatSheet(CheatSheet.CODE_INTENT, extras);
        intent.putExtra(CheatSheet.CODE_INTENT, cheatSheet);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_REFRESH){
            OnResultRefresh(resultCode, data);
        }
    }

    protected void OnResultRefresh(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            reload();
        }
    }
    /**
     * 重新加载页面数据
     */
    public void reload(){

    }

    /**
     * 判断字符串是否为空或空字符串
     * @param str
     * @return
     */
    public boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }
}
