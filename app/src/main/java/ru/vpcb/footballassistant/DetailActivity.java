package ru.vpcb.footballassistant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.dbase.FDContract;
import ru.vpcb.footballassistant.dbase.FDLoader;
import ru.vpcb.footballassistant.dbase.FDProvider;
import ru.vpcb.footballassistant.utils.FDUtils;
import ru.vpcb.footballassistant.widgets.MatchWidgetService;
import timber.log.Timber;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static ru.vpcb.footballassistant.utils.Config.ADMOB_FADE_DURATION;
import static ru.vpcb.footballassistant.utils.Config.ADMOB_SHOW_DURATION;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_BUNDLE;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_URI;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATE_CENTER;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_RECYCLER_POS;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_VIEWPAGER_POS;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_VIEWPAGER_SPAN_DEFAULT;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_VIEWPAGER_SPAN_LIMITS;
import static ru.vpcb.footballassistant.utils.Config.CALENDAR_DIALOG_ACTION_APPLY;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_FIXTURE_DATE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;
import static ru.vpcb.footballassistant.utils.Config.FD_LOADERS_UPDATE_COUNTER;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_MATCH;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_MATCHES;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_SHARE;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_WIDGET;
import static ru.vpcb.footballassistant.utils.Config.MATCH_FRAGMENT_TAG;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.VIEWPAGER_BACK_DURATION;
import static ru.vpcb.footballassistant.utils.Config.VIEWPAGER_BACK_START_DELAY;
import static ru.vpcb.footballassistant.utils.Config.VIEWPAGER_OFF_SCREEN_PAGE_NUMBER;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_BUNDLE_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_BUNDLE_WIDGET_ID;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_INTENT_BUNDLE;
import static ru.vpcb.footballassistant.utils.FDUtils.formatDateFromSQLite;
import static ru.vpcb.footballassistant.utils.FDUtils.formatDateFromSQLiteZeroTime;
import static ru.vpcb.footballassistant.utils.FDUtils.formatDateToSQLite;
import static ru.vpcb.footballassistant.utils.FDUtils.setZeroTime;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ICallback, IReload {

    private static boolean sIsTimber;
    private static Handler mHandler;

    private ProgressBar mProgressValue;
    private ImageView mToolbarLogo;

    private ViewPager mViewPager;
    private ImageView mViewPagerBack;
    private TabLayout mTabLayout;
    private View mWidgetBar;


    private BottomNavigationView mBottomNavigation;


    // receiver
    private MessageReceiver mMessageReceiver;
    // progress
    private int mUpdateCounter;

    // mMap
    private Map<Integer, FDCompetition> mMap;
    private Map<Integer, FDTeam> mMapTeams;
    private Map<Integer, FDFixture> mMapFixtures;


    private Cursor[] mCursors;
    private Bundle mViewPagerBundle;
    private int mViewPagerPos;
    private int mRecyclerPos;
    private MatchFragment mMatchFragment;
    private ViewPagerAdapter mAdapter;


    // widget
    private int mWidgetWidgetId;
    private int mWidgetFixtureId;

    // notification
    private int mNotificationFixtureId;


    // analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private Tracker mTracker;
    // adMob
    private AdView mAdView;

    private static ViewPagerData mViewPagerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // log
        if (!sIsTimber) {
            Timber.plant(new Timber.DebugTree());
            sIsTimber = true;
        }
// handler
        if (mHandler == null) {
            mHandler = new Handler();
        }

// bind
        mProgressValue = findViewById(R.id.progress_value);
        mToolbarLogo = findViewById(R.id.toolbar_logo);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.viewpager_main);
        mViewPagerBack = findViewById(R.id.image_viewpager_back);
        mTabLayout = findViewById(R.id.toolbar_sliding_tabs);
        mWidgetBar = findViewById(R.id.match_widget_toolbar);
        mAdView = findViewById(R.id.adview_banner);

// params
        mCursors = new Cursor[5];
        mWidgetWidgetId = EMPTY_INT_VALUE;
        mWidgetFixtureId = EMPTY_INT_VALUE;
        mNotificationFixtureId = EMPTY_INT_VALUE;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FootballAssistant application = (FootballAssistant) getApplication();
        mTracker = application.getDefaultTracker();

