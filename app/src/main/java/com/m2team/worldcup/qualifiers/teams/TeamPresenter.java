package com.m2team.worldcup.qualifiers.teams;

import android.text.TextUtils;
import android.util.Log;

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

public class TeamPresenter {

    private static final String BASE_URL = "http://fifa.com";
    private OnDataCompleteListener listener;

    public TeamPresenter() {
    }

    public void setListener(OnDataCompleteListener listener) {
        this.listener = listener;
    }

    public void getTeam() {
        Observable<Team> teamObservable = Observable.create(new Observable.OnSubscribe<Team>() {
            @Override
            public void call(Subscriber<? super Team> subscriber) {

            }
        });

        teamObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TeamSubscriber());

    }

    public void getTeamDetail(String url) {
        url = url.replace("index", "profile-detail");
        final String detailUrl = BASE_URL + url;
        Log.d("HMWC", "url = " + detailUrl);

        Observable<List<String>> teamDetailObservable = Observable.create(new Observable.OnSubscribe<List<String>>() {
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
        });

        teamDetailObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<String>>() {
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
                });
    }

    private Team queryTeam(String url) {

        try {
            Document document = Jsoup.connect(url).get();
            if (document != null && document.body() != null) {
                Element element = document.body().getElementById("article-body");

            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot get team detail " + url);
        }
        return null;
    }

    private List<String> queryDetail(String url) {
        List<String> infos = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            if (document != null && document.body() != null) {
                String teamBG = document.body().getElementsByClass("article-top-content").first().getElementsByTag("img").first().attr("src");
                Element element = document.body().getElementById("article-body");
                if (element != null) {
                    Elements tag = element.getElementsByTag("p");
                    StringBuilder sb = new StringBuilder();
                    for (Element el : tag) {
                        sb.append(el.outerHtml());
                    }
                    infos.add(sb.toString());
                    infos.add(teamBG);
                    return infos;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot get team detail " + url);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot find image BG " + url);
        }
        return null;
    }

    class TeamSubscriber extends Subscriber<Team> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Team team) {

        }
    }
}
