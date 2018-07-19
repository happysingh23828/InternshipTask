package dynamicdrillers.happysingh.internshipdemo.di.modules;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModules {

    Application mApplication;

    public AppModules(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Module
    public static class NetworkModule {

        String mBaseUrl;

        public NetworkModule(String mBaseUrl) {
            this.mBaseUrl = mBaseUrl;
        }


        @Provides
        @Singleton
        Cache provideHttpCache(Application application) {
            int cacheSize = 10 * 1024 * 1024;
            Cache cache = new Cache(application.getCacheDir(), cacheSize);
            return cache;
        }

        @Provides
        @Singleton
        Gson provideGson() {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            return gsonBuilder.create();
        }

        @Provides
        @Singleton
        OkHttpClient provideOkhttpClient(Cache cache) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.cache(cache);
            return client.build();
        }

        @Provides
        @Singleton
        Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mBaseUrl)
                    .client(okHttpClient)
                    .build();
        }

    }
}
