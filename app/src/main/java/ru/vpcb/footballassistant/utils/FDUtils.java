package ru.vpcb.footballassistant.utils;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDFixtures;
import ru.vpcb.footballassistant.data.FDPlayer;
import ru.vpcb.footballassistant.data.FDPlayers;
import ru.vpcb.footballassistant.data.FDStanding;
import ru.vpcb.footballassistant.data.FDStandingGroup;
import ru.vpcb.footballassistant.data.FDTable;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.data.FDTeams;
import ru.vpcb.footballassistant.data.IFDRetrofitAPI;
import ru.vpcb.footballassistant.data.PostProcessingEnabler;
import ru.vpcb.footballassistant.dbase.FDContract;
import ru.vpcb.footballassistant.dbase.FDDbHelper;
import ru.vpcb.footballassistant.news.INDRetrofitAPI;
import ru.vpcb.footballassistant.news.NDArticle;
import ru.vpcb.footballassistant.news.NDNews;
import ru.vpcb.footballassistant.news.NDSource;
import ru.vpcb.footballassistant.news.NDSources;
import timber.log.Timber;

import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_ARTICLES;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_COMPETITIONS;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_CP_FIXTURES;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_CP_TEAMS;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_FIXTURES;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_SOURCES;
import static ru.vpcb.footballassistant.dbase.FDContract.MATCH_PARAMETERS_TEAMS;
import static ru.vpcb.footballassistant.utils.Config.ADMOB_SHOW_THRESHOLD;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DATE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;
import static ru.vpcb.footballassistant.utils.Config.FORMAT_MATCH_BET;
import static ru.vpcb.footballassistant.utils.Config.FORMAT_MATCH_SCORE;
import static ru.vpcb.footballassistant.utils.Config.FORMAT_MATCH_TIME_WIDGET;
import static ru.vpcb.footballassistant.utils.Config.EXCEPTION_CODE_7;
import static ru.vpcb.footballassistant.utils.Config.IMAGE_IDS;
import static ru.vpcb.footballassistant.utils.Config.LEAGUE_CODES;
import static ru.vpcb.footballassistant.utils.Config.ND_API_KEY;
import static ru.vpcb.footballassistant.utils.Config.ND_BASE_URI;
import static ru.vpcb.footballassistant.utils.Config.ND_CATEGORY_SPORT;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_DATE_SQLITE;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_DATE_SQLITE_ZERO_TIME;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_MATCH_DATE;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_MATCH_DATE_START;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_MATCH_DATE_WIDGET;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_MATCH_SCORE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_MATCH_TIME;
import static ru.vpcb.footballassistant.utils.Config.FD_BASE_URI;
import static ru.vpcb.footballassistant.utils.Config.FD_API_KEY;
import static ru.vpcb.footballassistant.utils.Config.LOAD_DB_DELAY;
import static ru.vpcb.footballassistant.utils.Config.EXCEPTION_CODE_4;
import static ru.vpcb.footballassistant.utils.Config.EXCEPTION_CODE_5;
import static ru.vpcb.footballassistant.utils.Config.EXCEPTION_CODE_6;
import static ru.vpcb.footballassistant.utils.Config.PATTERN_MATCH_TIME;
import static ru.vpcb.footballassistant.utils.Config.RANDOM_BASE_DEFAULT;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_A;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_B;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_C;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_D;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_E;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_F;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_G;
import static ru.vpcb.footballassistant.utils.Config.TABLE_GROUP_H;
import static ru.vpcb.footballassistant.utils.Config.UPDATE_SERVICE_PROGRESS;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 28-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */

public class FDUtils {
    //    private static final SimpleDateFormat formatDate =
//            new SimpleDateFormat(DATE_FULL_PATTERN, Locale.ENGLISH);
    private static final SimpleDateFormat formatMatchDateWidget =
            new SimpleDateFormat(PATTERN_MATCH_DATE_WIDGET, Locale.ENGLISH);
    private static final SimpleDateFormat formatMatchDate =
            new SimpleDateFormat(PATTERN_MATCH_DATE, Locale.ENGLISH);
    private static final SimpleDateFormat formatMatchTime =
            new SimpleDateFormat(PATTERN_MATCH_TIME, Locale.ENGLISH);
    private static final SimpleDateFormat formatMatchDateStart =
            new SimpleDateFormat(PATTERN_MATCH_DATE_START, Locale.ENGLISH);

    private static Random rnd = new Random();


    private static final SimpleDateFormat formatSQLiteDate =
            new SimpleDateFormat(PATTERN_DATE_SQLITE, Locale.ENGLISH);

    private static final SimpleDateFormat formatSQLiteDateZeroTime =
            new SimpleDateFormat(PATTERN_DATE_SQLITE_ZERO_TIME, Locale.ENGLISH);

    // maps
    public static boolean checkEmpty(Map<Integer, FDCompetition> map,
                                     Map<Integer, List<Integer>> mapTeamKeys,
                                     Map<Integer, FDTeam> mapTeams,
                                     Map<Integer, List<Integer>> mapFixtureKeys,
                                     Map<Integer, FDFixture> mapFixtures) {

        return map == null || map.isEmpty() ||
//                mapTeamKeys == null || mapTeamKeys.isEmpty() ||
//                mapFixtureKeys == null || mapFixtureKeys.isEmpty() ||
                mapTeams == null || mapTeams.isEmpty() ||
                mapFixtures == null || mapFixtures.isEmpty();

    }

    public static boolean checkEmpty(Map<String, NDSource> map,
                                     Map<String, List<NDArticle>> mapArticles) {
        return map == null || map.isEmpty() ||
                mapArticles == null || mapArticles.isEmpty();

    }

    // strings
    public static String formatString(int value) {
        return String.format(Locale.ENGLISH, "%d", value);
    }


