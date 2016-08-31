package com.m2team.worldcup.qualifiers.teams;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.model.Team;
import com.m2team.worldcup.qualifiers.group.presenter.Presenter;

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

public class TeamPresenter extends Presenter {

    private static final String BASE_URL = "http://fifa.com";
    private OnDataCompleteListener listener;
    private Context mContext;
    private String detailUrl, teamId;

    public TeamPresenter(Context context) {
        mContext = context;
    }

    public void setListener(OnDataCompleteListener listener) {
        this.listener = listener;
    }

    public void getTeamDetail(String teamId, String url) {
        url = url.replace("index", "profile-detail");
        detailUrl = BASE_URL + url;
        this.teamId = teamId;
        Log.d("HMWC", "url = " + detailUrl);

        getData(mContext, teamId, new TeamSubscriber(), Common.ONE_MONTH_IN_MILLISECONDS);
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

    @Override
    public Observable getFromCache() {
         return Observable.create(new Observable.OnSubscribe<List<String>>() {
             @Override
             public void call(Subscriber<? super List<String>> subscriber) {
                 Gson gson = new Gson();
                 String json = Common.getPrefString(mContext, teamId, Common.KEY_JSON_DATA);
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

    @Override
    public Observable getFromServer() {
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
                Common.putPrefValue(mContext, teamId, Common.KEY_JSON_DATA, json);
                Common.putPrefValue(mContext, teamId, Common.KEY_JSON_EXPIRED, System.currentTimeMillis());
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
