package com.m2team.worldcup.qualifiers.group.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Group;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.m2team.worldcup.common.Common.TAG;

public abstract class Presenter {

    public void getData(final Context mContext, String preference, final Subscriber subscriber, long expiredPeriod) {
        Observable result = getDataStore(mContext, preference, expiredPeriod);
        result.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "onError -> try to get from cache");
                getFromCache().observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(subscriber);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    private Observable getDataStore(Context mContext, String preference, long expiredPeriod) {
        String json = Common.getPrefString(mContext, preference, Common.KEY_JSON_DATA);
        Log.d(TAG, "json from cache " + json);
        if (!TextUtils.isEmpty(json)) {
            long expiredTime = Common.getPrefLong(mContext, preference, Common.KEY_JSON_EXPIRED);
            Log.d(TAG, "expired time = " + expiredTime);
            if (System.currentTimeMillis() - expiredTime < expiredPeriod) { //data less than 1 days
                Log.d(TAG, "Get from cache");
                return getFromCache();
            }
        }
        return getFromServer();
    }

    public abstract Observable getFromCache();

    public abstract Observable getFromServer();
}
