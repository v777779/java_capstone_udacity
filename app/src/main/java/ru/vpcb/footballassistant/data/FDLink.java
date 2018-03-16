package ru.vpcb.footballassistant.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 24-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDLink {
    @SerializedName("href")
    @Expose
    private String href;

    public FDLink() {
        href = EMPTY_STRING;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
