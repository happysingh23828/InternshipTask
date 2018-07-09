package dynamicdrillers.happysingh.internshipdemo.retrofit;

import java.util.List;

import dynamicdrillers.happysingh.internshipdemo.models.WorldPopulationModel;
import retrofit2.http.GET;

public interface ApiInterface {


    @GET("tutorial/jsonparsetutorial.txt")
    io.reactivex.Observable<WorldPopulationModel> getCountryList();
}
