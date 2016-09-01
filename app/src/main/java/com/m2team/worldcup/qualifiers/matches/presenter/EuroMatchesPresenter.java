package com.m2team.worldcup.qualifiers.matches.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Match;
import com.m2team.worldcup.model.Team;

import org.jsoup.Connection;
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

public class EuroMatchesPresenter {


    private String link;
    private OnMatchesDataCompleteListener listener;
    private Context mContext;

    public EuroMatchesPresenter(Context context, String link)
    {
        mContext = context;
        this.link = link;
    }

    public void getData(final Context mContext, final String preference, long expiredPeriod) {
        Observable result = getDataStore(mContext, preference, expiredPeriod);

        result.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "onError -> try to get from cache");
                getFromCache(preference).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new MatchSubscriber());
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new MatchSubscriber());
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


    public Observable<List<Match>> getFromCache(final String preference) {
        return Observable.create(new Observable.OnSubscribe<List<Match>>() {
            @Override
            public void call(Subscriber<? super List<Match>> subscriber) {
                Gson gson = new Gson();
                String json = Common.getPrefString(mContext, preference, Common.KEY_JSON_DATA);
                if (!TextUtils.isEmpty(json)) {
                    List<Match> matchList = gson.fromJson(json, new TypeToken<List<Match>>() {
                    }.getType());
                    subscriber.onNext(matchList);
                }
                subscriber.onCompleted();
                Log.d(TAG, "Get matches from cache done");
            }
        });
    }

    public Observable<List<Match>> getFromServer(final String preference) {
        return Observable.create(new Observable.OnSubscribe<List<Match>>() {
            @Override
            public void call(Subscriber<? super List<Match>> subscriber) {

                List<Match> matchList = query(link);

                if (matchList == null) {
                    subscriber.onError(new Throwable("Cannot get infor matches"));

                } else {
                    subscriber.onNext(matchList);
                }
                subscriber.onCompleted();
                Log.d(TAG, "Get matches from server done");
            }
        }).doOnNext(new Action1<List<Match>>() {
            @Override
            public void call(List<Match> groups) {
                Gson gson = new Gson();
                String json = gson.toJson(groups, new TypeToken<List<Match>>() {
                }.getType());
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_DATA, json);
                Common.putPrefValue(mContext, preference, Common.KEY_JSON_EXPIRED, System.currentTimeMillis());
                Log.d(TAG, "save matches to cache done");
            }
        });

    }


    private List<Match> query(String url) {
        List<Match> matchList = new ArrayList<>();
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            if (document != null) {
                Element body = document.body();

                Elements days = body.select(".mu-i-datetime");
                Elements groups = body.select(".mu-i-group");

                Elements homeTeamNames = body.select(".home .t-nText");
                Elements awayTeamNames = body.select(".away .t-nText");

                Elements times = body.select(".s");
                Elements links = body.select(".mu-m-link");

                Elements homeFlags = body.select(".home img");
                Elements awayFlags = body.select(".away img");

                for (int i = 0; i < links.size(); i++) {
                    Match match = new Match();
                    match.day = days.get(i).text();
                    match.group = groups.get(i).text();
                    match.time = times.get(i).text();
                    match.link = links.get(i).attr("href");


                    Team homeTeam = new Team();
                    homeTeam.setName(homeTeamNames.get(i).text());
                    homeTeam.setAvatar(homeFlags.get(i).attr("src"));

                    Team awayTeam = new Team();
                    awayTeam.setName(awayTeamNames.get(i).text());
                    awayTeam.setAvatar(awayFlags.get(i).attr("src"));

                    match.teamA = awayTeam;
                    match.teamH = homeTeam;
                    matchList.add(match);
                }

                Log.d(TAG, "size rows = " + links.size());

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot get matches");
            return null;
        }
        return matchList;
    }

    public void setOnDataComplete(OnMatchesDataCompleteListener listener) {
        this.listener = listener;
    }

    class MatchSubscriber extends Subscriber<List<Match>> {

        @Override
        public void onCompleted() {
            listener.loadDone();
        }

        @Override
        public void onError(Throwable e) {
            listener.updateView(null);
        }

        @Override
        public void onNext(List<Match> list) {
            listener.updateView(list);
        }
    }
}