//        mWidgetBundle = null;            // from widget

        // progress
        setupActionBar();
        setupBottomNavigation();
        setupProgress();
        setupReceiver();
        setupListeners();
        setupViewPager();
        setAdMob();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(WIDGET_INTENT_BUNDLE)) {
                Bundle widgetBundle = intent.getBundleExtra(WIDGET_INTENT_BUNDLE);
                if (widgetBundle != null) {
                    mWidgetWidgetId = widgetBundle.getInt(WIDGET_BUNDLE_WIDGET_ID, EMPTY_INT_VALUE);
                    mWidgetFixtureId = widgetBundle.getInt(WIDGET_BUNDLE_FIXTURE_ID, EMPTY_INT_VALUE);
                }
            }
            if (intent != null && intent.hasExtra(NT_BUNDLE_INTENT_FIXTURE_ID)) {
                mNotificationFixtureId = intent.getIntExtra(NT_BUNDLE_INTENT_FIXTURE_ID, EMPTY_INT_VALUE);
            }

            mViewPagerBundle = getDatesSpanBundle(Calendar.getInstance());
            mViewPagerPos = EMPTY_INT_VALUE; // center of -span 0 span+
            mRecyclerPos = EMPTY_INT_VALUE;
            mViewPagerBack.setVisibility(View.VISIBLE);
            mViewPager.setAlpha(0);
            mTabLayout.setAlpha(0);

            showAdMob();
        } else {
            mViewPagerBundle = savedInstanceState.getBundle(BUNDLE_LOADER_DATA_BUNDLE);
            mViewPagerPos = savedInstanceState.getInt(BUNDLE_VIEWPAGER_POS,EMPTY_INT_VALUE);
            mRecyclerPos = savedInstanceState.getInt(BUNDLE_RECYCLER_POS,EMPTY_INT_VALUE);
            mViewPagerBack.setVisibility(View.INVISIBLE);
//            mWidgetBundle = savedInstanceState.getBundle(WIDGET_BUNDLE_INTENT_EXTRA);
//            mWidgetBundle = null;
            mAdView.setVisibility(View.INVISIBLE);
        }

        mMap = new HashMap<>();
        mMapTeams = new HashMap<>();
        mMapFixtures = new HashMap<>();


        mViewPagerBack.setImageResource(FDUtils.getImageBackId());

