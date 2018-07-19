package dynamicdrillers.happysingh.internshipdemo.retrofit;

import dynamicdrillers.happysingh.internshipdemo.models.WorldPopulationModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {


    @GET("tutorial/jsonparsetutorial.txt")
    io.reactivex.Observable<WorldPopulationModel> getCountryList();



    @GET("tutorial/jsonparsetutorial.txt")
    Call<WorldPopulationModel>  getRespones();
}
