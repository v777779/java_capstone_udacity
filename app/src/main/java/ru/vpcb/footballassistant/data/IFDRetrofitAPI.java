package ru.vpcb.footballassistant.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.vpcb.footballassistant.news.NDSources;

import static ru.vpcb.footballassistant.utils.Config.FD_COMPETITIONS_FIXTURES_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_COMPETITIONS_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_COMPETITIONS_TABLE_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_COMPETITIONS_TEAMS_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_QUERY_ID;
import static ru.vpcb.footballassistant.utils.Config.FD_QUERY_MATCHDAY;
import static ru.vpcb.footballassistant.utils.Config.FD_QUERY_SEASON;
import static ru.vpcb.footballassistant.utils.Config.FD_QUERY_TIMEFRAME;
import static ru.vpcb.footballassistant.utils.Config.FD_TEAM_FIXTURES_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_TEAM_GET;
import static ru.vpcb.footballassistant.utils.Config.FD_TEAM_PLAYERS_GET;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 20-Nov-17
 * Email: vadim.v.voronov@gmail.com
 */

/**
 * Retrofit API Interface
 */
public interface IFDRetrofitAPI {

    /**
     * Returns parsed List<RecipeItem>  after JSON data have been downloaded
     *
     * @param s String empty parameter
     * @return List<RecipeItem>  list of recipes downloaded from the server
     */

    // request with query   "baseURL/competitions/?seasons=2015"
    @GET(FD_COMPETITIONS_GET)
    Call<List<FDCompetition>> getCompetitions(@Query(FD_QUERY_SEASON) String s);

    // request with query   "baseURL/competitions"
    @GET(FD_COMPETITIONS_GET)
    Call<List<FDCompetition>> getCompetitions();


    @GET(FD_COMPETITIONS_TEAMS_GET)
    Call<FDTeams> getTeams(@Path(value = FD_QUERY_ID, encoded = true) String id);

    @GET(FD_COMPETITIONS_FIXTURES_GET)
    Call<FDFixtures> getFixtures(@Path(value = FD_QUERY_ID, encoded = true) String s);


    @GET(FD_COMPETITIONS_FIXTURES_GET)
    Call<FDFixtures> getFixturesMatch(@Path(value = FD_QUERY_ID, encoded = true) String s,
                                      @Query(FD_QUERY_MATCHDAY) int day);


    @GET(FD_COMPETITIONS_FIXTURES_GET)
    Call<FDFixtures> getFixturesTime(@Path(value = FD_QUERY_ID, encoded = true) String s,
                                     @Query(FD_QUERY_TIMEFRAME) String time);

    @GET(FD_COMPETITIONS_TABLE_GET)
    Call<FDTable> getTable(@Path(value = FD_QUERY_ID, encoded = true) String s);

    @GET(FD_TEAM_GET)
    Call<FDTeam> getTeam(@Path(value = FD_QUERY_ID, encoded = true) String id);

    @GET(FD_TEAM_FIXTURES_GET)
    Call<FDFixtures> getTeamFixtures(@Path(value = FD_QUERY_ID, encoded = true) String s);


    @GET(FD_TEAM_FIXTURES_GET)
    Call<FDFixtures> getTeamFixtures(@Path(value = FD_QUERY_ID, encoded = true) String s,
                                     @Query(FD_QUERY_TIMEFRAME) String time,
                                     @Query(FD_QUERY_SEASON) String season
    );


    @GET(FD_TEAM_PLAYERS_GET)
    Call<FDPlayers> getTeamPlayers(@Path(value = "id", encoded = true) String s);

    @GET(FD_COMPETITIONS_GET)
    Call<NDSources> getSources();


}


