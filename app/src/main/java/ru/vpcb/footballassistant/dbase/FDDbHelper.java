package ru.vpcb.footballassistant.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import static ru.vpcb.footballassistant.dbase.FDContract.DATABASE_NAME;
import static ru.vpcb.footballassistant.dbase.FDContract.DATABASE_VERSION;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 23-Oct-17
 * Email: vadim.v.voronov@gmail.com
 */

/**
 * RecipeDbHelper class for RecipeItem Database Content Provider
 */
public class FDDbHelper extends SQLiteOpenHelper {

    FDDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates Database of RecipeItems
     *
     * @param db SQLiteDatabase database
     */
    @Override
   synchronized public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_COMPETITIONS = "CREATE TABLE " + FDContract.CpEntry.TABLE_NAME + " (" +
//                FDContract.CpEntry._ID + " INTEGER PRIMARY KEY, " +
                FDContract.CpEntry.COLUMN_COMPETITION_ID + " INTEGER PRIMARY KEY, " +       // int
                FDContract.CpEntry.COLUMN_COMPETITION_CAPTION + " TEXT, " +                    // string
                FDContract.CpEntry.COLUMN_COMPETITION_LEAGUE + " TEXT, " +         // string
                FDContract.CpEntry.COLUMN_COMPETITION_YEAR + " TEXT, " +           // string
                FDContract.CpEntry.COLUMN_CURRENT_MATCHDAY + " INTEGER, " +        // int
                FDContract.CpEntry.COLUMN_NUMBER_MATCHDAYS + " INTEGER, " +        // int
                FDContract.CpEntry.COLUMN_NUMBER_TEAMS + " INTEGER, " +            // int
                FDContract.CpEntry.COLUMN_NUMBER_GAMES + " INTEGER, " +            // int
                FDContract.CpEntry.COLUMN_LAST_UPDATE + " TEXT);";                          // string int from date


        final String CREATE_TABLE_COMPETITION_TEAMS = "CREATE TABLE " + FDContract.CpTmEntry.TABLE_NAME + " (" +
                FDContract.CpTmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +                 // int
                FDContract.CpTmEntry.COLUMN_COMPETITION_ID + " INTEGER NOT NULL, " +                // int
                FDContract.CpTmEntry.COLUMN_TEAM_ID + " INTEGER NOT NULL);";                       // int

        final String CREATE_TABLE_COMPETITION_FIXTURES = "CREATE TABLE " + FDContract.CpFxEntry.TABLE_NAME + " (" +
                FDContract.CpFxEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FDContract.CpFxEntry.COLUMN_COMPETITION_ID + " INTEGER NOT NULL, " + // int
                FDContract.CpFxEntry.COLUMN_FIXTURE_ID + " INTEGER NOT NULL);";        // int


        final String CREATE_TABLE_TEAMS = "CREATE TABLE " + FDContract.TmEntry.TABLE_NAME + " (" +
//                FDContract.TmEntry._ID + " INTEGER PRIMARY KEY, " +
                FDContract.TmEntry.COLUMN_TEAM_ID + " INTEGER PRIMARY KEY, " +          // int
                FDContract.TmEntry.COLUMN_TEAM_NAME + " TEXT NOT NULL, " +              // string
                FDContract.TmEntry.COLUMN_TEAM_CODE + " TEXT, " +                       // string
                FDContract.TmEntry.COLUMN_TEAM_SHORT_NAME + " TEXT, " +                 // string
                FDContract.TmEntry.COLUMN_TEAM_MARKET_VALUE + " TEXT, " +               // string
                FDContract.TmEntry.COLUMN_TEAM_CREST_URI + " TEXT);";                  // string


