package ru.vpcb.footballassistant.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Locale;

import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_LONG_DASH;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_PLAYER_DATE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_TWO_DASH;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDPlayer {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("jerseyNumber")
    @Expose
    private int jerseyNumber;

    @SerializedName("dateOfBirth")
    @Expose
    private Date dateOfBirth;

    @SerializedName("nationality")
    @Expose
    private String nationality;

    @SerializedName("contractUntil")
    @Expose
    private Date contractUntil;

    @SerializedName("marketValue")
    @Expose
    private String marketValue;

    public String getName() {
        if (name == null || name.isEmpty()) return EMPTY_LONG_DASH;
        return name;
    }

    public String getPosition() {
        if (position == null || position.isEmpty()) return EMPTY_LONG_DASH;
        return position;
    }

    public String getJerseyNumber() {
        if (jerseyNumber <= 0) return EMPTY_TWO_DASH;
        return String.format(Locale.ENGLISH, "%02d", jerseyNumber);
    }

    public String getDateOfBirth() {
        if (dateOfBirth == null) return EMPTY_PLAYER_DATE;
        return FDUtils.formatStringDate(dateOfBirth,".");
    }

    public String getNationality() {
        if (nationality == null || nationality.isEmpty()) return EMPTY_LONG_DASH;
        return nationality;
    }

    public String getContractUntil() {
        if (contractUntil == null) return EMPTY_PLAYER_DATE;
        return FDUtils.formatStringDate(contractUntil,".");
    }

    public String getMarketValue() {
        if(marketValue == null || marketValue.isEmpty()) return EMPTY_LONG_DASH;
        return marketValue;
    }
}
