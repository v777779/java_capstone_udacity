package ru.vpcb.footballassistant.utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.concurrent.TimeUnit;

import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.widgets.MatchWidgetProvider;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */

public class Config {
    // api_keys
    public static final String FD_API_KEY = "70e68c465fd24d2e84c17aa8d71ca9b3";
    public static final String ND_API_KEY = "3dbbb32e16f747e582119f20967996bc";

    // data
    public static final String FD_BASE_URI = "http://api.football-data.org/v1/";
    public static final String FD_COMPETITIONS_GET = "competitions/";

    public static final String FD_QUERY_ID = "id";
    public static final String FD_QUERY_MATCHDAY = "matchday";
    public static final String FD_QUERY_TIMEFRAME = "timeFrame";
    public static final String FD_QUERY_SEASON = "season";

    public static final String FD_COMPETITIONS_TEAMS_GET = "competitions/{" + FD_QUERY_ID + "}/teams";
    public static final String FD_COMPETITIONS_FIXTURES_GET = "competitions/{" + FD_QUERY_ID + "}/fixtures";
    public static final String FD_COMPETITIONS_TABLE_GET = "competitions/{" + FD_QUERY_ID + "}/leagueTable";


    public static final String FD_TEAM_GET = "teams/{" + FD_QUERY_ID + "}";
    public static final String FD_TEAM_FIXTURES_GET = "teams/{" + FD_QUERY_ID + "}/fixtures";
    public static final String FD_TEAM_PLAYERS_GET = "teams/{" + FD_QUERY_ID + "}/players";

    public static final String FD_TIME_PAST = "p";
    public static final String FD_TIME_FUTUTRE = "n";

    public static final String FD_REGEX_TEAMS = ".*teams/";
    public static final String FD_REGEX_FIXTURES = ".*fixtures/";
    public static final String FD_REGEX_COMPETITIONS = ".*competitions/";

    // tables
    public static final int TABLE_SCALE_ID = 10000;
    public static final String EMPTY_GROUP = "";
    public static final String TABLE_GROUP_A = "A";
    public static final String TABLE_GROUP_B = "B";
    public static final String TABLE_GROUP_C = "C";
    public static final String TABLE_GROUP_D = "D";
    public static final String TABLE_GROUP_E = "E";
    public static final String TABLE_GROUP_F = "F";
    public static final String TABLE_GROUP_G = "G";
    public static final String TABLE_GROUP_H = "H";


    // news
    public static final String ND_BASE_URI = "https://newsapi.org/v2/";
    public static final String ND_HEADLINES_GET = "top-headlines";
    public static final String ND_EVERYTHING_GET = "everything";
    public static final String ND_SOURCES_GET = "sources";
    public static final String ND_QUERY_SOURCE = "sources";
    public static final String ND_QUERY_PAGE = "page";
    public static final String ND_QUERY_LANGUAGE = "language";
    public static final String ND_QUERY_CATEGORY = "category";
    public static final String ND_QUERY_API_KEY = "apiKey";
    public static final String ND_LANGUAGE_EN = "en";
    public static final String ND_CATEGORY_SPORT = "sports";
    public static final String ND_DEFAULT_SOURCE = "fox-sport"; // contain


    // retrofit
    // competitions

    public static final String EMPTY_TWO_DASH = "--";
    public static final String EMPTY_MATCH_TIME = "- : -";
    public static final String EMPTY_MATCH_SCORE = "- : -";
    public static final String EMPTY_FIXTURE_DATE = "--/--/--";
    public static final String EMPTY_PLAYER_DATE = "--.--.--";
    public static final String EMPTY_STRING = "";
    public static final int EMPTY_INT_VALUE = -1;

    // news
    public static final String EMPTY_DASH = "-";
    public static final String EMPTY_LONG_DASH = "\u2014";
    public static final String EMPTY_DATE = "--/--/--";
    public static final String EMPTY_LONG_DATE = "--/--/----  --:--";
    public static final String EMPTY_DASH_AGO = "- ago";
    public static final String EMPTY_NOTIFICATION = " home vs away --/--/----  --:--";


    public static final String DATE_FULL_SSS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FULL_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String PATTERN_DATE_SQLITE = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_SQLITE_ZERO_TIME = "yyyy-MM-dd";


