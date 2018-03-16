package ru.vpcb.footballassistant.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.utils.FDUtils;
import timber.log.Timber;

import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_PROGRESS;
import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_TAG;
import static ru.vpcb.footballassistant.utils.FDUtils.getRnd;
import static ru.vpcb.footballassistant.utils.FDUtils.isOnline;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 25-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class UpdateService extends IntentService {


    public UpdateService() {
        super(UPDATE_SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals(getString(R.string.action_update))) {
                onActionUpdate();
            }
        }
    }

    private void onActionUpdate() {

        try {
            Map<Integer, FDCompetition> map = new HashMap<>();
            Map<Integer, List<Integer>> mapTeamKeys = new HashMap<>();
            Map<Integer, FDTeam> mapTeams = new HashMap<>();
            Map<Integer, List<Integer>> mapFixtureKeys = new HashMap<>();
            Map<Integer, FDFixture> mapFixtures = new HashMap<>();
            FDUtils.readDatabase(this, map, mapTeamKeys, mapTeams,
                    mapFixtureKeys, mapFixtures);

            if (!FDUtils.checkEmpty(map, mapTeamKeys, mapTeams,
                    mapFixtureKeys, mapFixtures) && FDUtils.isFootballDataRefreshed(this)) {
                sendBroadcast(new Intent(getString(R.string.broadcast_data_update_finished)));
                return;
            }

//            map = FDUtils.readCompetitions(this);  // competitions only
//            if (map != null  && !map.isEmpty()) {
//                sendBroadcast(new Intent(getString(R.string.broadcast_data_update_finished)));
//                return;
//            }


            if (!isOnline(this)) {                                     // no network
                sendBroadcast(new Intent(getString(R.string.broadcast_data_no_network)));
                return;
            }
            sendBroadcast(new Intent(getString(R.string.broadcast_data_update_started)));
            FDUtils.sendProgress(this, 0);

            boolean isUpdated = FDUtils.loadDatabase(this, map, mapTeamKeys, mapTeams,
                    mapFixtureKeys, mapFixtures);
            FDUtils.sendProgress(this, (int) (UPDATE_SERVICE_PROGRESS * 0.8));
            if (isUpdated) {
                FDUtils.writeDatabase(this, map, false); //  true delete false update
            }
            FDUtils.setRefreshTime(this);
            sendBroadcast(new Intent(getString(R.string.broadcast_data_update_finished)));

        } catch (IOException e) {
            Timber.d(getString(R.string.retrofit_response_exception), e.getMessage());
            sendBroadcast(new Intent(getString(R.string.broadcast_data_update_error)));

        } catch (NullPointerException | NumberFormatException e) {
            Timber.d(getString(R.string.retrofit_response_empty), e.getMessage());
            sendBroadcast(new Intent(getString(R.string.broadcast_data_update_error)));

        } catch (OperationApplicationException | RemoteException e) {
            Timber.d(getString(R.string.update_content_error,e.getMessage()));
            sendBroadcast(new Intent(getString(R.string.broadcast_data_update_error)));

        }

    }


}
