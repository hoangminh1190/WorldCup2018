package com.m2team.worldcup.qualifiers.group.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2team.worldcup.common.Common;
import com.m2team.worldcup.model.Group;
import com.m2team.worldcup.model.Team;
import com.m2team.worldcup.qualifiers.group.OnDataCompleteListener;
import com.nostra13.universalimageloader.utils.L;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import static com.m2team.worldcup.common.Common.TAG;

public class EuroQualifierPresenter extends Presenter {


    public static String EURO_LINK = "http://www.fifa.com/worldcup/preliminaries/europe/index.html";

    private OnDataCompleteListener listener;
    private Context mContext;

    public EuroQualifierPresenter(Context context) {
        mContext = context;
    }

    public void getData() {
        getData(mContext, Common.EURO_GROUPS_QUALIFIER, new EuroSubscriber(), Common.ONE_DAY_IN_MILLISECONDS);
    }

    public Observable<List<Group>> getFromCache() {
        return Observable.create(new Observable.OnSubscribe<List<Group>>() {
            @Override
            public void call(Subscriber<? super List<Group>> subscriber) {
                Gson gson = new Gson();
                String json = Common.getPrefString(mContext, Common.EURO_GROUPS_QUALIFIER, Common.KEY_JSON_DATA);
                if (!TextUtils.isEmpty(json)) {
                    List<Group> groupList = gson.fromJson(json, new TypeToken<List<Group>>() {
                    }.getType());
                    subscriber.onNext(groupList);
                }
                subscriber.onCompleted();
                Log.d(TAG, "Get asia qualifier from cache done");
            }
        });
    }

    public Observable<List<Group>> getFromServer() {
        return Observable.create(new Observable.OnSubscribe<List<Group>>() {
            @Override
            public void call(Subscriber<? super List<Group>> subscriber) {

                List<Group> groups = query(EURO_LINK);

                if (groups == null) {
                    subscriber.onError(new Throwable("Cannot get infor euro teams"));
                } else {
                    subscriber.onNext(groups);
                }
                subscriber.onCompleted();
                Log.d(TAG, "Get euro from server done");
            }
        }).doOnNext(new Action1<List<Group>>() {
            @Override
            public void call(List<Group> groups) {
                Gson gson = new Gson();
                String json = gson.toJson(groups, new TypeToken<List<Group>>() {
                }.getType());
                Common.putPrefValue(mContext, Common.EURO_GROUPS_QUALIFIER, Common.KEY_JSON_DATA, json);
                Common.putPrefValue(mContext, Common.EURO_GROUPS_QUALIFIER, Common.KEY_JSON_EXPIRED, System.currentTimeMillis());
                Log.d(TAG, "save to cache done");
            }
        });

    }


    private List<Group> query(String url) {
        List<Group> groups = new ArrayList<>();
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            if (document != null) {
                Elements allRows = document.body().getElementsByTag("tr");

                Elements allImgs = document.body().getElementsByClass("teamname-link").select("img");
                Elements allMps = document.body().getElementsByClass("tbl-matchplayed").select("td");

                Elements allWins = document.body().getElementsByClass("tbl-win").select("td");
                Elements allDraws = document.body().getElementsByClass("tbl-draw").select("td");
                Elements allLoses = document.body().getElementsByClass("tbl-lost").select("td");

                Elements allGAs = document.body().getElementsByClass("tbl-goalagainst").select("td");
                Elements allGFs = document.body().getElementsByClass("tbl-goalfor").select("td");
                Elements allGDeltas = document.body().getElementsByClass("tbl-diffgoal").select("td");

                Elements allPts = document.body().getElementsByClass("tbl-pts").select("td");
                Elements allTeamNames = document.body().getElementsByClass("teamname-link").select("td");
                Elements allCodes = document.body().getElementsByClass("tbl-teamcode").select("td");
                Log.d("HMWC", "size rows = " + allRows.size());

                Group group = new Group();
                List<Team> teams = new ArrayList<>();

                int index = 65;
                int indexTeam = 0;
                boolean isFirstTime = true;

                for (int i = 0; i < allRows.size(); i++) {
                    Element row = allRows.get(i);
                    if (!row.hasAttr("data-url") && !row.hasClass("expandcol")) {
                        if (isFirstTime) {
                            group.setGroupName("Group " + Character.toString((char) index));
                        } else {
                            group.setTeams(teams);
                            groups.add(group);

                            index++;
                            isFirstTime = false;
                            group = new Group();
                            group.setGroupName("Group " + Character.toString((char) index));
                            teams = new ArrayList<>();
                        }


                    } else if (row.hasAttr("data-url")) {
                        isFirstTime = false;
                        String team_matches_url = row.attr("data-url");
                        String name = allTeamNames.get(indexTeam).text();
                        String team_url = row.children().get(1).getElementsByTag("a").first().attr("href");

                        Team team = new Team(name, allCodes.get(indexTeam).text(), allImgs.get(indexTeam).attr("data-src"),
                                allMps.get(indexTeam).text(), allWins.get(indexTeam).text(), allDraws.get(indexTeam).text(), allLoses.get(indexTeam).text(),
                                allGFs.get(indexTeam).text(), allGAs.get(indexTeam).text(), allGDeltas.get(indexTeam).text(),
                                allPts.get(indexTeam).text(), team_url, team_matches_url);

                        teams.add(team);
                        indexTeam++;

                    }
                }
                //add last group
                group.setTeams(teams);
                groups.add(group);
                Log.d("HMWC", "Finish get teams with size " + groups.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("HMWC", "Cannot get teams");
            return null;
        }
        return groups;
    }

    public void setOnDataComplete(OnDataCompleteListener listener) {
        this.listener = listener;
    }

    class EuroSubscriber extends Subscriber<List<Group>> {

        @Override
        public void onCompleted() {
            listener.loadDone();
        }

        @Override
        public void onError(Throwable e) {
            listener.updateView(null);
        }

        @Override
        public void onNext(List<Group> groups) {
            listener.updateView(groups);
        }
    }
}