    @SuppressWarnings("SameParameterValue")
    public static String formatStringDate(Date date, String delim) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        String day = String.format(Locale.ENGLISH, "%02d", c.get(Calendar.DAY_OF_MONTH));
        String month = String.format(Locale.ENGLISH, "%02d", c.get(Calendar.MONTH) + 1);
        String year = String.format(Locale.ENGLISH, "%04d", c.get(Calendar.YEAR));
        return "" + day + delim + month + delim + year.substring(2, year.length()) + "";
    }

    public static String formatStringDate(Context context, Calendar c) {

        if (c == null) return EMPTY_LONG_DATE;

        return context.getString(R.string.notification_time,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH),
                c.get(Calendar.YEAR), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }


    public static void showMessage(Context context, String s) {
        if (!(context instanceof Activity) || s == null || s.isEmpty()) return;

        try {
            if (isSnackbarStyle(context)) {
                Snackbar.make(((Activity) context).getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Timber.d(context.getString(R.string.notification_empty_activity_exception, e.getMessage()));
        }
    }

    // stings
    public static int formatHrefToId(String href) throws NumberFormatException {
        try {
            return Integer.valueOf(href.substring(href.lastIndexOf("/") + 1));
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            return EMPTY_INT_VALUE;
        }
    }

    public static int formatStringToId(String s) throws NumberFormatException {
        try {
            if (s == null || s.isEmpty()) return EMPTY_INT_VALUE;
            return Integer.valueOf(s);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            return EMPTY_INT_VALUE;
        }
    }


    public static String formatIntToString(int value) {
        if (value < 0) return EMPTY_LONG_DASH;

        return String.valueOf(value);
    }


    public static Calendar getCalendarFromSQLite(String s) {
        Date date = formatDateFromSQLite(s);
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }


    public static String formatMatchTimeWidget(String s) {
        if (s == null || s.isEmpty()) return EMPTY_MATCH_TIME;

        Calendar c = getCalendarFromSQLite(s);
        if (c == null) return EMPTY_MATCH_TIME;

        return String.format(Locale.ENGLISH, FORMAT_MATCH_TIME_WIDGET,
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    public static String formatMatchDateWidget(String s) {
        if (s == null || s.isEmpty()) return EMPTY_LONG_DASH;
        Date date = formatDateFromSQLite(s);
        if (date == null) return EMPTY_LONG_DASH;
        return formatMatchDateWidget.format(date);
    }

    public static String formatMatchDate(String s) {
        if (s == null || s.isEmpty()) return EMPTY_LONG_DASH;
        Date date = formatDateFromSQLite(s);
        if (date == null) return EMPTY_LONG_DASH;
        return formatMatchDate.format(date);
    }

    public static String formatMatchTime(String s) {
        if (s == null || s.isEmpty()) return EMPTY_LONG_DASH;
        Date date = formatDateFromSQLite(s);
        if (date == null) return EMPTY_LONG_DASH;
        return formatMatchTime.format(date);
    }

    public static String formatMatchDateStart(String s) {
        if (s == null || s.isEmpty()) return EMPTY_LONG_DASH;
        Date date = formatDateFromSQLite(s);
        if (date == null) return EMPTY_LONG_DASH;
        return formatMatchDateStart.format(date);
    }


    public static String formatMatchScore(int home, int away) {
        if (home < 0 || away < 0) return EMPTY_MATCH_SCORE;
        return String.format(Locale.ENGLISH, FORMAT_MATCH_SCORE, home, away);
    }

    // move to pattern SQLIte
    public static String formatDateToSQLite(String s) {
        if (s == null) return EMPTY_STRING;
        return s.replace("T", " ").replace("Z", "");
    }

    public static String formatFromInt(int value, String emptyString) {
        if (value < 0) return emptyString;
        return String.format(Locale.ENGLISH, "%d", value);
    }

    public static String formatMatchBet(double value) {
        if (value <= 0) return EMPTY_DASH;
        return String.format(Locale.ENGLISH, FORMAT_MATCH_BET, value);
    }

    // int
    public static boolean isShowAdMob() {

        return getRnd(RANDOM_BASE_DEFAULT) < ADMOB_SHOW_THRESHOLD;
    }

    @SuppressWarnings("SameParameterValue")
    public static int getRnd(int base) {
        if (rnd == null) rnd = new Random();
        if (base <= 0) base = RANDOM_BASE_DEFAULT;
        return rnd.nextInt(base);
    }


    // date and time
    public static Comparator<Pair<Long, Integer>> cPx = new Comparator<Pair<Long, Integer>>() {
        @Override
        public int compare(Pair<Long, Integer> o1, Pair<Long, Integer> o2) {
            if (o1 == null || o2 == null || o1.first == null || o2.first == null)
                throw new IllegalArgumentException();

            return o1.first.compareTo(o2.first);
        }
    };

    public static Comparator<FDFixture> cFx = new Comparator<FDFixture>() {
        @Override
        public int compare(FDFixture o1, FDFixture o2) {
            if (o1 == null || o2 == null ||
                    o1.getDate() == null || o2.getDate() == null)
                throw new IllegalArgumentException();

            Date dateO1 = FDUtils.formatDateFromSQLite(o1.getDate());
            Date dateO2 = FDUtils.formatDateFromSQLite(o2.getDate());
            if (dateO1 == null || dateO2 == null) throw new IllegalArgumentException();

            return dateO1.compareTo(dateO2);
        }
    };

    public static void setZeroTime(Calendar c) {
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    }


    public static void setDay(Calendar c, Date date) {
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    }


    public static Date formatDateFromSQLite(String s) {
        try {
            return formatSQLiteDate.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date formatDateFromSQLiteZeroTime(String s) {
        try {
            return formatSQLiteDateZeroTime.parse(s.substring(0, s.lastIndexOf(" ")));
        } catch (ParseException e) {
            return null;
        }
    }


    public static String formatDateToSQLite(Date date) {
        try {
            return formatSQLiteDate.format(date);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getCountry(String league) {
        if (league == null || league.isEmpty()) return EMPTY_STRING;

        for (int i = 0; i < Config.LEAGUE_CODES.length; i += 3) {
            if (league.equals(Config.LEAGUE_CODES[i])) return LEAGUE_CODES[i + 1];

        }
        return EMPTY_STRING;
    }

    // images
    public static int getImageBackId() {
        return IMAGE_IDS[rnd.nextInt(IMAGE_IDS.length)];
    }

    // shared preferences
    public static boolean isWebViewAction(Context context) {
        boolean default_value = context.getString(R.string.pref_webview_action_default).equals("true");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_webview_action_key), default_value);
    }


    private static boolean isSnackbarStyle(Context context) {
        boolean default_value = context.getString(R.string.pref_snackbar_default).equals("true");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_snackbar_key), default_value);
    }


    public static boolean getPrefBool(Context context, int keyId, int valueId) {
        Resources res = context.getResources();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(res.getString(keyId), res.getBoolean(valueId));
    }

    public static int getPrefString(Context context, int keyId, int defaultId) {
        Resources res = context.getResources();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String s = sp.getString(res.getString(keyId), res.getString(defaultId));
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void setRefreshTime(Context context) {
        Resources res = context.getResources();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(res.getString(R.string.pref_data_update_time_key), String.valueOf(FDUtils.currentTimeMinutes()));
        editor.apply();
    }

    public static boolean isFootballDataRefreshed(Context context) {
        int time = getPrefString(context,
                R.string.pref_data_update_time_key,
                R.string.pref_data_update_time_default);
        int delay = getPrefString(context,
                R.string.pref_data_delay_time_key,
                R.string.pref_data_delay_time_default);
        if (time < 0 || delay < 0) return false;

        return FDUtils.currentTimeMinutes() - time < delay;
    }

    public static void setNewsRefreshTime(Context context) {
        Resources res = context.getResources();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(res.getString(R.string.pref_news_update_time_key), String.valueOf(FDUtils.currentTimeMinutes()));
        editor.apply();
    }

    public static boolean isNewsDataRefreshed(Context context) {
        int time = getPrefString(context,
                R.string.pref_news_update_time_key,
                R.string.pref_news_update_time_default);
        int delay = getPrefString(context,
                R.string.pref_news_delay_time_key,
                R.string.pref_news_delay_time_default);

        if (time < 0 || delay < 0) return false;

        return FDUtils.currentTimeMinutes() - time < delay;
    }

    // data
//    private static boolean isRefreshed(Context context, Date lastRefresh) {
//        if (lastRefresh == null) return false;
//
//        long delay = TimeUnit.MINUTES.toMillis(
//                getPrefInt(context, R.string.pref_data_delay_time_key, R.integer.pref_data_delay_time_default));
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        long refreshTime = lastRefresh.getTime();
//
//        return currentTime - refreshTime < delay;
//    }


    // database
    public static Uri buildTableNameUri(String tableName) {
        return FDContract.BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
    }

    public static Uri buildItemIdUri(String tableName, long id) {
        return FDContract.BASE_CONTENT_URI.buildUpon().appendPath(tableName).appendPath(Long.toString(id)).build();
    }

    public static Uri buildItemIdUri(String tableName, String id) {
        return FDContract.BASE_CONTENT_URI.buildUpon().appendPath(tableName).appendPath(id).build();
    }


    public static Uri buildItemIdUri(String tableName, long id, long id2) {
        return FDContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(tableName)
                .appendPath(Long.toString(id))
                .appendPath(Long.toString(id2))
                .build();
    }

    public static Uri buildItemIdUri(String tableName, String id, String id2) {
        return FDContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(tableName)
                .appendPath(id)
                .appendPath(id2)
                .build();
    }


    public static int currentTimeMinutes() {
        return (int) TimeUnit.MILLISECONDS.toMinutes(Calendar.getInstance().getTimeInMillis());
    }

    private static Date currentTimeMinutesToDate() {
        return minutesToDate(currentTimeMinutes());
    }

    private static int dateToMinutes(Date date) {
        if (date == null) return 0;
        return (int) TimeUnit.MILLISECONDS.toMinutes(date.getTime());
    }

    public static Date minutesToDate(int time) {
        return new Date(TimeUnit.MINUTES.toMillis(time));
    }

    private static String formatLoad(Context context, int code, FDCompetition competition, String message) {
        int id = -1;
        if (competition != null) id = competition.getId();
        return context.getString(R.string.load_database, code, id, message);
    }

    @SuppressWarnings("SameParameterValue")
    private static String formatLoad(Context context, int code, FDTeam team, String message) {
        int id = -1;
        if (team == null) id = team.getId();
        return context.getString(R.string.load_database, code, id, message);
    }

    private static String formatLoad(Context context, int code, int id, String message) {
        return context.getString(R.string.load_database, code, id, message);
    }

    @SuppressWarnings("SameParameterValue")
    private static String formatLoad(Context context, int code, String id, String message) {
        return context.getString(R.string.load_database_news, code, id, message);
    }

    @SuppressWarnings("SameParameterValue")
    private static String formatWrite(Context context, int code, int id, String message) {
        return context.getString(R.string.write_database, code, id, message);
    }


    // data
    private static boolean setListTeams(FDCompetition competition,
                                        Map<Integer, List<Integer>> mapKeys,
                                        Map<Integer, FDTeam> mapTeams) {
        if (competition == null || competition.getId() <= 0 ||
                mapKeys == null || mapKeys.isEmpty() ||
                mapTeams == null || mapTeams.isEmpty()) return false;

        List<Integer> listKeys = mapKeys.get(competition.getId());
//        if (listKeys == null) return false;
        if (listKeys == null) return true;   // map does not have keys by source

        List<FDTeam> list = new ArrayList<>();
        for (Integer key : listKeys) {
            FDTeam team = mapTeams.get(key);
            if (team == null || team.getId() != key) continue;
            list.add(team);
        }
//        if (list == null || list.isEmpty()) return false;
        competition.setTeams(list);
        return true;
    }

    private static boolean setListFixtures(FDCompetition competition,
                                           Map<Integer, List<Integer>> mapKeys,
                                           Map<Integer, FDFixture> mapFixtures) {
        if (competition == null || competition.getId() <= 0 ||
                mapKeys == null || mapKeys.isEmpty() ||
                mapFixtures == null || mapFixtures.isEmpty()) return false;


        List<Integer> listKeys = mapKeys.get(competition.getId());
//        if (listKeys == null) return false;
        if (listKeys == null) return true;  // map does not have keys by source
        List<FDFixture> list = new ArrayList<>();
        for (Integer key : listKeys) {
            FDFixture fixture = mapFixtures.get(key);
            if (fixture == null || fixture.getId() != key) continue;
            list.add(fixture);
        }
//        if (list == null || list.isEmpty()) return false;
        competition.setFixtures(list);
        return true;
    }


    // competitions
    public static void getCompetitionTeams(Context context,
                                           Map<Integer, FDCompetition> competitions,
                                           Map<Integer, List<Integer>> mapKeys,
                                           Map<Integer, FDTeam> mapTeams) {

        for (FDCompetition competition : competitions.values()) {
            setListTeams(competition, mapKeys, mapTeams);
        }
    }

    public static void getCompetitionFixtures(Context context,
                                              Map<Integer, FDCompetition> competitions,
                                              Map<Integer, List<Integer>> mapKeys,
                                              Map<Integer, FDFixture> mapFixtures) {

        for (FDCompetition competition : competitions.values()) {
            setListFixtures(competition, mapKeys, mapFixtures);

        }
    }


    // read
    // news
    // sources
    private static NDSource readSource(Cursor cursor) {
        String id = cursor.getString(FDDbHelper.INsEntry.COLUMN_SOURCE_ID);
        if (id == null || id.isEmpty()) return null;


        String source_name = cursor.getString(FDDbHelper.INsEntry.COLUMN_SOURCE_NAME);
        String description = cursor.getString(FDDbHelper.INsEntry.COLUMN_DESCRIPTION);
        String sourceURL = cursor.getString(FDDbHelper.INsEntry.COLUMN_SOURCE_URL);
        String category = cursor.getString(FDDbHelper.INsEntry.COLUMN_SOURCE_CATEGORY);
        String language = cursor.getString(FDDbHelper.INsEntry.COLUMN_SOURCE_LANGUAGE);
        String country = cursor.getString(FDDbHelper.INsEntry.COLUMN_COUNTRY);

        return new NDSource(id, source_name, description, sourceURL,
                category, language, category, country);
    }

    public static Map<String, NDSource> readSources(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;

        Map<String, NDSource> map = new LinkedHashMap<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NDSource source = readSource(cursor);
            if (source == null) continue;
            map.put(source.getId(), source);
        }
        return map;
    }

    // sources
    public static Map<String, NDSource> readSources(Context context) {
        Uri uri = FDContract.NsEntry.CONTENT_URI;                               // вся таблица
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_SOURCES].getSortOrder();
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<String, NDSource> map = readSources(cursor);
        cursor.close();
        return map;
    }


    // articles
    private static NDArticle readArticle(Cursor cursor) {
        String id = cursor.getString(FDDbHelper.INaEntry.COLUMN_ARTICLE_ID);
        String source_id = cursor.getString(FDDbHelper.INaEntry.COLUMN_SOURCE_ID);
        String source_name = cursor.getString(FDDbHelper.INaEntry.COLUMN_SOURCE_NAME);
        String author = cursor.getString(FDDbHelper.INaEntry.COLUMN_AUTHOR);
        String title = cursor.getString(FDDbHelper.INaEntry.COLUMN_TITLE);
        String description = cursor.getString(FDDbHelper.INaEntry.COLUMN_DESCRIPTION);
        String articleURL = cursor.getString(FDDbHelper.INaEntry.COLUMN_ARTICLE_URL);
        String imageURL = cursor.getString(FDDbHelper.INaEntry.COLUMN_IMAGE_URL);
        String publishedAt = cursor.getString(FDDbHelper.INaEntry.COLUMN_PUBLISHED_AT);

        NDSource source = new NDSource(source_id, source_name, author);

        return new NDArticle(id, source, author,
                title, description, articleURL, imageURL, publishedAt);
    }

    public static Map<String, List<NDArticle>> readArticles(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;

        Map<String, List<NDArticle>> map = new HashMap<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NDArticle article = readArticle(cursor);
            if (article == null) continue;
            String key = article.getSource().getId();
            if (key == null) continue;
            List<NDArticle> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(article);

        }
        return map;
    }

    // articles
    public static Map<String, List<NDArticle>> readArticles(Context context) {
        Uri uri = FDContract.NaEntry.CONTENT_URI;                               // вся таблица
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_ARTICLES].getSortOrder();
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<String, List<NDArticle>> map = readArticles(cursor);
        cursor.close();
        return map;
    }


    // competitions
    public static Map<Integer, FDCompetition> readCompetitions(Context context) {
        Uri uri = FDContract.CpEntry.CONTENT_URI;                               // вся таблица
//        String sortOrder = FDContract.CpEntry.COLUMN_COMPETITION_ID + " ASC";   // sort by id
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_COMPETITIONS].getSortOrder();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, FDCompetition> map = readCompetitions(cursor);
        cursor.close();      // disables notifications
        return map;
    }

    // competition
    private static FDCompetition readCompetition(Cursor cursor) {
        int id = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_COMPETITION_ID);
        if (id <= 0) return null;

        String caption = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_CAPTION);
        String league = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_LEAGUE);
        String year = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_YEAR);
        int currentMatchDay = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_CURRENT_MATCHDAY);
        int numberOfMatchDays = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_MATCHDAYS);
        int numberOfTeams = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_TEAMS);
        int numberOfGames = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_GAMES);
        String lastUpdated = cursor.getString(FDDbHelper.ICpEntry.COLUMN_LAST_UPDATE);
