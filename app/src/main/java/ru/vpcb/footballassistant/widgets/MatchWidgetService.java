package ru.vpcb.footballassistant.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.utils.FDUtils;
import timber.log.Timber;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_WIDGET_ID;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_BUNDLE_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_BUNDLE_WIDGET_ID;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_INTENT_BUNDLE;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_SERVICE_FILL_ACTION;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_SERVICE_UPDATE_ACTION;


public class MatchWidgetService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MatchWidgetService() {
        super(MatchWidgetService.class.getSimpleName());
    }


    public static void startFillWidgetAction(Context context, int widgetId, int fixtureId) {
        if (widgetId <= 0 || fixtureId <= 0) {
            return;
        }
        Bundle args = new Bundle();
        args.putInt(WIDGET_BUNDLE_WIDGET_ID, widgetId);
        args.putInt(WIDGET_BUNDLE_FIXTURE_ID, fixtureId);
        Intent intent = new Intent(context, MatchWidgetService.class);
        intent.putExtra(WIDGET_INTENT_BUNDLE, args);
        intent.setAction(WIDGET_SERVICE_FILL_ACTION);
        context.startService(intent);
    }

    public static void startWidgetUpdateAction(Context context) {
        Intent intent = new Intent(context, MatchWidgetService.class);
        intent.setAction(WIDGET_SERVICE_UPDATE_ACTION);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) return;
            if (action.equals(WIDGET_SERVICE_FILL_ACTION)) {
                Bundle bundle = intent.getBundleExtra(WIDGET_INTENT_BUNDLE);
                startFillWidgetAction(bundle);
            }
            if (action.equals(WIDGET_SERVICE_UPDATE_ACTION)) {
                startWidgetUpdateAction();
            }
        }
    }

    private void startWidgetUpdateAction() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName componentName = new ComponentName(this, MatchWidgetProvider.class);
        int[] ids = appWidgetManager.getAppWidgetIds(componentName);
        if (ids == null) return;
        for (int widgetId : ids) {
            int fixtureId = MatchWidgetProvider.getWidgetFixtureId(this, widgetId);

            FDFixture fixture = FDUtils.readFixture(this, fixtureId);
//            if (fixture != null) {
//                FDCompetition competition = FDUtils.readCompetition(this, fixture.getCompetitionId());
//                if (competition != null) {
//                    fixture.setCaption(competition.getCaption());
//                }
//            }

            MatchWidgetProvider.updateWidget(this, appWidgetManager, widgetId, fixture);
        }
    }


    // test!!!





    private void startFillWidgetAction(Bundle bundle) {
        if (bundle == null) return;
        try {
            int widgetId = bundle.getInt(WIDGET_BUNDLE_WIDGET_ID, EMPTY_WIDGET_ID);
            int fixtureId = bundle.getInt(WIDGET_BUNDLE_FIXTURE_ID, EMPTY_FIXTURE_ID);

            FDFixture fixture = FDUtils.readFixture(this, fixtureId);
//            if (fixture == null) return;
//            FDCompetition competition = FDUtils.readCompetition(this, fixture.getCompetitionId());
//            if (competition != null) {
//                fixture.setCaption(competition.getCaption());
//            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            MatchWidgetProvider.fillWidget(this, appWidgetManager, widgetId, fixture);

        } catch (NullPointerException e) {
            Timber.d(getString(R.string.widget_read_fixture_exception, e.getMessage()));
        }
    }

}
