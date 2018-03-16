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
public class FDStandingGroup {
    @SerializedName("A")
    @Expose
    private List<FDStanding> groupA;
    @SerializedName("B")
    @Expose
    private List<FDStanding> groupB;
    @SerializedName("C")
    @Expose
    private List<FDStanding> groupC;
    @SerializedName("D")
    @Expose
    private List<FDStanding> groupD;
    @SerializedName("E")
    @Expose
    private List<FDStanding> groupE;
    @SerializedName("F")
    @Expose
    private List<FDStanding> groupF;
    @SerializedName("G")
    @Expose
    private List<FDStanding> groupG;
    @SerializedName("H")
    @Expose
    private List<FDStanding> groupH;

    public List<FDStanding> getGroupA() {
        return groupA;
    }

    public void setGroupA(List<FDStanding> groupA) {
        this.groupA = groupA;
    }

    public List<FDStanding> getGroupB() {
        return groupB;
    }

    public void setGroupB(List<FDStanding> groupB) {
        this.groupB = groupB;
    }

    public List<FDStanding> getGroupC() {
        return groupC;
    }

    public void setGroupC(List<FDStanding> groupC) {
        this.groupC = groupC;
    }

    public List<FDStanding> getGroupD() {
        return groupD;
    }

    public void setGroupD(List<FDStanding> groupD) {
        this.groupD = groupD;
    }

    public List<FDStanding> getGroupE() {
        return groupE;
    }

    public void setGroupE(List<FDStanding> groupE) {
        this.groupE = groupE;
    }

    public List<FDStanding> getGroupF() {
        return groupF;
    }

    public void setGroupF(List<FDStanding> groupF) {
        this.groupF = groupF;
    }

    public List<FDStanding> getGroupG() {
        return groupG;
    }

    public void setGroupG(List<FDStanding> groupG) {
        this.groupG = groupG;
    }

    public List<FDStanding> getGroupH() {
        return groupH;
    }

    public void setGroupH(List<FDStanding> groupH) {
        this.groupH = groupH;
    }
}