// TODO Check Date Format

        return new FDCompetition(id, caption, league,
                year, currentMatchDay, numberOfMatchDays, numberOfTeams,
                numberOfGames, lastUpdated);
    }

    public static FDCompetition readCompetition(Context context, int id) {
        if (id <= 0) return null;
        Uri uri = FDContract.CpEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();                               // вся таблица
//        String sortOrder = FDContract.CpEntry.COLUMN_COMPETITION_ID + " ASC";   // sort by id
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_COMPETITIONS].getSortOrder();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        FDCompetition competition = readCompetition(cursor);
        cursor.close();
        return competition;
    }


    // competition_teams
    public static Map<Integer, List<Integer>> readCompetitionTeams(Context context) {
        Uri uri = FDContract.CpTmEntry.CONTENT_URI;                                     // all table
//        String sortOrder = FDContract.CpTmEntry.COLUMN_COMPETITION_ID + " ASC";          // sort by id
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_CP_TEAMS].getSortOrder();


        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, List<Integer>> map = readCompetitionTeams(cursor);
        cursor.close();     // disables notifications
        return map;
    }

    // teams
    public static Map<Integer, FDTeam> readTeams(Context context) {
        Uri uri = FDContract.TmEntry.CONTENT_URI;                               // вся таблица
//        String sortOrder = FDContract.TmEntry.COLUMN_TEAM_ID + " ASC";   // sort by id
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_TEAMS].getSortOrder();


        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, FDTeam> map = readTeams(cursor);
        cursor.close();     // disables notifications
        return map;
    }


    // fixtures
    // competition_fixture
    public static Map<Integer, List<Integer>> readCompetitionFixtures(Context context) {
        Uri uri = FDContract.CpFxEntry.CONTENT_URI;                                     // all table
//        String sortOrder = FDContract.CpFxEntry.COLUMN_COMPETITION_ID + " ASC";          // sort by id
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_CP_FIXTURES].getSortOrder();


        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, List<Integer>> map = readCompetitionFixtures(cursor);
        cursor.close();     // disables notifications
        return map;
    }

    // fixture
    public static Map<Integer, FDFixture> readFixtures(Context context) {
        Uri uri = FDContract.FxEntry.CONTENT_URI;                               // вся таблица
        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_FIXTURES].getSortOrder();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, FDFixture> map = readFixtures(cursor);
        cursor.close();     // disables notifications
        return map;
    }

    // fixture
    private static FDFixture readFixture(Cursor cursor) {
        int id = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_FIXTURE_ID);
        if (id <= 0) return null;

        int competitionId = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_COMPETITION_ID);
        int homeTeamId = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_TEAM_HOME_ID);
        int awayTeamId = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_TEAM_AWAY_ID);
        String date = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_DATE);
        String status = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_STATUS);
        int matchday = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_FIXTURE_MATCHDAY);
        String homeTeamName = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_TEAM_HOME);
        String awayTeamName = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_TEAM_AWAY);

        int goalsHomeTeam = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_FIXTURE_GOALS_HOME);
        int goalsAwayTeam = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_FIXTURE_GOALS_AWAY);
        double homeWin = cursor.getDouble(FDDbHelper.IFxEntry.COLUMN_FIXTURE_ODDS_WIN);
        double draw = cursor.getDouble(FDDbHelper.IFxEntry.COLUMN_FIXTURE_ODDS_DRAW);
        double awayWin = cursor.getDouble(FDDbHelper.IFxEntry.COLUMN_FIXTURE_ODDS_AWAY);
        boolean isFavorite = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_FAVORITES_STATE) != 0;
        boolean isNotified = cursor.getInt(FDDbHelper.IFxEntry.COLUMN_NOTIFICATION_STATE) != 0;
        String notificationId = cursor.getString(FDDbHelper.IFxEntry.COLUMN_NOTIFICATION_ID);
        String league = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_LEAGUE);
        String caption = cursor.getString(FDDbHelper.IFxEntry.COLUMN_FIXTURE_CAPTION);

        return new FDFixture(id, competitionId, homeTeamId, awayTeamId,
                date, status, matchday, homeTeamName, awayTeamName,
                goalsHomeTeam, goalsAwayTeam, homeWin, draw, awayWin,
                isFavorite, isNotified, notificationId, league, caption);
    }

    public static FDFixture readFixture(Context context, String id) {
        if (id == null || id.isEmpty()) return null;
        Uri uri = FDContract.FxEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();                               // вся таблица

        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_FIXTURES].getSortOrder();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        FDFixture fixture = readFixture(cursor);
        cursor.close();     // disables notifications
        return fixture;
    }

    public static FDFixture readFixture(Context context, int id) {
        if (id <= 0) return null;
        Uri uri = FDContract.FxEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();                               // вся таблица

        String sortOrder = FDContract.MATCH_PARAMETERS[MATCH_PARAMETERS_FIXTURES].getSortOrder();

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        FDFixture fixture = readFixture(cursor);
        cursor.close();     // disables notifications
        return fixture;
    }

    // read news
    public static void readDatabaseNews(Context context, Map<String, NDSource> map,
                                        Map<String, List<NDArticle>> mapArticles) {

        Map<String, NDSource> readMap = readSources(context);

        if (readMap != null && readMap.size() > 0) {
            map.clear();
            map.putAll(readMap);
        }

        Map<String, List<NDArticle>> readMapArticle = readArticles(context);

        if (readMapArticle != null && readMapArticle.size() > 0) {
            mapArticles.clear();
            mapArticles.putAll(readMapArticle);
        }
    }


    // read database
    public static void readDatabase(
            Context context, Map<Integer, FDCompetition> map,
            Map<Integer, List<Integer>> mapTeamKeys, Map<Integer, FDTeam> mapTeams,
            Map<Integer, List<Integer>> mapFixtureKeys, Map<Integer, FDFixture> mapFixtures) {


        Map<Integer, FDCompetition> readMap = readCompetitions(context);
        Map<Integer, List<Integer>> readMapTeamKeys = readCompetitionTeams(context);
        Map<Integer, FDTeam> readMapTeams = readTeams(context);
        Map<Integer, List<Integer>> readMapFixtureKeys = readCompetitionFixtures(context);
        Map<Integer, FDFixture> readMapFixtures = readFixtures(context);

        if (readMap != null && readMap.size() > 0) {
            map.clear();
            map.putAll(readMap);
        }

        if (readMapTeamKeys != null && readMapTeamKeys.size() > 0) {
            mapTeamKeys.clear();
            mapTeamKeys.putAll(readMapTeamKeys);
        }

        if (readMapTeams != null && readMapTeams.size() > 0) {
            mapTeams.clear();
            mapTeams.putAll(readMapTeams);
        }

        if (readMapFixtureKeys != null && readMapFixtureKeys.size() > 0) {
            mapFixtureKeys.clear();
            mapFixtureKeys.putAll(readMapFixtureKeys);
        }

        if (readMapFixtures != null && readMapFixtures.size() > 0) {
            mapFixtures.clear();
            mapFixtures.putAll(readMapFixtures);
        }
    }

    // read cursors
