
package dynamicdrillers.happysingh.internshipdemo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Worldpopulation {

    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("population")
    @Expose
    private String population;
    @SerializedName("flag")
    @Expose
    private String flag;

    public Worldpopulation() {
    }

    public Worldpopulation(Integer rank, String country, String population, String flag) {

        this.rank = rank;
        this.country = country;
        this.population = population;
        this.flag = flag;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
