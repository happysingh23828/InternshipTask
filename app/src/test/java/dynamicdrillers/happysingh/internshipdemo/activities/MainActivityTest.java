package dynamicdrillers.happysingh.internshipdemo.activities;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dynamicdrillers.happysingh.internshipdemo.models.WorldPopulationModel;
import dynamicdrillers.happysingh.internshipdemo.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MainActivityTest {

    String statusCode="";

    @Test
    public void test_api() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.androidbegin.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<WorldPopulationModel> worldPopulationModelCall = apiInterface.getRespones();

        worldPopulationModelCall.enqueue(new Callback<WorldPopulationModel>() {
            @Override
            public void onResponse(Call<WorldPopulationModel> call, Response<WorldPopulationModel> response) {
                statusCode = String.valueOf(response.code());
            }

            @Override
            public void onFailure(Call<WorldPopulationModel> call, Throwable t) {
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("200",statusCode);

    }


}