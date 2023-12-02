package ke.co.shofcosacco.app;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import java.util.Locale;

import ke.co.shofcosacco.app.dagger.components.ApplicationComponent;
import ke.co.shofcosacco.app.dagger.components.DaggerApplicationComponent;
import ke.co.shofcosacco.app.dagger.modules.ApplicationModule;


public class MainApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent =  DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        setDefaultLocale();
    }

    public static MainApplication getInstance(Application application) {
        return (MainApplication) application;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void setDefaultLocale() {
        String lang = getSharedPreferences("Settings", MODE_PRIVATE).getString("Language", "");
        if (!TextUtils.isEmpty(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }
}
