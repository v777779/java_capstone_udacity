package ru.vpcb.footballassistant;

import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.dbase.FDContract;
import ru.vpcb.footballassistant.dbase.FDLoader;
import ru.vpcb.footballassistant.dbase.FDProvider;
import ru.vpcb.footballassistant.glide.GlideUtils;
import ru.vpcb.footballassistant.notifications.NotificationUtils;
import ru.vpcb.footballassistant.utils.FDUtils;
import timber.log.Timber;

import static ru.vpcb.footballassistant.glide.GlideUtils.setTeamImage;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_APP_BAR_HEIGHT;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_URI;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_MATCH_AWAY_TEAM;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_MATCH_FIXTURE;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_MATCH_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_MATCH_HOME_TEAM;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_SHARE;
import static ru.vpcb.footballassistant.utils.Config.NT_FB_JOB_DISPATCHER_ID;

public class MatchFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, ICallback, IReload {

    private static FavoriteAsyncTask mFavoriteTask;
    private RequestBuilder<PictureDrawable> mRequestSvgH;
    private RequestBuilder<Drawable> mRequestPngH;
    private RequestBuilder<PictureDrawable> mRequestSvgA;
    private RequestBuilder<Drawable> mRequestPngA;


    // match toolbar
    @BindView(R.id.image_sm_team_home)
    ImageView mViewTeamHome;
    @BindView(R.id.image_sm_team_away)
    ImageView mViewTeamAway;
    @BindView(R.id.text_sm_item_country)
    TextView mTextCountry;
    @BindView(R.id.text_sm_item_league)
    TextView mViewLeague;
    @BindView(R.id.text_sm_item_time)
    TextView mTextTime;
    @BindView(R.id.text_sm_team_home)
    TextView mTextTeamHome;
    @BindView(R.id.text_sm_team_away)
    TextView mTextTeamAway;
    @BindView(R.id.text_sm_item_date)
    TextView mTextDate;
    @BindView(R.id.text_sm_item_score_home)
    TextView mTextScoreHome;
    @BindView(R.id.text_sm_item_score_away)
    TextView mTextScoreAway;
    @BindView(R.id.text_sm_item_status)
    TextView mTextStatus;
    @BindView(R.id.match_notification_back)
    ImageView mViewNotificationBack;
    @BindView(R.id.match_favorite_back)
    ImageView mViewFavoriteBack;
    @BindView(R.id.match_notification)
    ImageView mViewNotification;
    @BindView(R.id.match_favorite)
    ImageView mViewFavorite;

    @BindView(R.id.icon_match_arrow_back)
    ImageView mViewArrowBack;
    @BindView(R.id.icon_match_share_action)
    ImageView mViewShare;

    // match start
    @BindView(R.id.match_start_date)
    TextView mTextStartDate;
    @BindView(R.id.match_time_day_high)
    TextView mTextDayHigh;
    @BindView(R.id.match_time_day_middle)
    TextView mTextDayMiddle;
    @BindView(R.id.match_time_day_low)
    TextView mTextDayLow;
    @BindView(R.id.match_time_hour_high)
    TextView mTextHourHigh;
    @BindView(R.id.match_time_hour_low)
    TextView mTextHourLow;
    @BindView(R.id.match_time_min_high)
    TextView mTextMinHigh;
    @BindView(R.id.match_time_min_low)
    TextView mTextMinLow;
    // match bet
    @BindView(R.id.match_bet_home)
    TextView mTextBetHome;
    @BindView(R.id.match_bet_draw)
    TextView mTextBetDraw;
    @BindView(R.id.match_bet_away)
    TextView mTextBetAway;
    // meetings
    @BindView(R.id.match_recycler)
    RecyclerView mRecycler;

    private DetailActivity mActivity;
    private Context mContext;
    private int mAppBarHeight;
    private Unbinder mUnbinder;


    private Map<Integer, FDCompetition> mMap;
    private Map<Integer, FDTeam> mMapTeams;
    private Map<Integer, FDFixture> mMapFixtures;
    private FDFixture mFixture;
    private FDTeam mHomeTeam;
    private FDTeam mAwayTeam;


