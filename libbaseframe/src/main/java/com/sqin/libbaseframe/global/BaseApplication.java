package com.sqin.libbaseframe.global;

import android.app.Application;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.activeandroid.ActiveAndroid;
import com.sqin.libbaseframe.klog.KLog;

/**
 * 作者：sqin
 * 时间：2016/02/29
 */
public class BaseApplication extends Application{

    public static BaseApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        registerWebConnectionListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //清理
        ActiveAndroid.dispose();
    }

    public static BaseApplication getInstance() {
        return application;
    }
    /**
     * 获得网络连接状态
     * -1:未获得手机状态读取权限
     * TelephonyManager.DATA_DISCONNECTED:网络断开
     * TelephonyManager.DATA_CONNECTING:网络正在连接
     * TelephonyManager.DATA_CONNECTED:网络连接上
     * @return
     */
    public static int getNetworkState(){
        return networkState;
    }

    public static boolean isNetWorkDisConnected(){
        return getNetworkState() == TelephonyManager.DATA_DISCONNECTED;
    }

    private static int networkState = -1;
    private void registerWebConnectionListener() {
        try {
            final TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mTelephonyMgr.listen(new PhoneStateListener() {
                @Override
                public void onDataConnectionStateChanged(int state) {
                    super.onDataConnectionStateChanged(state);
                    networkState = state;
                    // TelephonyManager.DATA_DISCONNECTED// 网络断开
                    // TelephonyManager.DATA_CONNECTING// 网络正在连接
                    // TelephonyManager.DATA_CONNECTED// 网络连接上
                }
            }, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        } catch (Exception e) {
            KLog.e("BaseApplication", e.getMessage());
        }
    }

}
