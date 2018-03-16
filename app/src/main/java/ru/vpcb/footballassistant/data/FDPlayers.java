package ru.vpcb.footballassistant.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.vpcb.footballassistant.utils.FDUtils;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDPlayers implements PostProcessingEnabler.PostProcessable {
    @SerializedName("_links")
    @Expose
    private FDLinks links;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("players")
    @Expose
    private List<FDPlayer> players;

    private int id;


    public FDPlayers() {
        this.id = -1;
    }


    public class FDLinks {
        @SerializedName("self")
        @Expose
        private FDLink self;

        @SerializedName("team")
        @Expose
        private FDLink team;

    }

    public String getLinkTeam() {
        return links.team.getHref();
    }


    public void setId(){
        if(links != null && links.self != null) {
            id = FDUtils.formatHrefToId(links.self.getHref());
        }
    }


    public int getId() {
        return id;
    }

    public List<FDPlayer> getPlayers() {
        return players;
    }


    @Override
    public void postProcess() {
        setId();
    }

}
