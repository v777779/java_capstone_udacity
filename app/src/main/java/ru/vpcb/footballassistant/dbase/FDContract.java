package ru.vpcb.footballassistant.dbase;

import android.net.Uri;
import android.provider.BaseColumns;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 25-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDContract {
    public static final String CONTENT_AUTHORITY = "ru.vpcb.footballassistant";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String TABLE_COMPETITIONS = "competitions";
    public static final String TABLE_COMPETITION_TEAMS = "competition_teams";
    public static final String TABLE_COMPETITION_FIXTURES = "competition_fixtures";
    public static final String TABLE_TEAMS = "teams";
    public static final String TABLE_FIXTURES = "fixtures";
    public static final String TABLE_TABLES = "tables";
    public static final String TABLE_PLAYERS = "players";
    public static final String TABLE_ARTICLES = "articles";
    public static final String TABLE_SOURCES = "sources";

    public static final String DATABASE_NAME = "footballDb.db";
    public static final int DATABASE_VERSION = 1;


    /**
     * Entry class for RecipeItem Database Content Provider
     */


    public static final class CpEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_COMPETITIONS;
        public static final int TABLE_MATCHER = 100;
        public static final int TABLE_ID_MATCHER = 101;
        public static final int TABLE_ID_MATCHER2 = 102;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_COMPETITION_ID = "competition_id";            // int
        public static final String COLUMN_COMPETITION_CAPTION = "competition_caption";  // string
        public static final String COLUMN_COMPETITION_LEAGUE = "competition_league";    // string
        public static final String COLUMN_COMPETITION_YEAR = "competition_year";        // string
        public static final String COLUMN_CURRENT_MATCHDAY = "current_matchday";        // int
        public static final String COLUMN_NUMBER_MATCHDAYS = "number_matchdays";        // int
        public static final String COLUMN_NUMBER_TEAMS = "number_teams";                // int
        public static final String COLUMN_NUMBER_GAMES = "number_games";                // int
        public static final String COLUMN_LAST_UPDATE = "last_update";                  // string int from date

        public static final int LOADER_ID = 1220;                                       // int
    }

    public static final class CpTmEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_COMPETITION_TEAMS;
        public static final int TABLE_MATCHER = 120;
        public static final int TABLE_ID_MATCHER = 121;
        public static final int TABLE_ID_MATCHER2 = 122;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_COMPETITION_ID = "competition_id";            // int
        public static final String COLUMN_TEAM_ID = "team_id";                          // int

        public static final int LOADER_ID = 1221;                                       // int
    }

    public static final class CpFxEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_COMPETITION_FIXTURES;
        public static final int TABLE_MATCHER = 140;
        public static final int TABLE_ID_MATCHER = 141;
        public static final int TABLE_ID_MATCHER2 = 142;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_COMPETITION_ID = "competition_id";            // int
        public static final String COLUMN_FIXTURE_ID = "fixture_id";                    // int

        public static final int LOADER_ID = 1222;                                       // int
    }


    public static final class TmEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_TEAMS;
        public static final int TABLE_MATCHER = 200;
        public static final int TABLE_ID_MATCHER = 201;
        public static final int TABLE_ID_MATCHER2 = 202;    // id ? or ?
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_TEAM_ID = "team_id";                          // int
        public static final String COLUMN_TEAM_NAME = "team_name";                      // string
        public static final String COLUMN_TEAM_CODE = "team_code";                      // string
        public static final String COLUMN_TEAM_SHORT_NAME = "team_short_name";          // string
        public static final String COLUMN_TEAM_MARKET_VALUE = "team_market_value";      // string
        public static final String COLUMN_TEAM_CREST_URI = "team_crest_url";            // string

        public static final int LOADER_ID = 1223;                                       // int
    }


    public static final class FxEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_FIXTURES;
        public static final int TABLE_MATCHER = 300;
        public static final int TABLE_ID_MATCHER = 301;  // id
        public static final int TABLE_ID_MATCHER2 = 302;  // date between ? and ?
        public static final int TABLE_ID_MATCHER3 = 303;  // team ? and ?
        public static final int TABLE_ID_MATCHER4 = 304;  // notificationId ?
        public static final int TABLE_ID_MATCHER5 = 305;  // notificationId ?
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_FIXTURE_ID = "fixture_id";                    // int
        public static final String COLUMN_COMPETITION_ID = "competition_id";            // int
        public static final String COLUMN_TEAM_HOME_ID = "team_home_id";                // int
        public static final String COLUMN_TEAM_AWAY_ID = "team_away_id";                // int
        public static final String COLUMN_FIXTURE_DATE = "fixture_date";                // string int from date
        public static final String COLUMN_FIXTURE_STATUS = "fixture_status";            // string
        public static final String COLUMN_FIXTURE_MATCHDAY = "fixture_matchday";        // int
        public static final String COLUMN_FIXTURE_TEAM_HOME = "fixture_team_home";      // string
        public static final String COLUMN_FIXTURE_TEAM_AWAY = "fixture_team_away";      // string
        public static final String COLUMN_FIXTURE_GOALS_HOME = "fixture_goals_home";    // int
        public static final String COLUMN_FIXTURE_GOALS_AWAY = "fixture_goals_away";    // int
        public static final String COLUMN_FIXTURE_ODDS_WIN = "fixture_odds_home_win";   // real
        public static final String COLUMN_FIXTURE_ODDS_DRAW = "fixture_odds_draw";      // real
        public static final String COLUMN_FIXTURE_ODDS_AWAY = "fixture_odds_away_win";  // real
        public static final String COLUMN_FAVORITES_STATE = "favorites_state";          // int
        public static final String COLUMN_NOTIFICATION_STATE = "notification_state";    // int
        public static final String COLUMN_NOTIFICATION_ID = "notification_id";          // int
        public static final String COLUMN_FIXTURE_LEAGUE = "fixture_league";            // string
        public static final String COLUMN_FIXTURE_CAPTION = "fixture_caption";          // string

        public static final int LOADER_ID = 1224;                                       // int
    }

    public static final class TbEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_TABLES;
        public static final int TABLE_MATCHER = 400;
        public static final int TABLE_ID_MATCHER = 401;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_COMPETITION_ID = "competition_id";                // int
        public static final String COLUMN_TEAM_ID = "team_id";                              // int
        public static final String COLUMN_GROUP_NAME = "group_name";                        // string
        public static final String COLUMN_COMPETITION_MATCHDAY = "competition_matchday";    // int
        public static final String COLUMN_LEAGUE_CAPTION = "league_caption";                // string
        public static final String COLUMN_TEAM_POSITION = "team_position";                  // int
        public static final String COLUMN_TEAM_NAME = "team_name";                          // string
        public static final String COLUMN_CREST_URI = "crest_uri";                          // string
        public static final String COLUMN_TEAM_PLAYED_GAMES = "team_played_games";          // int
        public static final String COLUMN_TEAM_POINTS = "team_points";                      // int
        public static final String COLUMN_TEAM_GOALS = "team_goals";                        // int
        public static final String COLUMN_TEAM_GOALS_AGAINST = "team_goals_against";        // int
        public static final String COLUMN_TEAM_GOALS_DIFFERENCE = "team_goals_difference";  // int
        public static final String COLUMN_TEAM_WINS = "team_wins";                          // int
        public static final String COLUMN_TEAM_DRAWS = "team_draws";                        // int
        public static final String COLUMN_TEAM_LOSSES = "team_losses";                      // int

        public static final int LOADER_ID = 1225;                                           // int
    }

    public static final class PlEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_PLAYERS;
        public static final int TABLE_MATCHER = 500;
        public static final int TABLE_ID_MATCHER = 501;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_PLAYER_ID = "player_id";                          // int
        public static final String COLUMN_TEAM_ID = "team_id";                              // int
        public static final String COLUMN_PLAYER_NAME = "player_name";                      // string
        public static final String COLUMN_PLAYER_POSITION = "player_position";              // string
        public static final String COLUMN_PLAYER_JERSEY_NUMBER = "player_jersey_number";    // int
        public static final String COLUMN_PLAYER_DATE_BIRTH = "player_date_birth";          // int from date
        public static final String COLUMN_PLAYER_NATIONALITY = "player_nationality";        // string
        public static final String COLUMN_PLAYER_DATE_CONTRACT = "player_date_contract";    // int from date
        public static final String COLUMN_PLAYER_MARKET_VALUE = "player_market_value";      // string

        public static final int LOADER_ID = 1226;                                       // int
    }

    public static final class NaEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_ARTICLES;
        public static final int TABLE_MATCHER = 600;
        public static final int TABLE_ID_MATCHER = 601;
        public static final int TABLE_ID_MATCHER4 = 604;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_ARTICLE_ID = "article_id";                        // string
        public static final String COLUMN_SOURCE_ID = "source_id";                          // string
        public static final String COLUMN_SOURCE_NAME = "source_name";                      // string
        public static final String COLUMN_AUTHOR = "author";                                // string
        public static final String COLUMN_TITLE = "title";                                  // string
        public static final String COLUMN_DESCRIPTION = "description";                       // string
        public static final String COLUMN_ARTICLE_URL = "article_url";                      // string
        public static final String COLUMN_IMAGE_URL = "image_url";                          // string
        public static final String COLUMN_PUBLISHED_AT = "published_at";                    // string dat sqlite

        public static final int LOADER_ID = 1227;                                           // int
    }

    public static final class NsEntry implements BaseColumns {
        public static final String TABLE_NAME = TABLE_SOURCES;
        public static final int TABLE_MATCHER = 700;
        public static final int TABLE_ID_MATCHER = 701;
        public static final int TABLE_ID_MATCHER4 = 704;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_SOURCE_ID = "id";                                 // string primary
        public static final String COLUMN_SOURCE_NAME = "name";                             // string
        public static final String COLUMN_DESCRIPTION = "description";                      // string
        public static final String COLUMN_SOURCE_URL = "source_url";                        // string
        public static final String COLUMN_SOURCE_CATEGORY = "source_category";              // string
        public static final String COLUMN_SOURCE_LANGUAGE = "source_language";              // string
        public static final String COLUMN_COUNTRY = "number_teams";                         // string

        public static final int LOADER_ID = 1228;                                           // int
    }

    public static final FDParams[] MATCH_PARAMETERS = new FDParams[]{
            new FDParams(CpEntry.LOADER_ID, CpEntry.TABLE_NAME, CpEntry.TABLE_MATCHER,
                    CpEntry.TABLE_ID_MATCHER, CpEntry.COLUMN_COMPETITION_ID        // id?

            ),
            new FDParams(CpTmEntry.LOADER_ID, CpTmEntry.TABLE_NAME,
                    CpTmEntry.TABLE_MATCHER,
                    CpTmEntry.TABLE_ID_MATCHER, CpTmEntry.COLUMN_COMPETITION_ID,    // id?
                    CpTmEntry.TABLE_ID_MATCHER2,
                    CpTmEntry.COLUMN_COMPETITION_ID, CpTmEntry.COLUMN_TEAM_ID       // id? and id?
            ),
            new FDParams(CpFxEntry.LOADER_ID, CpFxEntry.TABLE_NAME, CpFxEntry.TABLE_MATCHER,
                    CpFxEntry.TABLE_ID_MATCHER, CpFxEntry.COLUMN_COMPETITION_ID,    // id?
                    CpFxEntry.TABLE_ID_MATCHER2,
                    CpFxEntry.COLUMN_COMPETITION_ID, CpFxEntry.COLUMN_FIXTURE_ID    // id? and id?
            ),
            new FDParams(TmEntry.LOADER_ID, TmEntry.TABLE_NAME,
                    TmEntry.TABLE_MATCHER,
                    TmEntry.TABLE_ID_MATCHER, TmEntry.COLUMN_TEAM_ID,               // id?
                    TmEntry.TABLE_ID_MATCHER2,
                    TmEntry.COLUMN_TEAM_ID, TmEntry.COLUMN_TEAM_ID                  // id? or id?
            ),
            new FDParams(FxEntry.LOADER_ID, FxEntry.TABLE_NAME, FxEntry.TABLE_MATCHER,
                    FxEntry.TABLE_ID_MATCHER, FxEntry.COLUMN_FIXTURE_ID,            // id?
                    FxEntry.TABLE_ID_MATCHER2,
                    FxEntry.COLUMN_TEAM_HOME_ID, FxEntry.COLUMN_TEAM_AWAY_ID,        // id? and id?
                    FxEntry.TABLE_ID_MATCHER3,
                    FxEntry.COLUMN_FIXTURE_DATE, FxEntry.COLUMN_FIXTURE_DATE,        // btw ? and ?
                    FxEntry.TABLE_ID_MATCHER4,
                    FxEntry.COLUMN_NOTIFICATION_ID,                                   // id4?  check this  used for news
                    FxEntry.TABLE_ID_MATCHER5,
                    FxEntry.COLUMN_FAVORITES_STATE                                    // id?

            ),
            new FDParams(TbEntry.LOADER_ID, TbEntry.TABLE_NAME, TbEntry.TABLE_MATCHER,
                    TbEntry.TABLE_ID_MATCHER, TbEntry.COLUMN_COMPETITION_ID         // id?

            ),

            new FDParams(PlEntry.LOADER_ID, PlEntry.TABLE_NAME, PlEntry.TABLE_MATCHER,
                    PlEntry.TABLE_ID_MATCHER, PlEntry.COLUMN_TEAM_ID               // id?

            ),

            new FDParams(NaEntry.LOADER_ID, NaEntry.TABLE_NAME, NaEntry.TABLE_MATCHER,
                    NaEntry.TABLE_ID_MATCHER, NaEntry.COLUMN_ARTICLE_ID,              // id?   //все новости по данному источнику
                    EMPTY_INT_VALUE, null, null,
                    EMPTY_INT_VALUE, null, null,
                    NaEntry.TABLE_ID_MATCHER4, NaEntry.COLUMN_ARTICLE_ID
            ),
            new FDParams(NsEntry.LOADER_ID, NsEntry.TABLE_NAME, NsEntry.TABLE_MATCHER,
                    NsEntry.TABLE_ID_MATCHER, NsEntry.COLUMN_SOURCE_ID,              // id?   //запись по источнику
                    EMPTY_INT_VALUE, null, null,
                    EMPTY_INT_VALUE, null, null,
                    NsEntry.TABLE_ID_MATCHER4, NsEntry.COLUMN_SOURCE_ID             // id4?
            ),

    };

    public static final int MATCH_PARAMETERS_COMPETITIONS = 0;
    public static final int MATCH_PARAMETERS_CP_TEAMS = 1;
    public static final int MATCH_PARAMETERS_CP_FIXTURES = 2;
    public static final int MATCH_PARAMETERS_TEAMS = 3;
    public static final int MATCH_PARAMETERS_FIXTURES = 4;
    public static final int MATCH_PARAMETERS_TABLES = 5;
    public static final int MATCH_PARAMETERS_PLAYERS = 6;
    public static final int MATCH_PARAMETERS_ARTICLES = 7;
    public static final int MATCH_PARAMETERS_SOURCES = 8;


    public static final class FDParams {
        int id;
        String tableName;
        int tableMatcher;
        int tableIdMatcher;
        String columnId;
        int tableIdMatcher2;
        String columnId2;
        String columnId3;
        int tableIdMatcher3;
        String columnId4;
        String columnId5;
        int tableIdMatcher4;
        String columnId6;
        int tableIdMatcher5;
        String columnId7;


        public FDParams(int id, String tableName, int tableMatcher, int tableIdMatcher, String coolumnId) {
            this.id = id;
            this.tableName = tableName;
            this.tableMatcher = tableMatcher;
            this.tableIdMatcher = tableIdMatcher;
            this.columnId = coolumnId;
            this.tableIdMatcher2 = -1;
            this.columnId2 = null;
            this.columnId3 = null;
            this.tableIdMatcher3 = -1;
            this.columnId4 = null;
            this.columnId5 = null;

        }

        public FDParams(int id, String tableName, int tableMatcher, int tableIdMatcher, String columnId,
                        int tableIdMatcher2, String columnId2, String columnId3) {
            this(id, tableName, tableMatcher, tableIdMatcher, columnId);
            this.tableIdMatcher2 = tableIdMatcher2;
            this.columnId2 = columnId2;
            this.columnId3 = columnId3;
        }

        public FDParams(int id, String tableName, int tableMatcher,
                        int tableIdMatcher, String columnId,
                        int tableIdMatcher2, String columnId2, String columnId3,
                        int tableIdMatcher3, String columnId4, String columnId5) {
            this(id, tableName, tableMatcher,
                    tableIdMatcher, columnId,
                    tableIdMatcher2, columnId2, columnId3);
            this.tableIdMatcher3 = tableIdMatcher3;
            this.columnId4 = columnId4;
            this.columnId5 = columnId5;
        }

        public FDParams(int id, String tableName, int tableMatcher,
                        int tableIdMatcher, String columnId,
                        int tableIdMatcher2, String columnId2, String columnId3,
                        int tableIdMatcher3, String columnId4, String columnId5,
                        int tableIdMatcher4, String columnId6) {
            this(id, tableName, tableMatcher,
                    tableIdMatcher, columnId,
                    tableIdMatcher2, columnId2, columnId3, tableIdMatcher3, columnId4, columnId5);
            this.tableIdMatcher4 = tableIdMatcher4;
            this.columnId6 = columnId6;
        }

        @SuppressWarnings("SameParameterValue")
        public FDParams(int id, String tableName, int tableMatcher,
                        int tableIdMatcher, String columnId,
                        int tableIdMatcher2, String columnId2, String columnId3,
                        int tableIdMatcher3, String columnId4, String columnId5,
                        int tableIdMatcher4, String columnId6,
                        int tableIdMatcher5, String columnId7) {
            this(id, tableName, tableMatcher,
                    tableIdMatcher, columnId,
                    tableIdMatcher2, columnId2, columnId3, tableIdMatcher3, columnId4, columnId5,
                    tableIdMatcher4,columnId6);
            this.tableIdMatcher5 = tableIdMatcher5;
            this.columnId7 = columnId7;
        }


        public String getSortOrder() {
            if (columnId.equals(FxEntry.COLUMN_FIXTURE_ID)) {
                return "datetime(" + FxEntry.COLUMN_FIXTURE_DATE + ") ASC";
            } else if (columnId.equals(NaEntry.COLUMN_ARTICLE_ID)) {
                return "datetime(" + NaEntry.COLUMN_PUBLISHED_AT + ") DESC";  // fresh record first
            } else {
                return columnId + " ASC";
            }

        }
    }


}
