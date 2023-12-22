package net.fkm.cloudmusictest.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import net.fkm.cloudmusictest.CloudMusicApplication;

import me.drakeet.support.toast.ToastCompat;

/**
 * 单例Toast
 * An Android library to hook and fix Toast BadTokenException
 * 提供一个轻量级的android弹出框
 */
public class ToastUtil {

    private static ToastCompat mToast;

    /**
     * 用于显示短时间的消息弹出框
     * 如果没有显示的Toast消息，创建一个新的Toast用于显示
     * 如果已经有Toast显示的，取消当前的Toast,创建并显示一个新的Toast
     * @param text
     */
    @SuppressLint("ShowToast")
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = ToastCompat.makeText(CloudMusicApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = ToastCompat.makeText(CloudMusicApplication.getInstance(), text, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    /**
     * 显示长时间的Toast
     * @param text
     */
    @SuppressLint("ShowToast")
    public static void showToastLong(String text) {
        if (mToast == null) {
            mToast = ToastCompat.makeText(CloudMusicApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = ToastCompat.makeText(CloudMusicApplication.getInstance(), text, Toast.LENGTH_LONG);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }

}
