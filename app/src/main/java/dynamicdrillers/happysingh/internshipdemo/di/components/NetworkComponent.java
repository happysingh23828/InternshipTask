package dynamicdrillers.happysingh.internshipdemo.di.components;

import javax.inject.Singleton;

import dagger.Component;
import dynamicdrillers.happysingh.internshipdemo.activities.MainActivity;
import dynamicdrillers.happysingh.internshipdemo.di.modules.AppModules;

@Singleton
@Component(modules = {AppModules.class, AppModules.NetworkModule.class})
public interface NetworkComponent {

    void inject(MainActivity mainActivity);
}
