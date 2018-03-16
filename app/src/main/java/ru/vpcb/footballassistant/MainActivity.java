package ru.vpcb.footballassistant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.vpcb.footballassistant.services.NewsService;
import ru.vpcb.footballassistant.services.UpdateService;
import ru.vpcb.footballassistant.utils.FDUtils;
import timber.log.Timber;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_FIXTURE_ID;
import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_PROGRESS;
import static ru.vpcb.footballassistant.utils.Config.WIDGET_INTENT_BUNDLE;

public class MainActivity extends AppCompatActivity {

    private static boolean sIsTimber;
    private static Handler mHandler;

    private TextView mPoweredNewsApi;

    private ProgressBar mProgressBar;
    private ProgressBar mProgressValue;
    private TextView mProgressText;
    private ImageView mToolbarLogo;

    // receiver
    private MessageReceiver mMessageReceiver;
    // progress
    private int mServiceProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressText = findViewById(R.id.progress_text);
        mProgressValue = findViewById(R.id.progress_value);
        mToolbarLogo = findViewById(R.id.toolbar_logo);
        mPoweredNewsApi = findViewById(R.id.powered_news_api);


// params


// progress
        setupActionBar();
        setupProgress();
        setupReceiver();
        setupListeners();

        refresh(getString(R.string.action_update));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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


// methods
    private void setupListeners() {
        mPoweredNewsApi.setPaintFlags(mPoweredNewsApi.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mPoweredNewsApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.news_api_link)));
                startActivity(intent);
                finish();
            }
        });
    }

    private void makeTransition() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        if (getResources().getBoolean(R.bool.transition_light)) {
//            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//        }
//        if (getResources().getBoolean(R.bool.transition_dark)) {
//            overridePendingTransition(R.anim.slide_right, R.anim.slide_left_out);
//        }

    }


    private void startTransition() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle options = ActivityOptionsCompat.makeCustomAnimation(MainActivity.this,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                        .toBundle();
                if (getIntent() != null) {
// widget
                    Bundle bundle = getIntent().getBundleExtra(WIDGET_INTENT_BUNDLE);
                    if (bundle != null) {
                        intent.putExtra(WIDGET_INTENT_BUNDLE, bundle);
                    }
// notification
                    int fixtureId = getIntent().getIntExtra(NT_BUNDLE_INTENT_FIXTURE_ID, EMPTY_INT_VALUE);
                    intent.putExtra(NT_BUNDLE_INTENT_FIXTURE_ID, fixtureId);

                }
                startActivity(intent, options);
            }
        }, 250);


    }


    private void setProgressValue(boolean isIndeterminate) {
        mProgressValue.setIndeterminate(isIndeterminate);
    }

    private void setProgressValue() {
        int value = mServiceProgress;
        int max = UPDATE_SERVICE_PROGRESS;
        if (value < 0) return;
        if (value > max) value = max;
        mProgressBar.setProgress(value);
        mProgressText.setText(String.valueOf(value));
        mProgressValue.setProgress(value);
        mProgressText.setVisibility(View.VISIBLE);

        if (value >= max) {
            mProgressValue.setIndeterminate(false);
            mProgressValue.setProgress(value);
        }
    }


    private void setupProgress() {
        mServiceProgress = 0;
        setProgressValue();
        setProgressValue(false);                // static at start
    }


    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mToolbarLogo.setVisibility(View.VISIBLE);
        mToolbarLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.hide();
        }

    }

    private void refresh(String action) {
        Intent intent = new Intent(action, null, this, UpdateService.class);
        startService(intent);

        intent = new Intent(action, null, this, NewsService.class);
        startService(intent);

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
        registerReceiver(mMessageReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(mMessageReceiver);
    }


    // classes
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // an Intent broadcast.
            if (intent != null) {
                String action = intent.getAction();
                if (action.equals(context.getString(R.string.broadcast_data_update_started))) {
                    setProgressValue(true); // indeterminate

                } else if (action.equals(context.getString(R.string.broadcast_data_update_finished))) {
                    mServiceProgress = UPDATE_SERVICE_PROGRESS;
                    setProgressValue();
                    startTransition();

                } else if (action.equals(context.getString(R.string.broadcast_data_update_progress))) {
                    int value = intent.getIntExtra(getString(R.string.extra_progress_counter), -1);
                    if (value >= 0) {
                        mServiceProgress = value;
                        setProgressValue();
                    }
                    if(value > UPDATE_SERVICE_PROGRESS/2)  {
                        mPoweredNewsApi.setOnClickListener(null);  // block click Listener
                        mPoweredNewsApi.setPaintFlags(mPoweredNewsApi.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    }
                } else if (action.equals(context.getString(R.string.broadcast_data_no_network))) {
                    FDUtils.showMessage(context, getString(R.string.matches_no_network_message));
                } else {
                    throw new UnsupportedOperationException("Not yet implemented");
                }

            }

        }
    }


}