    public static MatchFragment newInstance(FDFixture fixture, FDTeam homeTeam, FDTeam awayTeam) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MATCH_FIXTURE, fixture);
        if (homeTeam != null)
            args.putParcelable(BUNDLE_MATCH_HOME_TEAM, homeTeam);
        if (awayTeam != null)
            args.putParcelable(BUNDLE_MATCH_AWAY_TEAM, awayTeam);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (DetailActivity) context;
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            AppBarLayout appBarLayout = mActivity.getWindow().getDecorView().findViewById(R.id.app_bar);
            mAppBarHeight = appBarLayout.getHeight();
        } else {
            mAppBarHeight = savedInstanceState.getInt(BUNDLE_APP_BAR_HEIGHT);
        }
// args
        Bundle args = getArguments();
        if (args == null) return;
        mFixture = args.getParcelable(BUNDLE_MATCH_FIXTURE);
        mHomeTeam = args.getParcelable(BUNDLE_MATCH_HOME_TEAM);
        mAwayTeam = args.getParcelable(BUNDLE_MATCH_AWAY_TEAM);

        if (mFixture == null || mFixture.getId() <= 0 ||
                mFixture.getHomeTeamId() <= 0 ||
                mFixture.getAwayTeamId() <= 0) {
            return;
        }

// parameters
        mRequestSvgH = GlideUtils.getRequestBuilderSvg(mContext, R.drawable.fc_logo_widget_home);  // four requests for image logo
        mRequestPngH = GlideUtils.getRequestBuilderPng(mContext, R.drawable.fc_logo_widget_home);
        mRequestSvgA = GlideUtils.getRequestBuilderSvg(mContext, R.drawable.fc_logo_widget_away);
        mRequestPngA = GlideUtils.getRequestBuilderPng(mContext, R.drawable.fc_logo_widget_away);

        mMapTeams = new LinkedHashMap<>();
        mMapTeams.put(mHomeTeam.getId(), mHomeTeam);
        mMapTeams.put(mAwayTeam.getId(), mAwayTeam);

