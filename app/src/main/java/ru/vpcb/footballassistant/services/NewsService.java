package ru.vpcb.footballassistant.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDPlayer;
import ru.vpcb.footballassistant.data.FDTable;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.data.IFDRetrofitAPI;
import ru.vpcb.footballassistant.news.NDArticle;
import ru.vpcb.footballassistant.news.NDSource;
import ru.vpcb.footballassistant.utils.FDUtils;

import timber.log.Timber;

import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_PROGRESS;
import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_TAG;
import static ru.vpcb.footballassistant.utils.FDUtils.isOnline;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 25-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NewsService extends IntentService {

    private boolean isCursorEmpty;
    private boolean isSmartUpdate;

    private Retrofit mRetrofit;
    private IFDRetrofitAPI mRetrofitAPI;
    private OkHttpClient mOkHttpClient;

    private Map<Integer, FDCompetition> mMapCompetitions;

    private Map<Integer, FDFixture> mMapFixture;
    private Map<Integer, FDTable> mMapTables;
    private Map<Integer, List<FDPlayer>> mMapPlayers;


    private Map<Integer, List<Integer>> mapCpTeams;
    private Map<Integer, FDTeam> mMapTeams;


    public NewsService() {
        super(UPDATE_SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            if (action == null) return;
            if (action.equals(getString(R.string.action_update))) {
                onActionUpdate(false);
                return;
            }
            if (action.equals(getString(R.string.action_force_update))) {
                onActionUpdate(true);

            }
        }
    }


    private void onActionUpdate(boolean forceUpdate) {
        try {
// loader body
            Map<String, NDSource> map = new HashMap<>();
            Map<String, List<NDArticle>> mapArticles = new HashMap<>();
            if (!forceUpdate) {
                FDUtils.readDatabaseNews(this, map, mapArticles);
                if (!FDUtils.checkEmpty(map, mapArticles) && FDUtils.isNewsDataRefreshed(this)) {
                    sendBroadcast(new Intent(getString(R.string.broadcast_news_update_finished)));

                    return;
                }
            }
            if (!isOnline(this)) {                                     // no network
                sendBroadcast(new Intent(getString(R.string.broadcast_news_no_network)));
                return;
            }
            sendBroadcast(new Intent(getString(R.string.broadcast_news_update_started)));


            boolean isUpdated = FDUtils.loadDatabaseSources(this, map);
            if (isUpdated) {
                FDUtils.writeDatabaseSources(this, map, false); //  true delete false update
            }
            isUpdated = FDUtils.loadDatabaseArticles(this, map);
            if (isUpdated) {
                FDUtils.writeDatabaseArticles(this, map, false); //  true delete false update
            }

            FDUtils.setNewsRefreshTime(this);
            sendBroadcast(new Intent(getString(R.string.broadcast_news_update_finished)));


        } catch (IOException e) {
// test !!!  catch errors
            Timber.d(getString(R.string.retrofit_response_exception), e.getMessage());
            sendBroadcast(new Intent(getString(R.string.broadcast_news_update_error)));
            return;
        } catch (NullPointerException | NumberFormatException e) {
            Timber.d(getString(R.string.retrofit_response_empty), e.getMessage());
            sendBroadcast(new Intent(getString(R.string.broadcast_news_update_error)));
            return;
        } catch (OperationApplicationException | RemoteException e) {
            Timber.d(getString(R.string.update_content_error, e.getMessage()));
            sendBroadcast(new Intent(getString(R.string.broadcast_news_update_error)));
            return;
        }
        sendBroadcast(new Intent(getString(R.string.broadcast_news_update_finished)));
    }


}