// read
// competitions
    public static Map<Integer, FDCompetition> readCompetitions(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;

        Map<Integer, FDCompetition> map = new HashMap<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_COMPETITION_ID);
            if (id <= 0) continue;

            String caption = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_CAPTION);
            String league = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_LEAGUE);
            String year = cursor.getString(FDDbHelper.ICpEntry.COLUMN_COMPETITION_YEAR);
            int currentMatchDay = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_CURRENT_MATCHDAY);
            int numberOfMatchDays = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_MATCHDAYS);
            int numberOfTeams = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_TEAMS);
            int numberOfGames = cursor.getInt(FDDbHelper.ICpEntry.COLUMN_NUMBER_GAMES);
            String lastUpdated = cursor.getString(FDDbHelper.ICpEntry.COLUMN_LAST_UPDATE);
// TODO Check Date Format
            FDCompetition competition = new FDCompetition(id, caption, league,
                    year, currentMatchDay, numberOfMatchDays, numberOfTeams,
                    numberOfGames, lastUpdated);

            map.put(competition.getId(), competition);
        }
//        cursor.close();  // notification support
        return map;
    }

    // competition_teams
    public static Map<Integer, List<Integer>> readCompetitionTeams(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, List<Integer>> map = new HashMap<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(FDDbHelper.ICpTmEntry.COLUMN_COMPETITION_ID);
            int id2 = cursor.getInt(FDDbHelper.ICpTmEntry.COLUMN_TEAM_ID);
            if (id <= 0 || id2 <= 0) continue;

            List<Integer> list = map.get(id);
            if (list == null) list = new ArrayList<>();
            list.add(id2);
            map.put(id, list);
        }
//        cursor.close(); // notification support
        return map;
    }

    // teams
    public static Map<Integer, FDTeam> readTeams(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, FDTeam> map = new HashMap<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(FDDbHelper.ITmEntry.COLUMN_TEAM_ID);
            if (id <= 0) continue;

            String name = cursor.getString(FDDbHelper.ITmEntry.COLUMN_TEAM_NAME);
            String code = cursor.getString(FDDbHelper.ITmEntry.COLUMN_TEAM_CODE);
            String shortName = cursor.getString(FDDbHelper.ITmEntry.COLUMN_TEAM_SHORT_NAME);
            String squadMarketValue = cursor.getString(FDDbHelper.ITmEntry.COLUMN_TEAM_MARKET_VALUE);
            String crestURL = cursor.getString(FDDbHelper.ITmEntry.COLUMN_TEAM_CREST_URI);


            FDTeam team = new FDTeam(id, name, code, shortName, squadMarketValue, crestURL);
            map.put(team.getId(), team);
        }
//        cursor.close();
        return map;
    }


    // fixtures
    // competition_fixture
    public static Map<Integer, List<Integer>> readCompetitionFixtures(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, List<Integer>> map = new HashMap<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(FDDbHelper.ICpFxEntry.COLUMN_COMPETITION_ID);
            int id2 = cursor.getInt(FDDbHelper.ICpFxEntry.COLUMN_FIXTURE_ID);
            if (id <= 0 || id2 <= 0) continue;

            List<Integer> list = map.get(id);
            if (list == null) list = new ArrayList<>();
            list.add(id2);
            map.put(id, list);
        }