// loader
        startLoaders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.match_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);

        setupActionBar();
        setupListeners();
        setupRecycler();
        bindViews();

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_APP_BAR_HEIGHT, mAppBarHeight);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        restoreActionBar();
        mUnbinder.unbind();

    }

    // callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return FDLoader.getInstance(mContext, id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader == null || loader.getId() <= 0 || cursor == null || cursor.getCount() == 0) {
            return;
        }
        switch (loader.getId()) {
            case FDContract.FxEntry.LOADER_ID:
                Map<Integer, FDFixture> mapFixtures = FDUtils.readFixtures(cursor);
                if (mapFixtures == null || mapFixtures.isEmpty()) {
                    break;
                }
                if (mMapFixtures == null) mMapFixtures = new LinkedHashMap<>(); // fixed order
                mMapFixtures.putAll(mapFixtures);
                updateFixture();
                bindViewsFixtures();
                break;
            case FDContract.CpEntry.LOADER_ID:

                Map<Integer, FDCompetition> map = FDUtils.readCompetitions(cursor);
                if (map == null || map.isEmpty()) {
                    break;
                }
                if (mMap == null) mMap = new LinkedHashMap<>();
                mMap.putAll(map);
                bindViewsFixtures();
                break;
            case FDContract.TmEntry.LOADER_ID:

                break;
            default:
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onComplete(View view, int value) {
        setupIcons();
    }

    @Override
    public void onComplete(int mode, Calendar calendar) {
    }

    @Override
    public void onComplete(View view, String link, String title) {  // from detail activity
    }

    @Override
    public void onReload() {
        restartLoaders();
    }

    // methods
    private void updateFixture() {
        if (mFixture == null || mMapFixtures == null) return;

//        if (fixture == null || fixture.getId() != mFixture.getId()) return;
//        fixture.set_League(mFixture.getLeague());
//        fixture.set_Caption(mFixture.getCaption());
        mFixture = mMapFixtures.get(mFixture.getId());
    }

    private void restartLoaders() {
        Uri uri = FDProvider.buildLoaderIdUri(mContext, FDContract.FxEntry.LOADER_ID,
                mFixture.getHomeTeamId(), mFixture.getAwayTeamId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_LOADER_DATA_URI, uri);
        getLoaderManager().initLoader(FDContract.CpEntry.LOADER_ID, null, this);  // fixtures
        getLoaderManager().restartLoader(FDContract.FxEntry.LOADER_ID, bundle, this);  // fixtures
    }

    private void startLoaders() {
        Uri uri = FDProvider.buildLoaderIdUri(mContext, FDContract.FxEntry.LOADER_ID,
                mFixture.getHomeTeamId(), mFixture.getAwayTeamId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_LOADER_DATA_URI, uri);
        getLoaderManager().initLoader(FDContract.CpEntry.LOADER_ID, null, this);  // fixtures
        getLoaderManager().initLoader(FDContract.FxEntry.LOADER_ID, bundle, this);  // fixtures

    }

    private void setupRecycler() {
        RecyclerMatchAdapter adapter = new RecyclerMatchAdapter(mContext, null, mMapTeams);
        adapter.setHasStableIds(true);
        mRecycler.setAdapter(adapter);
        mRecycler.setLayoutFrozen(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(layoutManager);

    }

    private void setupIcons() {
        if (mFixture.isFavorite()) {
            mViewFavorite.setImageResource(R.drawable.ic_star);
        } else {
            mViewFavorite.setImageResource(R.drawable.ic_star_border_white);
        }
        if (mFixture.isNotified()) {
            mViewNotification.setImageResource(R.drawable.ic_notifications_match);
        } else {
            mViewNotification.setImageResource(R.drawable.ic_notifications_none_white);
        }
    }

    private int getFixtureId() {
        Bundle args = getArguments();
        if (args == null) return EMPTY_FIXTURE_ID;
        return args.getInt(BUNDLE_MATCH_FIXTURE_ID, EMPTY_FIXTURE_ID);

    }

    private void bindViewsFixtures() {
        if (mMapFixtures == null || mMap == null) return;  // waits all loaders
        setupIcons();
//        for (FDFixture fixture : mMapFixtures.values()) {
//            FDCompetition competition = mMap.get(fixture.getCompetitionId());
//            if (competition == null) continue;
//            fixture.set_League(competition.getLeague());
//            fixture.set_Caption(competition.getCaption());
//        }
        List<FDFixture> list = new ArrayList<>(mMapFixtures.values());
        mRecycler.setLayoutFrozen(false);
        ((RecyclerMatchAdapter) mRecycler.getAdapter()).swap(list);
        mRecycler.setLayoutFrozen(true);


    }

    private int[] getOrders(int value) {
        int[] a = new int[3];
        for (int i = 0; i < a.length; i++) {
            a[i] = value % 10;
            value /= 10;
        }
        return a;
    }

    private void bindViews() {
// toolbar
        String country = FDUtils.getCountry(mFixture.getLeague());
        mTextCountry.setText(country);
        mViewLeague.setText(mFixture.getCaption());
        setTeamImage(mHomeTeam.getCrestURL(), mViewTeamHome, mRequestSvgH, mRequestPngH,
                R.drawable.fc_logo_widget_home);
        setTeamImage(mAwayTeam.getCrestURL(), mViewTeamAway, mRequestSvgA, mRequestPngA,
                R.drawable.fc_logo_widget_away);
        mTextTime.setText(FDUtils.formatMatchTime(mFixture.getDate()));
        mTextTeamHome.setText(mFixture.getHomeTeamName());
        mTextTeamAway.setText(mFixture.getAwayTeamName());
        mTextDate.setText(FDUtils.formatMatchDate(mFixture.getDate()));
        mTextScoreHome.setText(FDUtils.formatFromInt(mFixture.getGoalsHome(), EMPTY_DASH));
        mTextScoreAway.setText(FDUtils.formatFromInt(mFixture.getGoalsAway(), EMPTY_DASH));
        mTextStatus.setText(mFixture.getStatus());
        setupIcons();

// start
        Date date = FDUtils.formatDateFromSQLite(mFixture.getDate());
        if (date != null) {
            long current = Calendar.getInstance().getTimeInMillis();
            long time = date.getTime();
            mTextStartDate.setText(FDUtils.formatMatchDateStart(mFixture.getDate()));
            if (current > time) {
                mTextDayHigh.setText(EMPTY_DASH);
                mTextDayMiddle.setText(EMPTY_DASH);
                mTextDayLow.setText(EMPTY_DASH);
                mTextHourHigh.setText(EMPTY_DASH);
                mTextHourLow.setText(EMPTY_DASH);
                mTextMinHigh.setText(EMPTY_DASH);
                mTextMinLow.setText(EMPTY_DASH);
            } else {
                long delta = time - current;
                long days = delta / TimeUnit.DAYS.toMillis(1);
                delta = delta - days * TimeUnit.DAYS.toMillis(1);
                long hours = delta / TimeUnit.HOURS.toMillis(1);
                delta = delta - hours * TimeUnit.HOURS.toMillis(1);
                long minutes = delta / TimeUnit.MINUTES.toMillis(1);

                int[] a = getOrders((int) days);
                if (a[2] > 0) {
                    mTextDayHigh.setVisibility(View.VISIBLE);
                    mTextDayHigh.setText(FDUtils.formatFromInt(a[2], EMPTY_STRING));
                }
                mTextDayMiddle.setText(FDUtils.formatFromInt(a[1], EMPTY_STRING));
                mTextDayLow.setText(FDUtils.formatFromInt(a[0], EMPTY_STRING));

                a = getOrders((int) hours);
                mTextHourHigh.setText(FDUtils.formatFromInt(a[1], EMPTY_STRING));
                mTextHourLow.setText(FDUtils.formatFromInt(a[0], EMPTY_STRING));

                a = getOrders((int) minutes);
                mTextMinHigh.setText(FDUtils.formatFromInt(a[1], EMPTY_STRING));
                mTextMinLow.setText(FDUtils.formatFromInt(a[0], EMPTY_STRING));


            }
        } else {
            mTextStartDate.setText(EMPTY_LONG_DASH);
            mTextDayHigh.setText(EMPTY_DASH);
            mTextDayMiddle.setText(EMPTY_DASH);
            mTextDayLow.setText(EMPTY_DASH);
            mTextHourHigh.setText(EMPTY_DASH);
            mTextHourLow.setText(EMPTY_DASH);
            mTextMinHigh.setText(EMPTY_DASH);
            mTextMinLow.setText(EMPTY_DASH);
        }

// bet

        mTextBetHome.setText(FDUtils.formatMatchBet(mFixture.getHomeWin()));
        mTextBetDraw.setText(FDUtils.formatMatchBet(mFixture.getDraw()));
        mTextBetAway.setText(FDUtils.formatMatchBet(mFixture.getAwayWin()));

//        mTextBetHome.setText("27.1");
//        mTextBetDraw.setText("32.2");
//        mTextBetAway.setText("17.9");


    }


    private void startActivityLeague(int id) {
//        Intent intent = new Intent(this, LeagueActivity.class);
//        intent.putExtra(BUNDLE_INTENT_LEAGUE_ID, id);
//        startActivity(intent);
    }

    private void startActivityTeam(int id) {
//        Intent intent = new Intent(this, TeamActivity.class);
//        intent.putExtra(BUNDLE_INTENT_TEAM_ID, id);
//        startActivity(intent);
    }


    private void startFragmentLeague() {

    }

    private void startFragmentTeam() {
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = TeamFragment.newInstance();
//
//        fm.popBackStackImmediate(FRAGMENT_TEAM_TAG, POP_BACK_STACK_INCLUSIVE);
//        fm.beginTransaction()
//                .add(R.id.container_match, fragment)
//                .addToBackStack(FRAGMENT_TEAM_TAG)
//                .commit();

    }

    private void setupListeners() {

        mViewArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });

        mViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFixture == null || mFixture.getId() <= 0) {
                    FDUtils.showMessage(mContext, getString(R.string.matches_share_no_data_message));
                    return;
                }
                int goalsHome = mFixture.getGoalsHome();
                int goalsAway = mFixture.getGoalsAway();

                String shareText = getString(R.string.action_share_match_message,
                        FDUtils.formatMatchDateStart(mFixture.getDate()) ,
                        mFixture.getLeague(),
                        mFixture.getCaption(),
                        mFixture.getHomeTeamName(),
                        mFixture.getAwayTeamName(),
                        (goalsHome < 0 ? EMPTY_DASH : String.valueOf(goalsHome)),
                        (goalsAway < 0 ? EMPTY_DASH : String.valueOf(goalsAway)),
                        mFixture.getStatus());

                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(shareText)
                        .getIntent(), getString(R.string.action_share)));

                mActivity.fireBaseEvent(FIREBASE_SHARE);
            }
        });

        mViewLeague.setPaintFlags(mViewLeague.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mViewLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentLeague();
//                startActivityLeague(548);
            }
        });

        mViewTeamHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentTeam();
