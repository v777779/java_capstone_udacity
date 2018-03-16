package ru.vpcb.footballassistant.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 11-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NDSources {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("sources")
    @Expose
    private List<NDSource> sources;

    public String getStatus() {
        return status;
    }

    public List<NDSource> getSources() {
        return sources;
    }
}