        final String CREATE_TABLE_FIXTURES = "CREATE TABLE " + FDContract.FxEntry.TABLE_NAME + " (" +
//                FDContract.FxEntry._ID + " INTEGER PRIMARY KEY, " +
                FDContract.FxEntry.COLUMN_FIXTURE_ID + " INTEGER PRIMARY KEY, " +       // int
                FDContract.FxEntry.COLUMN_COMPETITION_ID + " INTEGER, " +               // int
                FDContract.FxEntry.COLUMN_TEAM_HOME_ID + " INTEGER, " +                 // int
                FDContract.FxEntry.COLUMN_TEAM_AWAY_ID + " INTEGER, " +                 // int
                FDContract.FxEntry.COLUMN_FIXTURE_DATE + " TEXT NOT NULL, " +           // string int from date
                FDContract.FxEntry.COLUMN_FIXTURE_STATUS + " TEXT, " +                  // string
                FDContract.FxEntry.COLUMN_FIXTURE_MATCHDAY + " INTEGER, " +             // int
                FDContract.FxEntry.COLUMN_FIXTURE_TEAM_HOME + " TEXT, " +               // string
                FDContract.FxEntry.COLUMN_FIXTURE_TEAM_AWAY + " TEXT, " +               // string
                FDContract.FxEntry.COLUMN_FIXTURE_GOALS_HOME + " INTEGER, " +           // int
                FDContract.FxEntry.COLUMN_FIXTURE_GOALS_AWAY + " INTEGER, " +           // int
                FDContract.FxEntry.COLUMN_FIXTURE_ODDS_WIN + " REAL, " +                // real
                FDContract.FxEntry.COLUMN_FIXTURE_ODDS_DRAW + " REAL, " +               // real
                FDContract.FxEntry.COLUMN_FIXTURE_ODDS_AWAY + " REAL, " +               // real
                FDContract.FxEntry.COLUMN_FAVORITES_STATE + " INTEGER, " +              // int
                FDContract.FxEntry.COLUMN_NOTIFICATION_STATE + " INTEGER, " +           // int
                FDContract.FxEntry.COLUMN_NOTIFICATION_ID + " TEXT, " +                 // string
                FDContract.FxEntry.COLUMN_FIXTURE_LEAGUE + " TEXT, " +                  // string
                FDContract.FxEntry.COLUMN_FIXTURE_CAPTION + " TEXT);";                  // string


        final String CREATE_TABLE_TABLES = "CREATE TABLE " + FDContract.TbEntry.TABLE_NAME + " (" +
                FDContract.TbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FDContract.TbEntry.COLUMN_COMPETITION_ID + " INTEGER NOT NULL, " +      // int
                FDContract.TbEntry.COLUMN_TEAM_ID + " INTEGER NOT NULL, " +             // int
                FDContract.TbEntry.COLUMN_COMPETITION_MATCHDAY + " INTEGER, " +         // int
                FDContract.TbEntry.COLUMN_LEAGUE_CAPTION + " TEXT, " +                  // string
                FDContract.TbEntry.COLUMN_TEAM_POSITION + " INTEGER NOT NULL, " +       // int
                FDContract.TbEntry.COLUMN_TEAM_NAME + " TEXT, " +                       // string
                FDContract.TbEntry.COLUMN_CREST_URI + " TEXT, " +                       // string
                FDContract.TbEntry.COLUMN_TEAM_PLAYED_GAMES + " INTEGER, " +            // int
                FDContract.TbEntry.COLUMN_TEAM_POINTS + " INTEGER, " +                  // int
                FDContract.TbEntry.COLUMN_TEAM_GOALS + " INTEGER, " +                   // int
                FDContract.TbEntry.COLUMN_TEAM_GOALS_AGAINST + " INTEGER, " +          // int
                FDContract.TbEntry.COLUMN_TEAM_GOALS_DIFFERENCE + " INTEGER, " +        // int
                FDContract.TbEntry.COLUMN_TEAM_WINS + " INTEGER, " +                    // int
                FDContract.TbEntry.COLUMN_TEAM_DRAWS + " INTEGER, " +                   // int
                FDContract.TbEntry.COLUMN_TEAM_LOSSES + " INTEGER);";                   // int

        final String CREATE_TABLE_PLAYERS = "CREATE TABLE " + FDContract.PlEntry.TABLE_NAME + " (" +
                FDContract.PlEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FDContract.PlEntry.COLUMN_TEAM_ID + " INTEGER NOT NULL, " +             // int
                FDContract.PlEntry.COLUMN_PLAYER_NAME + " TEXT, " +                     // string
                FDContract.PlEntry.COLUMN_PLAYER_POSITION + " TEXT, " +                 // string
                FDContract.PlEntry.COLUMN_PLAYER_JERSEY_NUMBER + " INTEGER, " +         // int
                FDContract.PlEntry.COLUMN_PLAYER_DATE_BIRTH + " TEXT, " +               // int from date
                FDContract.PlEntry.COLUMN_PLAYER_NATIONALITY + " TEXT, " +              // string
                FDContract.PlEntry.COLUMN_PLAYER_DATE_CONTRACT + " TEXT, " +            // int from date
                FDContract.PlEntry.COLUMN_PLAYER_MARKET_VALUE + " TEXT);";              // string