    public static final String FORMAT_MATCH_TIME_WIDGET = "%02d:%02d";
    public static final String FORMAT_MATCH_DATE = "%02d:%02d,%02d/%02d/%04d";
    public static final String FORMAT_MATCH_SCORE = "%d : % d";
    public static final String FORMAT_MATCH_BET = "%.1f";

    public static final String PATTERN_MATCH_DATE = "dd/MM/yy";
    public static final String PATTERN_MATCH_TIME = "HH:mm";
    public static final String PATTERN_MATCH_DATETIME = "HH:mm,dd/MM/yy";
    public static final String PATTERN_MATCH_DATE_START = "dd MMM yyyy HH:mm:ss";

    // update service
    public static final String UPDATE_SERVICE_TAG = "UpdaterService";
    public static final int UPDATE_SERVICE_PROGRESS = 100;
    public static final int MAIN_ACTIVITY_PROGRESS = 100;
    public static final int EXCEPTION_CODE_1 = 1;
    public static final int EXCEPTION_CODE_2 = 2;
    public static final int EXCEPTION_CODE_3 = 3;
    public static final int EXCEPTION_CODE_4 = 4;
    public static final int EXCEPTION_CODE_5 = 5;
    public static final int EXCEPTION_CODE_6 = 6;
    public static final int EXCEPTION_CODE_7 = 7;

    public static final long LOAD_DB_TIMEOUT = TimeUnit.SECONDS.toMillis(90);
    public static final long LOAD_DB_DELAY = TimeUnit.SECONDS.toMillis(2);


    // loaders
    public static final int FD_LOADERS_UPDATE_COUNTER = 5;
    public static final int ND_LOADERS_UPDATE_COUNTER = 2;

    // notifications
    public static final String NT_FB_JOB_DISPATCHER_ID = "nt_fb_job_dispatcher_id";
    public static final String NT_FB_JOB_CHANNEL_ID = "nt_fb_job_channel_id";
    public static final int NT_NOTIFICATION_ID = 1240;
    // test!!!
    public static final int NT_FLEXTIME_SECONDS = 1; // seconds
    public static final int NT_DELAY_TIME_MINIMUM = 1; // seconds

    //    actions
    public static final String NT_ACTION_CANCEL = "ru.vpcb.footballassistant.ACTION_NOTIFICATION_CANCEL";
    public static final String NT_ACTION_APPLY = "ru.vpcb.footballassistant.ACTION_NOTIFCATION_APPLY";
    public static final String NT_ACTION_ACTIVITY = "ru.vpcb.footballassistant.ACTION_NOTIFICATION_ACTIVITY";
    public static final String NT_ACTION_CREATE = "ru.vpcb.footballassistant.ACTION_NOTIFCATION_CREATE";

    public static final int NT_ACTION_CANCEL_PENDING_ID = 1250;
    public static final int NT_ACTION_APPLY_PENDING_ID = 1260;
    public static final int NT_ACTION_ACTIVITY_PENDING_ID = 1270;
    public static final int NT_RANDOM_RANGE = 1000;

    public static final String NT_BUNDLE_INTENT_NOTIFICATION_BODY = "nt_bundle_intent_notification_body";
    public static final String NT_BUNDLE_INTENT_NOTIFICATION_ID = "nt_bundle_intent_notification_id";
    public static final String NT_BUNDLE_INTENT_FIXTURE_ID = "nt_bundle_intent_fixture_id";

    public static final int EMPTY_NOTIFICATION_ID = -1;

    // widgets
    public static final String WIDGET_BUNDLE_FIXTURE_ID = "widget_fixture_id";
    public static final String WIDGET_BUNDLE_WIDGET_ID = "widget_widget_id";
    public static final String WIDGET_BUNDLE_INTENT_EXTRA = "widget_bundle_intent_extra";
    public static final String WIDGET_PACKAGE = MatchWidgetProvider.class.getPackage().getName() + ".";

    public static final String WIDGET_SERVICE_FILL_ACTION = WIDGET_PACKAGE + "ACTION_FILL";
    public static final String WIDGET_SERVICE_UPDATE_ACTION = WIDGET_PACKAGE + "ACTION_UPDATE";
    public static final String WIDGET_SERVICE_REFRESH_ACTION = WIDGET_PACKAGE + "ACTION_REFRESH";