//            refresh(getString(R.string.action_update));
        startLoaders();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(BUNDLE_LOADER_DATA_BUNDLE, mViewPagerBundle);
        outState.putInt(BUNDLE_VIEWPAGER_POS, mViewPagerPos);
        outState.putInt(BUNDLE_RECYCLER_POS,mRecyclerPos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_calendar) {
            startCalendar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    // callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return FDLoader.getInstance(this, id, args);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader == null || loader.getId() <= 0)
            return;

        switch (loader.getId()) {
            case FDContract.CpEntry.LOADER_ID:
                mCursors[0] = cursor;
//                mMap = FDUtils.readCompetitions(cursor);
                mUpdateCounter++;
                break;

            case FDContract.CpTmEntry.LOADER_ID:
                mCursors[1] = cursor;
//                mMapTeamKeys = FDUtils.readCompetitionTeams(cursor);
                mUpdateCounter++;
                break;

            case FDContract.TmEntry.LOADER_ID:
                mCursors[2] = cursor;
//                mMapTeams = FDUtils.readTeams(cursor);
                mUpdateCounter++;
                break;

            case FDContract.CpFxEntry.LOADER_ID:
                mCursors[3] = cursor;
//                mMapFixtureKeys = FDUtils.readCompetitionFixtures(cursor);
                mUpdateCounter++;
                break;

            case FDContract.FxEntry.LOADER_ID:
                mCursors[4] = cursor;
//                mMapFixtures = FDUtils.readFixtures(cursor);
                mUpdateCounter++;
                break;

            case FDContract.TbEntry.LOADER_ID:
                break;

            case FDContract.PlEntry.LOADER_ID:
                break;

            default:
                throw new IllegalArgumentException("Unknown id: " + loader.getId());
        }


        if (mUpdateCounter == FD_LOADERS_UPDATE_COUNTER) {
//            setupViewPagerSource();
//            setupViewPager();
//            stopProgress();
            new DataLoader().execute(mCursors);

            mUpdateCounter = 0;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// cursors will be closed by supportLoaderManager().CursorLoader()

    }

    @Override
    public void onComplete(View view, int fixtureId) {
// widget
        if (mWidgetWidgetId > 0) {
            MatchWidgetService.startFillWidgetAction(this, mWidgetWidgetId, fixtureId);
            mViewPagerBundle = null;
            mWidgetBar.setVisibility(View.INVISIBLE);
            FDUtils.showMessage(this, getString(R.string.widget_button_update));
            mWidgetWidgetId = EMPTY_INT_VALUE;

            fireBaseEvent(FIREBASE_WIDGET);  // analytics
            return;
        }

        startMatchFragment(fixtureId);

        fireBaseEvent(FIREBASE_MATCH);

    }


    @Override
    public void onComplete(int mode, Calendar calendar) {
        try {
            if (mode == CALENDAR_DIALOG_ACTION_APPLY) {
                mViewPagerBundle = getDatesSpanBundle(calendar);
                mViewPagerPos = EMPTY_INT_VALUE;
                mRecyclerPos = EMPTY_INT_VALUE;
                restartLoaders();
            }

        } catch (NullPointerException e) {
            Timber.d(getString(R.string.calendar_set_date_exception, e.getMessage()));
        }
    }

    @Override
    public void onComplete(View view, String value, String title) {

    }


    // methods
    private void showAdMob() {
        if (FDUtils.isShowAdMob()) {
            mAdView.setVisibility(View.VISIBLE);
            mAdView.animate().setStartDelay(ADMOB_SHOW_DURATION)
                    .setDuration(ADMOB_FADE_DURATION)
                    .alpha(0)
                    .translationY(getResources().getDimension(R.dimen.admob_height))
                    .start();
        } else {
            mAdView.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Setup AdMob application and banner and Interstitial Ad objects
     * Builds both object for emulator device only.
     */
    private void setAdMob() {
        MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }


    public void fireBaseEvent(int code) {
        switch (code) {
            case FIREBASE_MATCHES:
                fireBaseEvent();
                analyticsPage(getString(R.string.fb_matches));
                break;
            case FIREBASE_MATCH:
                fireBaseEvent(getString(R.string.fb_match_id), getString(R.string.fb_match));
                analyticsEvent(getString(R.string.fb_match));
                break;
            case FIREBASE_WIDGET:
                fireBaseEvent(getString(R.string.fb_widget_id), getString(R.string.fb_widget));
                analyticsEvent(getString(R.string.fb_widget));
                break;
            case FIREBASE_SHARE:
                fireBaseEvent(getString(R.string.fb_share_id), getString(R.string.fb_share_match));
                analyticsEvent(getString(R.string.fb_share_match));
                break;

        }
    }

    private void analyticsPage(String name) {
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void analyticsEvent(String name) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.fb_action_id))
                .setAction(name)
                .build());

    }

    private void fireBaseEvent(String action, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, action);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, getString(R.string.fb_matches_content));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void fireBaseEvent() {
        if (mAdapter == null || mViewPagerPos < 0) return;
        CharSequence charSequence = mAdapter.getPageTitle(mViewPagerPos);
        String s = EMPTY_DASH;
        if (charSequence != null) s = charSequence.toString();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.fb_matches_id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.fb_matches, s));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, getString(R.string.fb_matches_content));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void startMatchFragment(int fixtureId) {

        FDFixture fixture = mMapFixtures.get(fixtureId);
        if (fixture == null || fixture.getId() <= 0) return;

        FDTeam homeTeam = mMapTeams.get(fixture.getHomeTeamId());
        FDTeam awayTeam = mMapTeams.get(fixture.getAwayTeamId());

//        FDCompetition competition = mMap.get(fixture.getCompetitionId());
//        if (competition != null) {
//            fixture.set_Caption(competition.getCaption());
//            fixture.set_League(competition.getLeague());
//        }

        startMatchFragment(fixture, homeTeam, awayTeam);
    }

    public Map<Integer, FDCompetition> getMap() {
        return mMap;
    }

    // TODO Optimize loaders
    private void restartLoaders() {
        Bundle bundle = mViewPagerBundle;
        getSupportLoaderManager().initLoader(FDContract.CpEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.CpTmEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.CpFxEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.TmEntry.LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(FDContract.FxEntry.LOADER_ID, bundle, this);

    }

    private void startLoaders() {
        Bundle bundle = mViewPagerBundle;
        getSupportLoaderManager().initLoader(FDContract.CpEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.CpTmEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.CpFxEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.TmEntry.LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(FDContract.FxEntry.LOADER_ID, bundle, this);

    }

    private int getFixtureDatesSpan() {
        String stringDateSpan = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_date_span_key),
                        getString(R.string.pref_date_span_default));
        int dateSpan = BUNDLE_VIEWPAGER_SPAN_DEFAULT;
        try {
            dateSpan = Integer.valueOf(stringDateSpan);

        } catch (NumberFormatException e) {
            Timber.d(getString(R.string.read_shared_preference_exception));
        }
        return dateSpan;
    }

    private Bundle getDatesSpanBundle(Calendar c) {
        String dateBefore;
        String dateAfter;
        String dateCenter;

        int dateSpan = getFixtureDatesSpan();
        setZeroTime(c);

        if (dateSpan > 0) {

            dateCenter = formatDateToSQLite(c.getTime());
            c.add(Calendar.DATE, -dateSpan / 2);
            dateBefore = formatDateToSQLite(c.getTime());
            c.add(Calendar.DATE, dateSpan + (1 - (dateSpan % 2)));
            dateAfter = formatDateToSQLite(c.getTime());
        } else {
            dateCenter = formatDateToSQLite(c.getTime());
            c.add(Calendar.DATE, -BUNDLE_VIEWPAGER_SPAN_LIMITS);
            dateBefore = formatDateToSQLite(c.getTime());
            c.add(Calendar.DATE, BUNDLE_VIEWPAGER_SPAN_LIMITS * 2);
            dateAfter = formatDateToSQLite(c.getTime());
        }

        Bundle bundle = new Bundle();
//        bundle.putString(BUNDLE_LOADER_DATA_ID, dateBefore);
//        bundle.putString(BUNDLE_LOADER_DATA_ID2, dateAfter);
        bundle.putString(BUNDLE_LOADER_DATE_CENTER, dateCenter);

        Uri uri = FDProvider.buildLoaderIdUri(this, FDContract.FxEntry.LOADER_ID, dateBefore, dateAfter);
        bundle.putParcelable(BUNDLE_LOADER_DATA_URI, uri);
        return bundle;
    }

    private void startActivitySettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(intent, bundle);
        finish();
    }

    private void startActivityFavorites() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(intent, bundle);
        finish();
    }