//        cursor.close(); // notification support
        return map;
    }

    // fixture
    public static Map<Integer, FDFixture> readFixtures(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return null;
        Map<Integer, FDFixture> map = new LinkedHashMap<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            FDFixture fixture = readFixture(cursor);
            if (fixture == null) continue;
            map.put(fixture.getId(), fixture);
        }
        return map;
    }


    // write
    //    source
    public static ArrayList<ContentProviderOperation> writeSource(NDSource source, boolean forceDelete) {
        if (source == null || source.getId() == null || source.getId().isEmpty()) return null;

        Uri uri = buildItemIdUri(FDContract.NsEntry.TABLE_NAME, source.getId());
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        if (forceDelete) {
            operations.add(ContentProviderOperation.newDelete(uri).build());  // delete one record from Competitions table
        }

        ContentValues values = new ContentValues();

        values.put(FDContract.NsEntry.COLUMN_SOURCE_ID, source.getId());                        // string
        values.put(FDContract.NsEntry.COLUMN_SOURCE_NAME, source.getName());                    // string
        values.put(FDContract.NsEntry.COLUMN_DESCRIPTION, source.getDescription());             // string
        values.put(FDContract.NsEntry.COLUMN_SOURCE_URL, source.getUrl());                      // string
        values.put(FDContract.NsEntry.COLUMN_SOURCE_CATEGORY, source.getCategory());            // string
        values.put(FDContract.NsEntry.COLUMN_SOURCE_LANGUAGE, source.getLanguage());            // string
        values.put(FDContract.NsEntry.COLUMN_COUNTRY, source.getCountry());                     // string

        operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        return operations;
    }

    // source
    public static ContentProviderResult[] writeSource(Context context, NDSource source, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeSource(source, forceDelete));
    }

    // sources
    public static ContentProviderResult[] writeSources(
            Context context, Map<String, NDSource> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;
        Uri uri = FDContract.NsEntry.CONTENT_URI;
        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();
        if (forceDelete) {
            listOperations.add(ContentProviderOperation.newDelete(uri).build());    // delete all records from Competitions table
        }

        for (NDSource source : map.values()) {
            List<ContentProviderOperation> operations = writeSource(source, false); // if true table already cleared
            if (operations == null) continue;
            listOperations.addAll(operations);
        }

        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    //    article
    public static ArrayList<ContentProviderOperation> writeArticle(NDArticle article, boolean forceDelete) {
        if (article == null || article.getSource() == null ||
                article.getTitle() == null || article.getDescription() == null) return null;
        NDSource source = article.getSource();
        String sourceId = source.getId();
        String title = article.getTitle();
        String description = article.getDescription();
        String url = article.getUrl();
        String urlToImage = article.getUrlToImage();
        String publishedAt = article.getPublishedAt();

        if (sourceId == null || sourceId.isEmpty() || title == null || title.isEmpty() ||
                description == null || description.isEmpty() ||
                url == null || url.isEmpty() ||
                urlToImage == null || urlToImage.isEmpty() ||
                publishedAt == null || publishedAt.isEmpty()
                ) return null;

        String id = sourceId.toLowerCase() + "_" + title.toLowerCase();

        Uri uri = buildItemIdUri(FDContract.NaEntry.TABLE_NAME, id);
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        if (forceDelete) {
            operations.add(ContentProviderOperation.newDelete(uri).build());  // delete one record from Competitions table
        }

        ContentValues values = new ContentValues();

        values.put(FDContract.NaEntry.COLUMN_ARTICLE_ID, id);                               // string
        values.put(FDContract.NaEntry.COLUMN_SOURCE_ID, sourceId);                    // string
        values.put(FDContract.NaEntry.COLUMN_SOURCE_NAME, source.getName());                // string
        values.put(FDContract.NaEntry.COLUMN_AUTHOR, source.getAuthor());                   // string
        values.put(FDContract.NaEntry.COLUMN_TITLE, title);                    // string
        values.put(FDContract.NaEntry.COLUMN_DESCRIPTION, article.getDescription());        // string
        values.put(FDContract.NaEntry.COLUMN_ARTICLE_URL, url);                // string
        values.put(FDContract.NaEntry.COLUMN_IMAGE_URL, urlToImage);           // string
        values.put(FDContract.NaEntry.COLUMN_PUBLISHED_AT, article.getPublishedAt());       // string

        operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        return operations;
    }

    // article
    public static ContentProviderResult[] writeArticle(Context context, NDArticle article, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeArticle(article, forceDelete));
    }

    // articles
    public static ContentProviderResult[] writeArticles(
            Context context, Map<String, NDSource> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;
        Uri uri = FDContract.NaEntry.CONTENT_URI;
        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();
        if (forceDelete) {
            listOperations.add(ContentProviderOperation.newDelete(uri).build());    // delete all records from Competitions table
        }

        for (NDSource source : map.values()) {
            if (source == null) continue;
            List<NDArticle> list = source.getArticles();

            if (list == null || list.isEmpty()) continue;
            String sourceId = source.getId();
            for (NDArticle article : list) {
                if (article == null || article.getSource() == null) continue;
                article.setSource(sourceId);
                List<ContentProviderOperation> operations = writeArticle(article, false); // if true table already cleared
                if (operations == null) continue;
                listOperations.addAll(operations);
            }
        }
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // competition
    public static ArrayList<ContentProviderOperation> writeCompetition(
            FDCompetition competition, boolean forceDelete) {

        if (competition == null || competition.getId() <= 0) return null;

        Uri uri = buildItemIdUri(FDContract.CpEntry.TABLE_NAME, competition.getId());


        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        if (forceDelete) {
            operations.add(ContentProviderOperation.newDelete(uri).build());  // delete one record from Competitions table
        }

        ContentValues values = new ContentValues();
// TODO Check Date
        String lastUpdated = formatDateToSQLite(competition.getLastUpdated());

        values.put(FDContract.CpEntry.COLUMN_COMPETITION_ID, competition.getId());                  // int
        values.put(FDContract.CpEntry.COLUMN_COMPETITION_CAPTION, competition.getCaption());        // string
        values.put(FDContract.CpEntry.COLUMN_COMPETITION_LEAGUE, competition.getLeague());          // string
        values.put(FDContract.CpEntry.COLUMN_COMPETITION_YEAR, competition.getYear());              // string
        values.put(FDContract.CpEntry.COLUMN_CURRENT_MATCHDAY, competition.getCurrentMatchDay());   // int
        values.put(FDContract.CpEntry.COLUMN_NUMBER_MATCHDAYS, competition.getNumberOfMatchDays()); // int
        values.put(FDContract.CpEntry.COLUMN_NUMBER_TEAMS, competition.getNumberOfTeams());         // int
        values.put(FDContract.CpEntry.COLUMN_NUMBER_GAMES, competition.getNumberOfGames());         // int
        values.put(FDContract.CpEntry.COLUMN_LAST_UPDATE, lastUpdated);                             // string SQLite format
        operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());

        return operations;
    }

    // competition
    public static ContentProviderResult[] writeCompetition(Context context, FDCompetition
            competition, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeCompetition(competition, forceDelete));
    }

    // competitions
    public static ContentProviderResult[] writeCompetitions(
            Context context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;
        Uri uri = FDContract.CpEntry.CONTENT_URI;
        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();
        if (forceDelete) {
            listOperations.add(ContentProviderOperation.newDelete(uri).build());    // delete all records from Competitions table
        }

        for (FDCompetition competition : map.values()) {
            List<ContentProviderOperation> operations = writeCompetition(competition, false);
            if (operations == null) continue;
            listOperations.addAll(operations);
        }

        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // competition_team
    public static ArrayList<ContentProviderOperation> writeCompetitionTeams(FDCompetition competition) {

        int refreshTime = (int) (TimeUnit.MILLISECONDS.toMinutes(Calendar.getInstance().getTime().getTime()));
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        // must be deleted because of ids are not keys
        Uri uri = buildItemIdUri(FDContract.CpTmEntry.TABLE_NAME, competition.getId());
        operations.add(ContentProviderOperation.newDelete(uri).build());  // delete all records for Competition from table


        for (FDTeam team : competition.getTeams()) {
            if (team == null || team.getId() <= 0) continue;
            uri = buildItemIdUri(FDContract.CpTmEntry.TABLE_NAME, competition.getId(), team.getId());

            ContentValues values = new ContentValues();
            values.put(FDContract.CpTmEntry.COLUMN_COMPETITION_ID, competition.getId());            // int
            values.put(FDContract.CpTmEntry.COLUMN_TEAM_ID, team.getId());                          // int
            operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        }
        return operations;
    }

    public static ContentProviderResult[] writeCompetitionTeams(Context context, FDCompetition
            competition,
                                                                boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeCompetitionTeams(competition));
    }

    public static ContentProviderResult[] writeCompetitionTeams(
            Context context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;

        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

        if (forceDelete) {
            Uri uri = FDContract.CpTmEntry.CONTENT_URI;
            listOperations.add(ContentProviderOperation.newDelete(uri).build());
        }

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0 ||
                    competition.getTeams() == null) continue;
            List<ContentProviderOperation> operations = writeCompetitionTeams(competition);
            if (operations == null) continue;
            listOperations.addAll(operations);

        }
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // teams
    // team
    public static ArrayList<ContentProviderOperation> writeTeam(FDTeam team, boolean forceDelete) {

        if (team == null || team.getId() <= 0) return null;
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        Uri uri = buildItemIdUri(FDContract.TmEntry.TABLE_NAME, team.getId());
        int refreshTime = (int) (TimeUnit.MILLISECONDS.toMinutes(Calendar.getInstance().getTime().getTime()));

        if (forceDelete) { // force clear Teams table
            operations.add(ContentProviderOperation.newDelete(uri).build());
        }

        ContentValues values = new ContentValues();
        values.put(FDContract.TmEntry.COLUMN_TEAM_ID, team.getId());                                // int
        values.put(FDContract.TmEntry.COLUMN_TEAM_NAME, team.getName());                            // string
        values.put(FDContract.TmEntry.COLUMN_TEAM_CODE, team.getCode());                            // string
        values.put(FDContract.TmEntry.COLUMN_TEAM_SHORT_NAME, team.getShortName());                 // string
        values.put(FDContract.TmEntry.COLUMN_TEAM_MARKET_VALUE, team.getSquadMarketValue());        // string
        values.put(FDContract.TmEntry.COLUMN_TEAM_CREST_URI, team.getCrestURL());                   // string

        operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        return operations;
    }

    // team
    public static ContentProviderResult[] writeTeam(Context context, FDTeam team,
                                                    boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeTeam(team, forceDelete));
    }

    // teams
    public static ContentProviderResult[] writeTeams(Context
                                                             context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;


        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

        if (forceDelete) {                                          // force clear Teams table
            Uri uri = FDContract.TmEntry.CONTENT_URI;
            listOperations.add(ContentProviderOperation.newDelete(uri).build());
        }

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0) continue;
            List<FDTeam> teams = competition.getTeams();
            if (teams == null || teams.size() == 0) continue;
            for (FDTeam team : teams) {
                List<ContentProviderOperation> operations = writeTeam(team, false);
                if (operations == null) continue;
                listOperations.addAll(operations);
            }
        }
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // competition fixture
    public static ArrayList<ContentProviderOperation> writeCompetitionFixtures(
            FDCompetition competition) {


        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        // all fixtures of given competition  must be deleted because ids are not key
        Uri uri = buildItemIdUri(FDContract.CpFxEntry.TABLE_NAME, competition.getId());
        operations.add(ContentProviderOperation.newDelete(uri).build());  // delete all records for Competition from table


        for (FDFixture fixture : competition.getFixtures()) {
            if (fixture == null || fixture.getId() <= 0) continue;
            uri = buildItemIdUri(FDContract.CpFxEntry.TABLE_NAME, competition.getId(), fixture.getId());

            ContentValues values = new ContentValues();
            values.put(FDContract.CpFxEntry.COLUMN_COMPETITION_ID, competition.getId());            // int
            values.put(FDContract.CpFxEntry.COLUMN_FIXTURE_ID, fixture.getId());                    // int
            operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        }
        return operations;
    }

    public static ContentProviderResult[] writeCompetitionFixtures(
            Context context, FDCompetition competition, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, writeCompetitionTeams(competition));
    }

    public static ContentProviderResult[] writeCompetitionFixtures(
            Context context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;

        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

        if (forceDelete) {
            Uri uri = FDContract.CpFxEntry.CONTENT_URI;
            listOperations.add(ContentProviderOperation.newDelete(uri).build());
        }

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0 || competition.getFixtures() == null)
                continue;
            List<ContentProviderOperation> operations = writeCompetitionFixtures(competition);
            if (operations == null) continue;
            listOperations.addAll(operations);

        }
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // fixture
    public static ArrayList<ContentProviderOperation> writeFixture(FDFixture fixture,
                                                                   FDCompetition competition, boolean forceDelete) {

        if (fixture == null || fixture.getId() <= 0) return null;
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        Uri uri = buildItemIdUri(FDContract.FxEntry.TABLE_NAME, fixture.getId());
        if (forceDelete) { // force clear fixture from table
            operations.add(ContentProviderOperation.newDelete(uri).build());
        }
        ContentValues values = new ContentValues();
// TODO Check SQLite Date Format
        String fixtureDate = formatDateToSQLite(fixture.getDate());

        values.put(FDContract.FxEntry.COLUMN_FIXTURE_ID, fixture.getId());                          // int
        values.put(FDContract.FxEntry.COLUMN_COMPETITION_ID, fixture.getCompetitionId());           // int
        values.put(FDContract.FxEntry.COLUMN_TEAM_HOME_ID, fixture.getHomeTeamId());                // int
        values.put(FDContract.FxEntry.COLUMN_TEAM_AWAY_ID, fixture.getAwayTeamId());                // int
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_DATE, fixtureDate);                            // string date SQLite format
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_STATUS, fixture.getStatus());                  // string
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_MATCHDAY, fixture.getMatchDay());              // string
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_TEAM_HOME, fixture.getHomeTeamName());         // string
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_TEAM_AWAY, fixture.getAwayTeamName());         // string
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_GOALS_HOME, fixture.getGoalsHome());           // int
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_GOALS_AWAY, fixture.getGoalsAway());           // int
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_ODDS_WIN, fixture.getHomeWin());               // double
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_ODDS_DRAW, fixture.getDraw());                 // double
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_ODDS_AWAY, fixture.getAwayWin());              // double
//        values.put(FDContract.FxEntry.COLUMN_FAVORITES_STATE, fixture.isFavorite() ? 1 : 0);        // int
//        values.put(FDContract.FxEntry.COLUMN_NOTIFICATION_STATE, fixture.isNotified() ? 1 : 0);     // int
//        values.put(FDContract.FxEntry.COLUMN_NOTIFICATION_ID, fixture.getNotificationId());         // int
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_LEAGUE, competition.getLeague());              // string
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_CAPTION, competition.getCaption());          // string

        operations.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
        return operations;
    }

    // fixture
    public static ArrayList<ContentProviderOperation> updateFixtureProjection(FDFixture fixture) {

        if (fixture == null || fixture.getId() <= 0) return null;
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        Uri uri = buildItemIdUri(FDContract.FxEntry.TABLE_NAME, fixture.getId());
        ContentValues values = new ContentValues();
        values.put(FDContract.FxEntry.COLUMN_FIXTURE_ID, fixture.getId());                          // int
        values.put(FDContract.FxEntry.COLUMN_FAVORITES_STATE, fixture.isFavorite() ? 1 : 0);        // int
        values.put(FDContract.FxEntry.COLUMN_NOTIFICATION_STATE, fixture.isNotified() ? 1 : 0);     // int
        values.put(FDContract.FxEntry.COLUMN_NOTIFICATION_ID, fixture.getNotificationId());         // string
        operations.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
        return operations;
    }

    // not insert, just update
    @SuppressWarnings("SameParameterValue")
    public static void updateFixtureProjection(Context context, FDFixture fixture,
                                               boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, updateFixtureProjection(fixture));
    }

    // teams
    public static void writeFixtures(
            Context context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return ;

        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

        if (forceDelete) {                                          // force clear Teams table
            Uri uri = FDContract.FxEntry.CONTENT_URI;
            listOperations.add(ContentProviderOperation.newDelete(uri).build());
        }

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0) continue;
            List<FDFixture> fixtures = competition.getFixtures();
            if (fixtures == null || fixtures.size() == 0) continue;
            for (FDFixture fixture : fixtures) {
                List<ContentProviderOperation> operations = writeFixture(fixture, competition, forceDelete);
                if (operations == null) continue;
                listOperations.addAll(operations);
            }
        }
         context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }

    // write database