    public static final String WIDGET_INTENT_BUNDLE = WIDGET_PACKAGE + "widget.intent.BUNDLE_ARGS";
    public static final String WIDGET_PREFERENCES = WIDGET_PACKAGE + "widget.preferences.";

    public static final int EMPTY_WIDGET_ID = -1;
    public static final int EMPTY_FIXTURE_ID = -1;

    public static final String PATTERN_MATCH_DATE_WIDGET = "EEE, dd MMM yyyy";


    public static final int WIDGET_PID_BASE = 12800000;
    public static final int WIDGET_PID_SCALE = 1000;
    public static final int WIDGET_PID_OFFSET0 = 0;
    public static final int WIDGET_PID_OFFSET1 = 0;
    public static final int WIDGET_PID_OFFSET2 = 0;

    public static final int WIDGET_ANIMATE_TIMEOUT = 100;

    //main_activity
    public static final int MAIN_ACTIVITY_STATE_0 = 0;
    public static final int MAIN_ACTIVITY_STATE_1 = 1;
    public static final int MAIN_ACTIVITY_STATE_2 = 2;
    public static final int MAIN_ACTIVITY_STATE_3 = 3;
    public static final int MAIN_ACTIVITY_STATE_4 = 4;
    public static final int MAIN_ACTIVITY_STATE_5 = 5;
    public static final int MAIN_ACTIVITY_INDEFINITE = -1;


    public static final int[] IMAGE_IDS = {
            R.drawable.back_001,
            R.drawable.back_002,
            R.drawable.back_003,
            R.drawable.back_004

    };


    // recycler
    private static final int HIGH_SCALE_WIDTH = 180;     // dpi
    private static final int HIGH_SCALE_HEIGHT = 300;     // dpi 200
    private static final double SCALE_RATIO_VERT = 0.6;   // dw/dh
    private static final double SCALE_RATIO_HORZ = 0.45;   // dw/dh

    private static final int MIN_SPAN = 1;
    private static final int MIN_HEIGHT = 100;
    private static final int MIN_WIDTH = 100;

    public static final int RM_ITEM_VIEW_TYPE_LIGHT = 0;
    public static final int RM_ITEM_VIEW_TYPE_DARK = 1;
    public static final int RM_HEAD_VIEW_TYPE = 2;
    public static final int RT_ITEM_VIEW_TYPE_LIGHT = 0;
    public static final int RT_ITEM_VIEW_TYPE_DARK = 1;
    public static final int RT_HEAD_VIEW_TYPE = 2;


    // viewpager
    public static final int VIEWPAGER_OFF_SCREEN_PAGE_NUMBER = 3;


    //detail activity
    public static final int CALENDAR_DIALOG_ACTION_APPLY = 0;
    public static final int CALENDAR_DIALOG_ACTION_CANCEL = 1;
    public static final String BUNDLE_VIEWPAGER_POS = "bundle_viewpager_pos";
    public static final String BUNDLE_RECYCLER_POS = "bundle_recycler_pos";
    public static final int BUNDLE_VIEWPAGER_POS_DEFAULT = 0;
    public static final int BUNDLE_VIEWPAGER_SPAN_DEFAULT = 10;
    public static final int BUNDLE_VIEWPAGER_SPAN_LIMITS = 730;

    public static final int VIEWPAGER_BACK_START_DELAY = 0;
    public static final int VIEWPAGER_BACK_DURATION = 750;

    public static final int MESSAGE_TOAST_SUPER_LONG = 30000;
    public static final int MESSAGE_TOAST_TICK = 1000;

    // loaders
    public static final String BUNDLE_LOADER_REQUEST = "bundle_laoder_request";
    public static final int BUNDLE_LOADER_REQUEST_DATES = 0;
    public static final int BUNDLE_LOADER_REQUEST_ID = 1;
    public static final int BUNDLE_LOADER_REQUEST_TEAMS = 2;
    public static final int BUNDLE_LOADER_REQUEST_FIXTURES = 3;

    public static final String BUNDLE_LOADER_DATA_BUNDLE = "bundle_loader_data_bundle";
    public static final String BUNDLE_LOADER_DATA_ID = "bundle_loader_data_first_id";
    public static final String BUNDLE_LOADER_DATA_ID2 = "bundle_loader_data_second_id";
    public static final String BUNDLE_LOADER_DATE_CENTER = "bundle_loader_date_center";
    public static final String BUNDLE_LOADER_DATA_URI = "bundle_loader_data_uri";
// calendar
    public static final String BUNDLE_CALENDAR_DATA = "bundle_calendar_data";

