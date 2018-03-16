package ru.vpcb.footballassistant.news;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static ru.vpcb.footballassistant.utils.Config.ND_EVERYTHING_GET;
import static ru.vpcb.footballassistant.utils.Config.ND_HEADLINES_GET;
import static ru.vpcb.footballassistant.utils.Config.ND_QUERY_API_KEY;
import static ru.vpcb.footballassistant.utils.Config.ND_QUERY_CATEGORY;
import static ru.vpcb.footballassistant.utils.Config.ND_QUERY_PAGE;
import static ru.vpcb.footballassistant.utils.Config.ND_QUERY_SOURCE;
import static ru.vpcb.footballassistant.utils.Config.ND_SOURCES_GET;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 11-Feb-17
 * Email: vadim.v.voronov@gmail.com
 */

/**
 * Retrofit API Interface
 */
public interface INDRetrofitAPI {
    /**
     * Returns parsed News object after JSON data have been downloaded
     *
     * @param source  String    the  name of news company that provided news data
     * @param page    String    the number of page
     * @param key     String    the API key for request
     * @return News  object downloaded from the news server
     */

    // request with queries   "baseURL/everything?source=name&page=1&apiKey=key_string"
    @GET(ND_EVERYTHING_GET)
    Call<NDNews> getEverything(@Query(ND_QUERY_SOURCE) String source,
                               @Query(ND_QUERY_PAGE) String page,
                               @Query(ND_QUERY_API_KEY) String key
    );

    // request with queries   "baseURL/top-headlines?source=name&page=1&apiKey=key_string"
    @GET(ND_HEADLINES_GET)
    Call<NDNews> getHeadlines(@Query(ND_QUERY_SOURCE) String source,
                              @Query(ND_QUERY_PAGE) String page,
                              @Query(ND_QUERY_API_KEY) String key
    );

    // request with queries   "baseURL/sources?language=en&category=sports&apiKey=key_string"
    @GET(ND_SOURCES_GET)
    Call<NDSources> getSources(@Query(ND_QUERY_CATEGORY) String category,
                               @Query(ND_QUERY_API_KEY) String key

    );
}