// sources
    @SuppressWarnings("SameParameterValue")
    public static void writeDatabaseSources(Context context, Map<String, NDSource> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        writeSources(context, map, forceDelete);
    }

    // articles
    @SuppressWarnings("SameParameterValue")
    public static void writeDatabaseArticles(Context context, Map<String, NDSource> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {
        writeArticles(context, map, forceDelete);
    }

    // news
    public static void writeDatabaseNews(Context context, Map<String, NDSource> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        writeSources(context, map, forceDelete);
        writeArticles(context, map, forceDelete);

    }

// competitions

    @SuppressWarnings("SameParameterValue")
    public static void writeDatabase(Context context, Map<Integer, FDCompetition> map, boolean forceDelete)
            throws OperationApplicationException, RemoteException {

        double progress = UPDATE_SERVICE_PROGRESS * 0.8;
        double step = 4;
        sendProgress(context, (int) progress);
        writeCompetitions(context, map, forceDelete);
        progress += step;
        sendProgress(context, (int) progress);
        writeCompetitionTeams(context, map, forceDelete);
        progress += step / 2;
        sendProgress(context, (int) progress);
        writeTeams(context, map, forceDelete);
        progress += step;
        sendProgress(context, (int) progress);
        writeCompetitionFixtures(context, map, forceDelete);
        progress += step / 2;
        sendProgress(context, (int) progress);
        writeFixtures(context, map, forceDelete);

    }

    // write long term data
    // table
    private static ContentProviderOperation writeTableRecord(
            FDStanding standing, int id, int matchDay, String caption, String group, Uri uri)
            throws OperationApplicationException, RemoteException {

// id = competitionId * 10000 + teamId
        ContentValues values = new ContentValues();
        values.put(FDContract.TbEntry.COLUMN_COMPETITION_ID, id);                                   // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_ID, standing.getId());                            // int
        values.put(FDContract.TbEntry.COLUMN_GROUP_NAME, group);                                     // string
        values.put(FDContract.TbEntry.COLUMN_COMPETITION_MATCHDAY, matchDay);                       // int
        values.put(FDContract.TbEntry.COLUMN_LEAGUE_CAPTION, caption);                              // string
        values.put(FDContract.TbEntry.COLUMN_TEAM_POSITION, standing.getPosition());                // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_NAME, standing.getTeamName());                    // string
        values.put(FDContract.TbEntry.COLUMN_CREST_URI, standing.getCrestURI());                    // string
        values.put(FDContract.TbEntry.COLUMN_TEAM_POINTS, standing.getPoints());                    // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_GOALS, standing.getGoals());                      // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_GOALS_AGAINST, standing.getGoalsAgainst());       // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_GOALS_DIFFERENCE, standing.getGoalDifference());  // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_WINS, standing.getWins());                        // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_DRAWS, standing.getDraws());                      // int
        values.put(FDContract.TbEntry.COLUMN_TEAM_LOSSES, standing.getLosses());                    // int

        return ContentProviderOperation.newInsert(uri).withValues(values).build();
    }

    public static List<ContentProviderOperation> writeTableRecords(
            Context context, List<FDStanding> standings, FDTable table, String group, Uri uri)
            throws OperationApplicationException, RemoteException {

        List<ContentProviderOperation> operations = new ArrayList<>();
        int id = table.getId();
        int matchDay = table.getMatchDay();
        String caption = table.getLeagueCaption();

        if (standings == null) return operations;

        for (FDStanding standing : standings) {
            try {
                standing.setId();
                ContentProviderOperation op = writeTableRecord(standing, id, matchDay, caption, group, uri);
                operations.add(op);

            } catch (NullPointerException | NumberFormatException e) {
                Timber.d(formatWrite(context, EXCEPTION_CODE_7, id, e.getMessage()));
            }
        }

        return operations;
    }

    public static ArrayList<ContentProviderOperation> writeTable(Context context, FDTable table)
            throws OperationApplicationException, RemoteException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        if (table == null || table.getId() <= 0) return operations;
        Uri uri = buildItemIdUri(FDContract.TbEntry.TABLE_NAME, table.getId());

//          // force clear table for competition
//            Uri uri = buildItemIdUri(FDContract.TbEntry.TABLE_NAME, table.getId());
//            operations.add(ContentProviderOperation.newDelete(uri).build());

        List<FDStanding> standings = table.getStanding();
        FDStandingGroup standingGroup = table.getStandings();

        if (standings != null) {
            operations.addAll(writeTableRecords(context, standings, table, EMPTY_STRING, uri));
        }

        if (standingGroup != null) {
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_A, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_B, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_C, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_D, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_E, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_F, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_G, uri));
            operations.addAll(writeTableRecords(context, standingGroup.getGroupA(), table, TABLE_GROUP_H, uri));
        }

        return operations;
    }

    // tables
    public static ContentProviderResult[] writeTables(Context context, Map<Integer, FDCompetition> map)
            throws OperationApplicationException, RemoteException {

        if (map == null || map.size() == 0) return null;
        ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

        Uri uri = FDContract.TbEntry.CONTENT_URI;
        listOperations.add(ContentProviderOperation.newDelete(uri).build());  // clear because _ID autoincrement

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0) continue;
            FDTable table = competition.getTable();

            List<ContentProviderOperation> operations = writeTable(context, table);
            if (operations == null) continue;
            listOperations.addAll(operations);

        }
        return context.getContentResolver().applyBatch(FDContract.CONTENT_AUTHORITY, listOperations);
    }


