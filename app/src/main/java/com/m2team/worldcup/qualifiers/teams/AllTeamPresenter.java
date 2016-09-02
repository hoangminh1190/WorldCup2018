package com.m2team.worldcup.qualifiers.teams;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Team;

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

public class AllTeamPresenter {

    private static final String BASE_URL = "http://fifa.com";
    private OnAllTeamsDataCompleteListener listener;
    private Context mContext;
    private String link;

    public AllTeamPresenter(Context context, String link) {
        mContext = context;
        this.link = link;
    }

    public void setListener(OnAllTeamsDataCompleteListener listener) {
        this.listener = listener;
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

    private List<Team> queryAllTeams(String url) {
        List<Team> infos = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            if (document == null) return null;

            Element body = document.body();
            if (body != null) {

                Elements allTeamLinks = body.select(".team-qualifiedteams .team");
                Elements allNames = body.select(".team-name");
                Elements allFlags = body.select(".team-qualifiedteams .flag");

                for (int i = 0; i < allTeamLinks.size(); i++) {
                    Team team = new Team();
                    team.setAvatar(allFlags.get(i).attr("src"));
                    team.setName(allNames.get(i).text());
                    team.setTeamUrl(BASE_URL + allTeamLinks.get(i).attr("href"));
                    infos.add(team);
                }

                Log.i(TAG, "Get all teams with size " + infos.size());
                return infos;

            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot get all teams " + url);
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
                 Log.d(TAG, "Get all teams from cache done");
             }
         });
    }

    public Observable getFromServer(final String preference) {
        return Observable.create(new Observable.OnSubscribe<List<Team>>() {
            @Override
            public void call(Subscriber<? super List<Team>> subscriber) {
                List<Team> detail = queryAllTeams(link);

                if (detail == null) {
                    subscriber.onError(new Throwable("Cannot get all teams"));
                } else {
                    subscriber.onNext(detail);
                }
                subscriber.onCompleted();
            }
        }).doOnNext(new Action1<List<Team>>() {
            @Override
            public void call(List<Team> strings) {
                Gson gson = new Gson();
                String json = gson.toJson(strings, new TypeToken<List<Team>>() {
                }.getType());
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_DATA, json);
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_EXPIRED, System.currentTimeMillis());
                Log.d(TAG, "save to cache done");
            }
        });
    }

    class TeamSubscriber extends Subscriber<List<Team>> {
        @Override
        public void onCompleted() {
            listener.loadDone();
            Log.d(TAG, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            listener.updateView(null);
        }

        @Override
        public void onNext(List<Team> s) {
            listener.updateView(s);
        }
    }
}
