package com.m2team.worldcup.qualifiers.group.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Group;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.m2team.worldcup.common.Common.TAG;

/**
 * Created by Tom on 8/30/2016.
 */
public abstract class Presenter {

    public void getData(Context mContext, String preference, Subscriber subscriber) {
        Observable<List<Group>> result = getDataStore(mContext, preference);
        result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    private Observable getDataStore(Context mContext, String preference) {
        String json = Common.getPrefString(mContext, preference, Common.KEY_JSON_DATA);
        Log.d(TAG, "json from cache " + json);
        if (!TextUtils.isEmpty(json)) {
            long expiredTime = Common.getPrefLong(mContext, preference, Common.KEY_JSON_EXPIRED);
            Log.d(TAG, "expired time = " + expiredTime);
            if (System.currentTimeMillis() - expiredTime < Common.ONE_DAY_IN_MILLISECONDS) { //data less than 1 days
                Log.d(TAG, "Get from cache");
                return getFromCache();
            }
        }
        return getFromServer();
    }

    public abstract Observable getFromCache();

    public abstract Observable getFromServer();
}
