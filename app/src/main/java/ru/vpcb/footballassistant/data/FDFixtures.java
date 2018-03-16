package ru.vpcb.footballassistant.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDFixtures implements PostProcessingEnabler.PostProcessable {
    @SerializedName("_links")
    @Expose
    private FDLinks links;

    @SerializedName("fixtures")
    @Expose
    private List<FDFixture> fixtures;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("timeFrameStart")
    @Expose
    private String timeFrameStart;

    @SerializedName("timeFrameEnd")
    @Expose
    private String timeFrameEnd;

    private int competitionId;

    public FDFixtures() {
        this.competitionId = EMPTY_INT_VALUE;
    }

    @Override
    public void postProcess() {
        setId();
    }

    private void setId() {
        if (links != null && links.competition != null) {
            this.competitionId = FDUtils.formatHrefToId(links.competition.getHref());  // id competition
        }
        timeFrameEnd = FDUtils.formatDateToSQLite(timeFrameEnd);
        timeFrameStart = FDUtils.formatDateToSQLite(timeFrameStart);

    }

    public List<FDFixture> getFixtures() {
        return fixtures;
    }

    private class FDLinks {
        @SerializedName("self")
        @Expose
        private FDLink self;

        @SerializedName("competition")
        @Expose
        private FDLink competition;

        public FDLinks() {
            this.self = new FDLink();
            this.competition = new FDLink();
        }
    }



}