//    private void startActivityNews() {
//        Intent intent = new Intent(this, NewsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear stack hard but flashes fade in out
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  // standard transition
//    }

    private void startActivityNews() {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(intent, bundle);
        finish();
    }


    private void startMatchFragment(FDFixture fixture, FDTeam homeTeam, FDTeam awayTeam) {
        FragmentManager fm = getSupportFragmentManager();
        mMatchFragment = MatchFragment.newInstance(fixture, homeTeam, awayTeam);
        fm.popBackStackImmediate(MATCH_FRAGMENT_TAG, POP_BACK_STACK_INCLUSIVE);
        fm.beginTransaction()
                .replace(R.id.container_detail, mMatchFragment)
                .addToBackStack(MATCH_FRAGMENT_TAG)
                .commit();
    }


    private void setupListeners() {
    }


    private Calendar getViewPagerDate() {
        try {
            String s = mViewPagerData.mList.get(mViewPager.getCurrentItem()).get(0).getDate();
            Calendar c = FDUtils.getCalendarFromSQLite(s);
            if (c == null) return null;
            setZeroTime(c);
            return c;
        } catch (NullPointerException e) {
            return null;
        }
    }


    private void startCalendar() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = CalendarDialog.newInstance(getViewPagerDate());
        fm.beginTransaction()
                .add(fragment, getString(R.string.calendar_title))
                .commit();

    }

    private RecyclerView getRecycler(List<FDFixture> list) {
//        Config.Span sp = Config.getDisplayMetrics(this);

        View recyclerLayout = getLayoutInflater().inflate(R.layout.recycler_main, mViewPager,false);
        RecyclerView recyclerView = recyclerLayout.findViewById(R.id.recycler_main_container);

        RecyclerDetailAdapter adapter = new RecyclerDetailAdapter(this, list, mMapTeams);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mRecyclerPos = (layoutManager.findFirstVisibleItemPosition());
            }
        });

        return recyclerView;
    }

    private List<Long> getTimesList(List<FDFixture> fixtures) {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < fixtures.size(); i++) {
            Date date = formatDateFromSQLite(fixtures.get(i).getDate());
            if(date == null) continue;
            list.add(date.getTime());
        }
        return list;
    }

    private int getIndex(List<Long> list, long time) {
        int index = Collections.binarySearch(list, time);
        if (index < 0) index = -index - 1;
        if (index < 0) return 0;
        if (index >= list.size()) return list.size() - 1;
        return index;

    }

    private ViewPagerData getViewPagerData() {
        int last = 0;
        int next = 0;
        int current = 0;
        List<FDFixture> fixtures = new ArrayList<>(mMapFixtures.values()); // sorted by date
        Map<Long, Integer> map = new HashMap<>();

//        Collections.sort(fixtures, cFx);
        List<List<FDFixture>> list = new ArrayList<>();

        Date date = null;
        long time = 0;
        if (mViewPagerBundle != null) {
            date = formatDateFromSQLiteZeroTime(mViewPagerBundle.getString(BUNDLE_LOADER_DATE_CENTER));
        }

        if(date != null) time = date.getTime();
        else time = Calendar.getInstance().getTimeInMillis();

        List<Long> times = getTimesList(fixtures);
        current = getIndex(times, time);

        while (next < times.size() - 1) {
            date = formatDateFromSQLiteZeroTime(fixtures.get(next).getDate());
            if (date == null) {  // skip wrong date
                next++;
                continue;
            }
            time = date.getTime();
//            map.put(time, list.size());
            time += TimeUnit.DAYS.toMillis(1);
            next = getIndex(times, time);
            list.add(new ArrayList<>(fixtures.subList(last, next)));
            last = next;
            if (next == current) {
                if (mViewPagerPos < 0)
                    mViewPagerPos = list.size();  // index of current day records
            }
        }

        List<View> recyclers = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        for (List<FDFixture> listFixtures : list) {
            recyclers.add(getRecycler(listFixtures));
            titles.add(getRecyclerTitle(listFixtures));
        }

        return new ViewPagerData(recyclers, titles, current, list, map);
    }

    private String getRecyclerTitle(List<FDFixture> list) {
        try {
            return FDUtils.formatMatchDate(list.get(0).getDate());
        } catch (NullPointerException e) {
            return EMPTY_FIXTURE_DATE;
        }
    }


    private void setupViewPager() {
        mAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(VIEWPAGER_OFF_SCREEN_PAGE_NUMBER);  //    ATTENTION  Prevents Adapter Exception
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerPos = position;
                fireBaseEvent(FIREBASE_MATCHES);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void updateViewPager(final ViewPagerData data) {
        if (mViewPager == null || data == null) return;


        mViewPagerData = data;
        int pos = mViewPagerPos;  // swap changes pos
        final int recyclerPos = mRecyclerPos;

        ((ViewPagerAdapter) mViewPager.getAdapter()).swap(data.mRecyclers, data.mTitles);
        mViewPager.setCurrentItem(pos);
        RecyclerView recyclerView = (RecyclerView)data.mRecyclers.get(pos);
        recyclerView.scrollToPosition(recyclerPos);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(recyclerPos >= 0 ) {
                    mRecyclerPos = recyclerPos;
                }
            }
        });