//                startActivityTeam(548);
            }
        });
        mViewTeamAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentTeam();
//                startActivityTeam(535);
//                startActivityTeam(530);
            }
        });

        mViewNotificationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = FDUtils.formatDateFromSQLite(mFixture.getDate());
                if (date == null) {
                    return;
                }

                if (!mFixture.isNotified()) {
//TODO Notification Text
// notification text
//                    Calendar c = Calendar.getInstance();
//                    c.add(Calendar.SECOND, +15);
//                    String s = FDUtils.formatDateToSQLite(c.getTime());
//                    mFixture.setDate(s);
//
                    String id = NotificationUtils.scheduleReminder(mContext, mFixture);
                    if (id == null || id.isEmpty() || !id.contains(NT_FB_JOB_DISPATCHER_ID)) return;
                    mFixture.setNotified(true);
                    mFixture.setNotificationId(id);
                    mFavoriteTask = new FavoriteAsyncTask(mContext, mFixture, MatchFragment.this);
                    mFavoriteTask.execute();
                    FDUtils.showMessage(mContext,
                            getString(R.string.notification_set_message,
                                    FDUtils.formatMatchDateStart(mFixture.getDate())));

                } else { // reset notification
                    boolean isSuccess = NotificationUtils.scheduleRemover(mContext, mFixture);


                    mFixture.setNotified(false);
                    mFixture.setNotificationId(EMPTY_STRING);
                    mFavoriteTask = new FavoriteAsyncTask(mContext, mFixture, MatchFragment.this);
                    mFavoriteTask.execute();

                    String message;
                    if (isSuccess) {
                        message = getString(R.string.notification_reset_message,
                                FDUtils.formatMatchDateStart(mFixture.getDate()));
                    } else {
                        message = getString(R.string.notification_reset_error_message);
                    }
                    FDUtils.showMessage(mContext, message);

                }
            }
        });

        mViewFavoriteBack.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (mFixture == null || mFixture.getId() <= 0) return;
                mFixture.setFavorite(!mFixture.isFavorite());  // check/uncheck
                mFavoriteTask = new FavoriteAsyncTask(mContext, mFixture, MatchFragment.this);
                mFavoriteTask.execute();
            }
        });

    }

    private void restoreActionBar() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) actionBar.show();

        AppBarLayout appBarLayout = mActivity.getWindow().getDecorView().findViewById(R.id.app_bar);
        appBarLayout.getLayoutParams().height = mAppBarHeight;
    }

    private void setupActionBar() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        AppBarLayout appBarLayout = mActivity.getWindow().getDecorView().findViewById(R.id.app_bar);
        appBarLayout.getLayoutParams().height = 0;
    }


    private static class FavoriteAsyncTask extends AsyncTask<Void, Void, FDFixture> {
        private final WeakReference<Context> weakContext;
        private FDFixture mFixture;
        private ICallback mCallback;

        FavoriteAsyncTask(Context context, FDFixture fixture, ICallback callback) {
            this.weakContext = new WeakReference<>(context);
            this.mFixture = fixture;
            this.mCallback = callback;
        }

        @Override
        protected FDFixture doInBackground(Void... params) {
            Context context = weakContext.get();
            if (context == null || mFixture == null || mFixture.getId() <= 0) return null;
            try {
                FDUtils.updateFixtureProjection(context, mFixture, false); // update
                return FDUtils.readFixture(context, mFixture.getId());


            } catch (OperationApplicationException | RemoteException e) {
                Timber.d(context.getString(R.string.favorites_database_exception, e.getMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(FDFixture fixture) {
            Context context = weakContext.get();
            if (fixture == null || context == null) return;

            if (mFixture.getId() != fixture.getId() || mFixture.isFavorite() != fixture.isFavorite() ||
                    mFixture.isNotified() != fixture.isNotified()) {
                FDUtils.showMessage(context, context.getString(R.string.favorites_change_error));
                return;
            }
// notificationID does not updated when load database first time and =null
            String id =     mFixture.getNotificationId();
            String newId =  fixture.getNotificationId();
            if(id != null && newId != null && !id.equals(newId)){
                FDUtils.showMessage(context, context.getString(R.string.favorites_change_error));
                return;
            }

            mCallback.onComplete(null, 0);    // setup icons
            ((IReload) context).onReload();             // restart activity loaders
        }
    }


}
