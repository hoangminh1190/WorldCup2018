package com.m2team.worldcup.stadiums;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.qualifiers.teams.OnDataCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.m2team.worldcup.common.Common.TAG;

public class StadiumDetailPresenter {

    private static final String BASE_URL = "http://fifa.com";
    private OnDataCompleteListener listener;
    private Context mContext;
    private String detailUrl;

    public StadiumDetailPresenter(Context context) {
        mContext = context;
    }

    public void setListener(OnDataCompleteListener listener) {
        this.listener = listener;
    }

    public void getStadium(String id, String url) {
        detailUrl = url;

        getData(id, Common.ONE_MONTH_IN_MILLISECONDS);
    }

    public void getData(final String preference, long expiredPeriod) {
        Observable result = getDataStore(mContext, preference, expiredPeriod);
        result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TeamSubscriber());
    }

    private Observable getDataStore(Context mContext, String preference, long expiredPeriod) {
        String json = Common.getPrefString(mContext, preference, Common.KEY_JSON_DATA);
        Log.d(TAG, "json from cache " + json);
        if (!TextUtils.isEmpty(json)) {
            long expiredTime = Common.getPrefLong(mContext, preference, Common.KEY_JSON_EXPIRED);
            Log.d(TAG, "expired time = " + expiredTime);
            if (System.currentTimeMillis() - expiredTime < expiredPeriod) { //data less than 1 days
                Log.d(TAG, "Get from cache");
                return getFromCache(preference);
            }
        }
        return getFromServer(preference);
    }

    private List<String> queryDetail(String url) {
        List<String> infos = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            if (document == null) return null;

            Element body = document.body();
            if (body != null) {

                Element element = body.getElementById("article-body");
                if (element != null) {
                    Elements tag = element.getElementsByTag("p");
                    StringBuilder sb = new StringBuilder();
                    for (Element el : tag) {
                        sb.append(el.outerHtml());
                    }
                    infos.add(sb.toString());
                    try {
                        String teamBG = body.getElementsByClass("article-top-content").first().getElementsByTag("img").first().attr("src");
                        infos.add(teamBG);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.e("HMWC", "Cannot find image BG " + url);
                        infos.add("");
                    }
                    return infos;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot get team detail " + url);
        }
        return null;
    }

    public Observable getFromCache(final String preference) {
         return Observable.create(new Observable.OnSubscribe<List<String>>() {
             @Override
             public void call(Subscriber<? super List<String>> subscriber) {
                 Gson gson = new Gson();
                 String json = Common.getPrefString(mContext, preference, Common.KEY_JSON_DATA);
                 if (!TextUtils.isEmpty(json)) {
                     List<String> s = gson.fromJson(json, new TypeToken<List<String>>() {
                     }.getType());
                     subscriber.onNext(s);
                 }
                 subscriber.onCompleted();
                 Log.d(TAG, "Get team detail from cache done");
             }
         });
    }

    public Observable getFromServer(final String preference) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> detail = queryDetail(detailUrl);

                if (detail == null) {
                    subscriber.onError(new Throwable("Cannot get team detail"));
                } else {
                    subscriber.onNext(detail);
                }
                subscriber.onCompleted();
            }
        }).doOnNext(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Gson gson = new Gson();
                String json = gson.toJson(strings, new TypeToken<List<String>>() {
                }.getType());
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_DATA, json);
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_EXPIRED, System.currentTimeMillis());
                Log.d(TAG, "save to cache done");
            }
        });
    }

    class TeamSubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {
            listener.loadDone();
            Log.d("HMWC", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            listener.updateTeamDetail(null);
        }

        @Override
        public void onNext(List<String> s) {
            listener.updateTeamDetail(s);
        }
    }
}
