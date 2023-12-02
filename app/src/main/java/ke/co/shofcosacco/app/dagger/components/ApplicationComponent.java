package ke.co.shofcosacco.app.dagger.components;


import javax.inject.Singleton;

import dagger.Component;
import ke.co.shofcosacco.app.dagger.modules.ApplicationModule;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.main.MainViewModel;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {


    void inject(AuthViewModel authViewModel);

    void inject(LiveDataViewModel liveDataViewModel);

    void inject(MainViewModel mainViewModel);
}