// animation
        mViewPagerBack.animate().alpha(0).setStartDelay(VIEWPAGER_BACK_START_DELAY)
                .setDuration(VIEWPAGER_BACK_DURATION).start();
        if (mViewPager.getAlpha() == 0) {
            mViewPager.animate()
                    .alpha(1)
                    .setStartDelay(VIEWPAGER_BACK_START_DELAY)
                    .setDuration(VIEWPAGER_BACK_DURATION).start();
            mTabLayout.animate()
                    .alpha(1)
                    .setStartDelay(VIEWPAGER_BACK_START_DELAY)
                    .setDuration(VIEWPAGER_BACK_DURATION).start();
        }
// tab_layout scrolling
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setScrollPosition(mViewPagerPos, 0, false);
            }
        });

// widget support
        if (mWidgetWidgetId > 0) {
            mWidgetBar.setVisibility(View.VISIBLE);
            mWidgetBar.animate()
                    .alpha(1)
                    .setStartDelay(VIEWPAGER_BACK_START_DELAY)
                    .setDuration(VIEWPAGER_BACK_DURATION).start();
        }
        if (mWidgetFixtureId > 0) {
            startMatchFragment(mWidgetFixtureId);
            mWidgetFixtureId = EMPTY_INT_VALUE;
        }