        final String CREATE_TABLE_ARTICLES = "CREATE TABLE " + FDContract.NaEntry.TABLE_NAME + " (" +
                FDContract.NaEntry.COLUMN_ARTICLE_ID + " TEXT PRIMARY KEY, " +          // string key=source_id_title
                FDContract.NaEntry.COLUMN_SOURCE_ID + " TEXT NOT NULL, " +              // string
                FDContract.NaEntry.COLUMN_SOURCE_NAME + " TEXT, " +                     // string
                FDContract.NaEntry.COLUMN_AUTHOR + " TEXT, " +                          // string
                FDContract.NaEntry.COLUMN_TITLE + " TEXT NOT NULL, " +                  // string
                FDContract.NaEntry.COLUMN_DESCRIPTION + " TEXT, " +                     // string
                FDContract.NaEntry.COLUMN_ARTICLE_URL + " TEXT, " +                     // string
                FDContract.NaEntry.COLUMN_IMAGE_URL + " TEXT, " +                       // string
                FDContract.NaEntry.COLUMN_PUBLISHED_AT + " TEXT);";                     // string from date

        final String CREATE_TABLE_SOURCES = "CREATE TABLE " + FDContract.NsEntry.TABLE_NAME + " (" +
                FDContract.NsEntry.COLUMN_SOURCE_ID + " TEXT PRIMARY KEY, " +           // string
                FDContract.NsEntry.COLUMN_SOURCE_NAME + " TEXT, " +                     // string
                FDContract.NsEntry.COLUMN_DESCRIPTION + " TEXT, " +                     // string
                FDContract.NsEntry.COLUMN_SOURCE_URL + " TEXT, " +                      // string
                FDContract.NsEntry.COLUMN_SOURCE_CATEGORY + " TEXT, " +                 // string
                FDContract.NsEntry.COLUMN_SOURCE_LANGUAGE + " TEXT, " +                 // string
                FDContract.NsEntry.COLUMN_COUNTRY + " TEXT);";                          // string

