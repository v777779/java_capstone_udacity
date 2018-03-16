package ru.vpcb.footballassistant.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 11-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NDSource {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("country")
    @Expose
    private String country;
    private List<NDArticle>  articles;


    public NDSource() {
        this.id =EMPTY_STRING;  // no id

    }


    public NDSource(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }


    public NDSource(String id, String name, String author,String description,
                    String url,String category,String language, String country ) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language =language;
        this.country = country;
    }

    public String getId() {
        if (id == null || id.isEmpty())return EMPTY_STRING;
        return id;
    }

    public String getName() {
        if (name == null || name.isEmpty()) return EMPTY_DASH;
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public List<NDArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NDArticle> articles) {
        this.articles = articles;
    }

    public void setId(String id) {
        this.id = id;
    }
}