// notification support
        if (mNotificationFixtureId > 0) {
            startMatchFragment(mNotificationFixtureId);
            mNotificationFixtureId = EMPTY_INT_VALUE;
        }

    }


    private void stopProgress() {
        mProgressValue.setVisibility(View.INVISIBLE);
    }


    private void setupProgress() {
        mProgressValue.setIndeterminate(true);
        mProgressValue.setVisibility(View.INVISIBLE);
    }


    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.screen_match));
        setSupportActionBar(toolbar);

        mToolbarLogo.setVisibility(View.INVISIBLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.show();
        }

    }


    private void setupReceiver() {
        mMessageReceiver = new MessageReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.broadcast_data_update_started));
        intentFilter.addAction(getString(R.string.broadcast_data_update_finished));
        intentFilter.addAction(getString(R.string.broadcast_data_no_network));
        intentFilter.addAction(getString(R.string.broadcast_data_update_progress));
        intentFilter.addAction(getString(R.string.broadcast_notification_change));
        registerReceiver(mMessageReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onReload() {
        restartLoaders();
    }


    // classes
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // an Intent broadcast.
            if (intent != null) {
                String action = intent.getAction();
                if (action.equals(context.getString(R.string.broadcast_data_update_started))) {

                } else if (action.equals(context.getString(R.string.broadcast_data_update_finished))) {

                } else if (action.equals(context.getString(R.string.broadcast_data_update_progress))) {

                } else if (action.equals(context.getString(R.string.broadcast_data_no_network))) {
                    FDUtils.showMessage(context, getString(R.string.matches_no_network_message));


                } else if (action.equals(getString(R.string.broadcast_notification_change))) {
                    int fixtureId = intent.getIntExtra(NT_BUNDLE_INTENT_FIXTURE_ID, EMPTY_INT_VALUE);
                    if (fixtureId <= 0) return;
                    restartLoaders();                                           // update activity loaders
                    if (mMatchFragment != null && mMatchFragment.isVisible()) {
                        mMatchFragment.onReload();
                    }

                } else {
                    throw new UnsupportedOperationException("Not yet implemented");
                }

            }

        }
    }

    private void setupBottomNavigation() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.setSelectedItemId(R.id.navigation_matches);
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        View rootView = getWindow().getDecorView();

                        Context context = DetailActivity.this;
                        switch (item.getItemId()) {
                            case R.id.navigation_matches:
                                Toast.makeText(context, getString(R.string.activity_same_message),
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.navigation_news:
                                startActivityNews();
                                return true;
                            case R.id.navigation_favorites:
                                startActivityFavorites();
                                return true;
                            case R.id.navigation_settings:
                                startActivitySettings();
                                return true;
                        }
                        return false;
                    }
                });
    }


    private class ViewPagerData {
        private List<View> mRecyclers;
        private List<String> mTitles;
        private int mPos;
        private List<List<FDFixture>> mList;
        private Map<Long, Integer> mMap;


        public ViewPagerData(List<View> recyclers, List<String> titles, int pos,
                             List<List<FDFixture>> list,
                             Map<Long, Integer> map) {
            this.mRecyclers = recyclers;
            this.mTitles = titles;
            this.mPos = pos;
            this.mList = list;
            this.mMap = map;

        }

        public List<List<FDFixture>> getList() {
            return mList;
        }

        public Map<Long, Integer> getMap() {
            return mMap;
        }
    }

    private class DataLoader extends AsyncTask<Cursor[], Void, ViewPagerData> {


        @Override
        protected ViewPagerData doInBackground(Cursor[]... cursors) {
            try {

                Map<Integer, FDCompetition> map = FDUtils.readCompetitions(cursors[0][0]);
                Map<Integer, List<Integer>> mapTeamKeys = new HashMap<>();
                Map<Integer, FDTeam> mapTeams = FDUtils.readTeams(cursors[0][2]);
                Map<Integer, List<Integer>> mapFixtureKeys = new HashMap<>();
                Map<Integer, FDFixture> mapFixtures = FDUtils.readFixtures(cursors[0][4]);

                if (FDUtils.checkEmpty(map, mapTeamKeys, mapTeams, mapFixtureKeys, mapFixtures)) {
                    return null;
                }

                mMap = map;
                mMapTeams = mapTeams;
                mMapFixtures = mapFixtures;
                return getViewPagerData();


            } catch (Exception e) {  // catch all exceptions from database
                Timber.d(getString(R.string.read_database_exception, e.getMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(ViewPagerData viewPagerData) {
            stopProgress();
            if (viewPagerData == null) {
                FDUtils.showMessage(DetailActivity.this, getString(R.string.matches_no_data_message));
                return;
            }

            mViewPagerData = viewPagerData;
            updateViewPager(viewPagerData);


        }

    }


}