        db.execSQL(CREATE_TABLE_COMPETITIONS);
        db.execSQL(CREATE_TABLE_COMPETITION_TEAMS);
        db.execSQL(CREATE_TABLE_COMPETITION_FIXTURES);
        db.execSQL(CREATE_TABLE_TEAMS);
        db.execSQL(CREATE_TABLE_FIXTURES);
        db.execSQL(CREATE_TABLE_TABLES);
        db.execSQL(CREATE_TABLE_PLAYERS);
        db.execSQL(CREATE_TABLE_ARTICLES);
        db.execSQL(CREATE_TABLE_SOURCES);

    }

    /**
     * Upgrades old version database to new version
     *
     * @param db         SQLiteDatabase database for upgrading
     * @param oldVersion int old version to compare
     * @param newVersion int new version to compare
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.CpEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.CpTmEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.CpFxEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.TmEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.FxEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.TbEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FDContract.PlEntry.TABLE_NAME);
        onCreate(db);
    }

    public interface ICpEntry {
        int COLUMN_COMPETITION_ID = 0;          // int
        int COLUMN_COMPETITION_CAPTION = 1;     // string
        int COLUMN_COMPETITION_LEAGUE = 2;      // string
        int COLUMN_COMPETITION_YEAR = 3;        // string
        int COLUMN_CURRENT_MATCHDAY = 4;        // int
        int COLUMN_NUMBER_MATCHDAYS = 5;        // int
        int COLUMN_NUMBER_TEAMS = 6;            // int
        int COLUMN_NUMBER_GAMES = 7;            // int
        int COLUMN_LAST_UPDATE = 8;             // string int from date

    }


    public interface ICpTmEntry {
        int _ID = 0;                            // int auto
        int COLUMN_COMPETITION_ID = 1;          // int
        int COLUMN_TEAM_ID = 2;                 // int
    }

    public interface ICpFxEntry {
        int _ID = 0;                            // int auto
        int COLUMN_COMPETITION_ID = 1;          // int
        int COLUMN_FIXTURE_ID = 2;              // int
    }

    public interface ITmEntry {
        int COLUMN_TEAM_ID = 0;                 // int
        int COLUMN_TEAM_NAME = 1;               // string
        int COLUMN_TEAM_CODE = 2;               // string
        int COLUMN_TEAM_SHORT_NAME = 3;         // string
        int COLUMN_TEAM_MARKET_VALUE = 4;       // string
        int COLUMN_TEAM_CREST_URI = 5;          // string
    }

    public interface IFxEntry {
        int COLUMN_FIXTURE_ID = 0;              // int
        int COLUMN_COMPETITION_ID = 1;          // int
        int COLUMN_TEAM_HOME_ID = 2;            // int
        int COLUMN_TEAM_AWAY_ID = 3;            // int
        int COLUMN_FIXTURE_DATE = 4;            // string int from date
        int COLUMN_FIXTURE_STATUS = 5;          // string
        int COLUMN_FIXTURE_MATCHDAY = 6;        // int
        int COLUMN_FIXTURE_TEAM_HOME = 7;       // string
        int COLUMN_FIXTURE_TEAM_AWAY = 8;       // string
        int COLUMN_FIXTURE_GOALS_HOME = 9;      // int
        int COLUMN_FIXTURE_GOALS_AWAY = 10;     // int
        int COLUMN_FIXTURE_ODDS_WIN = 11;       // real
        int COLUMN_FIXTURE_ODDS_DRAW = 12;      // real
        int COLUMN_FIXTURE_ODDS_AWAY = 13;      // real
        int COLUMN_FAVORITES_STATE = 14;        // int
        int COLUMN_NOTIFICATION_STATE = 15;     // int
        int COLUMN_NOTIFICATION_ID = 16;        // int
        int COLUMN_FIXTURE_LEAGUE = 17;         // string
        int COLUMN_FIXTURE_CAPTION = 18;        // string
    }

    public interface ITbEntry {
        int _ID = 0;                            // int auto
        int COLUMN_COMPETITION_ID = 1;          // int
        int COLUMN_TEAM_ID = 2;                 // int
        int COLUMN_GROUP_NAME = 3;              // string
        int COLUMN_COMPETITION_MATCHDAY = 4;    // int
        int COLUMN_LEAGUE_CAPTION = 5;          // string
        int COLUMN_TEAM_POSITION = 6;           // int
        int COLUMN_TEAM_NAME = 7;               // string
        int COLUMN_CREST_URI = 8;               // string
        int COLUMN_TEAM_PLAYED_GAMES = 9;       // int
        int COLUMN_TEAM_POINTS = 10;             // int
        int COLUMN_TEAM_GOALS = 11;              // int
        int COLUMN_TEAM_GOALS_AGAINST = 12;     // int
        int COLUMN_TEAM_GOALS_DIFFERENCE = 13;  // int
        int COLUMN_TEAM_WINS = 14;              // int
        int COLUMN_TEAM_DRAWS = 15;             // int
        int COLUMN_TEAM_LOSSES = 16;            // int
    }

    public interface IPlEntry {
        int _ID = 0;                            // int auto
        int COLUMN_TEAM_ID = 1;                 // int
        int COLUMN_PLAYER_NAME = 2;             // string
        int COLUMN_PLAYER_POSITION = 3;         // string
        int COLUMN_PLAYER_JERSEY_NUMBER = 4;    // int
        int COLUMN_PLAYER_DATE_BIRTH = 5;       // int from date
        int COLUMN_PLAYER_NATIONALITY = 6;      // string
        int COLUMN_PLAYER_DATE_CONTRACT = 7;    // int from date
        int COLUMN_PLAYER_MARKET_VALUE = 8;     // string
        int COLUMN_LAST_REFRESH = 9;            // int from date
    }

    public interface INaEntry {
        int COLUMN_ARTICLE_ID = 0;              // string source_id+title
        int COLUMN_SOURCE_ID = 1;               // string
        int COLUMN_SOURCE_NAME = 2;             // string
        int COLUMN_AUTHOR = 3;                  // string
        int COLUMN_TITLE = 4;                   // string
        int COLUMN_DESCRIPTION = 5;             // string
        int COLUMN_ARTICLE_URL = 6;             // string
        int COLUMN_IMAGE_URL = 7;               // string
        int COLUMN_PUBLISHED_AT = 8;            // string int from date

    }

    public interface INsEntry {
        int COLUMN_SOURCE_ID = 0;               // string
        int COLUMN_SOURCE_NAME = 1;             // string
        int COLUMN_DESCRIPTION = 2;             // string
        int COLUMN_SOURCE_URL = 3;              // string
        int COLUMN_SOURCE_CATEGORY = 4;         // string
        int COLUMN_SOURCE_LANGUAGE = 5;         // string
        int COLUMN_COUNTRY = 6;                 // string

    }

}
