package ru.vpcb.footballassistant.data;

import android.os.Parcel;
import android.os.Parcelable;

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
public class FDTeam implements PostProcessingEnabler.PostProcessable, Parcelable {
    @SerializedName("_links")
    @Expose
    private FDLinks links;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("shortName")
    @Expose
    private String shortName;

    @SerializedName("squadMarketValue")
    @Expose
    private String squadMarketValue;

    @SerializedName("crestUrl")
    @Expose
    private String crestURL;

    private int id;
    private List<FDPlayer> players;

    public FDTeam() {
        this.id = EMPTY_INT_VALUE;
    }

    public FDTeam(int id, String name, String code, String shortName,
                  String squadMarketValue, String crestURL) {
        this.id = id;
        this.name = name;
        this.code=code;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.crestURL = crestURL;
    }


    protected FDTeam(Parcel in) {
        name = in.readString();
        code = in.readString();
        shortName = in.readString();
        squadMarketValue = in.readString();
        crestURL = in.readString();
        id = in.readInt();
// links
        links = new FDLinks();
        links.self.setHref(in.readString());
        links.players.setHref(in.readString());
        links.fixtures.setHref(in.readString());

    }

    public static final Creator<FDTeam> CREATOR = new Creator<FDTeam>() {
        @Override
        public FDTeam createFromParcel(Parcel in) {
            return new FDTeam(in);
        }

        @Override
        public FDTeam[] newArray(int size) {
            return new FDTeam[size];
        }
    };

    public void setId() throws NullPointerException, NumberFormatException {
        if(links.self != null) {
            id = FDUtils.formatHrefToId(links.self.getHref());
        }
     }

    public int getId() {
        return id;
    }

    public List<FDPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<FDPlayer> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public String getCrestURL() {
        return crestURL;
    }

    @Override
    public void postProcess() {
        setId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(shortName);
        parcel.writeString(squadMarketValue);
        parcel.writeString(crestURL);
        parcel.writeInt(id);
// links
        if(links == null) links = new FDLinks();
        if(links.self == null) links.self = new FDLink();
        if(links.players == null) links.players = new FDLink();
        if(links.fixtures == null) links.fixtures = new FDLink();
        parcel.writeString(links.self.getHref());
        parcel.writeString(links.players.getHref());
        parcel.writeString(links.fixtures.getHref());


    }

    // classes
    private class FDLinks {
        @SerializedName("self")
        @Expose
        private FDLink self;

        @SerializedName("fixtures")
        @Expose
        private FDLink fixtures;

        @SerializedName("players")
        @Expose
        private FDLink players;

        FDLinks() {
            this.self = new FDLink();
            this.fixtures = new FDLink();
            this.players = new FDLink();
        }
    }


}