    // match fragment
    public static final String MATCH_FRAGMENT_TAG = "match_fragment";

    public static final String BUNDLE_MATCH_FIXTURE_ID = "bundle_match_fixture_id";
    public static final String BUNDLE_APP_BAR_HEIGHT = "bundle_app_bar_height";

    public static final String BUNDLE_MATCH_FIXTURE = "bundle_match_fixture";
    public static final String BUNDLE_MATCH_HOME_TEAM = "bundle_match_home_team";
    public static final String BUNDLE_MATCH_AWAY_TEAM = "bundle_match_away_team";

    public static final boolean SHOW_MESSAGE_INFINITE = true;

    // news activity
    public static final String ND_SOURCE_SELECTED = "fox";

    // news fragment
    public static final String NEWS_FRAGMENT_TAG = "news_fragment";
    public static final String BUNDLE_NEWS_LINK = "bundle_news_link";
    public static final String BUNDLE_NEWS_TITLE = "bundle_news_title";

    // favorites activity

    public static final String CP_IS_FAVORITE_SEARCH_KEY = "1";   // is_favorite = 1
    // analytics
    public static final int FIREBASE_MATCHES = 0;
    public static final int FIREBASE_MATCH = 1;
    public static final int FIREBASE_WIDGET = 2;
    public static final int FIREBASE_NEWS= 3;
    public static final int FIREBASE_NEWS_ITEM = 4;
    public static final int FIREBASE_SHARE = 5;
    public static final int FIREBASE_NEWS_SIZE = 20;

// admob
    public static final int RANDOM_BASE_DEFAULT = 100;
    public static final int ADMOB_SHOW_THRESHOLD = 50;
    public static final int ADMOB_SHOW_DURATION = 15000;
    public static final int ADMOB_FADE_DURATION = 750;

    // team activity
    public static final String BUNDLE_INTENT_TEAM_ID = "bundle_intent_team_id";
    public static final String BUNDLE_TEAM_ID = "bundle_team_id";
    public static final int EMPTY_TEAM_ID = -1;

    // fragment_team
    public static final String FRAGMENT_TEAM_TAG = "fragment_team";


    // league activity
    public static final String BUNDLE_INTENT_LEAGUE_ID = "bundle_intent_league_id";
    public static final String BUNDLE_LEAGUE_ID = "bundle_league_id";
    public static final int EMPTY_LEAGUE_ID = -1;


    public static final String[] LEAGUE_CODES = new String[]{
            "BL1", "Germany", "1. Bundesliga",
            "BL2", "Germany", "2. Bundesliga",
            "BL3", "Germany", "3. Bundesliga",
            "DFB", "Germany", "Dfb-Cup",
            "PL", "England", "Premiere League",
            "EL1", "England", "League One",
            "ELC", "England", "Championship",
            "FAC", "England", "FA-Cup",
            "SA", "Italy", "Serie A",
            "SB", "Italy", "Serie B",
            "PD", "Spain", "Primera Division",
            "SD", "Spain", "Segunda Division",
            "CDR", "Spain", "Copa del Rey",
            "FL1", "France", "Ligue 1",
            "FL2", "France", "Ligue 2",
            "DED", "Netherlands", "Eredivisie",
            "PPL", "Portugal", "Primeira Liga",
            "GSL", "Greece", "Super League",
            "CL", "Europe", "Champions-League",
            "EL", "Europe", "UEFA-Cup",
            "EC", "Europe", "European-Cup of Nations",
            "WC", "World", "World-Cup"
    };

    private static final String[] TEAM_LOGOS = new String[]{

            "Logo_of_Beşiktaş_JK.svg",
            "https://upload.wikimedia.org/wikipedia/en/4/47/Besiktas_JK%27s_official_logo.png"
    };

//news

    public static String imageCheckReplaceURL(String imageURL) {
        for (int i = 0; i < TEAM_LOGOS.length; i += 2) {
            if (imageURL.contains(TEAM_LOGOS[i])) return TEAM_LOGOS[i + 1];

        }
        return imageURL;
    }

