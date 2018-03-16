package ru.vpcb.footballassistant;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.glide.GlideUtils.setTeamImage;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_APP_BAR_HEIGHT;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_NEWS_LINK;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_NEWS_TITLE;
import static ru.vpcb.footballassistant.utils.Config.FIREBASE_SHARE;

public class NewsFragment extends Fragment implements ICallback {

    @BindView(R.id.webview_news)
    WebView mWebView;
    @BindView(R.id.icon_news_arrow_back)
    ImageView mViewArrowBack;
    @BindView(R.id.icon_news_share_action)
    ImageView mViewShare;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar_text)
    TextView mTextToolbar;


    private RequestBuilder<PictureDrawable> mRequestSvgH;
    private RequestBuilder<Drawable> mRequestPngH;
    private RequestBuilder<PictureDrawable> mRequestSvgA;
    private RequestBuilder<Drawable> mRequestPngA;


    // match toolbar
    // match start


    private NewsActivity mActivity;
    private Context mContext;
    private int mAppBarHeight;
    private Unbinder mUnbinder;


    private Map<Integer, FDCompetition> mMap;
    private Map<Integer, FDTeam> mMapTeams;
    private Map<Integer, FDFixture> mMapFixtures;
    private FDFixture mFixture;
    private FDTeam mHomeTeam;
    private FDTeam mAwayTeam;
    private String mLink;
    private String mTitle;


    public static Fragment newInstance(String link, String title) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_NEWS_LINK, link);
        args.putString(BUNDLE_NEWS_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (NewsActivity) context;
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState == null) {
//        } else {
//        }
// args
        Bundle args = getArguments();
        if (args == null) return;
        mLink = args.getString(BUNDLE_NEWS_LINK);
        mTitle = args.getString(BUNDLE_NEWS_TITLE);

        if (mLink == null || mLink.isEmpty()) {
            FDUtils.showMessage(mContext, getString(R.string.news_no_data_message));
        }

// parameters
// loader

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.news_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);

        setupActionBar();
        setupListeners();
        setupProgress();
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
    public void onComplete(View view, int value) {
        FDUtils.showMessage(mContext, getString(R.string.text_test_recycler_click));
    }

    @Override
    public void onComplete(int mode, Calendar calendar) {
    }

    @Override
    public void onComplete(View view, String value, String title) {

    }

    // methods
    private void startActivityShare() {
        if (mLink == null || mLink.isEmpty()) {
            FDUtils.showMessage(mContext, getString(R.string.news_no_data_message));
            return;
        }
        String shareText = getString(R.string.action_share_news_message,mTitle,mLink);
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(shareText)
                .getIntent(), getString(R.string.action_share)));

        mActivity.fireBaseEvent(FIREBASE_SHARE);
    }

    private void stopProgress() {
// test!!!
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.INVISIBLE);
        else
            FDUtils.showMessage(mContext, getString(R.string.progress_bar_message));
    }


    private void setupProgress() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    private void bindViews() {
        mTextToolbar.setText(mTitle);
        mWebView.loadUrl(mLink);
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
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
                startActivityShare();
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {   // without client issues an  Exception on readdress


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                super.shouldOverrideUrlLoading(view, request);
                view.loadUrl(request.getUrl().toString());  // intercepts request
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopProgress();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                FDUtils.showMessage(mContext,getString(R.string.webview_error_page_loading));
            }
        });


    }

    private void restoreActionBar() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) actionBar.show();
    }

    private void setupActionBar() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) actionBar.hide();

    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.setWebViewClient(null);  // prevents call progressbar=null from listener

    }
}
