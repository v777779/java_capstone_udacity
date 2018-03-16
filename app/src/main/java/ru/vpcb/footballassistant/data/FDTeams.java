package ru.vpcb.footballassistant.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDTeams {
    @SerializedName("_links")
    @Expose
    private FDLinks links;

    @SerializedName("teams")
    @Expose
    private List<FDTeam> teams;

    @SerializedName("count")
    @Expose
    private int count;


    private class FDLinks {
        @SerializedName("self")
        @Expose
        private FDLink self;

        @SerializedName("competition")
        @Expose
        private FDLink competition;
    }

    public FDLinks getLinks() {
        return links;
    }

    public List<FDTeam> getTeams() {
        return teams;
    }

}
