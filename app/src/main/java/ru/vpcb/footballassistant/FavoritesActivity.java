package ru.vpcb.footballassistant;


import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.dbase.FDContract;
import ru.vpcb.footballassistant.dbase.FDLoader;
import ru.vpcb.footballassistant.dbase.FDProvider;
import ru.vpcb.footballassistant.services.UpdateService;
import ru.vpcb.footballassistant.utils.Config;
import ru.vpcb.footballassistant.utils.FDUtils;
import timber.log.Timber;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_URI;
import static ru.vpcb.footballassistant.utils.Config.CP_IS_FAVORITE_SEARCH_KEY;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.FRAGMENT_TEAM_TAG;
import static ru.vpcb.footballassistant.utils.FDUtils.cFx;

public class FavoritesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ICallback, IReload {

    private static boolean sIsTimber;
    private static Handler mHandler;


    private ProgressBar mProgressValue;
    private RecyclerView mRecycler;


    private BottomNavigationView mBottomNavigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mBottomNavigationListener;
    private Map<Integer, FDFixture> mMapFixtures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorites_activity);

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
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mRecycler = findViewById(R.id.recyclerview_main);


// params
        mMapFixtures = new LinkedHashMap<>();

// progress
        setupActionBar();
        setupBottomNavigation();
        setupProgress();
        setupListeners();
        setupRecycler();


        startLoaders();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            restartLoaders();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return FDLoader.getInstance(this, id, args);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader == null || loader.getId() <= 0 || cursor == null) {
            return;
        }

        if (cursor.getCount() == 0) {
            stopProgress();
            FDUtils.showMessage(this, getString(R.string.favorites_database_empty));
            return;
        }
        switch (loader.getId()) {
            case FDContract.FxEntry.LOADER_ID:
                Map<Integer, FDFixture> mapFixtures = FDUtils.readFixtures(cursor);
                if (mapFixtures == null || mapFixtures.isEmpty()) {
                    break;
                }
                if (mMapFixtures == null) mMapFixtures = new LinkedHashMap<>(); // fixed order
                mMapFixtures.clear();
                mMapFixtures.putAll(mapFixtures);
                if (mRecycler == null) return;


                bindViews();

                break;

            default:
                throw new IllegalArgumentException("Unknown id: " + loader.getId());
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// cursors will be closed by supportLoaderManager().CursorLoader()
    }

    @Override
    public void onComplete(View view, int fixtureId) {
        FDFixture fixture = mMapFixtures.get(fixtureId);
        if (fixture == null || fixture.getId() <= 0) return;


        fixture.setFavorite(!fixture.isFavorite());  // check/uncheck
        FavoriteAsyncTask mFavoriteTask = new FavoriteAsyncTask(this, fixture, this);
        mFavoriteTask.execute();

    }

    @Override
    public void onComplete(int mode, Calendar calendar) {

    }

    @Override
    public void onComplete(View view, String link, String title) {

    }

    @Override
    public void onReload() {
        bindViews();
    }


    // methods
    private List<FDFixture> getList(Map<Integer, FDFixture> map) {
        List<FDFixture> list = new ArrayList<>();

        for (FDFixture fixture : map.values()) {
            if (fixture == null || fixture.getId() <= 0) continue;
            list.add(fixture);
        }
        return list;
    }

    private int count = 0;

    private void bindViews() {
        stopProgress();
        if (mMapFixtures == null || mRecycler == null) return;

        final List<FDFixture> list = getList(mMapFixtures);

        RecyclerDetailAdapter adapter = (RecyclerDetailAdapter) mRecycler.getAdapter();
        adapter.swap(list);

    }


    private void restartLoaders() {

        Uri uri = FDProvider.buildLoaderIdUri(this, FDContract.FxEntry.LOADER_ID,
                CP_IS_FAVORITE_SEARCH_KEY, EMPTY_DASH);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_LOADER_DATA_URI, uri);              // uri/#/*


        getSupportLoaderManager().restartLoader(FDContract.FxEntry.LOADER_ID, bundle, this);

    }

    private void startLoaders() {

        Uri uri = FDProvider.buildLoaderIdUri(this, FDContract.FxEntry.LOADER_ID,
                CP_IS_FAVORITE_SEARCH_KEY, EMPTY_DASH);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_LOADER_DATA_URI, uri);              // uri/#/*

        getSupportLoaderManager().initLoader(FDContract.FxEntry.LOADER_ID, bundle, this);

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

    private void startActivityNews() {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(intent, bundle);
        finish();
    }

    private void startActivityMatches() {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear stack  top parent remained
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();
        startActivity(intent, bundle);
        finish();
    }




    private void setupListeners() {

    }



    private void setupRecycler() {
        RecyclerDetailAdapter adapter = new RecyclerDetailAdapter(this, null, null);
        adapter.setHasStableIds(true);
        mRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);

    }


    private void stopProgress() {
        mProgressValue.setVisibility(View.INVISIBLE);
    }


    private void setupProgress() {
        mProgressValue.setIndeterminate(true);
        mProgressValue.setVisibility(View.VISIBLE);
    }


    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.screen_match));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.show();
        }

    }


    private void setupBottomNavigation() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.setSelectedItemId(R.id.navigation_favorites);
        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        View rootView = getWindow().getDecorView();

                        Context context = FavoritesActivity.this;
                        switch (item.getItemId()) {
                            case R.id.navigation_matches:
                                startActivityMatches();
                                return true;
                            case R.id.navigation_news:
                                startActivityNews();
                                return true;
                            case R.id.navigation_favorites:
                                FDUtils.showMessage(context, getString(R.string.activity_same_message));
//                                restartLoaders();
                                return true;
                            case R.id.navigation_settings:
                                startActivitySettings();
                                return true;
                        }
                        return false;
                    }
                });
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
            String id = mFixture.getNotificationId();
            String newId = fixture.getNotificationId();
            if (id != null && newId != null && !id.equals(newId)) {
                FDUtils.showMessage(context, context.getString(R.string.favorites_change_error));
                return;
            }


            ((IReload) context).onReload();             // restart activity loaders
        }
    }



}