    /**
     * Span class used for RecyclerView as storage of display item parameters
     */
    public static class Span {
        /**
         * int number items of RecyclerView in width
         */
        private int spanX;
        /**
         * int number items of RecyclerView in height
         */
        private int spanY;
        /**
         * int width of RecyclerView item
         */
        private int width;
        /**
         * int height of RecyclerView item
         */
        private int height;

        /**
         * int width of screen
         */
        private int screenWidth;
        /**
         * int height of screen
         */
        private int screenHeight;


        /**
         * Constructor
         *
         * @param spanX  int number items of RecyclerView in width
         * @param spanY  int number items of RecyclerView in height
         * @param width  int width of RecyclerView item
         * @param height int height of RecyclerView item
         */
        public Span(int spanX, int spanY, int width, int height, int screenWidth, int screenHeight) {
            this.spanX = spanX;
            this.spanY = spanY;
            this.width = width;
            this.height = height;
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;

        }

        /**
         * Returns  span in width
         *
         * @return int span in width
         */
        public int getSpanX() {
            return spanX;
        }

        /**
         * Returns  span in height
         *
         * @return int span in height
         */
        public int getSpanY() {
            return spanY;
        }

        /**
         * Returns  width of RecyclerView item
         *
         * @return int width of RecyclerView item
         */
        public int getWidth() {
            return width;
        }

        /**
         * Returns  height of RecyclerView item
         *
         * @return int height of RecyclerView item
         */
        public int getHeight() {
            return height;
        }

        /**
         * Returns true if RecyclerView is smaller than screen
         *
         * @param nItems int number of items ofRecyclerView
         * @param isVert boolean true for VERTICAL scrolling mode
         * @return boolean true if RecyclerView is smaller than screen
         */
        public boolean isShowBar(int nItems, boolean isVert) {
            if (isVert) {
                return (Math.ceil((double) nItems / spanX)) * height < screenHeight;
            } else {
                return (Math.ceil((double) nItems / spanY)) * width < screenWidth;
            }
        }
    }

    /**
     * Returns double value from 0.0 to 1.0 the width between to guideline of ConstraintLayout
     * Used to get actual space occupied by RecyclerView
     *
     * @param context Context of calling activity
     * @param guideId int resource Id of guideline of ConstraintLayout
     * @return double value from 0.0 to 1.0 the width between to guideline of ConstraintLayout
     */
    private static double getPercent(AppCompatActivity context, int guideId) {
        return ((ConstraintLayout.LayoutParams)
                context.findViewById(guideId).getLayoutParams()).guidePercent;
    }

    /**
     * Returns  Span class object with number and size of items in width and height.
     * Used to build GridLayout for RecyclerView object.
     *
     * @param context Context of calling activity
     * @return Span class object with number and size of items in width and height.
     */
    public static Span getDisplayMetrics(AppCompatActivity context) {
        DisplayMetrics dp = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dp);
// tightened to layout
        double height_ratio = getPercent(context, R.id.guide_h2) - getPercent(context, R.id.guide_h1);
        double width_ratio = getPercent(context, R.id.guide_v2) - getPercent(context, R.id.guide_v1);

        double width = dp.widthPixels / dp.density * width_ratio;
        double height = dp.heightPixels / dp.density * height_ratio;  // real height

        int spanInWidth = (int) Math.round(width / HIGH_SCALE_WIDTH);
        int spanHeight = (int) (width * dp.density / spanInWidth / SCALE_RATIO_VERT);  // vertical only
        int spanInHeight = (int) Math.round(height / HIGH_SCALE_HEIGHT);
        int spanWidth = (int) (height * dp.density / spanInHeight * SCALE_RATIO_HORZ);  // horizontal only
        if (spanInWidth < MIN_SPAN) spanInWidth = MIN_SPAN;
        if (spanInHeight < MIN_SPAN) spanInHeight = MIN_SPAN;

        if (spanHeight < MIN_HEIGHT) spanHeight = MIN_HEIGHT;

        int minWidth = MIN_WIDTH;  // horizontal
        if (spanWidth < minWidth) spanWidth = minWidth;

// vertical
//        mSpan = spanInWidth;
//        mSpanHeight = spanHeight;
// horizontal
//        mSpan = spanInHeight;
//        mSpanWidth = spanWidth;

        return new Span(spanInWidth, spanInHeight, spanWidth, spanHeight, dp.widthPixels, dp.heightPixels);
    }

}
