package dynamicdrillers.happysingh.internshipdemo;

import android.app.Application;

import java.nio.channels.NetworkChannel;

import dynamicdrillers.happysingh.internshipdemo.di.components.DaggerNetworkComponent;
import dynamicdrillers.happysingh.internshipdemo.di.components.NetworkComponent;
import dynamicdrillers.happysingh.internshipdemo.di.modules.AppModules;

public class App extends Application {

    private NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();

       networkComponent =   DaggerNetworkComponent.builder().
               appModules(new AppModules(this)).
               networkModule(new AppModules.NetworkModule("http://www.androidbegin.com/")).build();

    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }
}
