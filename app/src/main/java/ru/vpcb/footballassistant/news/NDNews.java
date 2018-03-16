package ru.vpcb.footballassistant.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 11-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NDNews {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalResults")
    @Expose
    private int totalResults;

    @SerializedName("articles")
    @Expose
    private List<NDArticle> articles;

    public NDNews(String status, int totalResults, List<NDArticle> articles) {

        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        if(status == null || status.isEmpty())return EMPTY_DASH;
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<NDArticle> getArticles() {
        return articles;
    }

}