// connection

    /**
     * Returns status of connection to network
     *
     * @param context Context of calling activity
     * @return boolean status of connection, true if connected, false if not
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // network
    private static IFDRetrofitAPI setupRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("X-Auth-Token", FD_API_KEY)//BuildConfig.FD_API_KEY)
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(FD_BASE_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(IFDRetrofitAPI.class);
    }

    private static INDRetrofitAPI setupRetrofitNews() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new PostProcessingEnabler())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ND_BASE_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(INDRetrofitAPI.class);
    }

//    private static INDRetrofitAPI setupRetrofitNews() {
//logging
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);     // set your desired log level  NONE, BASIC, HEADERS, BODY
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);  // <-- this is the important line!
//
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new PostProcessingEnabler())
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ND_BASE_URI)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(httpClient.build())
//                .build();
//        return retrofit.create(INDRetrofitAPI.class);
//    }

    // news
    private static NDSources loadListSources()
            throws NullPointerException, IOException {

        INDRetrofitAPI retrofitAPI = setupRetrofitNews();
        return retrofitAPI.getSources(ND_CATEGORY_SPORT, ND_API_KEY).execute().body();
    }

    @SuppressWarnings("SameParameterValue")
    private static NDNews loadListArticles(NDSource source, int page)
            throws NullPointerException, IOException {
        if (source == null || page <= 0) return null;
        String id = source.getId();
        if (id == null || id.isEmpty()) return null;

        INDRetrofitAPI retrofitAPI = setupRetrofitNews();
        return retrofitAPI.getEverything(id, Integer.toString(page), ND_API_KEY).execute().body();
    }

    // competitions
    private static List<FDCompetition> loadListCompetitions()
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getCompetitions().execute().body();
    }

    // competitions
    private List<FDCompetition> loadListCompetitions(String season)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getCompetitions(season).execute().body();
    }

    // teams
    private static FDTeams loadListTeams(String competition)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getTeams(competition).execute().body();
    }

    // fixtures
    private static FDFixtures loadListFixtures(String competition)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getFixtures(competition).execute().body();
    }

    // table
    private static FDTable loadTable(String competition)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getTable(competition).execute().body();
    }

    // players
    private static FDPlayers loadListPlayers(String team)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getTeamPlayers(team).execute().body();
    }

    private static FDFixtures loadListTeamFixtures(String team)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getTeamFixtures(team).execute().body();
    }

    private static FDPlayers loadListTeamPlayers(String team)
            throws NullPointerException, IOException {

        IFDRetrofitAPI retrofitAPI = setupRetrofit();
        return retrofitAPI.getTeamPlayers(team).execute().body();
    }


// load
// competitions

// all maps read by loaders

    // news
    public static boolean loadDatabaseSources(Context context, Map<String, NDSource> map)
            throws NullPointerException, IOException {

// load map
        map.clear();
        NDSources sources = loadListSources();  // NullPointerException, IOException
        if (sources == null || sources.getSources() == null) return false;
        List<NDSource> list = sources.getSources();

        for (NDSource source : list) {
            if (source == null || source.getId() == null || source.getId().isEmpty()) continue;
            map.put(source.getId(), source);
        }
        return true;
    }

    public static boolean loadDatabaseArticles(Context context, Map<String, NDSource> map)
            throws NullPointerException, IOException {
// load map
        if (map.isEmpty()) {
            loadDatabaseSources(context, map);
        }

        for (NDSource source : map.values()) {
            if (source == null || source.getId() == null || source.getId().isEmpty()) continue;
// articles
            try {
                NDNews news = loadListArticles(source, 1);                    // load page 1
                if (news == null || news.getArticles() == null || news.getArticles().isEmpty())
                    continue;
                source.setArticles(news.getArticles());
            } catch (NullPointerException | NumberFormatException | IOException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_4, source.getId(), e.getMessage()));
            }
        }
        return true;
    }

    public static boolean loadDatabaseNews(Context context, Map<String, NDSource> map) throws NullPointerException, IOException {
// load map
        map.clear();
// sources
        NDSources sources = loadListSources();  // NullPointerException, IOException
        if (sources == null || sources.getSources() == null) return false;
        List<NDSource> list = sources.getSources();

        for (NDSource source : list) {
            if (source == null || source.getId() == null || source.getId().isEmpty()) continue;
            map.put(source.getId(), source);
        }


        for (NDSource source : map.values()) {
            if (source == null || source.getId() == null || source.getId().isEmpty()) continue;
// articles
            try {
                NDNews news = loadListArticles(source, 1);                    // load page 1
                if (news == null || news.getArticles() == null || news.getArticles().isEmpty())
                    continue;
                source.setArticles(news.getArticles());
            } catch (NullPointerException | NumberFormatException | IOException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_4, source.getId(), e.getMessage()));
            }
        }
        return true;
    }


    // load competitions
    public static boolean loadDatabase(
            Context context, Map<Integer, FDCompetition> map,
            Map<Integer, List<Integer>> mapTeamKeys, Map<Integer, FDTeam> mapTeams,
            Map<Integer, List<Integer>> mapFixtureKeys, Map<Integer, FDFixture> mapFixtures
    ) throws NullPointerException, IOException {

// progress
        double step;
        double progress = 0;

// load map
        map.clear();
        List<FDCompetition> list = loadListCompetitions();  // NullPointerException, IOException

        for (FDCompetition competition : list) {
            if (competition == null || competition.getId() <= 0) continue;
            map.put(competition.getId(), competition);
        }

        step = UPDATE_SERVICE_PROGRESS * 0.8 / (map.size() + 1); // 1 + t.map.size + f.map.size
        progress = step;
        sendProgress(context, (int) progress);  // +1

        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0) continue;
// teams
            try {
                List<FDTeam> teams = loadListTeams(competition);                    // load
                competition.setTeams(teams);
            } catch (NullPointerException | NumberFormatException | IOException | InterruptedException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_4, competition, e.getMessage()));
            }
// fixtures
            try {
                List<FDFixture> fixtures = loadListFixtures(competition);           // load
                competition.setFixtures(fixtures);

            } catch (NullPointerException | NumberFormatException | IOException | InterruptedException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_5, competition, e.getMessage()));
            }
// progress
            progress += step;
            sendProgress(context, (int) progress);// t,f
        }
        return true;
    }

    // teams
    // list from competition
    private static List<FDTeam> loadListTeams(FDCompetition competition)
            throws NumberFormatException, NullPointerException, IOException, InterruptedException {
        if (competition == null || competition.getId() <= 0) return null;

        String id = formatString(competition.getId());

        FDTeams teams = loadListTeams(id);                   // NullPointerException
//        if (teams == null) {
// test!!!
//            Thread.sleep(100);
//            teams = loadListTeams(id); // second trial
//        }
        List<FDTeam> list = new ArrayList<>();
        for (FDTeam team : teams.getTeams()) {
            if (team == null) continue;
            list.add(team);
        }
        return list;
    }

    // list from competition
    private static List<FDTeam> getListTeams(
            Context context, FDCompetition competition, boolean forceUpdate)
            throws NumberFormatException, NullPointerException, IOException {
        if (competition == null || competition.getId() <= 0) return null;
// smart update check
        List<FDTeam> list = competition.getTeams();
        if (!forceUpdate && list != null) {  // check smart update
            return list;
        }
// no teams
        String id = formatString(competition.getId());
        FDTeams teams = loadListTeams(id);      // NullPointerException
        list = new ArrayList<>();
        for (FDTeam team : teams.getTeams()) {
            try {
                team.setId();
            } catch (NullPointerException | NumberFormatException e) {
                continue;
            }
            list.add(team);
        }
        return list;
    }

    // map from competition
    private static Map<Integer, FDTeam> getTeams(
            Context context, FDCompetition competition, boolean forceUpdate)
            throws NumberFormatException, NullPointerException, IOException {
        Map<Integer, FDTeam> map = new HashMap<>();
        List<FDTeam> teams = getListTeams(context, competition, forceUpdate);
        competition.setTeams(teams);                        // NullPointerException
        for (FDTeam team : teams) {
            map.put(team.getId(), team);
        }
        return map;
    }

    // map from competitions
    public static Map<Integer, FDTeam> getTeams(
            Context context, Map<Integer, FDCompetition> competitions, boolean forceUpdate) {
        if (competitions == null) return null;

        Map<Integer, FDTeam> map = new HashMap<>();
        for (FDCompetition competition : competitions.values()) {
            try {
                map.putAll(getTeams(context, competition, forceUpdate));
            } catch (NumberFormatException | NullPointerException | IOException e) {
                Timber.d(context.getString(R.string.get_competitions_teams_null, e.getMessage()));
            }
        }
        return map;
    }


    // list from competition
    private static List<FDFixture> loadListFixtures(FDCompetition competition)
            throws NumberFormatException, NullPointerException, IOException, InterruptedException {
        if (competition == null || competition.getId() <= 0) return null;

        int competitionId = competition.getId();
        String id = formatString(competitionId);
        String caption = competition.getCaption();

        FDFixtures fixtures = loadListFixtures(id);      // NullPointerException

// TODO check for repetitions of load
        if (fixtures == null) {
// test!!!
//            Thread.sleep(100);
//            fixtures = loadListFixtures(id); // second trial
        }
        List<FDFixture> list = new ArrayList<>();
        for (FDFixture fixture : fixtures.getFixtures()) {
            try {
                fixture.setId();
            } catch (NullPointerException | NumberFormatException e) {
                continue;
            }
            list.add(fixture);
        }
        return list;
    }

    // load competitions
    public static boolean loadCompetitions(
            Map<Integer, FDCompetition> map,
            Map<Integer, List<Integer>> mapTeamKeys, Map<Integer, FDTeam> mapTeams,
            Map<Integer, List<Integer>> mapFixtureKeys, Map<Integer, FDFixture> mapFixtures) {

        if (map == null || map.size() == 0 ||
                mapTeamKeys == null || mapTeamKeys.isEmpty() ||
                mapTeams == null || mapTeams.isEmpty() ||
                mapFixtureKeys == null || mapFixtureKeys.isEmpty() ||
                mapFixtures == null || mapFixtures.isEmpty()) return false;

// load map
        for (FDCompetition competition : map.values()) {
            if (competition == null || competition.getId() <= 0) continue;
// teams
            if (competition.getTeams() == null) {
                setListTeams(competition, mapTeamKeys, mapTeams);    // restore teams from keys
            }
// fixtures
            if (competition.getFixtures() == null) {
                setListFixtures(competition, mapFixtureKeys, mapFixtures);
            }
        }
        return true;
    }


    // load fixtures from team
    private static List<FDFixture> loadListTeamFixtures(int teamId)
            throws NumberFormatException, NullPointerException, IOException, InterruptedException {
        if (teamId <= 0) return null;

        String id = formatString(teamId);
        long lastRefresh = Calendar.getInstance().getTimeInMillis();
        FDFixtures fixtures = loadListTeamFixtures(id);      // NullPointerException
        List<FDFixture> list = new ArrayList<>();
        for (FDFixture fixture : fixtures.getFixtures()) {
            try {
                fixture.setId();
            } catch (NullPointerException | NumberFormatException e) {
                continue;
            }
            list.add(fixture);
        }
        return list;
    }

    public static List<FDFixture> loadListTeamFixtures(Context context, int id) {
        try {
            return loadListTeamFixtures(id);
        } catch (NumberFormatException | NullPointerException | IOException | InterruptedException e) {
            Timber.d(context.getString(R.string.retrofit_response_empty), e.getMessage());
            return null;
        }
    }

    // load players from team
    private static List<FDPlayer> loadListTeamPlayers(int teamId)
            throws NumberFormatException, NullPointerException, IOException, InterruptedException {
        if (teamId <= 0) return null;

        String id = formatString(teamId);

        FDPlayers players = loadListTeamPlayers(id);      // NullPointerException
        List<FDPlayer> list = new ArrayList<>();
        for (FDPlayer player : players.getPlayers()) {
            if (player == null) continue;
            list.add(player);
        }
        return list;
    }

    public static List<FDPlayer> loadListTeamPlayers(Context context, int teamId) {
        try {
            return loadListTeamPlayers(teamId);
        } catch (NumberFormatException | NullPointerException | IOException | InterruptedException e) {
            Timber.d(context.getString(R.string.retrofit_response_empty), e.getMessage());
            return null;
        }
    }


    public static void sendProgress(Context context, int value) {
        if (value < 0) return;
        if (value > UPDATE_SERVICE_PROGRESS) value = UPDATE_SERVICE_PROGRESS;

        context.sendBroadcast(new Intent(context.getString(R.string.broadcast_data_update_progress))
                .putExtra(context.getString(R.string.extra_progress_counter), value));

    }

    // load long term
// load tables
    public static boolean loadTablesLongTerm(Context context, Map<Integer, FDCompetition> map,
                                             double progress, double step)
            throws NullPointerException, IOException {
        if (map == null || map.isEmpty()) return false;


        for (FDCompetition competition : map.values()) {

            if (competition == null || competition.getId() <= 0) continue;
            int competitionId = competition.getId();
            String id = formatString(competitionId);
// tables
            try {
                FDTable table = loadTable(id);
                table.setId();
                competition.setTable(table);
                Thread.sleep(LOAD_DB_DELAY);

            } catch (NullPointerException | NumberFormatException | IOException | InterruptedException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_6, competition, e.getMessage()));
            }

// progress
            progress += step;
            sendProgress(context, (int) progress);// t,f
        }
        return true;
    }

    // load players
    public static boolean loadPlayersLongTerm(Context context, Map<Integer, FDTeam> map,
                                              double progress, double step)
            throws NullPointerException, IOException {
        if (map == null || map.isEmpty()) return false;


        for (FDTeam team : map.values()) {
            if (team == null || team.getId() <= 0) continue;
            int teamId = team.getId();
            String id = formatString(teamId);
// tables
            try {
                FDPlayers players = loadListPlayers(id);
                team.setPlayers(players.getPlayers());
                Thread.sleep(LOAD_DB_DELAY);

            } catch (NullPointerException | NumberFormatException | IOException | InterruptedException e) {
                Timber.d(formatLoad(context, EXCEPTION_CODE_6, team, e.getMessage()));
            }
// progress
            progress += step;
            sendProgress(context, (int) progress);// t,f
        }
        return true;
    }


}
