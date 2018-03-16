package ru.vpcb.footballassistant.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_NOTIFICATION_ID;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDFixture implements PostProcessingEnabler.PostProcessable, Parcelable {
    @SerializedName("_links")
    @Expose
    private FDLinks links;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("matchDay")
    @Expose
    private int matchDay;

    @SerializedName("homeTeamName")
    @Expose
    private String homeTeamName;

    @SerializedName("awayTeamName")
    @Expose
    private String awayTeamName;

    @SerializedName("result")
    @Expose
    private FDResult result;

    @SerializedName("odds")
    @Expose
    private FDOdds odds;

    private int id;
    private int competitionId;
    private int homeTeamId;
    private int awayTeamId;

    // favorite
    private boolean isFavorite;

    // notification
    private boolean isNotified;
    private String notificationId;


    // widgets
    private String caption;
    private String league;

    public FDFixture() {
        this.id = EMPTY_INT_VALUE;                  // id
        this.competitionId = EMPTY_INT_VALUE;       // id competition
        this.homeTeamId = EMPTY_INT_VALUE;          // id teamHome
        this.awayTeamId = EMPTY_INT_VALUE;          // id teamAway

        this.isFavorite = false;
        this.isNotified = false;
        this.notificationId = EMPTY_STRING;
        this.caption = EMPTY_DASH;
        this.league = EMPTY_DASH;

        this.result = new FDResult();
        this.odds = new FDOdds();

    }

    public FDFixture(int id, int competitionId, int homeTeamId, int awayTeamId,
                     String date, String status, int matchDay, String homeTeamName,
                     String awayTeamName, int goalsHomeTeam, int goalsAwayTeam,
                     double homeWin, double draw, double awayWin,
                     boolean isFavorite, boolean isNotified, String notificationId,
                     String league, String caption) {
        this.id = id;
        this.competitionId = competitionId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.date = date;
        this.status = status;
        this.matchDay = matchDay;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.result = new FDResult(goalsHomeTeam, goalsAwayTeam);
        this.odds = new FDOdds(homeWin, draw, awayWin);
        this.isFavorite = isFavorite;
        this.isNotified = isNotified;
        this.notificationId = notificationId;
        this.league = league;
        this.caption = caption;
    }

    protected FDFixture(Parcel in) {
        date = in.readString();
        status = in.readString();
        matchDay = in.readInt();
        homeTeamName = in.readString();
        awayTeamName = in.readString();
        id = in.readInt();
        competitionId = in.readInt();
        homeTeamId = in.readInt();
        awayTeamId = in.readInt();
        isFavorite = in.readByte() != 0;
        isNotified = in.readByte() != 0;
        notificationId = in.readString();
        league = in.readString();
        caption = in.readString();
// FDLink
        links = new FDLinks();
        links.self.setHref(in.readString());
        links.competition.setHref(in.readString());
        links.homeTeam.setHref(in.readString());
        links.awayTeam.setHref(in.readString());
// FDOdds
        odds = new FDOdds();
        odds.homeWin = in.readDouble();
        odds.draw = in.readDouble();
        odds.awayWin = in.readDouble();

// FDResult
        result = new FDResult();
        result.goalsHomeTeam = in.readInt();
        result.goalsAwayTeam = in.readInt();
        result.halfTime = new FDHalfTime();
        result.halfTime.goalsHomeTeam = in.readInt();
        result.halfTime.goalsAwayTeam = in.readInt();

    }

    public static final Creator<FDFixture> CREATOR = new Creator<FDFixture>() {
        @Override
        public FDFixture createFromParcel(Parcel in) {
            return new FDFixture(in);
        }

        @Override
        public FDFixture[] newArray(int size) {
            return new FDFixture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(status);
        parcel.writeInt(matchDay);
        parcel.writeString(homeTeamName);
        parcel.writeString(awayTeamName);
        parcel.writeInt(id);
        parcel.writeInt(competitionId);
        parcel.writeInt(homeTeamId);
        parcel.writeInt(awayTeamId);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeByte((byte) (isNotified ? 1 : 0));
        parcel.writeString(notificationId);
        parcel.writeString(league);
        parcel.writeString(caption);
// FDLink
        if(links == null) links = new FDLinks();
        parcel.writeString(links.self.getHref());
        parcel.writeString(links.competition.getHref());
        parcel.writeString(links.homeTeam.getHref());
        parcel.writeString(links.awayTeam.getHref());
// FDOdds
        if(odds == null) odds = new FDOdds();
        parcel.writeDouble(odds.homeWin);
        parcel.writeDouble(odds.draw);
        parcel.writeDouble(odds.awayWin);
// FDResult
        if(result == null) result = new FDResult();
        parcel.writeInt(result.goalsHomeTeam);
        parcel.writeInt(result.goalsAwayTeam);
        if(result.halfTime == null) result.halfTime = new FDHalfTime();
        parcel.writeInt(result.halfTime.goalsHomeTeam);
        parcel.writeInt(result.halfTime.goalsAwayTeam);

    }

    private class FDLinks {
        @SerializedName("self")
        @Expose
        private FDLink self;

        @SerializedName("competition")
        @Expose
        private FDLink competition;

        @SerializedName("homeTeam")
        @Expose
        private FDLink homeTeam;

        @SerializedName("awayTeam")
        @Expose
        private FDLink awayTeam;

        FDLinks() {
            self = new FDLink();
            competition = new FDLink();
            homeTeam = new FDLink();
            awayTeam = new FDLink();
        }
    }

    private class FDHalfTime {
        @SerializedName("goalsHomeTeam")
        @Expose
        private Integer goalsHomeTeam;

        @SerializedName("goalsAwayTeam")
        @Expose
        private Integer goalsAwayTeam;

        FDHalfTime() {
            goalsHomeTeam = EMPTY_INT_VALUE;
            goalsAwayTeam = EMPTY_INT_VALUE;
        }
    }


    private class FDResult {
        @SerializedName("goalsHomeTeam")
        @Expose
        private Integer goalsHomeTeam;

        @SerializedName("goalsAwayTeam")
        @Expose
        private Integer goalsAwayTeam;

        @SerializedName("halfTime")
        @Expose
        private FDHalfTime halfTime;

        FDResult() {
            goalsHomeTeam = EMPTY_INT_VALUE;
            goalsAwayTeam = EMPTY_INT_VALUE;
            halfTime = new FDHalfTime();
        }

        FDResult(int goalsHomeTeam, int goalsAwayTeam) {
            this.goalsHomeTeam = goalsHomeTeam;
            this.goalsAwayTeam = goalsAwayTeam;
            this.halfTime = null;
        }
    }

    private class FDOdds {
        @SerializedName("homeWin")
        @Expose
        private Double homeWin;

        @SerializedName("draw")
        @Expose
        private Double draw;

        @SerializedName("awayWin")
        @Expose
        private Double awayWin;

        FDOdds() {
            homeWin = (double)EMPTY_INT_VALUE;
            draw = (double)EMPTY_INT_VALUE;
            awayWin = (double)EMPTY_INT_VALUE;
        }

        FDOdds(double homeWin, double draw, double awayWin) {
            this.homeWin = homeWin;
            this.draw = draw;
            this.awayWin = awayWin;
        }
    }


    public void setId() {
        if (links == null) return;
        if (links.self != null) {
            this.id = FDUtils.formatHrefToId(links.self.getHref());                    // id
        }
        if (links.competition != null) {
            this.competitionId = FDUtils.formatHrefToId(links.competition.getHref());  // id competition
        }
        if (links.homeTeam != null) {
            this.homeTeamId = FDUtils.formatHrefToId(links.homeTeam.getHref());        // id teamHome
        }
        if (links.awayTeam != null) {
            this.awayTeamId = FDUtils.formatHrefToId(links.awayTeam.getHref());        // id teamAway
        }

        if(odds == null) odds = new FDOdds();
        if(result == null) result = new FDResult();
        if(result.goalsHomeTeam == null) result.goalsHomeTeam = EMPTY_INT_VALUE;
        if(result.goalsAwayTeam == null) result.goalsAwayTeam = EMPTY_INT_VALUE;

        date = FDUtils.formatDateToSQLite(date);

    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getCompetitionId() {
        return competitionId;
    }


    public String getDate() {
        return date;
    }

    public String getStatus() {
        if (status == null || status.isEmpty()) return EMPTY_LONG_DASH;
        return status.toUpperCase();
    }

    public int getMatchDay() {
        return matchDay;
    }


    public String getHomeTeamName() {
        if (homeTeamName == null || homeTeamName.isEmpty()) {
            return EMPTY_DASH;
        }
        return homeTeamName;
    }

    public String getAwayTeamName() {
        if (awayTeamName == null || awayTeamName.isEmpty()) {
            return EMPTY_DASH;
        }
        return awayTeamName;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public FDResult getResult() {
        return result;
    }

    public int getGoalsHome() {
        if (result == null) return EMPTY_INT_VALUE;
        return result.goalsHomeTeam;
    }

    public int getGoalsAway() {
        if (result == null) return EMPTY_INT_VALUE;
        return result.goalsAwayTeam;
    }

    public double getHomeWin() {
        if (odds == null) return EMPTY_INT_VALUE;
        return odds.homeWin;
    }

    public double getDraw() {
        if (odds == null) return EMPTY_INT_VALUE;
        return odds.draw;
    }

    public double getAwayWin() {
        if (odds == null) return EMPTY_INT_VALUE;
        return odds.awayWin;
    }


    // widgets
    public String getCaption() {
        if (caption == null || caption.isEmpty()) return EMPTY_DASH;
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLeague() {
        if(league == null|| league.isEmpty())return EMPTY_DASH;
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public int getGoalsHomeTeam() {
        if (result == null || result.goalsHomeTeam < 0) return EMPTY_INT_VALUE;
        return result.goalsHomeTeam;
    }

    public int getGoalsAwayTeam() {
        if (result == null || result.goalsAwayTeam < 0) return EMPTY_INT_VALUE;
        return result.goalsAwayTeam;
    }

    // favorite
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // notification
    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public void postProcess() {
        setId();
    }


}
